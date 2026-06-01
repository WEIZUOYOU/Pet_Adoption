package com.pet.adoption.service.impl;

import com.pet.adoption.common.Result;
import com.pet.adoption.entity.Pet;
import com.pet.adoption.repository.PetRepository;
import com.pet.adoption.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public Result getPetList(String species, String keyword) {
        List<Pet> list;
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = petRepository.findByNameContainingOrDescriptionContaining(keyword, keyword);
        } else if (species != null && !species.trim().isEmpty()) {
            list = petRepository.findBySpecies(species);
        } else {
            list = petRepository.findAll();
        }
        return Result.success(list);
    }

    @Override
    public Result getPetDetail(Integer petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) {
            return Result.error("宠物信息不存在");
        }
        return Result.success(pet);
    }

    @Override
    public Result addPet(Pet pet) {
        pet.setAddTime(new Date());
        if (pet.getStatus() == null) {
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
        // 保持原发布时间，直接保存全部字段（简单做法）
        petRepository.save(pet);
        return Result.success("更新成功");
    }

    @Override
    public Result deletePet(Integer petId) {
        petRepository.deleteById(petId);
        return Result.success("删除成功");
    }
}