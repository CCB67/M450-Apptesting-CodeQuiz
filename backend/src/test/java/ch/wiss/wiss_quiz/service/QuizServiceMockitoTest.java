package ch.wiss.wiss_quiz.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ch.wiss.wiss_quiz.model.Answer;
import ch.wiss.wiss_quiz.model.Category;
import ch.wiss.wiss_quiz.model.CategoryRepository;
import ch.wiss.wiss_quiz.model.Question;
import ch.wiss.wiss_quiz.model.QuestionRepository;

@ExtendWith(MockitoExtension.class)
class QuizServiceMockitoTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private QuizService service;

    // Station 4 – Service und Datenbank (Mockito), Fehlerfall
    @Test
    void getQuizQuestionsByCategoryId_throwsException_whenNotEnoughQuestionsExist() {
        Category category = new Category();
        List<Question> questions = List.of(
                new Question(),
                new Question()
        );

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(questionRepository.findByCategory(category)).thenReturn(questions);

        assertThrows(IllegalStateException.class, () -> service.getQuizQuestionsByCategoryId(1));
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void getQuizQuestionsByCategoryId_throwsException_whenCategoryDoesNotExist() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getQuizQuestionsByCategoryId(99));
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void getQuizQuestionsByCategoryId_returnsThreeQuestions_whenEnoughValidQuestionsExist() {
        Category category = new Category();
        List<Question> questions = List.of(
                createValidQuestion("Q1"),
                createValidQuestion("Q2"),
                createValidQuestion("Q3"),
                createValidQuestion("Q4")
        );

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(questionRepository.findByCategory(category)).thenReturn(questions);

        List<Question> result = service.getQuizQuestionsByCategoryId(1);

        assertEquals(3, result.size());
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void getQuizQuestionsByCategoryId_throwsException_whenNotEnoughValidQuestionsExist() {
        Category category = new Category();
        List<Question> questions = List.of(
                createValidQuestion("Q1"),
                createValidQuestion("Q2"),
                createInvalidQuestion()
        );

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(questionRepository.findByCategory(category)).thenReturn(questions);

        assertThrows(IllegalStateException.class, () -> service.getQuizQuestionsByCategoryId(1));
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void categoryHasEnoughValidQuestions_returnsTrue_whenEnoughValidQuestionsExist() {
        Category category = new Category();
        List<Question> questions = List.of(
                createValidQuestion("Q1"),
                createValidQuestion("Q2"),
                createValidQuestion("Q3"),
                createInvalidQuestion()
        );

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(questionRepository.findByCategory(category)).thenReturn(questions);

        boolean result = service.categoryHasEnoughValidQuestions(1, 3);

        assertTrue(result);
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void categoryHasEnoughValidQuestions_returnsFalse_whenNotEnoughValidQuestionsExist() {
        Category category = new Category();
        List<Question> questions = List.of(
                createValidQuestion("Q1"),
                createInvalidQuestion(),
                createInvalidQuestion()
        );

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(questionRepository.findByCategory(category)).thenReturn(questions);

        boolean result = service.categoryHasEnoughValidQuestions(1, 2);

        assertFalse(result);
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void categoryHasEnoughValidQuestions_returnsFalse_whenCategoryDoesNotExist() {
        when(categoryRepository.findById(99)).thenReturn(Optional.empty());

        boolean result = service.categoryHasEnoughValidQuestions(99, 3);

        assertFalse(result);
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void categoryHasEnoughValidQuestions_returnsFalse_whenCategoryIdIsNull() {
        boolean result = service.categoryHasEnoughValidQuestions(null, 3);

        assertFalse(result);
        verifyNoInteractions(categoryRepository, questionRepository);
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void categoryHasEnoughValidQuestions_returnsFalse_whenRequiredCountIsZero() {
        boolean result = service.categoryHasEnoughValidQuestions(1, 0);

        assertFalse(result);
        verifyNoInteractions(categoryRepository, questionRepository);
    }

    // Station 4 – Service und Datenbank (Mockito)
    @Test
    void getQuizQuestionsByCategoryId_callsRepositories() {
        Category category = new Category();
        List<Question> questions = List.of(
                createValidQuestion("Q1"),
                createValidQuestion("Q2"),
                createValidQuestion("Q3")
        );

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(questionRepository.findByCategory(category)).thenReturn(questions);

        service.getQuizQuestionsByCategoryId(1);

        verify(categoryRepository).findById(1);
        verify(questionRepository).findByCategory(category);
    }

    private Question createValidQuestion(String text) {
        Question question = new Question();
        question.setQuestion(text);
        question.setAnswers(List.of(
                createAnswer("A", true),
                createAnswer("B", false)
        ));
        return question;
    }

    private Question createInvalidQuestion() {
        Question question = new Question();
        question.setQuestion("Invalid");
        question.setAnswers(List.of(
                createAnswer("A", true),
                createAnswer("B", true)
        ));
        return question;
    }

    private Answer createAnswer(String text, boolean correct) {
        Answer answer = new Answer();
        answer.setAnswer(text);
        answer.setCorrect(correct);
        return answer;
    }
}