package ru.eaglebutt.funnotes.dao;

import javassist.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.eaglebutt.funnotes.dao.implementations.GroupDAOImpl;
import ru.eaglebutt.funnotes.dao.interfaces.GroupDAO;
import ru.eaglebutt.funnotes.model.Group;
import ru.eaglebutt.funnotes.model.GroupMember;
import ru.eaglebutt.funnotes.model.User;
import ru.eaglebutt.funnotes.utils.Constants;

import java.util.Date;
import java.util.List;

class GroupDAOTest {

    private GroupDAO groupDAO = new GroupDAOImpl();
    private Group serverGroup;

    private User serverUser;

    @BeforeEach
    void setUp() {
        serverGroup = new Group();
        serverGroup.setGroupId(49);
        serverGroup.setType(Group.Types.ORDINAL);
        serverGroup.setCreationTime(new Date(1599474768348L));
        serverGroup.setName("Тестовая");

        long user_id = 49L;
        String email = "test@test.ru";
        String password = "test";
        String name = "Тест";
        String surname = "Русский";
        Date registrationTime = new Date(1599130180318L);
        String token = null;
        User.RegistrationTypes registrationType = User.RegistrationTypes.ORDINAL;
        serverUser = new User(
                user_id,
                email,
                password,
                name,
                surname,
                registrationTime,
                token,
                registrationType,
                null);
    }

    @Test
    void get() {
        Group group = null;
        try {
            group = groupDAO.get(serverGroup.getGroupId());
        } catch (NotFoundException e) {
            Assertions.fail();
        }
        try {
            groupDAO.get(0);
            Assertions.fail();
        } catch (NotFoundException ignored) {
            System.out.println(group.getCreationTime().getTime());
            System.out.println(serverGroup.getCreationTime().getTime());
            Assertions.assertEquals(group, serverGroup);
        }
    }

    @Test
    void getUserGroups() {
        groupDAO.get(serverUser);
    }

    @Test
    void getAll() {
        List<Group> groupList = groupDAO.getAll();
        Assertions.assertTrue(groupList.contains(serverGroup));
    }

    @Test
    void addAndDeleteUser() {
        if (groupDAO.addUser(serverUser, serverGroup, GroupMember.Statuses.ORDINAL) != Constants.Statuses.SUCCESSFUL) {
            Assertions.fail();
        }
        if (groupDAO.deleteUser(serverUser, serverGroup) != Constants.Statuses.SUCCESSFUL) {
            Assertions.fail();
        }
    }


    @Test
    void insert() {
        Group group = new Group();
        group.setName("Test");
        group.setType(Group.Types.ORDINAL);
        group.setCreationTime(new Date(System.currentTimeMillis()));
        Assertions.assertEquals(groupDAO.insert(serverUser, group), Constants.Statuses.SUCCESSFUL);
    }

    @Test
    void delete() {
        Group group = new Group();
        group.setName("Test");
        group.setType(Group.Types.ORDINAL);
        group.setCreationTime(new Date(System.currentTimeMillis()));
        if (groupDAO.insert(serverUser, group) != Constants.Statuses.SUCCESSFUL) {
            Assertions.fail();
        }
        if (groupDAO.delete(serverUser, group) != Constants.Statuses.SUCCESSFUL) {
            Assertions.fail();
        }
        if (groupDAO.delete(serverUser, group) == Constants.Statuses.SUCCESSFUL) {
            Assertions.fail();
        }

    }

    @Test
    void update() {
        Group group = new Group();
        group.setName("Test");
        group.setType(Group.Types.ORDINAL);
        group.setCreationTime(new Date(System.currentTimeMillis()));
        if (groupDAO.insert(serverUser, group) != Constants.Statuses.SUCCESSFUL) {
            Assertions.fail();
        }
        group.setName("updated");
        if (groupDAO.update(serverUser, group) != Constants.Statuses.SUCCESSFUL) {
            Assertions.fail();
        }
        try {
            Group updatedGroup = groupDAO.get(group.getGroupId());
            System.out.println(group.getCreationTime().getTime());
            System.out.println(updatedGroup.getCreationTime().getTime());
            Assertions.assertEquals(group, updatedGroup);
        } catch (NotFoundException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}