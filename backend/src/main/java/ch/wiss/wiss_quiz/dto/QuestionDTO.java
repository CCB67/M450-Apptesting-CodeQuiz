package ch.wiss.wiss_quiz.dto;

import ch.wiss.wiss_quiz.model.Answer;
import ch.wiss.wiss_quiz.model.Category;

import java.util.List;

public record QuestionDTO(
        Integer id,
        String questionText,
        Category category,
        List<Answer> answers
) {}
