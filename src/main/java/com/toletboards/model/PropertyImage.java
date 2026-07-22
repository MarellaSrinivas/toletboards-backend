package com.toletboards.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "property_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Property
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    /*
     * Original file name
     */

    @Column(nullable = false)
    private String fileName;

    /*
     * Saved file name
     */

    @Column(nullable = false, unique = true)
    private String storedFileName;

    /*
     * URL
     */

    @Column(nullable = false)
    private String imageUrl;

    /*
     * Cover image
     */

    @Builder.Default
    private Boolean coverImage = false;

    /*
     * Created
     */

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

}