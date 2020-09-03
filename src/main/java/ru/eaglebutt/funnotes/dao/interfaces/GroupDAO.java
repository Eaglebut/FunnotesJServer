package ru.eaglebutt.funnotes.dao.interfaces;

import ru.eaglebutt.funnotes.model.Group;
import ru.eaglebutt.funnotes.model.User;

import java.util.List;

public interface GroupDAO {

    Group get(long groupId);

    List<Group> get(User user);

    void addUser(User user, Group group);

    void deleteUser(User user, Group group);

    void insert(User user);

    void delete(User user, Group group);

    void update(User user, Group group);


}
