package ru.eaglebutt.funnotes.dao.interfaces;

import javassist.NotFoundException;
import ru.eaglebutt.funnotes.model.User;
import ru.eaglebutt.funnotes.utils.Constants;

public interface UserDAO {

    User get(String email, String password) throws NotFoundException;

    Constants.Statuses insert(User user);

    Constants.Statuses update(User user);

    Constants.Statuses delete(User user);

}
