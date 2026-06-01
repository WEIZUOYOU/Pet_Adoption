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

    @Operation(summary = "添加评论", description = "用户为宠物添加评论")
    @PostMapping("/add")
    public Result add(@RequestBody CommentRequest request, HttpSession session) {
        return commentService.addComment(request, session);
    }

    @Operation(summary = "获取宠物评论列表", description = "根据宠物ID获取该宠物的所有评论")
    @GetMapping("/list/{petId}")
    public Result listByPet(@Parameter(description = "宠物ID", required = true) @PathVariable Integer petId) {
        return commentService.getCommentsByPet(petId);
    }

    // 管理员删除评论接口在 AdminController 中
}