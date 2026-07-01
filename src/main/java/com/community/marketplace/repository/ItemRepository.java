package com.community.marketplace.repository;

import com.community.marketplace.entity.Item;
import com.community.marketplace.entity.Item.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

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

    @Query("SELECT i FROM Item i WHERE i.status = :status " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon")
    List<Item> findByStatusAndLocationRange(
            @Param("status") ItemStatus status,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon);

    @Query("SELECT i FROM Item i WHERE i.status = :status " +
           "AND i.category.id = :categoryId " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon")
    List<Item> findByStatusAndCategoryIdAndLocationRange(
            @Param("status") ItemStatus status,
            @Param("categoryId") Long categoryId,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon);

    @Query("SELECT i FROM Item i WHERE i.status = :status " +
           "AND i.title LIKE %:keyword% " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon")
    List<Item> findByStatusAndKeywordAndLocationRange(
            @Param("status") ItemStatus status,
            @Param("keyword") String keyword,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon);

    @Query("SELECT i FROM Item i WHERE i.status = :status " +
           "AND i.category.id = :categoryId " +
           "AND i.title LIKE %:keyword% " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon")
    List<Item> findByStatusAndCategoryIdAndKeywordAndLocationRange(
            @Param("status") ItemStatus status,
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon);

    @Query(value = "SELECT i.*, " +
           "(6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) AS distance " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "HAVING distance <= :radiusKm " +
           "ORDER BY distance ASC",
           countQuery = "SELECT COUNT(*) " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "AND (6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) <= :radiusKm",
           nativeQuery = true)
    Page<Item> findByStatusAndLocationRangeWithDistance(
            @Param("status") String status,
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable);

    @Query(value = "SELECT i.*, " +
           "(6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) AS distance " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.category_id = :categoryId " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "HAVING distance <= :radiusKm " +
           "ORDER BY distance ASC",
           countQuery = "SELECT COUNT(*) " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.category_id = :categoryId " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "AND (6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) <= :radiusKm",
           nativeQuery = true)
    Page<Item> findByStatusAndCategoryIdAndLocationRangeWithDistance(
            @Param("status") String status,
            @Param("categoryId") Long categoryId,
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable);

    @Query(value = "SELECT i.*, " +
           "(6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) AS distance " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.title LIKE CONCAT('%', :keyword, '%') " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "HAVING distance <= :radiusKm " +
           "ORDER BY distance ASC",
           countQuery = "SELECT COUNT(*) " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.title LIKE CONCAT('%', :keyword, '%') " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "AND (6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) <= :radiusKm",
           nativeQuery = true)
    Page<Item> findByStatusAndKeywordAndLocationRangeWithDistance(
            @Param("status") String status,
            @Param("keyword") String keyword,
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable);

    @Query(value = "SELECT i.*, " +
           "(6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) AS distance " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.category_id = :categoryId " +
           "AND i.title LIKE CONCAT('%', :keyword, '%') " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "HAVING distance <= :radiusKm " +
           "ORDER BY distance ASC",
           countQuery = "SELECT COUNT(*) " +
           "FROM items i " +
           "WHERE i.status = :status " +
           "AND i.category_id = :categoryId " +
           "AND i.title LIKE CONCAT('%', :keyword, '%') " +
           "AND i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "AND (6371 * acos(cos(radians(:userLat)) * cos(radians(i.latitude)) * " +
           "cos(radians(i.longitude) - radians(:userLon)) + " +
           "sin(radians(:userLat)) * sin(radians(i.latitude)))) <= :radiusKm",
           nativeQuery = true)
    Page<Item> findByStatusAndCategoryIdAndKeywordAndLocationRangeWithDistance(
            @Param("status") String status,
            @Param("categoryId") Long categoryId,
            @Param("keyword") String keyword,
            @Param("userLat") BigDecimal userLat,
            @Param("userLon") BigDecimal userLon,
            @Param("minLat") BigDecimal minLat,
            @Param("maxLat") BigDecimal maxLat,
            @Param("minLon") BigDecimal minLon,
            @Param("maxLon") BigDecimal maxLon,
            @Param("radiusKm") Double radiusKm,
            Pageable pageable);
}
