package com.pet.adoption.service;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Comment;
import java.util.List;

public interface CommentService {
    // 用户评论接口
    List<Comment> getCommentsByPetId(Long petId);
    Comment addComment(Comment comment);
    void likeComment(Long commentId);
    void deleteComment(Long commentId);

    // 后台管理接口
    Result listAllComment();
    Result adminDeleteComment(Long commentId);
}