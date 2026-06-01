package com.pet.adoption.controller;

import com.pet.adoption.common.Result;
import com.pet.adoption.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "宠物管理", description = "宠物相关接口")
@RestController
@RequestMapping("/api/pet")
public class PetController {

    @Autowired
    private PetService petService;

    @Operation(summary = "获取宠物列表", description = "可根据品种和关键词搜索宠物")
    @GetMapping("/list")
    public Result list(@Parameter(description = "宠物品种", required = false) @RequestParam(required = false) String species,
                       @Parameter(description = "搜索关键词", required = false) @RequestParam(required = false) String keyword) {
        return petService.getPetList(species, keyword);
    }

    @Operation(summary = "获取宠物详情", description = "根据ID获取宠物详细信息")
    @GetMapping("/{id}")
    public Result detail(@Parameter(description = "宠物ID", required = true) @PathVariable Integer id) {
        return petService.getPetDetail(id);
    }
}