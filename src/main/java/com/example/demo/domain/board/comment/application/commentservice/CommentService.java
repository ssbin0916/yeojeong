package com.example.demo.domain.board.comment.application.commentservice;

import com.example.demo.domain.board.comment.presentation.dto.CommentRequest;
import com.example.demo.domain.board.comment.presentation.dto.CommentResponse;
import org.springframework.data.domain.Page;

public interface CommentService {
    void save(CommentRequest.Save takenDto, Long takenBoardId, Long takenMemberId);
    Page<CommentResponse.FindByBoardId> findByBoardId(Long takenBoardId, int page);
    void updateById(Long commentId, Long takenMemberId, CommentRequest.Edit editDto);
    void deleteById(Long commentId, Long takenMemberId);
}
