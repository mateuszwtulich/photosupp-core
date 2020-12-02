package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;

import java.util.List;
import java.util.Objects;

public class ServiceEto extends AbstractApplicationEntityTransportObject {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Double basePrice;

    @NotNull
    private String locale;

    private List<IndicatorEto> indicatorEtoList;

    public ServiceEto() {
    }

    public ServiceEto(Long id, String name, String description, Double basePrice, String locale, List<IndicatorEto> indicators) {
        super(id);
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.locale = locale;
        this.indicatorEtoList = indicators;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<IndicatorEto> getIndicatorEtoList() {
        return indicatorEtoList;
    }

    public void setIndicatorEtoList(List<IndicatorEto> indicators) {
        this.indicatorEtoList = indicators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServiceEto that = (ServiceEto) o;
        return name.equals(that.name) &&
                Objects.equals(description, that.description) &&
                basePrice.equals(that.basePrice) &&
                locale.equals(that.locale) &&
                Objects.equals(indicatorEtoList, that.indicatorEtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, description, basePrice, locale, indicatorEtoList);
    }
}
