package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;

import java.util.Objects;

public class PriceIndicatorEto {

    @NotNull
    private IndicatorEto indicatorEto;

    private Long bookingId;

    @NotNull
    private Integer price;

    @NotNull
    private Integer amount;

    public PriceIndicatorEto() {
    }

    public PriceIndicatorEto(IndicatorEto indicatorEto, Long bookingId, Integer price, Integer amount) {
        this.indicatorEto = indicatorEto;
        this.bookingId = bookingId;
        this.price = price;
        this.amount = amount;
    }

    public IndicatorEto getIndicatorEto() {
        return indicatorEto;
    }

    public void setIndicatorEto(IndicatorEto indicatorEto) {
        this.indicatorEto = indicatorEto;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceIndicatorEto that = (PriceIndicatorEto) o;
        return indicatorEto.equals(that.indicatorEto) &&
                bookingId.equals(that.bookingId) &&
                price.equals(that.price) &&
                amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indicatorEto, bookingId, price, amount);
    }
}
