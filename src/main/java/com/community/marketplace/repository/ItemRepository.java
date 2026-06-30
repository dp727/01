package com.community.marketplace.repository;

import com.community.marketplace.entity.Item;
import com.community.marketplace.entity.Item.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByStatus(ItemStatus status, Pageable pageable);

    Page<Item> findByStatusAndCategoryId(ItemStatus status, Long categoryId, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.status = :status AND i.title LIKE %:keyword%")
    Page<Item> findByStatusAndKeyword(@Param("status") ItemStatus status, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.status = :status AND i.category.id = :categoryId AND i.title LIKE %:keyword%")
    Page<Item> findByStatusAndCategoryIdAndKeyword(@Param("status") ItemStatus status, @Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);

    Page<Item> findByUserIdAndStatus(Long userId, ItemStatus status, Pageable pageable);

    Page<Item> findByUserId(Long userId, Pageable pageable);
}
