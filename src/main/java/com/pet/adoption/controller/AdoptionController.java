package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.dto.AdoptionRequest;
import com.pet.adoption.service.AdoptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/adoption")
@Tag(name = "领养申请管理", description = "用户领养申请相关接口")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @Operation(summary = "提交领养申请", description = "用户提交宠物领养申请，需要登录")
    @PostMapping("/apply")
    public Result apply(@RequestBody AdoptionRequest request, HttpSession session) {
        return adoptionService.apply(request, session);
    }

    @Operation(summary = "我的申请列表", description = "查看当前用户的领养申请记录（分页）")
    @GetMapping("/my")
    public Result myApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpSession session) {
        // 参数验证
        if (page < 1) {
            page = 1;
        }
        if (size < 1 || size > 50) {
            size = 5; // 默认值，最大50
        }
        
        return adoptionService.myApplications(session, page, size);
    }

    @Operation(summary = "获取申请详情", description = "查看单个申请的详细信息")
    @GetMapping("/{appId}")
    public Result detail(@PathVariable Integer appId, HttpSession session) {
        return adoptionService.getApplicationDetail(appId, session);
    }

    @Operation(summary = "撤回申请", description = "用户撤回待审核的领养申请")
    @PostMapping("/cancel/{appId}")
    public Result cancel(@PathVariable Integer appId, HttpSession session) {
        return adoptionService.cancelApplication(appId, session);
    }
}