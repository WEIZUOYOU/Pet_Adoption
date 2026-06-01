package com.pet.adoption.service;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Pet;

public interface PetService {
    Result getPetList(String species, String keyword);
    Result getPetDetail(Integer petId);

    // 管理员使用的增删改，参数改为实体对象
    Result addPet(Pet pet);
    Result updatePet(Pet pet);
    Result deletePet(Integer petId);
}