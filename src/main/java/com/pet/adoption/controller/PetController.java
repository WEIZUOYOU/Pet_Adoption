package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "宠物管理", description = "宠物相关接口")
@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "获取宠物列表", description = "分页获取宠物，支持种类筛选和关键词搜索")
    @GetMapping("/list")
    public Result list(
            @Parameter(description = "宠物品种（猫/狗/其他）") @RequestParam(required = false) String species,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "8") int size) {
        // 参数验证
        if (page < 1) {
            page = 1;
        }
        if (size < 1 || size > 50) {
            size = 8; // 默认值，最大50
        }
        
        Pageable pageable = PageRequest.of(page - 1, size);
        return petService.getPetList(species, keyword, pageable);
    }

    @Operation(summary = "获取宠物详情")
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        return petService.getPetDetail(id);
    }
}