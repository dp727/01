package com.community.marketplace.service;

import com.community.marketplace.common.BusinessException;
import com.community.marketplace.common.GeoUtils;
import com.community.marketplace.dto.ItemCreateRequest;
import com.community.marketplace.dto.ItemDetailResponse;
import com.community.marketplace.dto.ItemSummaryResponse;
import com.community.marketplace.dto.ItemUpdateRequest;
import com.community.marketplace.dto.ItemWithDistance;
import com.community.marketplace.dto.PageResponse;
import com.community.marketplace.entity.Category;
import com.community.marketplace.entity.Item;
import com.community.marketplace.entity.Item.ItemStatus;
import com.community.marketplace.entity.User;
import com.community.marketplace.repository.CategoryRepository;
import com.community.marketplace.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    @Transactional
    public ItemDetailResponse createItem(Long userId, ItemCreateRequest request) {
        User user = userService.findById(userId);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(404, "分类不存在"));

        Item item = Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .coverImage(request.getCoverImage())
                .images(request.getImages())
                .community(request.getCommunity())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .user(user)
                .category(category)
                .status(ItemStatus.ON_SALE)
                .build();

        Item saved = itemRepository.save(item);
        return toDetailResponse(saved);
    }

    public ItemDetailResponse getItemDetail(Long id) {
        Item item = findById(id);
        return toDetailResponse(item);
    }

    public PageResponse<ItemSummaryResponse> listItems(String keyword, Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Item> itemPage;

        if (keyword != null && !keyword.trim().isEmpty() && categoryId != null) {
            itemPage = itemRepository.findByStatusAndCategoryIdAndKeyword(ItemStatus.ON_SALE, categoryId, keyword, pageRequest);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            itemPage = itemRepository.findByStatusAndKeyword(ItemStatus.ON_SALE, keyword, pageRequest);
        } else if (categoryId != null) {
            itemPage = itemRepository.findByStatusAndCategoryId(ItemStatus.ON_SALE, categoryId, pageRequest);
        } else {
            itemPage = itemRepository.findByStatus(ItemStatus.ON_SALE, pageRequest);
        }

        return toPageResponse(itemPage);
    }

    public PageResponse<ItemSummaryResponse> listItemsByLocation(
            String keyword, Long categoryId,
            BigDecimal userLatitude, BigDecimal userLongitude, Double radiusKm,
            int page, int size) {

        BigDecimal minLat = GeoUtils.calculateMinLatitude(userLatitude, radiusKm);
        BigDecimal maxLat = GeoUtils.calculateMaxLatitude(userLatitude, radiusKm);
        BigDecimal minLon = GeoUtils.calculateMinLongitude(userLatitude, userLongitude, radiusKm);
        BigDecimal maxLon = GeoUtils.calculateMaxLongitude(userLatitude, userLongitude, radiusKm);

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Item> itemPage;
        String status = ItemStatus.ON_SALE.name();

        if (keyword != null && !keyword.trim().isEmpty() && categoryId != null) {
            itemPage = itemRepository.findByStatusAndCategoryIdAndKeywordAndLocationRangeWithDistance(
                    status, categoryId, keyword, userLatitude, userLongitude,
                    minLat, maxLat, minLon, maxLon, radiusKm, pageRequest);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            itemPage = itemRepository.findByStatusAndKeywordAndLocationRangeWithDistance(
                    status, keyword, userLatitude, userLongitude,
                    minLat, maxLat, minLon, maxLon, radiusKm, pageRequest);
        } else if (categoryId != null) {
            itemPage = itemRepository.findByStatusAndCategoryIdAndLocationRangeWithDistance(
                    status, categoryId, userLatitude, userLongitude,
                    minLat, maxLat, minLon, maxLon, radiusKm, pageRequest);
        } else {
            itemPage = itemRepository.findByStatusAndLocationRangeWithDistance(
                    status, userLatitude, userLongitude,
                    minLat, maxLat, minLon, maxLon, radiusKm, pageRequest);
        }

        List<ItemWithDistance> pageContent = itemPage.getContent().stream()
                .map(item -> {
                    double distance = GeoUtils.calculateDistanceKm(
                            userLatitude, userLongitude,
                            item.getLatitude(), item.getLongitude());
                    return new ItemWithDistance(item, distance);
                })
                .collect(Collectors.toList());

        return toPageResponseWithDistance(pageContent, page, size, itemPage.getTotalElements());
    }

    public PageResponse<ItemSummaryResponse> listUserItems(Long userId, String status, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Item> itemPage;

        if (status != null && !status.trim().isEmpty()) {
            ItemStatus itemStatus = ItemStatus.valueOf(status);
            itemPage = itemRepository.findByUserIdAndStatus(userId, itemStatus, pageRequest);
        } else {
            itemPage = itemRepository.findByUserId(userId, pageRequest);
        }

        return toPageResponse(itemPage);
    }

    @Transactional
    public ItemDetailResponse updateItem(Long id, Long userId, ItemUpdateRequest request) {
        Item item = findById(id);

        if (!item.getUser().getId().equals(userId)) {
            throw new BusinessException(403, "无权编辑他人发布的物品");
        }

        if (request.getTitle() != null) {
            item.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }
        if (request.getCoverImage() != null) {
            item.setCoverImage(request.getCoverImage());
        }
        if (request.getImages() != null) {
            item.setImages(request.getImages());
        }
        if (request.getCommunity() != null) {
            item.setCommunity(request.getCommunity());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException(404, "分类不存在"));
            item.setCategory(category);
        }

        Item updated = itemRepository.save(item);
        return toDetailResponse(updated);
    }

    @Transactional
    public void offShelfItem(Long id, Long userId) {
        Item item = findById(id);

        if (!item.getUser().getId().equals(userId)) {
            throw new BusinessException(403, "无权下架他人发布的物品");
        }

        item.setStatus(ItemStatus.OFF_SHELF);
        itemRepository.save(item);
    }

    @Transactional
    public void onShelfItem(Long id, Long userId) {
        Item item = findById(id);

        if (!item.getUser().getId().equals(userId)) {
            throw new BusinessException(403, "无权重新上架他人发布的物品");
        }

        item.setStatus(ItemStatus.ON_SALE);
        itemRepository.save(item);
    }

    @Transactional
    public void claimItem(Long id, Long userId) {
        Item item = findById(id);

        if (item.getUser().getId().equals(userId)) {
            throw new BusinessException(400, "不能领取自己发布的物品");
        }

        if (item.getStatus() != ItemStatus.ON_SALE) {
            throw new BusinessException(400, "该物品不可领取");
        }

        item.setStatus(ItemStatus.CLAIMED);
        itemRepository.save(item);
    }

    public Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "物品不存在"));
    }

    private ItemDetailResponse toDetailResponse(Item item) {
        return ItemDetailResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .price(item.getPrice())
                .coverImage(item.getCoverImage())
                .images(item.getImages())
                .community(item.getCommunity())
                .status(item.getStatus().name())
                .categoryName(item.getCategory().getName())
                .categoryId(item.getCategory().getId())
                .publisherNickname(item.getUser().getNickname())
                .publisherId(item.getUser().getId())
                .publisherAvatar(item.getUser().getAvatar())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    private ItemSummaryResponse toSummaryResponse(Item item) {
        return ItemSummaryResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .price(item.getPrice())
                .coverImage(item.getCoverImage())
                .community(item.getCommunity())
                .createdAt(item.getCreatedAt())
                .categoryName(item.getCategory().getName())
                .build();
    }

    private ItemSummaryResponse toSummaryResponseWithDistance(Item item, double distanceKm) {
        return ItemSummaryResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .price(item.getPrice())
                .coverImage(item.getCoverImage())
                .community(item.getCommunity())
                .createdAt(item.getCreatedAt())
                .categoryName(item.getCategory().getName())
                .distance(GeoUtils.formatDistance(distanceKm))
                .build();
    }

    private PageResponse<ItemSummaryResponse> toPageResponse(Page<Item> itemPage) {
        return PageResponse.<ItemSummaryResponse>builder()
                .content(itemPage.getContent().stream().map(this::toSummaryResponse).collect(Collectors.toList()))
                .page(itemPage.getNumber())
                .size(itemPage.getSize())
                .totalElements(itemPage.getTotalElements())
                .totalPages(itemPage.getTotalPages())
                .build();
    }

    private PageResponse<ItemSummaryResponse> toPageResponseWithDistance(
            List<ItemWithDistance> itemsWithDistance, int page, int size, long totalElements) {
        return PageResponse.<ItemSummaryResponse>builder()
                .content(itemsWithDistance.stream()
                        .map(iwd -> toSummaryResponseWithDistance(iwd.getItem(), iwd.getDistanceKm()))
                        .collect(Collectors.toList()))
                .page(page)
                .size(size)
                .totalElements(totalElements)
                .totalPages((int) Math.ceil((double) totalElements / size))
                .build();
    }
}
