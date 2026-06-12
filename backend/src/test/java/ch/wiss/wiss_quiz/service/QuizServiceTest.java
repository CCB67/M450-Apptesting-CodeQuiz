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

}