package com.thantruongnhan.doanketthucmon.dto.response;

import com.thantruongnhan.doanketthucmon.entity.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String orderCode;

    // User info
    private Long userId;
    private String userName;

    // Customer info
    private String customerName;
    private String customerPhone;
    private String customerEmail;

    // Order details
    private OrderStatus status;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal finalAmount;
    private String notes;

    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;

    // Tickets and combos
    private List<TicketInfo> tickets;
    private List<ComboInfo> combos;

    // Payment info
    private PaymentInfo payment;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketInfo {
        private Long id;
        private String ticketCode;
        private String movieName;
        private String cinemaName;
        private String showDate;
        private String showTime;
        private String seatName;
        private String format;
        private Double price;
        private String qrCode;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComboInfo {
        private Long id;
        private String comboName;
        private Integer quantity;
        private BigDecimal price;
        private BigDecimal totalPrice;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        private Long id;
        private String paymentMethod;
        private BigDecimal amount;
        private String status;
        private LocalDateTime paidAt;
    }
}
