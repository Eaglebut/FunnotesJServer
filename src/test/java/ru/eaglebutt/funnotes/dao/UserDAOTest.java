package ru.eaglebutt.funnotes.dao;

import javassist.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.eaglebutt.funnotes.dao.implementations.UserDAOImpl;
import ru.eaglebutt.funnotes.dao.interfaces.UserDAO;
import ru.eaglebutt.funnotes.model.User;

import java.util.Date;

class UserDAOTest {

    UserDAO userDAO = new UserDAOImpl();
    User newUser = new User();
    User serverUser;
    User invalidUser = new User();

    @BeforeEach
    void setUp() {
        newUser.setEmail("test" + System.currentTimeMillis() + "@test.test");
        newUser.setPassword("test");
        newUser.setName("Test");
        newUser.setSurname("Тест");
        newUser.setRegistrationType(0);
        newUser.setRegistrationTime(new Date(System.currentTimeMillis()));

        invalidUser.setEmail("test123@test.ru");
        newUser.setPassword("test");
        newUser.setName("Test");
        newUser.setSurname("Тест");
        newUser.setRegistrationType(0);
        newUser.setRegistrationTime(new Date(System.currentTimeMillis()));


        long user_id = 49L;
        String email = "test@test.ru";
        String password = "test";
        String name = "Тест";
        String surname = "Русский";
        Date registrationTime = new Date(1599130180318L);
        String token = null;
        int registrationType = 0;
        serverUser = new User(user_id, email, password, name, surname, registrationTime, token, registrationType);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void get() {
        try {
            User user = userDAO.get(serverUser.getEmail(), serverUser.getPassword());
            if (userDAO.get(newUser.getEmail(), newUser.getPassword()) != null) {
                Assertions.fail();
            }
            Assertions.assertEquals(user, serverUser);
        } catch (NotFoundException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void insert() {
        try {
            boolean isSuccessful;
            isSuccessful = userDAO.insert(invalidUser) == UserDAOImpl.FAILED;
            isSuccessful = isSuccessful && userDAO.insert(serverUser) == UserDAOImpl.FAILED;
            isSuccessful = isSuccessful && userDAO.insert(newUser) == UserDAOImpl.SUCCESS;
            if (!isSuccessful) {
                Assertions.fail();
            }
            User user = userDAO.get(newUser.getEmail(), newUser.getPassword());
            newUser.setUserId(user.getUserId());
            userDAO.delete(user);
            Assertions.assertEquals(user, newUser);
        } catch (NotFoundException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void update() {
        try {
            serverUser.setName("Test");
            userDAO.update(serverUser);
            User user = userDAO.get(serverUser.getEmail(), serverUser.getPassword());
            Assertions.assertEquals(user, serverUser);
            serverUser.setName("Тест");
            userDAO.update(serverUser);
        } catch (NotFoundException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }

    @Test
    void delete() {
        try {
            newUser.setName("Deleted");
            userDAO.insert(newUser);
            newUser = userDAO.get(newUser.getEmail(), newUser.getPassword());
            userDAO.delete(newUser);
        } catch (NotFoundException e) {
            e.printStackTrace();
            Assertions.fail();
        }
    }
}