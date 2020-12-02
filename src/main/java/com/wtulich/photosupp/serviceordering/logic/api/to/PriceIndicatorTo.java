package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;

import java.util.Objects;

public class PriceIndicatorTo {

    @NotNull
    private Long indicatorId;

    private Long bookingId;

    @NotNull
    private Integer amount;

    @NotNull
    private Integer price;

    public PriceIndicatorTo() {
    }

    public PriceIndicatorTo(Long indicatorId, Long bookingId, Integer amount, Integer price) {
        this.indicatorId = indicatorId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.price = price;
    }

    public Long getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(Long indicatorId) {
        this.indicatorId = indicatorId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceIndicatorTo that = (PriceIndicatorTo) o;
        return indicatorId.equals(that.indicatorId) &&
                bookingId.equals(that.bookingId) &&
                amount.equals(that.amount) &&
                price.equals(that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indicatorId, bookingId, amount, price);
    }
}
