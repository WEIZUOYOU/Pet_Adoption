package com.pet.adoption.repository;

import com.pet.adoption.entity.Adoption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdoptionRepository extends JpaRepository<Adoption, Integer> {
    Page<Adoption> findByUserId(Integer userId, Pageable pageable);
    
    List<Adoption> findByStatus(String status);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Adoption a WHERE a.petId = :petId AND a.userId = :userId AND a.status IN ('待审核', '通过')")
    boolean existsPendingOrApproved(@Param("petId") Integer petId, @Param("userId") Integer userId);
}