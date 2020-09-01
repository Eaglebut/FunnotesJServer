package ru.eaglebutt.funnotes.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

//u9NFNteP

@Entity
@Table(name = "users")
public class User {
    private long userId;
    private String email;
    private String password;
    private String name;
    private String surname;
    private long signUpTime;
    private String token;
    private int signUpType;
    private List<GroupMember> groupMemberList;
    private List<Event> eventList;
    private List<Event> groupEventList;

    public User() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public long getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(long signUpTime) {
        this.signUpTime = signUpTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSignUpType() {
        return signUpType;
    }

    public void setSignUpType(int signUpType) {
        this.signUpType = signUpType;
    }

    public List<GroupMember> getGroupMemberList() {
        return groupMemberList;
    }

    public void setGroupMemberList(List<GroupMember> groupMemberList) {
        this.groupMemberList = groupMemberList;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Event> getGroupEventList() {
        return groupEventList;
    }

    public void setGroupEventList(List<Event> groupEventList) {
        this.groupEventList = groupEventList;
    }
}
