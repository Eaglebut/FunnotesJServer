package ru.eaglebutt.funnotes.model;

import javax.persistence.*;

@Entity
@Table(name = "group_members")
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long membersId;
    @ManyToOne
    private Group group;
    @ManyToOne
    private User user;
    private int status;

    public long getMembersId() {
        return membersId;
    }

    public void setMembersId(long membersId) {
        this.membersId = membersId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
