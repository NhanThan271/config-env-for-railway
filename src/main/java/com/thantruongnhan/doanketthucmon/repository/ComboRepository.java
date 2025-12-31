package com.thantruongnhan.doanketthucmon.repository;

import com.thantruongnhan.doanketthucmon.entity.Combo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComboRepository extends JpaRepository<Combo, Long> {

    // Tìm combo theo tên
    Optional<Combo> findByName(String name);

    // Tìm các combo đang active
    @Query("SELECT c FROM Combo c WHERE c.isActive = true")
    List<Combo> findActiveComboS();

    // Tìm combo theo status
    List<Combo> findByIsActive(boolean isActive);

    // Tìm combo có giá trong khoảng
    @Query("SELECT c FROM Combo c WHERE c.price BETWEEN :minPrice AND :maxPrice AND c.isActive = true")
    List<Combo> findByPriceRange(double minPrice, double maxPrice);

    // Kiểm tra tên combo đã tồn tại chưa
    boolean existsByName(String name);

    // Tìm combo theo từ khóa trong tên hoặc mô tả
    @Query("SELECT c FROM Combo c WHERE (LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND c.isActive = true")
    List<Combo> searchCombosByKeyword(String keyword);

    // Đếm số combo đang active
    long countByIsActive(boolean isActive);
}