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
                .createdAt(user.getCreatedAt())
                .build();
    }
}
