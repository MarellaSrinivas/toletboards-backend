

package com.toletboards.service.impl;

import com.toletboards.dto.DashboardResponse;
import com.toletboards.dto.PropertyRequest;
import com.toletboards.dto.PropertyResponse;
import com.toletboards.dto.UploadedImage;
import com.toletboards.model.Property;
import com.toletboards.model.PropertyImage;
import com.toletboards.model.User;
import com.toletboards.repository.PropertyImageRepository;
import com.toletboards.repository.PropertyRepository;
import com.toletboards.repository.PropertyVisitRepository;
import com.toletboards.repository.UserRepository;
import com.toletboards.service.PropertyImageService;
import com.toletboards.service.PropertyService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    private final PropertyImageRepository propertyImageRepository;

    private final PropertyImageService propertyImageService;

    private final UserRepository userRepository;

    private final PropertyVisitRepository PropertyVisitRepository;

    @Override
    public PropertyResponse createProperty(
            PropertyRequest request,
            List<MultipartFile> images,
            UserDetails userDetails) {

        User owner = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = Property.builder()

                // Owner
                .owner(owner)

                // Basic
                .propertyType(request.getPropertyType())
                .propertyCategory(request.getPropertyCategory())
                .propertyName(request.getPropertyName())
                .totalArea(request.getTotalArea())

                // Configuration
                .bhk(request.getBhk())
                .bathrooms(request.getBathrooms())
                .floors(request.getFloors())
                .balconies(request.getBalconies())
                .propertyAge(request.getPropertyAge())

                // Rental
                .monthlyRent(request.getMonthlyRent())
                .securityDeposit(request.getSecurityDeposit())
                .maintenanceCharges(request.getMaintenanceCharges())
                .propertyStatus(request.getPropertyStatus())

                // Tenant
                .preferredTenant(request.getPreferredTenant())
                .furnishingStatus(request.getFurnishingStatus())
                .foodPreference(request.getFoodPreference())

                .petsAllowed(request.getPetsAllowed())
                .smokingAllowed(request.getSmokingAllowed())
                .alcoholAllowed(request.getAlcoholAllowed())

                .availableImmediately(request.getAvailableImmediately())
                .availableFrom(request.getAvailableFrom())

                // Description
                .description(request.getDescription())

                // Address
                .state(request.getState())
                .city(request.getCity())
                .address(request.getAddress())

                .latitude(request.getLatitude())
                .longitude(request.getLongitude())

                .googleMapLink(request.getGoogleMapLink())

                .build();

        property = propertyRepository.save(property);

        List<UploadedImage> uploadedImages =
                propertyImageService.uploadImages(images);

        for (int i = 0; i < uploadedImages.size(); i++) {

            UploadedImage image = uploadedImages.get(i);

            PropertyImage propertyImage = PropertyImage.builder()

                    .property(property)

                    .fileName(image.getFileName())

                    .storedFileName(image.getStoredFileName())

                    .imageUrl(image.getImageUrl())

                    .coverImage(i == 0)

                    .build();

            propertyImageRepository.save(propertyImage);
        }

        return mapToResponse(property);

    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getAllProperties() {

        return propertyRepository.findByApprovedTrue()

                .stream()

                .map(this::mapToResponse)

                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyResponse> getMyProperties(
            UserDetails userDetails) {

        User owner = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return propertyRepository.findByOwner(owner)

                .stream()

                .map(this::mapToResponse)

                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyResponse getProperty(Long id) {

        Property property = propertyRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("Property not found"));

        return mapToResponse(property);

    }

    /*
     * DTO Mapper
     */

    private PropertyResponse mapToResponse(Property property) {

        List<PropertyImage> images =
                propertyImageRepository.findByProperty(property);

        List<String> imageUrls = images.stream()

                .map(PropertyImage::getImageUrl)

                .toList();

        String coverImage = images.stream()

                .filter(img -> Boolean.TRUE.equals(img.getCoverImage()))

                .map(PropertyImage::getImageUrl)

                .findFirst()

                .orElse(imageUrls.isEmpty() ? null : imageUrls.get(0));

        return PropertyResponse.builder()

                .id(property.getId())

                // Owner
                .ownerId(property.getOwner().getId())
                .ownerName(property.getOwner().getFullName())

                // Basic
                .propertyType(property.getPropertyType())
                .propertyCategory(property.getPropertyCategory())
                .propertyName(property.getPropertyName())
                .totalArea(property.getTotalArea())

                // Configuration
                .bhk(property.getBhk())
                .bathrooms(property.getBathrooms())
                .floors(property.getFloors())
                .balconies(property.getBalconies())
                .propertyAge(property.getPropertyAge())

                // Rent
                .monthlyRent(property.getMonthlyRent())
                .securityDeposit(property.getSecurityDeposit())
                .maintenanceCharges(property.getMaintenanceCharges())
                .propertyStatus(property.getPropertyStatus())

                // Tenant
                .preferredTenant(property.getPreferredTenant())
                .furnishingStatus(property.getFurnishingStatus())
                .foodPreference(property.getFoodPreference())

                .petsAllowed(property.getPetsAllowed())
                .smokingAllowed(property.getSmokingAllowed())
                .alcoholAllowed(property.getAlcoholAllowed())

                // Description
                .description(property.getDescription())

                // Address
                .state(property.getState())
                .city(property.getCity())
                .address(property.getAddress())

                .latitude(property.getLatitude())
                .longitude(property.getLongitude())

                .googleMapLink(property.getGoogleMapLink())

                // Images
                .coverImage(coverImage)
                .imageUrls(imageUrls)

                .approved(property.getApproved())

                .build();
    }




    @Override
public DashboardResponse getDashboard(UserDetails userDetails) {

    User owner = userRepository
            .findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    return DashboardResponse.builder()

            .totalListings(
                    propertyRepository.countByOwner(owner))

            .activeProperties(
                    propertyRepository.countByOwnerAndActiveTrue(owner))

            .pendingApproval(
                    propertyRepository.countByOwnerAndApprovedFalse(owner))

            .approvedProperties(
                    propertyRepository.countByOwnerAndApprovedTrue(owner))

            .totalVisits(
                    PropertyVisitRepository.countByOwnerId(owner.getId()))

            .build();
}

}