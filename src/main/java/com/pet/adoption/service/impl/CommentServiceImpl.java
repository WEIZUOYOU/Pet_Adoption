package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.dto.CommentRequest;
import com.pet.adoption.entity.Comment;
import com.pet.adoption.entity.User;
import com.pet.adoption.repository.CommentRepository;
import com.pet.adoption.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Result addComment(CommentRequest request, HttpSession session) {
        User user = SessionUtils.getUser(session);
        Comment comment = new Comment();
        comment.setPetId(request.getPetId());
        comment.setUserId(user.getId());
        comment.setContent(request.getContent());
        comment.setCreateTime(new Date());
        commentRepository.save(comment);
        return Result.success("评论成功");
    }

    @Override
    public Result getCommentsByPet(Integer petId) {
        List<Comment> list = commentRepository.findByPetIdOrderByCreateTimeDesc(petId);
        return Result.success(list);
    }

    @Override
    public Result deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
        return Result.success("删除成功");
    }
}