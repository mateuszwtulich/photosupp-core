package com.wtulich.photosupp.orderhandling.logic.api.to;

import com.sun.istack.NotNull;

import java.util.Objects;

public class OrderTo {

    @NotNull
    private Long coordinatorId;

    @NotNull
    private Long userId;

    private Long bookingId;

    @NotNull
    private Double price;

    public OrderTo() {
    }

    public OrderTo(Long coordinatorId, Long userId, Long bookingId, Double price) {
        this.coordinatorId = coordinatorId;
        this.userId = userId;
        this.bookingId = bookingId;
        this.price = price;
    }

    public Long getCoordinatorId() {
        return coordinatorId;
    }

    public void setCoordinatorId(Long coordinatorId) {
        this.coordinatorId = coordinatorId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderTo)) return false;
        OrderTo orderTo = (OrderTo) o;
        return coordinatorId.equals(orderTo.coordinatorId) &&
                userId.equals(orderTo.userId) &&
                Objects.equals(bookingId, orderTo.bookingId) &&
                price.equals(orderTo.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinatorId, userId, bookingId, price);
    }
}
