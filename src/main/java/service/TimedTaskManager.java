package service;

import data.NewFriendToConfirmRepository;
import model.NewFriendToConfirm;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import java.util.List;

@Singleton
@Startup
public class TimedTaskManager {

    @Inject
    private NewFriendToConfirmRepository newFriendToConfirmRepository;

    @Schedule(hour = "2",minute = "0",persistent = false)
    public void clearFriendsToConfirm(){
        System.out.println("Clear friends");
        List<NewFriendToConfirm> data = newFriendToConfirmRepository.getAllWhichExceedTime();
        data.forEach(newFriendToConfirmRepository::remove);
    }
}
