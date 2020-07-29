package com.application.springboot.service;

import com.application.springboot.model.notification.Notification;
import com.application.springboot.repository.NotificationRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRespository notificationRespository;

    //save the user notification
    public Notification saveUserVisit(Notification notification){
       return notificationRespository.save(notification);
    }

    public List<Notification> getNotification(String receiver){
        return notificationRespository.findByUserReceiver(receiver);
    }

    public int countNewMessages(boolean status,String receiver){
        return notificationRespository.countAllByStatusAndUserReceiver(status ,receiver);
    }

    public Notification deleteNotification(int id){
        return notificationRespository.deleteNotificationBy(id);
    }

    public Notification readNotification(int id){
        System.out.println(id);
        return notificationRespository.readNotifications(id);
    }

}
