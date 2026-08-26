package ch.wiss.wiss_quiz.controller;

import ch.wiss.wiss_quiz.dto.QuestionDTO;
import ch.wiss.wiss_quiz.model.Answer;
import ch.wiss.wiss_quiz.model.AnswerRepository;
import ch.wiss.wiss_quiz.model.Category;
import ch.wiss.wiss_quiz.model.CategoryRepository;
import ch.wiss.wiss_quiz.service.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // This means that this class is a Controller
@RequestMapping(path = "/question") // This means URL's start with /question (after Application path)
public class QuestionController {
    private final QuestionService questionService;
    private final CategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;

    public QuestionController(QuestionService questionService, CategoryRepository categoryRepository, AnswerRepository answerRepository) {
        this.questionService = questionService;
        this.categoryRepository = categoryRepository;
        this.answerRepository = answerRepository;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/{cat_id}") // Map ONLY POST Requests
    public String addNewQuestion(@PathVariable(value = "cat_id") Integer catId, @RequestBody QuestionDTO questionDTO) {

        Optional<Category> cat = categoryRepository.findById(catId);
        if (cat.isEmpty()) {
            return "Category not found";
        }

        questionService.createQuestion(questionDTO);

        List<Answer> answers = questionDTO.answers();

        // we need to store nested Answer-Objects seperately
        answers.forEach(a -> {
            a.setQuestion(questionService.convertToEntity(questionDTO));
            answerRepository.save(a);
        });

        return "Saved";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "")
    public List<QuestionDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

}