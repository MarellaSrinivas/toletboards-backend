package com.toletboards.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.toletboards.dto.PropertyResponse;
import com.toletboards.dto.admin.AdminDashboardResponse;
import com.toletboards.dto.admin.AdminPropertyResponse;
import com.toletboards.dto.admin.AdminUserStatsResponse;
import com.toletboards.dto.admin.AdminVisitResponse;
import com.toletboards.dto.admin.RecentPropertyResponse;
import com.toletboards.dto.admin.RecentVisitResponse;
import com.toletboards.dto.admin.UserListResponse;

import com.toletboards.model.Property;
import com.toletboards.model.PropertyApprovalStatus;
import com.toletboards.model.PropertyImage;
import com.toletboards.model.PropertyVisit;
import com.toletboards.model.Role;
import com.toletboards.model.User;

import com.toletboards.repository.PropertyImageRepository;
import com.toletboards.repository.PropertyRepository;
import com.toletboards.repository.PropertyVisitRepository;
import com.toletboards.repository.UserRepository;

import com.toletboards.service.AdminService;
 
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    private final PropertyRepository propertyRepository;

    private final PropertyVisitRepository propertyVisitRepository;

    private final PropertyImageRepository propertyImageRepository;

    @Override
    public AdminDashboardResponse getDashboard() {

        return AdminDashboardResponse.builder()

                .totalUsers(userRepository.count())

                .totalProperties(propertyRepository.count())

                .approvedProperties(
                        propertyRepository.countByApprovalStatus(
                                PropertyApprovalStatus.APPROVED))

                .pendingProperties(
                        propertyRepository.countByApprovalStatus(
                                PropertyApprovalStatus.PENDING))

                .rejectedProperties(
                        propertyRepository.countByApprovalStatus(
                                PropertyApprovalStatus.REJECTED))

                .todayVisits(
                        propertyVisitRepository.countByVisitDate(
                                LocalDate.now()))

                .recentProperties(getRecentProperties())

                .recentVisits(getRecentVisits())

                .build();
    }


    private List<RecentPropertyResponse> getRecentProperties() {

    return propertyRepository
            .findTop5ByOrderByCreatedAtDesc()
            .stream()
            .map(this::mapRecentProperty)
            .toList();

}



private RecentPropertyResponse mapRecentProperty(Property property) {

    List<PropertyImage> images =
            propertyImageRepository.findByProperty(property);

    String coverImage = images.stream()

            .filter(PropertyImage::getCoverImage)

            .map(PropertyImage::getImageUrl)

            .findFirst()

            .orElse(
                    images.isEmpty()
                            ? null
                            : images.get(0).getImageUrl());

    return RecentPropertyResponse.builder()

            .id(property.getId())

            .propertyName(property.getPropertyName())

            .ownerName(property.getOwner().getFullName())

            .propertyType(property.getPropertyType())

            .city(property.getCity())

            .coverImage(coverImage)

            .createdAt(property.getCreatedAt())

            .build();

}


private List<RecentVisitResponse> getRecentVisits() {

    return propertyVisitRepository

            .findTop5ByOrderByCreatedAtDesc()

            .stream()

            .map(this::mapRecentVisit)

            .toList();

}



private RecentVisitResponse mapRecentVisit(
        PropertyVisit visit) {

    return RecentVisitResponse.builder()

            .id(visit.getId())

            .propertyName(
                    visit.getPropertyName())

            .visitorName(
                    visit.getUserName())

            .visitDate(
                    visit.getVisitDate())

            .visitTime(
                    visit.getVisitTime())

            .status(
                    visit.getStatus())

            .build();

}




