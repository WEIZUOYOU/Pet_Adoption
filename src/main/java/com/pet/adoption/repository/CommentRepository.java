package com.pet.adoption.repository;

import com.pet.adoption.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPetIdOrderByCreateTimeDesc(Integer petId);
    
    // 分页查询宠物评论
    Page<Comment> findByPetIdOrderByCreateTimeDesc(Integer petId, Pageable pageable);
}