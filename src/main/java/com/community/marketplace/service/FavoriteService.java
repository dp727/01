package com.community.marketplace.service;

import com.community.marketplace.common.BusinessException;
import com.community.marketplace.dto.ItemSummaryResponse;
import com.community.marketplace.dto.PageResponse;
import com.community.marketplace.entity.Favorite;
import com.community.marketplace.entity.Item;
import com.community.marketplace.entity.User;
import com.community.marketplace.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Transactional
    public void addFavorite(Long userId, Long itemId) {
        User user = userService.findById(userId);
        Item item = itemService.findById(itemId);

        if (favoriteRepository.existsByUserAndItem(user, item)) {
            throw new BusinessException("已收藏该物品");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .item(item)
                .build();

        favoriteRepository.save(favorite);
    }

    @Transactional
    public void removeFavorite(Long userId, Long itemId) {
        User user = userService.findById(userId);
        Item item = itemService.findById(itemId);

        if (!favoriteRepository.existsByUserAndItem(user, item)) {
            throw new BusinessException("未收藏该物品");
        }

        favoriteRepository.deleteByUserAndItem(user, item);
    }

    public boolean isFavorite(Long userId, Long itemId) {
        User user = userService.findById(userId);
        Item item = itemService.findById(itemId);
        return favoriteRepository.existsByUserAndItem(user, item);
    }

    public PageResponse<ItemSummaryResponse> getUserFavorites(Long userId, int page, int size) {
        User user = userService.findById(userId);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Favorite> favoritePage = favoriteRepository.findByUser(user, pageRequest);

        return PageResponse.<ItemSummaryResponse>builder()
                .content(favoritePage.getContent().stream().map(this::toItemSummary).collect(Collectors.toList()))
                .page(favoritePage.getNumber())
                .size(favoritePage.getSize())
                .totalElements(favoritePage.getTotalElements())
                .totalPages(favoritePage.getTotalPages())
                .build();
    }

    private ItemSummaryResponse toItemSummary(Favorite favorite) {
        Item item = favorite.getItem();
        return ItemSummaryResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .price(item.getPrice())
                .coverImage(item.getCoverImage())
                .community(item.getCommunity())
                .createdAt(favorite.getCreatedAt())
                .categoryName(item.getCategory().getName())
                .build();
    }
}
