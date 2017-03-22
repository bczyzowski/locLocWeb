package data;

import model.NewFriendToConfirm;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class NewFriendToConfirmRepository {

    @PersistenceContext
    private EntityManager em;

    public void add(NewFriendToConfirm friendToConfirm) {
        friendToConfirm.setTime(LocalDateTime.now());
        em.persist(friendToConfirm);
    }

    public NewFriendToConfirm get(String token) {
        TypedQuery<NewFriendToConfirm> query = em.createNamedQuery("NewFriendToConfirm.findByToken", NewFriendToConfirm.class);
        query.setParameter("token", token);
        List<NewFriendToConfirm> rows = query.getResultList();
        if(rows.size()>0){
            return rows.get(0);
        }
        return null;
    }

    public List<NewFriendToConfirm> getAllWhichExceedTime() {
        TypedQuery<NewFriendToConfirm> query = em.createNamedQuery("NewFriendToConfirm.getAllWhichExceedTime", NewFriendToConfirm.class);
        query.setParameter("time", LocalDateTime.now());
        List<NewFriendToConfirm> data = query.getResultList();
        return data;
    }

    public void remove(NewFriendToConfirm friendToConfirm) {
        NewFriendToConfirm confirmToRemove = em.find(NewFriendToConfirm.class, friendToConfirm.getId());
        em.remove(confirmToRemove);
    }

}
