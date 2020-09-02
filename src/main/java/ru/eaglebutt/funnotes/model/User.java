package ru.eaglebutt.funnotes.model;

import javax.persistence.*;
import java.util.List;

//u9NFNteP

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long userId;
    private String email;
    private String password;
    private String name;
    private String surname;
    @Column(name = "registration_time")
    private long registrationTime;
    private String token;
    @Column(name = "registration_type")
    private int registrationType;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GroupMember> groupMemberList;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Event> eventList;
    @OneToMany(mappedBy = "group_members")
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

    public long getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(long signUpTime) {
        this.registrationTime = signUpTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(int signUpType) {
        this.registrationType = signUpType;
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
