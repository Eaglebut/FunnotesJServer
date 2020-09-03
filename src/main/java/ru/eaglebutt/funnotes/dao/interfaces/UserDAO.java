package ru.eaglebutt.funnotes.dao.interfaces;

import ru.eaglebutt.funnotes.model.User;

public interface UserDAO {

    User get(String email, String password);

    int insert(User user);

    int update(User user);

    int delete(User user);

}
