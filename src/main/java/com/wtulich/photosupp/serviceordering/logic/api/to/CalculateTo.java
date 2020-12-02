package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CalculateTo {

    @NotNull
    private Long serviceId;

    @NotNull
    private String start;

    @NotNull
    private String end;

    @NotNull
    private List<PriceIndicatorTo> priceIndicatorToList;

    public CalculateTo() {
    }

    public CalculateTo(Long serviceId, String start, String end, List<PriceIndicatorTo> priceIndicatorToList) {
        this.serviceId = serviceId;
        this.start = start;
        this.end = end;
        this.priceIndicatorToList = priceIndicatorToList;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public List<PriceIndicatorTo> getPriceIndicatorToList() {
        return priceIndicatorToList;
    }

    public void setPriceIndicatorToList(List<PriceIndicatorTo> priceIndicatorToList) {
        this.priceIndicatorToList = priceIndicatorToList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalculateTo)) return false;
        CalculateTo that = (CalculateTo) o;
        return serviceId.equals(that.serviceId) &&
                start.equals(that.start) &&
                end.equals(that.end) &&
                priceIndicatorToList.equals(that.priceIndicatorToList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceId, start, end, priceIndicatorToList);
    }
}
