package io.app.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ScheduleService {
//    @Scheduled(fixedRate = 1000)
    public void sendNotification(){
        System.out.println("This is schedule Message "+new Date().getTime());
    }
}
