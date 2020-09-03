package ru.eaglebutt.funnotes.dao.implementations;

import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import ru.eaglebutt.funnotes.HibernateUtil;
import ru.eaglebutt.funnotes.dao.interfaces.GroupDAO;
import ru.eaglebutt.funnotes.model.Group;
import ru.eaglebutt.funnotes.model.User;

import java.util.List;

public class GroupDAOImpl implements GroupDAO {

    @Override
    public Group get(long groupId) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();

            Group group = session.get(Group.class, groupId);
            session.getTransaction().commit();
            return group;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Group> get(User user) {
        return null;
    }

    @Override
    public List<Group> getAll() {
        return null;
    }

    @Override
    public int addUser(User user, Group group) {
        return 0;
    }

    @Override
    public int deleteUser(User user, Group group) {
        return 0;
    }

    @Override
    public int insert(User user) {
        return 0;
    }

    @Override
    public int delete(User user, Group group) {
        return 0;
    }

    @Override
    public int update(User user, Group group) {
        return 0;
    }
}
