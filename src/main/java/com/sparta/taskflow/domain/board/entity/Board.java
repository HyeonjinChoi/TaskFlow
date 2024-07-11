package com.sparta.taskflow.domain.board.entity;

import com.sparta.taskflow.common.util.Timestamped;
import com.sparta.taskflow.domain.board.dto.BoardCreateReqDto;
import com.sparta.taskflow.domain.board.dto.BoardUpdateReqDto;
import com.sparta.taskflow.domain.section.entity.Section;
import com.sparta.taskflow.domain.user.dto.ProfileUpdateReqDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.config.Task;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardInvitation> invitations = new ArrayList<>();

    public Board(BoardCreateReqDto boardCreateReqDto) {
        this.name = boardCreateReqDto.getName();
        this.description = boardCreateReqDto.getDescription();
    }

    public void update(BoardUpdateReqDto boardUpdateReqDto) {
        this.name = boardUpdateReqDto.getName();
        this.description = boardUpdateReqDto.getDescription();
    }
}
