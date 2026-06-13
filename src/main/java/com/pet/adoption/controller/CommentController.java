package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Comment;
import com.pet.adoption.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "评论接口")
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 1. 获取评论列表 GET /api/comment/list?petId=xxx
     */
    @GetMapping("/list")
    public Map<String, Object> getCommentList(@RequestParam Long petId) {
        Map<String, Object> result = new HashMap<>();
        List<Comment> list = commentService.getCommentsByPetId(petId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    /**
     * 2. 发布评论 POST /api/comment/add
     */
    @PostMapping("/add")
    public Map<String, Object> addComment(@RequestBody Comment comment) {
        Map<String, Object> result = new HashMap<>();

        // 日志打印，方便定位问题
        System.out.println("收到评论请求：petId=" + comment.getPetId() + ", content=" + comment.getContent());

        // 必填字段校验
        if (comment.getPetId() == null || comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            result.put("code", 400);
            result.put("msg", "宠物ID和评论内容不能为空");
            return result;
        }

        // 写死昵称，后续对接登录可以改成当前用户
        comment.setNickname("游客");
        Comment saved = commentService.addComment(comment);

        System.out.println("评论保存成功：id=" + saved.getId());

        result.put("code", 200);
        result.put("data", saved);
        return result;
    }

    /**
     * 3. 点赞评论 POST /api/comment/like
     */
    @PostMapping("/like")
    public Map<String, Object> likeComment(@RequestBody Map<String, Long> param) {
        Map<String, Object> result = new HashMap<>();
        Long id = param.get("id");
        commentService.likeComment(id);
        result.put("code", 200);
        result.put("msg", "点赞成功");
        return result;
    }

    // ====================== 后台管理接口 开始 ======================

    @Operation(summary = "查询所有评论（后台管理）")
    @GetMapping("/listAll")
    public Result listAllComment(){
        return commentService.listAllComment();
    }

    @Operation(summary = "删除评论（后台管理）")
    @DeleteMapping("/admin/{id}")
    public Result adminDeleteComment(@PathVariable Long id){
        return commentService.adminDeleteComment(id);
    }

    // ====================== 后台管理接口 结束 ======================
}