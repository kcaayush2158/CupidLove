package com.application.springboot.service;

import com.application.springboot.model.Topic;
import com.application.springboot.model.User;
import com.application.springboot.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public List<Topic> findAllTopic() {
        return this.topicRepository.findAll();
    }

    public Topic saveTopic(Topic topic) {
        return this.topicRepository.save(topic);
    }

    public void deleteTopic(Integer id) {
        this.topicRepository.deleteById(id);
    }

    public void updateTopic(String topicQuestion, String topicAnswer, String topicName, int id) {
        this.topicRepository.updateTopic(id, topicAnswer, topicQuestion, topicName);
    }

    public List<Topic> findTopic(User user) {
        return this.topicRepository.findTopicsByUser(user);
    }
}
