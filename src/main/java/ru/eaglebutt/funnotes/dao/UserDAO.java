package ru.eaglebutt.funnotes.dao;

import ru.eaglebutt.funnotes.model.Event;
import ru.eaglebutt.funnotes.model.User;

import java.util.List;

public interface UserDAO {

    User get(String email, String password);

    void insert(User user);

    void update(User user);

    void delete(User user);

    List<Event> getGroupEventList(User user);
}
