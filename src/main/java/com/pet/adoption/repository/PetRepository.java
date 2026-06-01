package com.pet.adoption.repository;

import com.pet.adoption.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    List<Pet> findBySpecies(String species);
    List<Pet> findByNameContainingOrDescriptionContaining(String name, String description);
    List<Pet> findByStatus(String status);
}