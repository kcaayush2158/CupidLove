package com.application.springboot.repository;

import com.application.springboot.model.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface NotificationRespository  extends JpaRepository<Notification,Integer> {

    // receives all the notification by subscribed username
     List<Notification> findByUserReceiver(String receiver);

    int countAllByStatusAndUserReceiver(boolean status,String receivers);

     List<Notification> getAllByStatus(boolean status);

     @Query("delete from Notification n WHERE n.id =:id ")
     Notification deleteNotificationBy(int id);

     @Modifying
     @Transactional
     @Query(value="UPDATE Notification n set n.status=true where n.id=:id")
     Notification readNotifications(@Param("id") int id);
}
