package com.sparta.taskflow.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String refreshToken;


    //:::::::::::// ENUM //::::::::::://
    public enum Role{
        USER,
        MANAGER
    }
    public enum Status{
        NORMAL,
        DELETED
    }

    public boolean isBlocked(){
        return this.status == Status.DELETED;
    }

    @Transient
    public boolean isActivity() {
        return this.status == Status.NORMAL;
    }

}
