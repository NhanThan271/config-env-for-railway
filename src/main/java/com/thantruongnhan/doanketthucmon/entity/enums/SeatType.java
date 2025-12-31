package com.thantruongnhan.doanketthucmon.entity.enums;

public enum SeatType {
    NORMAL("Thường"),
    VIP("VIP");

    private String displayName;

    SeatType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
