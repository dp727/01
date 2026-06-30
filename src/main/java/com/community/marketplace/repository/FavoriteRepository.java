package com.community.marketplace.repository;

import com.community.marketplace.entity.Favorite;
import com.community.marketplace.entity.Item;
import com.community.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByUserAndItem(User user, Item item);

    Page<Favorite> findByUser(User user, Pageable pageable);

    boolean existsByUserAndItem(User user, Item item);

    void deleteByUserAndItem(User user, Item item);
}
