package com.thantruongnhan.doanketthucmon.entity.enums;

public enum TicketStatus {
    BOOKED("Sắp chiếu"),
    USED("Đã xem"),
    CANCELLED("Đã hủy");

    private String displayName;

    TicketStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}