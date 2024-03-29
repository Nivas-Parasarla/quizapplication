package com.nivas.quizservice.service;


import com.nivas.quizservice.dao.QuizDao;
import com.nivas.quizservice.feign.QuizInterface;
import com.nivas.quizservice.model.QuestionWrapper;
import com.nivas.quizservice.model.Quiz;
import com.nivas.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.generateQuestionsForQuiz(category,numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }


    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {

        Quiz quiz = quizDao.findById(id).get();
        List<Integer>  questionIds =quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questionsForUser= quizInterface.getQuestionsFromId(questionIds);
        return questionsForUser;
    }

    public ResponseEntity<Integer> getScore(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score= quizInterface.getScore(responses);
        return score;
    }
}
