package com.example.demo.board.board.domain;

import com.example.demo.board.board.presentation.dto.BoardRequest;
import com.example.demo.board.boardscore.domain.BoardScore;
import com.example.demo.board.comment.domain.Comment;
import com.example.demo.config.util.BaseTime;
import com.example.demo.member.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String locationName;

    @Column(nullable = false)
    private String formattedAddress;

    // 경도
    @Column(nullable = false)
    private String longitude;

    // 위도
    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private Integer view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    private List<BoardScore> comment;

    @Column(nullable = false)
    private Integer avgScore;

    @Column(nullable = false)
    private Integer commentCount;

    public void update(BoardRequest.BoardUpdateRequest request){
        this.locationName = request.locationName();
        this.formattedAddress = request.formattedAddress();
        this.latitude = request.latitude();
        this.longitude = request.longitude();
        this.title = request.title();
        this.body = request.body();
    }

    public void addViewCount() {
        this.view += 1;
    }

    public void commentCountUp(){this.commentCount += 1;}

    public void commentCountDown(){this.commentCount -= 1;}

    public void avgScorePatch(Integer avgScore) {
        this.avgScore = avgScore;
    }

}
