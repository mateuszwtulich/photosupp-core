package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class CalculateCto {

    @NotNull
    private ServiceEto serviceEto;

    @NotNull
    private Double predictedPrice;

    @NotNull
    private String start;

    @NotNull
    private String end;

    @NotNull
    private List<PriceIndicatorEto> priceIndicatorEtoList;

    public CalculateCto() {
    }

    public CalculateCto(ServiceEto serviceEto, Double predictedPrice, String start,
                        String end, List<PriceIndicatorEto> priceIndicatorEtoList) {
        this.serviceEto = serviceEto;
        this.predictedPrice = predictedPrice;
        this.start = start;
        this.end = end;
        this.priceIndicatorEtoList = priceIndicatorEtoList;
    }

    public ServiceEto getServiceEto() {
        return serviceEto;
    }

    public void setServiceEto(ServiceEto serviceEto) {
        this.serviceEto = serviceEto;
    }

    public Double getPredictedPrice() {
        return predictedPrice;
    }

    public void setPredictedPrice(Double predictedPrice) {
        this.predictedPrice = predictedPrice;
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

    public List<PriceIndicatorEto> getPriceIndicatorEtoList() {
        return priceIndicatorEtoList;
    }

    public void setPriceIndicatorEtoList(List<PriceIndicatorEto> priceIndicatorEtoList) {
        this.priceIndicatorEtoList = priceIndicatorEtoList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalculateCto)) return false;
        CalculateCto that = (CalculateCto) o;
        return serviceEto.equals(that.serviceEto) &&
                predictedPrice.equals(that.predictedPrice) &&
                start.equals(that.start) &&
                end.equals(that.end) &&
                priceIndicatorEtoList.equals(that.priceIndicatorEtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceEto, predictedPrice, start, end, priceIndicatorEtoList);
    }
}
