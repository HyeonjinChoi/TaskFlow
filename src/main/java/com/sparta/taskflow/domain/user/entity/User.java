package com.sparta.taskflow.domain.user.entity;

import com.sparta.taskflow.common.util.Timestamped;
import com.sparta.taskflow.domain.user.dto.ProfileUpdateReqDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String refreshToken;

    public void addRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


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

    @Builder
    public User(Long id, String username, String password,String email, String nickname, String introduction, Role role, Status status, String refreshToken) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.introduction = introduction;
        this.role = role != null ? role : Role.USER;
        this.status = status != null ? status : Status.NORMAL;
        this.refreshToken = refreshToken;
    }

    public void update(ProfileUpdateReqDto profileUpdateReqDto) {
        this.nickname = profileUpdateReqDto.getNickname();
        this.introduction = profileUpdateReqDto.getInstroduction();
    }

    public void passwordUpdate(String password) {this.password = password;}
}
