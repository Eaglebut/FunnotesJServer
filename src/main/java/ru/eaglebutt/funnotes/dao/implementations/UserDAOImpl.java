package ru.eaglebutt.funnotes.dao.implementations;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import ru.eaglebutt.funnotes.HibernateUtil;
import ru.eaglebutt.funnotes.dao.interfaces.UserDAO;
import ru.eaglebutt.funnotes.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDAOImpl implements UserDAO {


    public static int SUCCESS = 0;
    public static int FAILED = 1;

    @Override
    public User get(String email, String password) {
        try {
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
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int insert(User user) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            session.save(user);
            session.getTransaction().commit();
            return SUCCESS;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return FAILED;
        }
    }

    @Override
    public int update(User user) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            session.update(user);
            session.getTransaction().commit();
            return SUCCESS;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return FAILED;
        }
    }

    @Override
    public int delete(User user) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            session.delete(user);
            session.getTransaction().commit();
            return SUCCESS;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return FAILED;
        }
    }
}
