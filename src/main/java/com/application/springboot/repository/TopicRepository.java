package com.application.springboot.repository;

import com.application.springboot.model.Topic;
import com.application.springboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Integer> {

    @Modifying
    @Query("UPDATE Topic  t set  t.topicAnswer= :topicAnswer , t.topicName = :topicName , t.topicQuestion=:topicQuestion where t.id = :id")
    void updateTopic(@Param("id") int id, @Param("topicName") String topicName, @Param("topicQuestion") String topicQuestion, @Param("topicAnswer") String topicAnswer);

    List<Topic> findTopicsByUser(User user);

}
