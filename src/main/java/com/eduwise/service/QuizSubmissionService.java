package com.eduwise.service;

import com.eduwise.dto.quiz.*;
import com.eduwise.dto.user.UserResponse;
import com.eduwise.model.*;
import com.eduwise.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizSubmissionService {

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public QuizSubmissionResponse submitQuiz(QuizSubmissionRequest request, String studentUsername) {
        User student = userRepository.findByUsername(studentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        // Check if student has already submitted this quiz
        if (quizSubmissionRepository.existsByStudentIdAndQuizId(student.getId(), quiz.getId())) {
            throw new IllegalStateException("You have already submitted this quiz");
        }

        // Create submission
        QuizSubmission submission = new QuizSubmission();
        submission.setStudent(student);
        submission.setQuiz(quiz);
        submission.setSubmissionDate(LocalDateTime.now());

        // Process each question submission
        List<QuestionSubmission> questionSubmissions = new ArrayList<>();
        int totalScore = 0;
        int maxScore = 0;

        for (QuestionSubmissionRequest questionSubmission : request.getQuestionSubmissions()) {
            Question question = questionRepository.findById(questionSubmission.getQuestionId())
                    .orElseThrow(() -> new EntityNotFoundException("Question not found"));

            QuestionSubmission qs = new QuestionSubmission();
            qs.setQuestion(question);
            qs.setQuizSubmission(submission);
            qs.setSelectedAnswers(questionSubmission.getSelectedAnswerIds().stream()
                    .map(answerId -> answerRepository.findById(answerId)
                            .orElseThrow(() -> new EntityNotFoundException("Answer not found")))
                    .collect(Collectors.toList()));

            // Calculate score for this question
            boolean isCorrect = isAnswerCorrect(question, qs.getSelectedAnswers());
            qs.setCorrect(isCorrect);
            qs.setPointsEarned(isCorrect ? question.getPoints() : 0);

            questionSubmissions.add(qs);
            totalScore += qs.getPointsEarned();
            maxScore += question.getPoints();
        }

        submission.setQuestionSubmissions(questionSubmissions);
        submission.setTotalScore(totalScore);
        submission.setMaxScore(maxScore);
        submission.setPassed(totalScore >= quiz.getPassingScore());

        return mapToQuizSubmissionResponse(quizSubmissionRepository.save(submission));
    }

    @Transactional(readOnly = true)
    public List<QuizSubmissionResponse> getStudentSubmissions(String studentUsername) {
        return quizSubmissionRepository.findByStudentUsername(studentUsername).stream()
                .map(this::mapToQuizSubmissionResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuizSubmissionResponse getSubmissionById(Long id) {
        return quizSubmissionRepository.findById(id)
                .map(this::mapToQuizSubmissionResponse)
                .orElseThrow(() -> new EntityNotFoundException("Quiz submission not found"));
    }

    private boolean isAnswerCorrect(Question question, List<Answer> selectedAnswers) {
        List<Answer> correctAnswers = question.getAnswers().stream()
                .filter(Answer::isCorrect)
                .collect(Collectors.toList());

        if (selectedAnswers.size() != correctAnswers.size()) {
            return false;
        }

        return selectedAnswers.stream()
                .allMatch(answer -> correctAnswers.stream()
                        .anyMatch(correct -> correct.getId().equals(answer.getId())));
    }

    private QuizSubmissionResponse mapToQuizSubmissionResponse(QuizSubmission submission) {
        return new QuizSubmissionResponse(
            submission.getId(),
            mapToUserResponse(submission.getStudent()),
            mapToQuizResponse(submission.getQuiz()),
            submission.getSubmissionDate(),
            submission.getTotalScore(),
            submission.getMaxScore(),
            submission.isPassed(),
            submission.getQuestionSubmissions().stream()
                .map(this::mapToQuestionSubmissionResult)
                .collect(Collectors.toList())
        );
    }

    private QuestionSubmissionResult mapToQuestionSubmissionResult(QuestionSubmission submission) {
        Question question = submission.getQuestion();
        return new QuestionSubmissionResult(
            question.getId(),
            question.getText(),
            submission.getPointsEarned(),
            question.getPoints(),
            question.getAnswers().stream()
                .map(answer -> new AnswerResult(answer.getId(), answer.getText(), answer.isCorrect()))
                .collect(Collectors.toList()),
            submission.getSelectedAnswers().stream()
                .map(Answer::getId)
                .collect(Collectors.toList()),
            submission.isCorrect()
        );
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole()
        );
    }

    private QuizResponse mapToQuizResponse(Quiz quiz) {
        return new QuizResponse(
            quiz.getId(),
            quiz.getTitle(),
            quiz.getDescription(),
            quiz.getTimeLimit(),
            quiz.getPassingScore(),
            quiz.getQuestions().stream()
                .map(this::mapToQuestionResponse)
                .collect(Collectors.toList())
        );
    }

    private QuestionResponse mapToQuestionResponse(Question question) {
        return new QuestionResponse(
            question.getId(),
            question.getText(),
            question.getPoints(),
            question.getAnswers().stream()
                .map(this::mapToAnswerResponse)
                .collect(Collectors.toList())
        );
    }

    private AnswerResponse mapToAnswerResponse(Answer answer) {
        return new AnswerResponse(
            answer.getId(),
            answer.getText(),
            answer.isCorrect()
        );
    }
} 