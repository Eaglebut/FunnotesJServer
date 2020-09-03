package ru.eaglebutt.funnotes.dao.interfaces;

import ru.eaglebutt.funnotes.model.Group;
import ru.eaglebutt.funnotes.model.User;

import java.util.List;

public interface GroupDAO {

    Group get(long groupId);

    List<Group> get(User user);

    List<Group> getAll();

    int addUser(User user, Group group);

    int deleteUser(User user, Group group);

    int insert(User user);

    int delete(User user, Group group);

    int update(User user, Group group);


}
