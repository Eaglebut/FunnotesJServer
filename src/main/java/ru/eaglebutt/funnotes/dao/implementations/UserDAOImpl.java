package ru.eaglebutt.funnotes.dao.implementations;

import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import ru.eaglebutt.funnotes.dao.interfaces.UserDAO;
import ru.eaglebutt.funnotes.model.User;
import ru.eaglebutt.funnotes.utils.Constants;
import ru.eaglebutt.funnotes.utils.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.eaglebutt.funnotes.utils.Constants.Statuses.FAILED;
import static ru.eaglebutt.funnotes.utils.Constants.Statuses.SUCCESSFUL;
import static ru.eaglebutt.funnotes.utils.Constants.Strings.UserStrings.UserNotFoundedExceptionString;
import static ru.eaglebutt.funnotes.utils.Constants.Strings.UserStrings.emailSQLName;


public class UserDAOImpl implements UserDAO {



    @Override
    public User get(String email, String password) throws NotFoundException {
        try {
            Session session = HibernateUtil.getSession();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            criteriaQuery.select(root);
            criteriaQuery.where(criteriaBuilder.equal(root.get(emailSQLName), email));
            List<User> users = session.createQuery(criteriaQuery).getResultList();
            session.close();
            for (User user : users) {
                if (user.getPassword().equals(password)) {
                    return user;
                }
            }
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        }
        throw new NotFoundException(UserNotFoundedExceptionString);
    }

    @Override
    public Constants.Statuses insert(User user) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            session.save(user);
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return FAILED;
        }
    }

    @Override
    public Constants.Statuses update(User user) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            session.update(user);
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return FAILED;
        }
    }

    @Override
    public Constants.Statuses delete(User user) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            session.delete(user);
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            return FAILED;
        }
    }
}
