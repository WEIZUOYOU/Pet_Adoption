package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.common.SessionUtils;
import com.pet.adoption.dto.AdoptionRequest;
import com.pet.adoption.entity.Adoption;
import com.pet.adoption.entity.Pet;
import com.pet.adoption.entity.User;
import com.pet.adoption.repository.AdoptionRepository;
import com.pet.adoption.repository.PetRepository;
import com.pet.adoption.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    @Autowired
    private AdoptionRepository adoptionRepository;
    @Autowired
    private PetRepository petRepository;

    @Override
    public Result apply(AdoptionRequest request, HttpSession session) {
        User user = SessionUtils.getUser(session);
        if (user == null) {
            return Result.error(401, "请先登录");
        }

        // 验证请求参数
        if (request.getPetId() == null) {
            return Result.error("宠物ID不能为空");
        }
        if (request.getReason() == null || request.getReason().trim().isEmpty()) {
            return Result.error("申请理由不能为空");
        }
        if (request.getReason().length() < 10) {
            return Result.error("申请理由至少10个字符");
        }
        if (request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            return Result.error("居住地址不能为空");
        }

        // 1. 检查宠物是否存在且状态为“待领养”
        Pet pet = petRepository.findById(request.getPetId()).orElse(null);
        if (pet == null) {
            return Result.error("宠物不存在");
        }
        if (!"待领养".equals(pet.getStatus())) {
            return Result.error("该宠物不可申请领养（当前状态：" + pet.getStatus() + "）");
        }

        // 2. 防止重复申请：检查是否已有“待审核”或“通过”的申请
        boolean exists = adoptionRepository.existsPendingOrApproved(request.getPetId(), user.getId());
        if (exists) {
            return Result.error("您已经对该宠物提交过申请，请等待审核结果或联系管理员");
        }

        // 3. 创建申请
        Adoption adoption = new Adoption();
        adoption.setPetId(request.getPetId());
        adoption.setUserId(user.getId());
        adoption.setReason(request.getReason().trim());
        adoption.setAddress(request.getAddress().trim());
        adoption.setStatus("待审核");
        adoption.setApplyTime(new Date());
        adoptionRepository.save(adoption);
        
        return Result.success("申请已提交，请耐心等待管理员审核");
    }

    @Override
    public Result myApplications(HttpSession session, int page, int size) {
        User user = SessionUtils.getUser(session);
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Adoption> adoptionPage = adoptionRepository.findByUserId(user.getId(), pageable);
        
        // 增强：为每个申请添加宠物信息
        java.util.List<java.util.Map<String, Object>> enhancedList = new java.util.ArrayList<>();
        for (Adoption adoption : adoptionPage.getContent()) {
            java.util.Map<String, Object> item = new java.util.HashMap<>();
            item.put("application", adoption);
            
            Pet pet = petRepository.findById(adoption.getPetId()).orElse(null);
            if (pet != null) {
                java.util.Map<String, Object> petInfo = new java.util.HashMap<>();
                petInfo.put("id", pet.getId());
                petInfo.put("name", pet.getName());
                petInfo.put("image", pet.getImage());
                petInfo.put("species", pet.getSpecies());
                petInfo.put("breed", pet.getBreed());
                item.put("pet", petInfo);
            }
            
            enhancedList.add(item);
        }
        
        // 构建分页响应
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("content", enhancedList);
        result.put("totalElements", adoptionPage.getTotalElements());
        result.put("totalPages", adoptionPage.getTotalPages());
        result.put("number", adoptionPage.getNumber());
        result.put("size", adoptionPage.getSize());
        result.put("first", adoptionPage.isFirst());
        result.put("last", adoptionPage.isLast());
        
        return Result.success(result);
    }

    @Override
    public Result getApplicationDetail(Integer appId, HttpSession session) {
        User user = SessionUtils.getUser(session);
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        Adoption adoption = adoptionRepository.findById(appId).orElse(null);
        if (adoption == null) {
            return Result.error("申请记录不存在");
        }
        // 普通用户只能看自己的申请，管理员可以看所有
        if (user.getRole() != 1 && !adoption.getUserId().equals(user.getId())) {
            return Result.error(403, "无权限查看");
        }
        // 关联宠物信息（可选，也可让前端再次请求）
        Pet pet = petRepository.findById(adoption.getPetId()).orElse(null);
        Map<String, Object> detail = new HashMap<>();
        detail.put("application", adoption);
        if (pet != null) {
            detail.put("petName", pet.getName());
            detail.put("petImage", pet.getImage());
        }
        return Result.success(detail);
    }

    @Override
    public Result cancelApplication(Integer appId, HttpSession session) {
        User user = SessionUtils.getUser(session);
        if (user == null) {
            return Result.error(401, "请先登录");
        }
        
        Adoption adoption = adoptionRepository.findById(appId).orElse(null);
        if (adoption == null) {
            return Result.error("申请不存在");
        }
        
        // 检查权限：只能撤回自己的申请
        if (!adoption.getUserId().equals(user.getId())) {
            return Result.error(403, "无权限操作");
        }
        
        // 只有“待审核”状态的申请可以撤回
        if (!"待审核".equals(adoption.getStatus())) {
            return Result.error("只有待审核的申请可以撤回（当前状态：" + adoption.getStatus() + "）");
        }
        
        // 更新状态为已撤回
        adoption.setStatus("已撤回");
        adoption.setAuditTime(new Date());  // 使用审核时间字段记录撤回时间
        adoptionRepository.save(adoption);
        
        return Result.success("申请已撤回");
    }
}