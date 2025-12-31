package com.thantruongnhan.doanketthucmon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "combos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Combo 1 (Bỏng + Nước), Combo 2 (Bỏng + 2 Nước)...

    @Column(columnDefinition = "TEXT")
    private String description; // Chi tiết combo

    @Column(nullable = false)
    private BigDecimal price; // 99.000đ, 129.000đ, 199.000đ

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String items; // 1 Bỏng Lớn + 1 Coca Lớn

    @Column(nullable = false)
    private Boolean isActive = true;

    private Integer displayOrder = 0; // Thứ tự hiển thị

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}