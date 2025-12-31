package com.thantruongnhan.doanketthucmon.service.impl;

import com.thantruongnhan.doanketthucmon.dto.request.OrderRequest;
import com.thantruongnhan.doanketthucmon.dto.response.OrderResponse;
import com.thantruongnhan.doanketthucmon.entity.*;
import com.thantruongnhan.doanketthucmon.entity.enums.OrderStatus;
import com.thantruongnhan.doanketthucmon.entity.enums.TicketStatus;
import com.thantruongnhan.doanketthucmon.exception.ResourceNotFoundException;
import com.thantruongnhan.doanketthucmon.mapper.OrderMapper;
import com.thantruongnhan.doanketthucmon.repository.*;
import com.thantruongnhan.doanketthucmon.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ShowTimeRepository showTimeRepository;
    private final SeatRepository seatRepository;
    private final ComboRepository comboRepository;
    private final TicketRepository ticketRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse createOrder(OrderRequest request, Long userId) {
        // Lấy user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Tạo order mới
        Order order = Order.builder()
                .user(user)
                .orderCode(generateOrderCode())
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .customerEmail(request.getCustomerEmail())
                .status(OrderStatus.PENDING)
                .notes(request.getNotes())
                .totalAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .finalAmount(BigDecimal.ZERO)
                .tickets(new ArrayList<>())
                .orderCombos(new ArrayList<>())
                .build();

        // Tạo tickets từ danh sách seatIds
        if (request.getSeatIds() != null && !request.getSeatIds().isEmpty()) {
            for (Long seatId : request.getSeatIds()) {
                Seat seat = seatRepository.findById(seatId)
                        .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

                // Kiểm tra ghế đã được đặt chưa
                if (seat.getStatus() != com.thantruongnhan.doanketthucmon.entity.enums.SeatStatus.AVAILABLE) {
                    throw new IllegalStateException("Seat " + seat.getSeatName() + " is not available");
                }

                // Tạo ticket
                Ticket ticket = Ticket.builder()
                        .order(order)
                        .showTime(seat.getShowTime())
                        .seat(seat)
                        .user(user)
                        .ticketCode(generateTicketCode())
                        .price(seat.getPrice())
                        .status(TicketStatus.BOOKED)
                        .qrCode(generateQRCode())
                        .build();

                order.getTickets().add(ticket);

                // Cập nhật trạng thái ghế
                seat.setStatus(com.thantruongnhan.doanketthucmon.entity.enums.SeatStatus.BOOKED);
                seatRepository.save(seat);
            }
        }

        // Tạo orderCombos từ danh sách combos
        if (request.getCombos() != null && !request.getCombos().isEmpty()) {
            for (OrderRequest.ComboItem comboItem : request.getCombos()) {
                Combo combo = comboRepository.findById(comboItem.getComboId())
                        .orElseThrow(() -> new ResourceNotFoundException("Combo not found"));

                OrderCombo orderCombo = OrderCombo.builder()
                        .order(order)
                        .combo(combo)
                        .quantity(comboItem.getQuantity())
                        .price(combo.getPrice())
                        .totalPrice(combo.getPrice().multiply(BigDecimal.valueOf(comboItem.getQuantity())))
                        .build();

                order.getOrderCombos().add(orderCombo);
            }
        }

        // Tính tổng tiền
        order.recalcTotal();

        // Lưu order
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with code: " + orderCode));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getUserOrdersByStatus(Long userId, OrderStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status);
        return orders.stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);

        if (status == OrderStatus.COMPLETED) {
            order.setPaidAt(LocalDateTime.now());
        } else if (status == OrderStatus.CANCELLED) {
            order.setCancelledAt(LocalDateTime.now());
            // Giải phóng ghế khi hủy đơn
            order.getTickets().forEach(ticket -> {
                Seat seat = ticket.getSeat();
                seat.setStatus(com.thantruongnhan.doanketthucmon.entity.enums.SeatStatus.AVAILABLE);
                seatRepository.save(seat);
            });
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toResponse(updatedOrder);
    }

    @Override
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Kiểm tra quyền hủy đơn
        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalStateException("You don't have permission to cancel this order");
        }

        // Chỉ cho phép hủy đơn ở trạng thái PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot cancel order with status: " + order.getStatus());
        }

        return updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Override
    public OrderResponse confirmPayment(Long orderId) {
        return updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersBetweenDates(startDate, endDate).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalRevenue() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(order -> order.getFinalAmount().doubleValue())
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public Double getRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersBetweenDates(startDate, endDate).stream()
                .filter(order -> order.getStatus() == OrderStatus.COMPLETED)
                .mapToDouble(order -> order.getFinalAmount().doubleValue())
                .sum();
    }

    @Override
    @Transactional(readOnly = true)
    public long countOrdersByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Soft delete - chỉ đổi status
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    // Helper methods
    private String generateOrderCode() {
        String code;
        do {
            code = "ORD" + System.currentTimeMillis();
        } while (orderRepository.existsByOrderCode(code));
        return code;
    }

    private String generateTicketCode() {
        return "TKT" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    private String generateQRCode() {
        return UUID.randomUUID().toString().toUpperCase();
    }
}