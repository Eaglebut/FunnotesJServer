package ru.eaglebutt.funnotes.dao.interfaces;

import javassist.NotFoundException;
import ru.eaglebutt.funnotes.model.Group;
import ru.eaglebutt.funnotes.model.GroupMember;
import ru.eaglebutt.funnotes.model.User;
import ru.eaglebutt.funnotes.utils.Constants;

import java.util.List;

public interface GroupDAO {

    Group get(long groupId) throws NotFoundException;

    List<Group> get(User user);

    List<Group> getAll();

    Constants.Statuses addUser(User user, Group group, GroupMember.Statuses status);

    Constants.Statuses deleteUser(User user, Group group);

    Constants.Statuses insert(User user, Group group);

    Constants.Statuses delete(User user, Group group);

    Constants.Statuses update(User user, Group group);


}
