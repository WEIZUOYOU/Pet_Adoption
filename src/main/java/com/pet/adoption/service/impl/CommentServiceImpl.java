package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Comment;
import com.pet.adoption.repository.CommentRepository;
import com.pet.adoption.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> getCommentsByPetId(Long petId) {
        return commentRepository.findByPetIdOrderByCreateTimeDesc(petId);
    }

    @Override
    public Comment addComment(Comment comment) {
        comment.setCreateTime(new Date());
        comment.setLikes(0);
        return commentRepository.save(comment);
    }

    @Override
    public void likeComment(Long commentId) {
        Optional<Comment> opt = commentRepository.findById(commentId);
        if (opt.isPresent()) {
            Comment c = opt.get();
            c.setLikes(c.getLikes() + 1);
            commentRepository.save(c);
        }
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Result listAllComment() {
        List<Comment> allComment = commentRepository.findAll();
        return Result.success(allComment);
    }

    @Override
    public Result adminDeleteComment(Long commentId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            return Result.error("评论不存在");
        }
        commentRepository.deleteById(commentId);
        return Result.success("删除成功");
    }
}