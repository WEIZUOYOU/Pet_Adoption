package com.pet.adoption.service;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.CommentRequest;
import javax.servlet.http.HttpSession;

public interface CommentService {
    Result addComment(CommentRequest request, HttpSession session);
    Result getCommentsByPet(Integer petId, int page, int size);  // 修改：添加分页参数
    Result deleteComment(Integer commentId, HttpSession session);  // 修改：添加 session 用于权限验证
}