package com.sparta.taskflow.domain.board.entity;

import com.sparta.taskflow.common.util.Timestamped;
import com.sparta.taskflow.domain.board.dto.BoardReqDto;
import com.sparta.taskflow.domain.section.entity.Section;
import com.sparta.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board")
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardInvitation> invitations = new ArrayList<>();

    public Board(BoardReqDto boardReqDto,User user) {
        this.name = boardReqDto.getName();
        this.description = boardReqDto.getDescription();
        this.user = user;
    }

    public void update(BoardReqDto boardUpdateReqDto) {
        this.name = boardUpdateReqDto.getName();
        this.description = boardUpdateReqDto.getDescription();
    }
}
