package com.pet.adoption.repository;

import com.pet.adoption.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 根据宠物ID查询评论，按时间倒序（最新评论在前）
    List<Comment> findByPetIdOrderByCreateTimeDesc(Long petId);
}