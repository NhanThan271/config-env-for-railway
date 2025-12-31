package com.thantruongnhan.doanketthucmon.service;

import com.thantruongnhan.doanketthucmon.dto.request.OrderRequest;
import com.thantruongnhan.doanketthucmon.dto.response.OrderResponse;
import com.thantruongnhan.doanketthucmon.entity.Order;
import com.thantruongnhan.doanketthucmon.entity.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    // Tạo đơn hàng mới
    OrderResponse createOrder(OrderRequest request, Long userId);

    // Lấy thông tin đơn hàng theo ID
    OrderResponse getOrderById(Long orderId);

    // Lấy đơn hàng theo mã orderCode
    OrderResponse getOrderByCode(String orderCode);

    // Lấy tất cả đơn hàng của user
    List<OrderResponse> getUserOrders(Long userId);

    // Lấy đơn hàng của user theo trạng thái
    List<OrderResponse> getUserOrdersByStatus(Long userId, OrderStatus status);

    // Lấy tất cả đơn hàng (admin)
    List<OrderResponse> getAllOrders();

    // Lấy đơn hàng theo trạng thái (admin)
    List<OrderResponse> getOrdersByStatus(OrderStatus status);

    // Cập nhật trạng thái đơn hàng
    OrderResponse updateOrderStatus(Long orderId, OrderStatus status);

    // Hủy đơn hàng
    OrderResponse cancelOrder(Long orderId, Long userId);

    // Xác nhận thanh toán
    OrderResponse confirmPayment(Long orderId);

    // Lấy đơn hàng trong khoảng thời gian
    List<OrderResponse> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    // Tính tổng doanh thu
    Double getTotalRevenue();

    // Tính doanh thu theo khoảng thời gian
    Double getRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    // Đếm số đơn hàng theo trạng thái
    long countOrdersByStatus(OrderStatus status);

    // Xóa đơn hàng (soft delete - chỉ đổi status)
    void deleteOrder(Long orderId);
}