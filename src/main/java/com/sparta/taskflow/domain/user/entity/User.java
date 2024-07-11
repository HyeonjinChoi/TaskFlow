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

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "introduction", nullable = true)
    private String introduction;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Builder
    public User(String username, String password, String email, String introduction, String nickname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.introduction = introduction;
        this.nickname = nickname;
    }

    public void update(ProfileUpdateReqDto profileUpdateReqDto) {
        this.nickname = profileUpdateReqDto.getNickname();
        this.introduction = profileUpdateReqDto.getInstroduction();
    }

    public void passwordUpdate(String password) {this.password = password;}
}
