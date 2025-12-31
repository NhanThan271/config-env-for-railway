package com.thantruongnhan.doanketthucmon.repository;

import com.thantruongnhan.doanketthucmon.entity.Order;
import com.thantruongnhan.doanketthucmon.entity.User;
import com.thantruongnhan.doanketthucmon.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tìm order theo mã đơn hàng
    Optional<Order> findByOrderCode(String orderCode);

    // Tìm tất cả đơn hàng của user
    List<Order> findByUserOrderByCreatedAtDesc(User user);

    // Tìm đơn hàng của user theo trạng thái
    List<Order> findByUserAndStatusOrderByCreatedAtDesc(User user, OrderStatus status);

    // Tìm đơn hàng theo trạng thái
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);

    // Tìm đơn hàng trong khoảng thời gian
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Tìm đơn hàng của user trong khoảng thời gian
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findUserOrdersBetweenDates(@Param("user") User user,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Đếm số đơn hàng theo trạng thái
    long countByStatus(OrderStatus status);

    // Đếm đơn hàng của user
    long countByUser(User user);

    // Tìm đơn hàng theo email hoặc phone
    @Query("SELECT o FROM Order o WHERE o.customerEmail = :email OR o.customerPhone = :phone ORDER BY o.createdAt DESC")
    List<Order> findByCustomerEmailOrPhone(@Param("email") String email, @Param("phone") String phone);

    // Kiểm tra orderCode đã tồn tại chưa
    boolean existsByOrderCode(String orderCode);
}