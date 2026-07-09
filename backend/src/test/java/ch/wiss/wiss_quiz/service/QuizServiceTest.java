package ch.wiss.wiss_quiz.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.wiss.wiss_quiz.model.Answer;
import ch.wiss.wiss_quiz.model.Question;

class QuizServiceTest {

    private QuizService service;

    @BeforeEach
    void setUp() {
        service = new QuizService(null, null);
    }

    // Station 1 – Standardfälle (Happy Path)
    @Test
    void returnsAllIfLessOrEqualMax() {
        List<Question> input = List.of(new Question(), new Question());

        List<Question> result = service.pickQuizQuestions(input, 3);

        assertEquals(2, result.size());
    }

    // Station 1 – Standardfälle (Happy Path)
    @Test
    void limitsToMaxIfMoreThanMax() {
        List<Question> input = List.of(
                new Question(), new Question(), new Question(), new Question(), new Question()
        );

        List<Question> result = service.pickQuizQuestions(input, 3);

        assertEquals(3, result.size());
    }

    // Station 1 – Standardfälle (Happy Path)
    @Test
    void doesNotModifyOriginalList() {
        List<Question> input = new ArrayList<>(List.of(
                new Question(), new Question(), new Question(), new Question()
        ));

        service.pickQuizQuestions(input, 3);

        assertEquals(4, input.size());
    }

    // Station 1 – Standardfälle (Happy Path)
    @Test
    void calculateScorePercent_returns60_when3Of5Correct() {
        double result = service.calculateScorePercent(3, 5);

        assertEquals(60.0, result);
    }

    // Station 1 – Standardfälle (Happy Path)
    @Test
    void isQuizPassed_returnsTrue_whenPercentIs60() {
        boolean result = service.isQuizPassed(60.0);

        assertTrue(result);
    }

    // Station 1 – Standardfälle (Happy Path)
    @Test
    void isQuizPassed_returnsFalse_whenPercentIs59() {
        boolean result = service.isQuizPassed(59.0);

        assertFalse(result);
    }

    // Station 2 – Randfälle (Edge Cases)
    @Test
    void handlesBadInputs() {
        assertTrue(service.pickQuizQuestions(null, 3).isEmpty());
        assertTrue(service.pickQuizQuestions(List.of(), 3).isEmpty());
        assertTrue(service.pickQuizQuestions(List.of(new Question()), 0).isEmpty());
    }

    // Station 2 – Randfälle (Edge Cases)
    @Test
    void pickQuizQuestions_returnsEmptyList_whenMaxQuestionsIsNegative() {
        List<Question> input = List.of(new Question(), new Question());

        List<Question> result = service.pickQuizQuestions(input, -1);

        assertTrue(result.isEmpty());
    }

    // Station 2 – Randfälle (Edge Cases)
    @Test
    void calculateScorePercent_returnsZero_whenTotalIsZero() {
        double result = service.calculateScorePercent(3, 0);

        assertEquals(0.0, result);
    }

    // Station 2 – Randfälle (Edge Cases)
    @Test
    void calculateScorePercent_returnsZero_whenTotalIsNegative() {
        double result = service.calculateScorePercent(3, -5);

        assertEquals(0.0, result);
    }

    // Station 2 – Randfälle (Edge Cases)
    @Test
    void hasEnoughQuestions_returnsTrue_whenListHasAtLeastThreeQuestions() {
        List<Question> input = List.of(new Question(), new Question(), new Question());

        boolean result = service.hasEnoughQuestions(input);

        assertTrue(result);
    }

    // Station 2 – Randfälle (Edge Cases)
    @Test
    void hasEnoughQuestions_returnsFalse_whenListHasLessThanThreeQuestions() {
        List<Question> input = List.of(new Question(), new Question());

        boolean result = service.hasEnoughQuestions(input);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenQuestionIsNull() {
        boolean result = service.validateQuestionForQuiz(null);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenQuestionTextIsNull() {
        Question question = new Question();
        question.setQuestion(null);
        question.setAnswers(createValidAnswers());

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenQuestionTextIsBlank() {
        Question question = new Question();
        question.setQuestion("   ");
        question.setAnswers(createValidAnswers());

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenAnswersAreNull() {
        Question question = new Question();
        question.setQuestion("Frage?");

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenLessThanTwoAnswersExist() {
        Question question = new Question();
        question.setQuestion("Frage?");
        question.setAnswers(List.of(createAnswer("Nur eine", true)));

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenAnswerIsNull() {
        Question question = new Question();
        question.setQuestion("Frage?");

        List<Answer> answers = new ArrayList<>();
        answers.add(createAnswer("A", true));
        answers.add(null);

        question.setAnswers(answers);

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }
    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenAnswerTextIsBlank() {
        Question question = new Question();
        question.setQuestion("Frage?");
        question.setAnswers(List.of(createAnswer(" ", true), createAnswer("B", false)));

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenNoCorrectAnswerExists() {
        Question question = new Question();
        question.setQuestion("Frage?");
        question.setAnswers(List.of(createAnswer("A", false), createAnswer("B", false)));

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsFalse_whenMultipleCorrectAnswersExist() {
        Question question = new Question();
        question.setQuestion("Frage?");
        question.setAnswers(List.of(createAnswer("A", true), createAnswer("B", true)));

        boolean result = service.validateQuestionForQuiz(question);

        assertFalse(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void validateQuestionForQuiz_returnsTrue_whenExactlyOneCorrectAnswerExists() {
        Question question = new Question();
        question.setQuestion("Frage?");
        question.setAnswers(createValidAnswers());

        boolean result = service.validateQuestionForQuiz(question);

        assertTrue(result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void countValidQuestions_returnsZero_whenQuestionsIsNull() {
        int result = service.countValidQuestions(null);

        assertEquals(0, result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void countValidQuestions_returnsZero_whenQuestionsIsEmpty() {
        int result = service.countValidQuestions(List.of());

        assertEquals(0, result);
    }

    // Station 3 – Validierung von Quizfragen
    @Test
    void countValidQuestions_countsOnlyValidQuestions() {
        Question valid1 = new Question();
        valid1.setQuestion("Q1");
        valid1.setAnswers(createValidAnswers());

        Question valid2 = new Question();
        valid2.setQuestion("Q2");
        valid2.setAnswers(createValidAnswers());

        Question invalid = new Question();
        invalid.setQuestion("Q3");
        invalid.setAnswers(List.of(createAnswer("Only one", true)));

        int result = service.countValidQuestions(List.of(valid1, invalid, valid2));

        assertEquals(2, result);
    }

    private List<Answer> createValidAnswers() {
        return List.of(
                createAnswer("A", true),
                createAnswer("B", false)
        );
    }

    private Answer createAnswer(String text, boolean correct) {
        Answer answer = new Answer();
        answer.setAnswer(text);
        answer.setCorrect(correct);
        return answer;
    }
}