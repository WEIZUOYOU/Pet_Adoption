package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.CommentRequest;
import com.pet.adoption.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Tag(name = "评论管理", description = "评论相关接口")
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "添加评论", description = "用户为宠物添加评论，需要登录")
    @PostMapping("/add")
    public Result add(@RequestBody CommentRequest request, HttpSession session) {
        return commentService.addComment(request, session);
    }

    @Operation(summary = "获取宠物评论列表", description = "根据宠物ID获取该宠物的所有评论（分页）")
    @GetMapping("/list/{petId}")
    public Result listByPet(
            @Parameter(description = "宠物ID", required = true) @PathVariable Integer petId,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        // 参数验证
        if (page < 1) {
            page = 1;
        }
        if (size < 1 || size > 50) {
            size = 10; // 默认值，最大50
        }
        
        return commentService.getCommentsByPet(petId, page, size);
    }

    @Operation(summary = "删除评论", description = "用户只能删除自己的评论，管理员可以删除任意评论")
    @DeleteMapping("/{commentId}")
    public Result delete(
            @Parameter(description = "评论ID", required = true) @PathVariable Integer commentId,
            HttpSession session) {
        return commentService.deleteComment(commentId, session);
    }
}