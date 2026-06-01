package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.AdoptionRequest;
import com.pet.adoption.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Tag(name = "领养申请管理", description = "领养申请相关接口")
@RestController
@RequestMapping("/api/adoption")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @Operation(summary = "提交领养申请", description = "用户提交宠物领养申请")
    @PostMapping("/apply")
    public Result apply(@RequestBody AdoptionRequest request, HttpSession session) {
        return adoptionService.apply(request, session);
    }

    @Operation(summary = "查看我的申请", description = "查看当前用户的所有领养申请记录")
    @GetMapping("/my")
    public Result myApplications(HttpSession session) {
        return adoptionService.myApplications(session);
    }
}