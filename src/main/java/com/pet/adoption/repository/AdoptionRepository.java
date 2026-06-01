package com.pet.adoption.repository;

import com.pet.adoption.entity.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionRepository extends JpaRepository<Adoption, Integer> {
    List<Adoption> findByUserId(Integer userId);
    List<Adoption> findByStatus(String status);
    List<Adoption> findByPetIdAndUserId(Integer petId, Integer userId);
}