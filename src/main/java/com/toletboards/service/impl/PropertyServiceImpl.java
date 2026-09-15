

package com.toletboards.service.impl;

import com.toletboards.dto.DashboardResponse;
import com.toletboards.dto.PropertyRequest;
import com.toletboards.dto.PropertyResponse;
import com.toletboards.dto.UploadedImage;
import com.toletboards.model.Property;
import com.toletboards.model.PropertyApprovalStatus;
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

   private boolean hasRole(UserDetails userDetails, String role) {
    return userDetails.getAuthorities()
            .stream()
            .anyMatch(a ->
                    a.getAuthority().equalsIgnoreCase(role)
                    || a.getAuthority().equalsIgnoreCase("ROLE_" + role));
}



   @Override
public PropertyResponse createProperty(
        PropertyRequest request,
        List<MultipartFile> images,
        UserDetails userDetails) {

    User uploader = userRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));

    boolean isAdmin = hasRole(userDetails, "ADMIN");
    boolean isAgent = hasRole(userDetails, "AGENT");
    boolean isUser = hasRole(userDetails, "USER");

    /*
     * =========================================================
     * PROPERTY SOURCE
     * =========================================================
     */

    String propertySource;

    if (isAgent) {

        // Agent can only upload agent properties
        propertySource = "AGENT";

    } else if (isAdmin) {

        // Admin must explicitly select OWNER or AGENT
        propertySource = request.getPropertySource();

        if (!"OWNER".equalsIgnoreCase(propertySource)
                && !"AGENT".equalsIgnoreCase(propertySource)) {

            throw new RuntimeException(
                    "Please select whether this is an Owner Property or Agent Property"
            );
        }

    } else if (isUser) {

        // Normal user is always treated as owner
        propertySource = "OWNER";

    } else {

        throw new RuntimeException(
                "You are not authorized to upload a property"
        );
    }


    /*
     * =========================================================
     * CONTACT DETAILS
     * =========================================================
     */

    String contactName = request.getContactName();
    String contactMobile = request.getContactMobile();

    /*
     * AGENT
     *
     * Agent must provide OWNER details.
     */
    if (isAgent) {

        if (contactName == null
                || contactName.trim().isEmpty()) {

            throw new RuntimeException(
                    "Owner name is required for agent property"
            );
        }

        if (contactMobile == null
                || !contactMobile.matches("\\d{10}")) {

            throw new RuntimeException(
                    "Valid 10 digit owner mobile number is required"
            );
        }
    }


    /*
     * ADMIN
     *
     * OWNER PROPERTY -> Owner details
     * AGENT PROPERTY -> Agent details
     */
    if (isAdmin) {

        if (contactName == null
                || contactName.trim().isEmpty()) {

            throw new RuntimeException(
                    "Contact name is required"
            );
        }

        if (contactMobile == null
                || !contactMobile.matches("\\d{10}")) {

            throw new RuntimeException(
                    "Valid 10 digit contact mobile number is required"
            );
        }
    }


    /*
     * =========================================================
     * AVAILABILITY
     * =========================================================
     *
     * Vacant:
     *   availableImmediately = false
     *   availableFrom = null
     *
     * Occupied / Notice Period:
     *   User can select Available Immediately
     *   OR provide Available From date.
     */

    Boolean availableImmediately = false;
    request.setAvailableFrom(null);

    String propertyStatus = request.getPropertyStatus();

    if ("Vacant".equalsIgnoreCase(propertyStatus)) {

        availableImmediately = false;
        request.setAvailableFrom(null);

    } else if ("Occupied".equalsIgnoreCase(propertyStatus)
            || "Notice Period".equalsIgnoreCase(propertyStatus)) {

        availableImmediately =
                Boolean.TRUE.equals(request.getAvailableImmediately());

        if (availableImmediately) {
            request.setAvailableFrom(null);
        }
    }


    /*
     * =========================================================
     * BUILD PROPERTY
     * =========================================================
     */

    Property property = Property.builder()

            // Logged-in uploader
            .owner(uploader)

            // Property contact
            .propertySource(propertySource)
            .contactName(contactName)
            .contactMobile(contactMobile)

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

            // Availability
            .availableImmediately(availableImmediately)
            .availableFrom(request.getAvailableFrom())

            // Tenant preferences
            .preferredTenant(request.getPreferredTenant())
            .furnishingStatus(request.getFurnishingStatus())
            .foodPreference(request.getFoodPreference())
            .petsAllowed(request.getPetsAllowed())
            .smokingAllowed(request.getSmokingAllowed())
            .alcoholAllowed(request.getAlcoholAllowed())

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


    /*
     * =========================================================
     * UPLOAD IMAGES
     * =========================================================
     */

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

        return propertyRepository.findByApprovalStatus(PropertyApprovalStatus.APPROVED)


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

                .approvalStatus(property.getApprovalStatus())
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
        propertyRepository.countByOwnerAndApprovalStatus(
                owner,
                PropertyApprovalStatus.PENDING))

.approvedProperties(
        propertyRepository.countByOwnerAndApprovalStatus(
                owner,
                PropertyApprovalStatus.APPROVED))

            .totalVisits(
                    PropertyVisitRepository.countByOwnerId(owner.getId()))

            .build();
}

}