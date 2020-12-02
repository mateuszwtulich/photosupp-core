package com.wtulich.photosupp.general.security.authentication.logic.impl.to;

import java.util.List;

public class AuthenticationPayload {

    private List<String> permissions;

    private List<String> orderNumbers;

    private List<Long> bookingIds;

    public List<String> getOrderNumbers() {
        return orderNumbers;
    }

    public void setOrderNumbers(List<String> orderNumbers) {
        this.orderNumbers = orderNumbers;
    }

    public List<Long> getBookingIds() {
        return bookingIds;
    }

    public void setBookingIds(List<Long> bookingIds) {
        this.bookingIds = bookingIds;
    }

    public AuthenticationPayload(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}