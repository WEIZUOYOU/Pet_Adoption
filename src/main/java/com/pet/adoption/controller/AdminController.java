package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Adoption;
import com.pet.adoption.entity.Pet;
import com.pet.adoption.entity.User;
import com.pet.adoption.repository.AdoptionRepository;
import com.pet.adoption.repository.PetRepository;
import com.pet.adoption.repository.UserRepository;
import com.pet.adoption.service.CommentService;
import com.pet.adoption.service.impl.PetServiceImpl;

import com.pet.adoption.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Tag(name = "管理员管理", description = "管理员后台接口")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PetRepository petRepository;
    @Autowired
    private AdoptionRepository adoptionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private PetService petService;

    // ---------- 宠物管理 ----------
    @Operation(summary = "添加宠物", description = "管理员添加新宠物信息")
    @PostMapping("/pet/add")
    public Result addPet(@RequestBody Pet pet) {
        return petService.addPet(pet);
    }

    @Operation(summary = "更新宠物信息", description = "管理员更新宠物信息")
    @PutMapping("/pet/update")
    public Result updatePet(@RequestBody Pet pet) {
        return petService.updatePet(pet);
    }

    @Operation(summary = "删除宠物", description = "管理员删除宠物信息")
    @DeleteMapping("/pet/{id}")
    public Result deletePet(@Parameter(description = "宠物ID", required = true) @PathVariable Integer id) {
        return petService.deletePet(id);
    }

    // ---------- 领养申请管理 ----------
    @Operation(summary = "获取所有领养申请", description = "管理员查看所有领养申请")
    @GetMapping("/adoptions")
    public Result allAdoptions() {
        return Result.success(adoptionRepository.findAll());
    }

    @Operation(summary = "审核领养申请", description = "管理员审核领养申请（通过/拒绝）")
    @PutMapping("/adoption/{id}")
    public Result audit(@Parameter(description = "申请ID", required = true) @PathVariable Integer id, 
                       @RequestBody Map<String, String> body) {
        Adoption adoption = adoptionRepository.findById(id).orElse(null);
        if (adoption == null) {
            return Result.error("申请不存在");
        }
        String status = body.get("status");
        adoption.setStatus(status);
        adoption.setAuditTime(new Date());
        adoptionRepository.save(adoption);
        // 如果通过，可更新宠物状态为已领养（简单处理）
        if ("通过".equals(status)) {
            Pet pet = petRepository.findById(adoption.getPetId()).orElse(null);
            if (pet != null) {
                pet.setStatus("已领养");
                petRepository.save(pet);
            }
        }
        return Result.success("审核完成");
    }

    // ---------- 用户管理 ----------
    @Operation(summary = "获取所有用户", description = "管理员查看所有用户信息")
    @GetMapping("/users")
    public Result allUsers() {
        return Result.success(userRepository.findAll());
    }

    @Operation(summary = "禁用用户", description = "管理员禁用指定用户")
    @PutMapping("/user/{id}/ban")
    public Result banUser(@Parameter(description = "用户ID", required = true) @PathVariable Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return Result.error("用户不存在");
        user.setStatus(1);
        userRepository.save(user);
        return Result.success("用户已禁用");
    }

    @Operation(summary = "启用用户", description = "管理员启用指定用户")
    @PutMapping("/user/{id}/unban")
    public Result unbanUser(@Parameter(description = "用户ID", required = true) @PathVariable Integer id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return Result.error("用户不存在");
        user.setStatus(0);
        userRepository.save(user);
        return Result.success("用户已启用");
    }

    // ---------- 评论管理 ----------
    @Operation(summary = "删除评论", description = "管理员删除指定评论")
    @DeleteMapping("/comment/{id}")
    public Result deleteComment(@Parameter(description = "评论ID", required = true) @PathVariable Integer id) {
        return commentService.deleteComment(id);
    }

    @Operation(summary = "获取所有评论", description = "管理员查看所有评论")
    @GetMapping("/comments")
    public Result allComments() {
        // 简单起见，直接返回所有评论，实际可查所有
        // 此处不在CommentRepository定义findAll，直接注入JPA repository即可，省略。
        return Result.success("获取成功"); // 按需完善
    }
}