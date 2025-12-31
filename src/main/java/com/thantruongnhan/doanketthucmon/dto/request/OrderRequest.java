package com.thantruongnhan.doanketthucmon.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotEmpty(message = "Seat list cannot be empty")
    private List<Long> seatIds; // Danh sách ID ghế đã chọn

    private List<ComboItem> combos; // Danh sách combo đồ ăn

    @NotBlank(message = "Customer name is required")
    @Size(max = 100)
    private String customerName;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Invalid phone number")
    private String customerPhone;

    @Email(message = "Invalid email format")
    private String customerEmail;

    private String notes;

    private Long promotionId; // ID khuyến mãi (nếu có)

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComboItem {
        @NotNull(message = "Combo ID is required")
        private Long comboId;

        @Min(value = 1, message = "Quantity must be at least 1")
        @Max(value = 10, message = "Quantity cannot exceed 10")
        private Integer quantity;
    }
}