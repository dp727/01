package com.community.marketplace.repository;

import com.community.marketplace.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByItemIdOrderByCreatedAtDesc(Long itemId, Pageable pageable);

    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
