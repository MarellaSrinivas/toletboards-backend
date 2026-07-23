package com.toletboards.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.toletboards.dto.UpdateProfileRequest;
import com.toletboards.dto.UserProfileResponse;

public interface UserService {

    UserProfileResponse getProfile(
            UserDetails userDetails);

    UserProfileResponse updateProfile(
            UpdateProfileRequest request,
            UserDetails userDetails);

    UserProfileResponse uploadProfileImage(
            MultipartFile image,
            UserDetails userDetails);

}