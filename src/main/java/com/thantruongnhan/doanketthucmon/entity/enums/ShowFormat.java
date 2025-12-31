package com.thantruongnhan.doanketthucmon.entity.enums;

public enum ShowFormat {
    TWO_D("2D"),
    THREE_D("3D"),
    IMAX("IMAX"),
    FOUR_DX("4DX");

    private String displayName;

    ShowFormat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}