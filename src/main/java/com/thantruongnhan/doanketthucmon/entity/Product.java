package com.thantruongnhan.doanketthucmon.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(nullable = false, length = 200)
    private String name; // Tên phim

    @Lob
    private String description; // Mô tả phim

    @Lob
    private String synopsis; // Tóm tắt cốt truyện chi tiết

    @Column(precision = 10, scale = 2)
    private BigDecimal price; // Giá vé cơ bản (có thể null, vì giá thực tế ở ShowTime)

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // Thể loại phim (Action, Horror, Comedy...)

    private String imageUrl; // Poster phim

    private String trailerUrl; // Link trailer YouTube

    private String backdropUrl; // Ảnh nền lớn

    private String director; // Đạo diễn

    @Column(length = 500)
    private String cast; // Diễn viên (cách nhau bởi dấu phẩy)

    private String language; // Ngôn ngữ (Tiếng Việt, English...)

    private Integer duration; // Thời lượng phim (phút)

    private LocalDate releaseDate; // Ngày khởi chiếu

    private Double rating; // Điểm đánh giá (0.0 - 10.0)

    @Column(length = 10)
    private String ageRating; // Phân loại độ tuổi (P, T13, T16, T18, C)

    private String country; // Quốc gia sản xuất

    private Integer stockQuantity = 0; // Không dùng cho phim, có thể bỏ hoặc để = 0

    private Boolean isActive = true;

    @Column(nullable = false)
    private Boolean isNowShowing = true; // Đang chiếu

    private Boolean isComingSoon = false; // Sắp chiếu

    private Boolean isFeatured = false; // Phim nổi bật

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // QUAN HỆ VỚI PROMOTION (giữ nguyên)
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    @ToString.Exclude
    private Set<Promotion> promotions;

    // QUAN HỆ MỚI - SUẤT CHIẾU
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<ShowTime> showTimes; // Các suất chiếu của phim này

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}