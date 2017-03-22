package data;

import model.Location;
import service.HashPassword;
import service.TokenGenerator;
import model.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;


@Stateless
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    public void add(User user) {
        em.persist(user);
    }

    public void update(User user) {
        em.merge(user);
    }


    public void updateLocation(User user, Location location) {
        location.setUser(user);
        em.persist(location);
    }

    public User get(Long id) {
        TypedQuery<User> query = em.createNamedQuery("User.findById", User.class);
        query.setParameter("id", id);
        List<User> resultList = query.getResultList();
        if (resultList.isEmpty())
            return null;
        else
            return resultList.get(0);
    }

    public User get(String email) {
        TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        List<User> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    public String getToken(String email) {
        TypedQuery<String> query = em.createNamedQuery("User.getToken", String.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }

    public List<Location> getLocationsByDayAndMonthAndYear(Integer day, Integer month, Integer year, Long userId) {
        TypedQuery<Location> query = em.createNamedQuery("Location.getLocationsByMonthAndYear", Location.class);
        query.setParameter("day", day);
        query.setParameter("month", month);
        query.setParameter("year", year);
        query.setParameter("id", userId);
        return query.getResultList();
    }

    public Location getLastLocation(User user){
        TypedQuery<Location> query = em.createNamedQuery("Location.getLastLocation",Location.class);
        query.setParameter("id",user.getId());
        List<Location> result = query.getResultList();
        if(result.size()>0){
            return result.get(result.size()-1);
        }else {
            return null;
        }
    }
}
