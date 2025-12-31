package com.thantruongnhan.doanketthucmon.mapper;

import com.thantruongnhan.doanketthucmon.dto.response.OrderResponse;
import com.thantruongnhan.doanketthucmon.entity.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .userId(order.getUser().getId())
                .userName(order.getUser().getFullName())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .customerEmail(order.getCustomerEmail())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .notes(order.getNotes())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .paidAt(order.getPaidAt())
                .cancelledAt(order.getCancelledAt())
                .tickets(order.getTickets().stream()
                        .map(this::toTicketInfo)
                        .collect(Collectors.toList()))
                .combos(order.getOrderCombos().stream()
                        .map(this::toComboInfo)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderResponse.TicketInfo toTicketInfo(Ticket ticket) {
        ShowTime showTime = ticket.getShowTime();
        Seat seat = ticket.getSeat();

        return OrderResponse.TicketInfo.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .movieName(showTime.getMovie().getName())
                .cinemaName(showTime.getCinema().getName())
                .showDate(showTime.getShowDate().format(DATE_FORMATTER))
                .showTime(showTime.getShowTime().format(TIME_FORMATTER))
                .seatName(seat.getSeatName())
                .format(showTime.getFormat().getDisplayName())
                .price(ticket.getPrice())
                .qrCode(ticket.getQrCode())
                .status(ticket.getStatus().getDisplayName())
                .build();
    }

    private OrderResponse.ComboInfo toComboInfo(OrderCombo orderCombo) {
        return OrderResponse.ComboInfo.builder()
                .id(orderCombo.getId())
                .comboName(orderCombo.getCombo().getName())
                .quantity(orderCombo.getQuantity())
                .price(orderCombo.getPrice())
                .totalPrice(orderCombo.getTotalPrice())
                .build();
    }
}