package com.thantruongnhan.doanketthucmon.controller;

import com.thantruongnhan.doanketthucmon.dto.request.OrderRequest;
import com.thantruongnhan.doanketthucmon.dto.response.ApiResponse;
import com.thantruongnhan.doanketthucmon.dto.response.OrderResponse;
import com.thantruongnhan.doanketthucmon.entity.enums.OrderStatus;
import com.thantruongnhan.doanketthucmon.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "APIs for managing movie ticket orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create new order", description = "Create a new movie ticket order")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderResponse response = orderService.createOrder(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", response));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long orderId) {
        OrderResponse response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/code/{orderCode}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get order by order code")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByCode(@PathVariable String orderCode) {
        OrderResponse response = orderService.getOrderByCode(orderCode);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-orders")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user's orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<OrderResponse> responses = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/my-orders/status/{status}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user's orders by status")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrdersByStatus(
            @PathVariable OrderStatus status,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<OrderResponse> responses = orderService.getUserOrdersByStatus(userId, status);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all orders (Admin only)")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> responses = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get orders by status (Admin only)")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponse> responses = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status (Admin only)")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderResponse response = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", response));
    }

    @PostMapping("/{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Cancel order")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        OrderResponse response = orderService.cancelOrder(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", response));
    }

    @PostMapping("/{orderId}/confirm-payment")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Confirm payment (Admin only)")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmPayment(@PathVariable Long orderId) {
        OrderResponse response = orderService.confirmPayment(orderId);
        return ResponseEntity.ok(ApiResponse.success("Payment confirmed", response));
    }

    @GetMapping("/between-dates")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get orders between dates (Admin only)")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<OrderResponse> responses = orderService.getOrdersBetweenDates(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/revenue/total")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get total revenue (Admin only)")
    public ResponseEntity<ApiResponse<Double>> getTotalRevenue() {
        Double revenue = orderService.getTotalRevenue();
        return ResponseEntity.ok(ApiResponse.success("Total revenue", revenue));
    }

    @GetMapping("/revenue/between-dates")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get revenue between dates (Admin only)")
    public ResponseEntity<ApiResponse<Double>> getRevenueBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double revenue = orderService.getRevenueBetweenDates(startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success("Revenue", revenue));
    }

    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Count orders by status (Admin only)")
    public ResponseEntity<ApiResponse<Long>> countOrdersByStatus(@PathVariable OrderStatus status) {
        long count = orderService.countOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Count", count));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete order (Admin only)")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success("Order deleted successfully", null));
    }

    // Helper method
    private Long getUserIdFromAuth(Authentication authentication) {
        // Implement logic to extract userId from authentication
        // This depends on your security configuration
        return 1L; // Placeholder
    }
}