@Override
public Page<UserListResponse> getUsers(

        int page,

        int size,

        String search,

        String role) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by("createdAt").descending());

    Page<User> users;

    if (role != null && !role.isBlank()) {

        Role userRole = Role.valueOf(role);

        if (search != null && !search.isBlank()) {

            users = userRepository
                    .findByRoleAndFullNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCaseOrRoleAndPhoneContaining(

                            userRole,
                            search,

                            userRole,
                            search,

                            userRole,
                            search,

                            pageable);

        } else {

            users = userRepository.findByRole(
                    userRole,
                    pageable);
        }

    } else {

        if (search != null && !search.isBlank()) {

            users = userRepository
                    .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(

                            search,

                            search,

                            search,

                            pageable);

        } else {

            users = userRepository.findAll(pageable);

        }

    }

    return users.map(this::mapUser);

}

private UserListResponse mapUser(User user){

    return UserListResponse.builder()

            .id(user.getId())

            .fullName(user.getFullName())

            .email(user.getEmail())

            .phone(user.getPhone())

            .role(user.getRole().name())

            .enabled(user.getEnabled())

            .verified(user.getVerified())

            .createdAt(user.getCreatedAt())

            .totalProperties(

                    (int) propertyRepository
                            .countByOwner(user)
            )

            .build();

}



@Override
public AdminUserStatsResponse getUserStats(){

    return AdminUserStatsResponse.builder()

            .totalUsers(
                    userRepository.count())

            .totalOwners(
                    userRepository.countByRole(
                            Role.ROLE_USER))

            .totalAgents(
                    userRepository.countByRole(
                            Role.ROLE_AGENT))

            .totalAdmins(
                    userRepository.countByRole(
                            Role.ROLE_ADMIN))

            .verifiedUsers(
                    userRepository.countByVerifiedTrue())

            .build();

}



@Override
public Page<AdminPropertyResponse> getProperties(

        int page,

        int size,

        String search,

        String status,

        String propertyType) {

    Pageable pageable = PageRequest.of(

            page,

            size,

            Sort.by("createdAt").descending());

    Page<Property> properties;

    if (status != null && !status.isBlank()) {

        PropertyApprovalStatus approvalStatus =
                PropertyApprovalStatus.valueOf(status);

        properties =
                propertyRepository.findByApprovalStatus(
                        approvalStatus,
                        pageable);

    }

    else if (propertyType != null && !propertyType.isBlank()) {

        properties =
                propertyRepository.findByPropertyType(
                        propertyType,
                        pageable);

    }

    else if (search != null && !search.isBlank()) {

        properties =
                propertyRepository
                        .findByPropertyNameContainingIgnoreCaseOrCityContainingIgnoreCase(

                                search,

                                search,

                                pageable);

    }

    else {

        properties =
                propertyRepository.findAll(pageable);

    }

    return properties.map(this::mapAdminProperty);
}


private AdminPropertyResponse mapAdminProperty(
        Property property) {

    List<PropertyImage> images =
            propertyImageRepository.findByProperty(property);

    String coverImage = images.stream()

            .filter(PropertyImage::getCoverImage)

            .map(PropertyImage::getImageUrl)

            .findFirst()

            .orElse(
                    images.isEmpty()
                            ? null
                            : images.get(0).getImageUrl());

    return AdminPropertyResponse.builder()

            .id(property.getId())

            .propertyName(property.getPropertyName())

            .ownerName(property.getOwner().getFullName())

            .ownerPhone(property.getOwner().getPhone())

            .propertyType(property.getPropertyType())

            .propertyCategory(property.getPropertyCategory())

            .city(property.getCity())

            .state(property.getState())

            .coverImage(coverImage)

            .approvalStatus(property.getApprovalStatus().name())

            .active(property.getActive())

            .createdAt(property.getCreatedAt())

            .build();

}


@Override
@Transactional
public PropertyResponse approveProperty(Long propertyId) {

    Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() ->
                    new RuntimeException("Property not found"));

    property.setApprovalStatus(
            PropertyApprovalStatus.APPROVED);

    propertyRepository.save(property);

    return mapPropertyResponse(property);
}


@Override
@Transactional
public PropertyResponse rejectProperty(Long propertyId) {

    Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() ->
                    new RuntimeException("Property not found"));

    property.setApprovalStatus(
            PropertyApprovalStatus.REJECTED);

    propertyRepository.save(property);

    return mapPropertyResponse(property);
}



