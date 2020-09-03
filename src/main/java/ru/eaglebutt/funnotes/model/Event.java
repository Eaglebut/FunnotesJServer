package ru.eaglebutt.funnotes.model;

import lombok.*;

import java.util.Date;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private long eventId;
    private User user;
    private Group group;
    private String title;
    private String description;
    private Date time;
    private Date creationTime;
    private Date lastUpdated;
    private int status;
    private int repetitionType;
    private int remindType;
    private int importance;
}