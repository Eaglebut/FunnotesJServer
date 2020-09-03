package ru.eaglebutt.funnotes.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    private String email;
    private String password;
    private String name;
    private String surname;
    @Column(name = "registration_time"/*, columnDefinition = "default"*/)
    private Date registrationTime;
    private String token;
    @Column(name = "registration_type")
    private int registrationType;
    /*private List<GroupMember> groupMemberList;
    private List<Event> eventList;
    private List<Event> groupEventList;*/


}
