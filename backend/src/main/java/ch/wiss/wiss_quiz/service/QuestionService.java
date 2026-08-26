package ch.wiss.wiss_quiz.service;

import ch.wiss.wiss_quiz.dto.QuestionDTO;
import ch.wiss.wiss_quiz.model.Question;
import ch.wiss.wiss_quiz.model.QuestionRepository;

import java.util.List;

public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {this.questionRepository = questionRepository;}

    public List<QuestionDTO> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public QuestionDTO createQuestion(QuestionDTO questionDTO) {
        Question question = convertToEntity(questionDTO);
        Question savedQuestion = questionRepository.save(question);
        return convertToDTO(savedQuestion);
    }

    public boolean existsById(Integer id) {return questionRepository.existsById(id);}

    private QuestionDTO convertToDTO(Question question) {
        return new QuestionDTO(
                question.getId(),
                question.getQuestion(),
                question.getCategory(),
                question.getAnswers()
        );
    }

    public Question convertToEntity(QuestionDTO dto) {
        Question question = new Question();
        question.setQuestion(dto.questionText());
        question.setCategory(dto.category());
        question.setAnswers(dto.answers());
        return question;
    }
}
