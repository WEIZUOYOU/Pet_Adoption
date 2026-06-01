package com.pet.adoption.service;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.CommentRequest;
import javax.servlet.http.HttpSession;

public interface CommentService {
    Result addComment(CommentRequest request, HttpSession session);
    Result getCommentsByPet(Integer petId);
    Result deleteComment(Integer commentId);
}