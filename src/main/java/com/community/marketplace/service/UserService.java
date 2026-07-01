package com.community.marketplace.service;

import com.community.marketplace.common.BusinessException;
import com.community.marketplace.dto.UserCreateRequest;
import com.community.marketplace.dto.UserResponse;
import com.community.marketplace.dto.UserUpdateRequest;
import com.community.marketplace.entity.User;
import com.community.marketplace.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        User user = User.builder()
                .nickname(request.getNickname())
                .avatar(request.getAvatar())
                .location(request.getLocation())
                .buildingNumber(request.getBuildingNumber())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse getUserById(Long id) {
        User user = findById(id);
        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findById(id);

        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }
        if (request.getBuildingNumber() != null) {
            user.setBuildingNumber(request.getBuildingNumber());
        }
        if (request.getLatitude() != null) {
            user.setLatitude(request.getLatitude());
        }
        if (request.getLongitude() != null) {
            user.setLongitude(request.getLongitude());
        }

        User updated = userRepository.save(user);
        return toResponse(updated);
    }

    @Transactional
    public UserResponse updateUserLocation(Long id, BigDecimal latitude, BigDecimal longitude, String location) {
        User user = findById(id);

        user.setLatitude(latitude);
        user.setLongitude(longitude);
        if (location != null) {
            user.setLocation(location);
        }

        User updated = userRepository.save(user);
        return toResponse(updated);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .location(user.getLocation())
                .buildingNumber(user.getBuildingNumber())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
