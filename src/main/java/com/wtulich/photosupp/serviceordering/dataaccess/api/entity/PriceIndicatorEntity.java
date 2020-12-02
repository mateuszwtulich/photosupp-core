package com.wtulich.photosupp.serviceordering.dataaccess.api.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "PRICE_INDICATOR")
@Entity
public class PriceIndicatorEntity {

    @EmbeddedId
    private PriceIndicatorKey id;

    @ManyToOne(targetEntity = IndicatorEntity.class)
    @MapsId("indicatorId")
    @JoinColumn(name = "INDICATOR_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private IndicatorEntity indicator;

    @ManyToOne(targetEntity = BookingEntity.class)
    @MapsId("bookingId")
    @JoinColumn(name = "BOOKING_ID", referencedColumnName = "id", insertable = false, updatable = false)
    private BookingEntity booking;

    @NotNull
    @Column(name = "PRICE", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "AMOUNT", nullable = false)
    private Integer amount;

    public PriceIndicatorEntity() {
    }

    public PriceIndicatorEntity(IndicatorEntity indicator, BookingEntity booking, Integer price, Integer amount) {
        this.indicator = indicator;
        this.booking = booking;
        this.price = price;
        this.amount = amount;
    }

    public PriceIndicatorKey getId() {
        return id;
    }

    public void setId(PriceIndicatorKey id) {
        this.id = id;
    }

    public IndicatorEntity getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorEntity indicator) {
        this.indicator = indicator;
    }

    public BookingEntity getBooking() {
        return booking;
    }

    public void setBooking(BookingEntity booking) {
        this.booking = booking;
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
        PriceIndicatorEntity that = (PriceIndicatorEntity) o;
        return indicator.equals(that.indicator) &&
                booking.equals(that.booking) &&
                price.equals(that.price) &&
                amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indicator, booking, price, amount);
    }
}
