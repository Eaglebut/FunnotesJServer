package ru.eaglebutt.funnotes.dao.implementations;

import javassist.NotFoundException;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import ru.eaglebutt.funnotes.dao.interfaces.GroupDAO;
import ru.eaglebutt.funnotes.model.Group;
import ru.eaglebutt.funnotes.model.GroupMember;
import ru.eaglebutt.funnotes.model.User;
import ru.eaglebutt.funnotes.utils.Constants;
import ru.eaglebutt.funnotes.utils.HibernateUtil;

import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static ru.eaglebutt.funnotes.utils.Constants.Statuses.*;
import static ru.eaglebutt.funnotes.utils.Constants.Strings.GroupStrings.GroupNotFoundedExceptionString;

public class GroupDAOImpl implements GroupDAO {


    @Override
    public Group get(long groupId) throws NotFoundException {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            Group group = session.get(Group.class, groupId);
            session.getTransaction().commit();
            session.close();
            if (group == null)
                throw new NotFoundException(GroupNotFoundedExceptionString);
            return group;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        }
        throw new NotFoundException(GroupNotFoundedExceptionString);
    }

    @Override
    public List<Group> get(User user) {
        Session session = HibernateUtil.getSession();

        return null;
    }


    @Override
    public List<Group> getAll() {
        Session session = HibernateUtil.getSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
        Root<Group> root = criteriaQuery.from(Group.class);
        criteriaQuery.select(root);
        List<Group> resultList = session.createQuery(criteriaQuery).getResultList();
        session.close();
        return resultList;
    }

    @Override
    public Constants.Statuses addUser(User user, Group group, GroupMember.Statuses status) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            GroupMember member = new GroupMember();
            member.setUserId(user.getUserId());
            member.setGroupId(group.getGroupId());
            member.setStatus(status);
            session.save(member);
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return FAILED;
    }

    @Override
    public Constants.Statuses deleteUser(User user, Group group) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            GroupMember member = new GroupMember();
            member.setUserId(user.getUserId());
            member.setGroupId(group.getGroupId());
            session.delete(member);
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return FAILED;
    }

    @Override
    public Constants.Statuses insert(User user, Group group) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            session.save(group);
            session.getTransaction().commit();
            session.getTransaction().begin();
            group = session.get(Group.class, group.getGroupId());
            session.getTransaction().commit();
            session.getTransaction().begin();
            GroupMember creator = new GroupMember();
            creator.setUserId(user.getUserId());
            creator.setGroupId(group.getGroupId());
            creator.setStatus(GroupMember.Statuses.CREATOR);
            creator.setUser(user);
            session.save(creator);
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
        }
        return FAILED;
    }

    private GroupMember.Statuses checkUserStatus(User user, Group group) throws NotFoundException {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            GroupMember member = new GroupMember();
            member.setUserId(user.getUserId());
            member.setGroupId(group.getGroupId());
            member = session.get(GroupMember.class, member);
            session.getTransaction().commit();
            session.close();
            if (member == null)
                throw new NotFoundException("Membership not founded");
            return member.getStatus();
        } catch (PersistenceException e) {
            throw new NotFoundException("Membership not founded");
        }
    }

    @Override
    public Constants.Statuses delete(User user, Group group) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            if (checkUserStatus(user, group) == GroupMember.Statuses.CREATOR) {
                session.delete(group);
            } else {
                return FORBIDDEN;
            }
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (PersistenceException e) {
            return FAILED;
        } catch (NotFoundException e) {
            return FORBIDDEN;
        }
    }

    @Override
    public Constants.Statuses update(User user, Group group) {
        try {
            Session session = HibernateUtil.getSession();
            session.getTransaction().begin();
            if (checkUserStatus(user, group) == GroupMember.Statuses.CREATOR) {
                session.update(group);
            } else {
                return FORBIDDEN;
            }
            session.getTransaction().commit();
            session.close();
            return SUCCESSFUL;
        } catch (PersistenceException e) {
            return FAILED;
        } catch (NotFoundException e) {
            return FORBIDDEN;
        }
    }
}
