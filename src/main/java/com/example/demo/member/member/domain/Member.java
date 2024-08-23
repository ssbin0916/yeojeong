package com.example.demo.member.member.domain;

import com.example.demo.board.board.domain.Board;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.config.exception.ServerException;
import com.example.demo.member.member.presentation.dto.MemberRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;


@Entity @Getter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false)
    private String role;

//    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
//    private List<Board> board;
//
//    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
//    private List<BoardScore> boardScore;
//
//    @OneToMany(mappedBy = "member", orphanRemoval = true)
//    private List<Comment> comments;

    public void patchPassword(String password) {
        this.password = password;
    }

    public void patchMember(MemberRequest.PatchMember takenDto, String newPassword) {
        if(newPassword != null) {
            if (takenDto.password().equals(newPassword)) throw new ServerException("비밀번호 암호화가 진행되지 않았습니다");
            this.password = newPassword;
        }
        if(takenDto.nickname() != null) this.nickname = takenDto.nickname();
    }
}
