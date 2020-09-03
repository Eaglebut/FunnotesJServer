package ru.eaglebutt.funnotes.dao.implementations;

import org.hibernate.Session;
import ru.eaglebutt.funnotes.HibernateUtil;
import ru.eaglebutt.funnotes.dao.interfaces.UserDAO;
import ru.eaglebutt.funnotes.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDAOImpl implements UserDAO {


    @Override
    public User get(String email, String password) {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("email"), email));
        List<User> users = session.createQuery(criteriaQuery).getResultList();
        for (User user : users) {
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void insert(User user) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.save(user);
        session.getTransaction().commit();
    }

    @Override
    public void update(User user) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.update(user);
        session.getTransaction().commit();
    }

    @Override
    public void delete(User user) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.delete(user);
        session.getTransaction().commit();
    }

}
