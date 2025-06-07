package com.eduwise.service;

import com.eduwise.dto.quiz.QuizRequest;
import com.eduwise.dto.quiz.QuizResponse;
import com.eduwise.dto.quiz.QuestionResponse;
import com.eduwise.dto.quiz.AnswerResponse;
import com.eduwise.model.Course;
import com.eduwise.model.Quiz;
import com.eduwise.model.Question;
import com.eduwise.model.Answer;
import com.eduwise.repository.CourseRepository;
import com.eduwise.repository.QuizRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public QuizResponse createQuiz(Long courseId, QuizRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setTimeLimit(request.getTimeLimit());
        quiz.setPassingScore(request.getPassingScore());
        quiz.setCourse(course);

        // Create and set questions
        List<Question> questions = request.getQuestions().stream()
                .map(questionRequest -> {
                    Question question = new Question();
                    question.setText(questionRequest.getText());
                    question.setPoints(questionRequest.getPoints());
                    question.setQuiz(quiz);

                    // Create and set answers
                    List<Answer> answers = questionRequest.getAnswers().stream()
                            .map(answerRequest -> {
                                Answer answer = new Answer();
                                answer.setText(answerRequest.getText());
                                answer.setCorrect(answerRequest.isCorrect());
                                answer.setQuestion(question);
                                return answer;
                            })
                            .collect(Collectors.toList());
                    question.setAnswers(answers);
                    return question;
                })
                .collect(Collectors.toList());
        quiz.setQuestions(questions);

        return mapToQuizResponse(quizRepository.save(quiz));
    }

    @Transactional
    public QuizResponse updateQuiz(Long id, QuizRequest request) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setTimeLimit(request.getTimeLimit());
        quiz.setPassingScore(request.getPassingScore());

        // Update questions
        quiz.getQuestions().clear();
        List<Question> questions = request.getQuestions().stream()
                .map(questionRequest -> {
                    Question question = new Question();
                    question.setText(questionRequest.getText());
                    question.setPoints(questionRequest.getPoints());
                    question.setQuiz(quiz);

                    // Update answers
                    List<Answer> answers = questionRequest.getAnswers().stream()
                            .map(answerRequest -> {
                                Answer answer = new Answer();
                                answer.setText(answerRequest.getText());
                                answer.setCorrect(answerRequest.isCorrect());
                                answer.setQuestion(question);
                                return answer;
                            })
                            .collect(Collectors.toList());
                    question.setAnswers(answers);
                    return question;
                })
                .collect(Collectors.toList());
        quiz.setQuestions(questions);

        return mapToQuizResponse(quizRepository.save(quiz));
    }

    @Transactional
    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new EntityNotFoundException("Quiz not found");
        }
        quizRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> getQuizzesByCourse(Long courseId) {
        return quizRepository.findByCourseId(courseId).stream()
                .map(this::mapToQuizResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuizResponse getQuizById(Long id) {
        return quizRepository.findById(id)
                .map(this::mapToQuizResponse)
                .orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
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