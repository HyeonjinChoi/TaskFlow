package com.sparta.taskflow.domain.board.entity;

import com.sparta.taskflow.common.util.Timestamped;
import com.sparta.taskflow.domain.board.dto.BoardReqDto;
import com.sparta.taskflow.domain.section.entity.Section;
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

    public Board(BoardReqDto boardReqDto) {
        this.name = boardReqDto.getName();
        this.description = boardReqDto.getDescription();
    }

    public void update(BoardReqDto boardUpdateReqDto) {
        this.name = boardUpdateReqDto.getName();
        this.description = boardUpdateReqDto.getDescription();
    }
}
