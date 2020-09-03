package ru.eaglebutt.funnotes.model;

import lombok.*;

import java.util.Date;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private long groupId;
    private String name;
    private Date creationTime;
    private int type;
    /*private List<GroupMember> groupMemberList;
    private List<User> memberList;*/
}
