package ru.eaglebutt.funnotes.dao;

import ru.eaglebutt.funnotes.model.Event;
import ru.eaglebutt.funnotes.model.Group;
import ru.eaglebutt.funnotes.model.User;

import java.util.List;

public interface EventDAO {

    List<Event> get(User user);

    void insert(User user, Event event);

    void insert(User user, Group group, Event event);

    void update(User user, Event event);

    void delete(User user, Event event);

}
