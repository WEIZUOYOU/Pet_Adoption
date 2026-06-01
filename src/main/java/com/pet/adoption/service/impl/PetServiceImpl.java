package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Pet;
import com.pet.adoption.repository.PetRepository;
import com.pet.adoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public Result getPetList(String species, String keyword, Pageable pageable) {
        // 处理空字符串
        if (species != null && species.trim().isEmpty()) species = null;
        if (keyword != null && keyword.trim().isEmpty()) keyword = null;
        
        Page<Pet> page = petRepository.searchPets(species, keyword, pageable);
        return Result.success(page);
    }

    @Override
    public Result getPetDetail(Integer petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return Result.error("宠物信息不存在");
        }
        // 如果是普通用户访问，不能查看“下架”的宠物
        // 权限控制放在拦截器或者这里判断，简化处理：只要不是下架即可
        if ("下架".equals(pet.getStatus())) {
            return Result.error("宠物已下架");
        }
        return Result.success(pet);
    }

    @Override
    public Result getAllPetsForAdmin(Pageable pageable) {
        Page<Pet> page = petRepository.findAll(pageable);
        return Result.success(page);
    }

    @Override
    public Result addPet(Pet pet) {
        // 验证必填字段
        if (pet.getName() == null || pet.getName().trim().isEmpty()) {
            return Result.error("宠物名称不能为空");
        }
        if (pet.getSpecies() == null || pet.getSpecies().trim().isEmpty()) {
            return Result.error("物种不能为空");
        }
        
        // 设置默认值
        pet.setAddTime(new Date());
        if (pet.getStatus() == null || pet.getStatus().trim().isEmpty()) {
            pet.setStatus("待领养");
        }
        
        petRepository.save(pet);
        return Result.success("发布成功");
    }

    @Override
    public Result updatePet(Pet pet) {
        if (!petRepository.existsById(pet.getId())) {
            return Result.error("宠物不存在");
        }
        // 保留原发布时间（防止覆盖）
        Pet original = petRepository.findById(pet.getId()).get();
        pet.setAddTime(original.getAddTime());
        petRepository.save(pet);
        return Result.success("更新成功");
    }

    @Override
    public Result deletePet(Integer petId) {
        // 检查宠物是否存在
        if (!petRepository.existsById(petId)) {
            return Result.error("宠物不存在");
        }
        
        // TODO: 检查是否有待处理的领养申请
        // 如果有 pending 状态的申请，应该提示管理员先处理
        
        petRepository.deleteById(petId);
        return Result.success("删除成功");
    }

    @Override
    public Result updatePetStatus(Integer petId, String status) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return Result.error("宠物不存在");
        }
        if (!"待领养".equals(status) && !"已领养".equals(status) && !"下架".equals(status)) {
            return Result.error("状态值无效，必须为：待领养、已领养、下架");
        }
        pet.setStatus(status);
        petRepository.save(pet);
        return Result.success("状态更新成功");
    }
}