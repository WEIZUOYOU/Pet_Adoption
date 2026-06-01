package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.dto.CommentRequest;
import com.pet.adoption.entity.Comment;
import com.pet.adoption.entity.User;
import com.pet.adoption.repository.CommentRepository;
import com.pet.adoption.repository.UserRepository;
import com.pet.adoption.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public Result addComment(CommentRequest request, HttpSession session) {
        User user = SessionUtils.getUser(session);
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        
        // 验证评论内容
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return Result.error("评论内容不能为空");
        }
        if (request.getContent().length() > 500) {
            return Result.error("评论内容不能超过500个字符");
        }
        
        Comment comment = new Comment();
        comment.setPetId(request.getPetId());
        comment.setUserId(user.getId());
        comment.setContent(request.getContent().trim());
        comment.setCreateTime(new Date());
        commentRepository.save(comment);
        return Result.success("评论成功");
    }

    @Override
    public Result getCommentsByPet(Integer petId, int page, int size) {
        // 参数验证
        if (page < 1) {
            page = 1;
        }
        if (size < 1 || size > 50) {
            size = 10; // 默认值，最大50
        }
        
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Comment> commentPage = commentRepository.findByPetIdOrderByCreateTimeDesc(petId, pageable);
        
        // 增强：为每条评论添加用户昵称
        List<Map<String, Object>> enhancedList = commentPage.getContent().stream()
            .map(comment -> {
                Map<String, Object> item = new HashMap<>();
                item.put("comment", comment);
                
                // 获取用户信息
                User user = userRepository.findById(comment.getUserId()).orElse(null);
                if (user != null) {
                    item.put("nickname", user.getNickname());
                } else {
                    item.put("nickname", "未知用户");
                }
                
                return item;
            })
            .collect(Collectors.toList());
        
        // 构建分页响应
        Map<String, Object> result = new HashMap<>();
        result.put("content", enhancedList);
        result.put("totalElements", commentPage.getTotalElements());
        result.put("totalPages", commentPage.getTotalPages());
        result.put("number", commentPage.getNumber());
        result.put("size", commentPage.getSize());
        result.put("first", commentPage.isFirst());
        result.put("last", commentPage.isLast());
        
        return Result.success(result);
    }

    @Override
    public Result deleteComment(Integer commentId, HttpSession session) {
        User user = SessionUtils.getUser(session);
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            return Result.error("评论不存在");
        }
        
        // 权限控制：只有评论作者或管理员可以删除
        if (!comment.getUserId().equals(user.getId()) && user.getRole() != 1) {
            return Result.error(403, "无权限删除该评论");
        }
        
        commentRepository.deleteById(commentId);
        return Result.success("删除成功");
    }
}