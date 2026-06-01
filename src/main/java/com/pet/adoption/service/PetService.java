package com.pet.adoption.service;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Pet;
import org.springframework.data.domain.Pageable;

public interface PetService {
    // 前台：分页获取宠物列表（支持种类、关键词）
    Result getPetList(String species, String keyword, Pageable pageable);
    
    Result getPetDetail(Integer petId);
    
    // 管理员：分页获取所有宠物（包含下架）
    Result getAllPetsForAdmin(Pageable pageable);
    
    Result addPet(Pet pet);
    Result updatePet(Pet pet);
    Result deletePet(Integer petId);
    
    // 新增：修改宠物领养状态
    Result updatePetStatus(Integer petId, String status);  // status: 待领养/已领养/下架
}