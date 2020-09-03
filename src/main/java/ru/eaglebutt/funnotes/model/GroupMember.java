package ru.eaglebutt.funnotes.model;

import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor

public class GroupMember {
    private Group group;
    private User user;
    private int status;
}