@Override
@Transactional
public PropertyResponse markPending(Long propertyId) {

    Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() ->
                    new RuntimeException("Property not found"));

    property.setApprovalStatus(
            PropertyApprovalStatus.PENDING);

    propertyRepository.save(property);

    return mapPropertyResponse(property);
}


private PropertyResponse mapPropertyResponse(Property property) {

    List<PropertyImage> images =
            propertyImageRepository.findByProperty(property);

    List<String> imageUrls = images.stream()
            .map(PropertyImage::getImageUrl)
            .toList();

    String coverImage = images.stream()

            .filter(PropertyImage::getCoverImage)

            .map(PropertyImage::getImageUrl)

            .findFirst()

            .orElse(
                    imageUrls.isEmpty()
                            ? null
                            : imageUrls.get(0));

    return PropertyResponse.builder()

            .id(property.getId())

            .ownerId(property.getOwner().getId())

            .ownerName(property.getOwner().getFullName())

            .propertyType(property.getPropertyType())

            .propertyCategory(property.getPropertyCategory())

            .propertyName(property.getPropertyName())

            .totalArea(property.getTotalArea())

            .bhk(property.getBhk())

            .bathrooms(property.getBathrooms())

            .floors(property.getFloors())

            .balconies(property.getBalconies())

            .propertyAge(property.getPropertyAge())

            .monthlyRent(property.getMonthlyRent())

            .securityDeposit(property.getSecurityDeposit())

            .maintenanceCharges(property.getMaintenanceCharges())

            .propertyStatus(property.getPropertyStatus())

            .preferredTenant(property.getPreferredTenant())

            .furnishingStatus(property.getFurnishingStatus())

            .foodPreference(property.getFoodPreference())

            .petsAllowed(property.getPetsAllowed())

            .smokingAllowed(property.getSmokingAllowed())

            .alcoholAllowed(property.getAlcoholAllowed())

            .description(property.getDescription())

            .state(property.getState())

            .city(property.getCity())

            .address(property.getAddress())

            .latitude(property.getLatitude())

            .longitude(property.getLongitude())

            .googleMapLink(property.getGoogleMapLink())

            .coverImage(coverImage)

            .imageUrls(imageUrls)

            .approvalStatus(property.getApprovalStatus())

            .active(property.getActive())

            .build();
}


@Override
@Transactional
public void deleteProperty(Long propertyId) {

    Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() ->
                    new RuntimeException("Property not found"));

    property.setActive(false);

    propertyRepository.save(property);
}



@Override
@Transactional
public PropertyResponse activateProperty(Long propertyId) {

    Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() ->
                    new RuntimeException("Property not found"));

    property.setActive(true);

    propertyRepository.save(property);

    return mapPropertyResponse(property);
}


@Override
@Transactional
public PropertyResponse deactivateProperty(Long propertyId) {

    Property property = propertyRepository.findById(propertyId)
            .orElseThrow(() ->
                    new RuntimeException("Property not found"));

    property.setActive(false);

    propertyRepository.save(property);

    return mapPropertyResponse(property);
}



@Override
public Page<AdminVisitResponse> getVisits(
        int page,
        int size,
        String status) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by("createdAt").descending());

    Page<PropertyVisit> visits;

    if(status != null && !status.isBlank()){

        visits = propertyVisitRepository.findByStatus(
                status,
                pageable);

    }else{

        visits = propertyVisitRepository.findAll(pageable);

    }

    return visits.map(this::mapVisit);

}



private AdminVisitResponse mapVisit(PropertyVisit visit){

    return AdminVisitResponse.builder()

            .id(visit.getId())

            .propertyName(visit.getPropertyName())

            .ownerName(visit.getOwnerName())
                        .ownerPhone(visit.getOwnerPhone())


            .visitorName(visit.getUserName())

            .visitorPhone(visit.getUserPhone())

            .visitDate(visit.getVisitDate())

            .visitTime(visit.getVisitTime())

            .status(visit.getStatus())

            .createdAt(visit.getCreatedAt())

            .build();

}

}