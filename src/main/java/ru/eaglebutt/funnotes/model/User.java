package ru.eaglebutt.funnotes.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

import static ru.eaglebutt.funnotes.utils.Constants.Strings.UserStrings.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = tableName)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = userIDSQLName)
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String surname;
    @Column(name = registrationTimeSQLName)
    private Date registrationTime;
    private String token;
    @Column(name = registrationTypeSQLName)
    private int registrationType;
    /*private List<GroupMember> groupMemberList;
    private List<Event> eventList;
    private List<Event> groupEventList;*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return registrationType == user.registrationType &&
                userId.equals(user.userId) &&
                email.equals(user.email) &&
                password.equals(user.password) &&
                name.equals(user.name) &&
                surname.equals(user.surname) &&
                registrationTime.getTime() == user.getRegistrationTime().getTime() &&
                Objects.equals(token, user.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, password, name, surname, registrationTime, token, registrationType);
    }
}
