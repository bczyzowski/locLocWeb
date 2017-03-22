package model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NamedQueries({
        @NamedQuery(name = "NewFriendToConfirm.findByToken",query = "SELECT f FROM NewFriendToConfirm  f WHERE f.token = :token "),
        @NamedQuery(name="NewFriendToConfirm.getAllWhichExceedTime",query = "SELECT f FROM NewFriendToConfirm f WHERE HOUR(TIMEDIFF(:time, f.time)) >= 24")
})
public class NewFriendToConfirm implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long newFriendId;
    private String token;
    private LocalDateTime time;


    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public NewFriendToConfirm() {
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getNewFriendId() {
        return newFriendId;
    }

    public void setNewFriendId(Long newFriendId) {
        this.newFriendId = newFriendId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
