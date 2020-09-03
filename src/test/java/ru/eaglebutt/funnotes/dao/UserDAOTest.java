package ru.eaglebutt.funnotes.dao;

import org.junit.jupiter.api.AfterEach;
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

    @BeforeEach
    void setUp() {
        newUser.setEmail("test" + System.currentTimeMillis() + "@test.test");
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
        User user = userDAO.get(serverUser.getEmail(), serverUser.getPassword());
        if (user.getRegistrationTime().getTime() == serverUser.getRegistrationTime().getTime())
            user.setRegistrationTime(serverUser.getRegistrationTime());
    }

    @Test
    void insert() {
        userDAO.insert(newUser);
    }

    @Test
    void update() {
        serverUser.setName("Test");
        userDAO.update(serverUser);
        serverUser.setName("Тест");
        userDAO.update(serverUser);
    }

    @Test
    void delete() {
        userDAO.insert(newUser);
        newUser = userDAO.get(newUser.getEmail(), newUser.getPassword());
        userDAO.delete(newUser);
    }
}