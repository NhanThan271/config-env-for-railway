package com.thantruongnhan.doanketthucmon.entity.enums;

public enum PaymentMethod {
    CASH("Tiền mặt"),
    CREDIT_CARD("Thẻ tín dụng"),
    DEBIT_CARD("Thẻ ghi nợ"),
    BANK_TRANSFER("Chuyển khoản ngân hàng"),
    MOMO("Ví MoMo"),
    ZALOPAY("Ví ZaloPay"),
    VNPAY("VNPay"),
    PAYPAL("PayPal");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
