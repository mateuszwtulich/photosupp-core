package com.wtulich.photosupp.serviceordering.dataaccess.api.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PriceIndicatorKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "INDICATOR_ID")
    public Long indicatorId;

    @Column(name = "BOOKING_ID")
    public Long bookingId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceIndicatorKey)) return false;
        PriceIndicatorKey that = (PriceIndicatorKey) o;
        return indicatorId.equals(that.indicatorId) &&
                bookingId.equals(that.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indicatorId, bookingId);
    }
}
