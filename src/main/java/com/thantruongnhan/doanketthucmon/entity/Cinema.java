package com.thantruongnhan.doanketthucmon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cinemas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // CGV Vincom Center, Lotte Cinema Q7...

    @Column(nullable = false)
    private String address;

    private String city; // Ho Chi Minh City, Ha Noi...

    private String district; // Q1, Q7...

    private String phone;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    private Double latitude;

    private Double longitude;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<ShowTime> showTimes;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
