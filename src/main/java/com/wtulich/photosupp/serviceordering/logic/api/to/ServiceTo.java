package com.wtulich.photosupp.serviceordering.logic.api.to;

import com.sun.istack.NotNull;

import java.util.List;
import java.util.Objects;

public class ServiceTo {

    @NotNull
    private String name;

    private String description;

    @NotNull
    private Double basePrice;

    @NotNull
    private String locale;

    private List<Long> indicatorsIds;

    public ServiceTo() {
    }

    public ServiceTo(String name, String description, Double basePrice, String locale, List<Long> indicatorsIds) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.locale = locale;
        this.indicatorsIds = indicatorsIds;
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

    public List<Long> getIndicatorsIds() {
        return indicatorsIds;
    }

    public void setIndicatorsIds(List<Long> indicatorsIds) {
        this.indicatorsIds = indicatorsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceTo serviceTo = (ServiceTo) o;
        return name.equals(serviceTo.name) &&
                Objects.equals(description, serviceTo.description) &&
                basePrice.equals(serviceTo.basePrice) &&
                locale.equals(serviceTo.locale) &&
                Objects.equals(indicatorsIds, serviceTo.indicatorsIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, basePrice, locale, indicatorsIds);
    }
}
