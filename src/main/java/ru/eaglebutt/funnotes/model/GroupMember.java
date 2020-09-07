package ru.eaglebutt.funnotes.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_members")
public class GroupMember implements Serializable {
    @Id
    @Column(name = "group_id")
    private long groupId;
    @Id
    @Column(name = "user_id")
    private long userId;
    private Statuses status;
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum Statuses {
        ORDINAL,
        CREATOR
    }
}
