package com.thantruongnhan.doanketthucmon.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thantruongnhan.doanketthucmon.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    // USER - Người đặt vé (REQUIRED)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // MÃ ĐỢN HÀNG - Unique code
    @Column(nullable = false, unique = true, length = 50)
    private String orderCode; // VD: ORD20241231123456

    // THÔNG TIN KHÁCH HÀNG
    @Column(length = 100)
    private String customerName;

    @Column(length = 20)
    private String customerPhone;

    @Column(length = 100)
    private String customerEmail;

    // TRẠNG THÁI ĐƠN HÀNG
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    // TỔNG TIỀN
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO; // Tổng tiền vé + combo

    @Column(precision = 12, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO; // Giảm giá

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal finalAmount = BigDecimal.ZERO; // Tiền cuối cùng sau giảm giá

    // KHUYẾN MÃI
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    // GHI CHÚ
    @Lob
    @Column(columnDefinition = "TEXT")
    private String notes;

    // THỜI GIAN
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime paidAt;

    private LocalDateTime cancelledAt;

    // QUAN HỆ VỚI VÉ (Thay thế OrderItem)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("order")
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>(); // Danh sách vé

    // QUAN HỆ VỚI COMBO ĐỒ ĂN
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("order")
    @Builder.Default
    private List<OrderCombo> orderCombos = new ArrayList<>(); // Danh sách combo đồ ăn

    // THANH TOÁN
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("order")
    private Payment payment;

    // BỎ: TableEntity và Employee (không cần trong hệ thống đặt vé phim)

    // TÍNH LẠI TỔNG TIỀN
    public void recalcTotal() {
        BigDecimal ticketTotal = BigDecimal.ZERO;
        BigDecimal comboTotal = BigDecimal.ZERO;

        // Tính tổng tiền vé
        if (tickets != null && !tickets.isEmpty()) {
            ticketTotal = tickets.stream()
                    .map(ticket -> BigDecimal.valueOf(ticket.getPrice()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        // Tính tổng tiền combo
        if (orderCombos != null && !orderCombos.isEmpty()) {
            comboTotal = orderCombos.stream()
                    .map(orderCombo -> {
                        orderCombo.calculateTotalPrice(); // Tính lại totalPrice
                        return orderCombo.getTotalPrice();
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        this.totalAmount = ticketTotal.add(comboTotal);

        // Tính tiền sau giảm giá
        if (discountAmount == null) {
            discountAmount = BigDecimal.ZERO;
        }
        this.finalAmount = this.totalAmount.subtract(discountAmount);

        // Đảm bảo finalAmount không âm
        if (this.finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            this.finalAmount = BigDecimal.ZERO;
        }
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
            createdAt = now;
        }
        if (updatedAt == null) {
            updatedAt = now;
        }
        // Tự động tạo orderCode nếu chưa có
        if (orderCode == null) {
            orderCode = "ORD" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}