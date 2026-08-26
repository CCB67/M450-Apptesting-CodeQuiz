package ch.wiss.wiss_quiz.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface QuestionRepository extends CrudRepository<Question, Integer> {

    // Override findAll to return List instead of Iterable
    @Override
    List<Question> findAll();

    List<Question> findByCategory(Category category);

}