package com.pet.adoption.repository;

import com.pet.adoption.entity.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    // 分页查询所有未下架的宠物（普通用户可见）
    @Query("SELECT p FROM Pet p WHERE p.status != '下架' AND (p.status = '待领养' OR p.status = '已领养')")
    Page<Pet> findAvailablePets(Pageable pageable);
    
    // 分页 + 种类筛选 + 关键词搜索（未下架）
    @Query("SELECT p FROM Pet p WHERE p.status != '下架' " +
           "AND (:species IS NULL OR :species = '' OR p.species = :species) " +
           "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.breed LIKE %:keyword%)")
    Page<Pet> searchPets(@Param("species") String species, 
                         @Param("keyword") String keyword, 
                         Pageable pageable);
    
    // 管理员查询所有宠物（包含下架），也需要分页
    Page<Pet> findAll(Pageable pageable);
}