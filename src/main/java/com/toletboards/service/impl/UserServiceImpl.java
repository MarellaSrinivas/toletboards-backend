package com.toletboards.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.toletboards.dto.UpdateProfileRequest;
import com.toletboards.dto.UserProfileResponse;
import com.toletboards.model.User;
import com.toletboards.repository.PropertyRepository;
import com.toletboards.repository.UserRepository;
 import com.toletboards.service.UserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PropertyRepository propertyRepository;

 
    @Override
    public UserProfileResponse getProfile(
            UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Integer activeListings =
                propertyRepository.findByOwner(user).size();

        return UserProfileResponse.builder()

                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole().name()) 
                .verified(user.getVerified())
                .activeProperties(activeListings)

                .build();
    }

    @Override
    public UserProfileResponse updateProfile(

            UpdateProfileRequest request,

            UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setFullName(request.getFullName());

        user.setPhone(request.getPhone());
 

        userRepository.save(user);

        return getProfile(userDetails);
    }

    @Override
    public UserProfileResponse uploadProfileImage(

            MultipartFile image,

            UserDetails userDetails) {

        User user = userRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
 
 
        userRepository.save(user);

        return getProfile(userDetails);
    }

}