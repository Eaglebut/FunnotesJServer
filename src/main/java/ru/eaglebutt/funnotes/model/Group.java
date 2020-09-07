package ru.eaglebutt.funnotes.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "groups")
public class Group implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private long groupId;
    private String name;
    @Column(name = "creation_time")
    private Date creationTime;
    private Types type;
    @OneToMany(mappedBy = "group")
    private List<GroupMember> groupMemberList;
    /*private List<User> memberList;*/

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != Group.class) {
            return false;
        }
        Group group = (Group) obj;
        return group.name.equals(this.name)
                && group.groupId == this.groupId
                && group.creationTime.getTime() == this.creationTime.getTime()
                && group.type == this.type;
    }

    public enum Types {
        ORDINAL
    }
}
