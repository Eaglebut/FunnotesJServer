package ru.eaglebutt.funnotes.dao.interfaces;

import ru.eaglebutt.funnotes.model.User;

public interface UserDAO {

    User get(String email, String password);

    void insert(User user);

    void update(User user);

    void delete(User user);

}
