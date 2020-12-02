package com.wtulich.photosupp.general.dataaccess.api.entity;

import com.sun.istack.NotNull;

import java.util.Objects;

public abstract class AbstractApplicationEntityTransportObject {

    @NotNull
    private Long id;

    public AbstractApplicationEntityTransportObject() {
    }

    public AbstractApplicationEntityTransportObject(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractApplicationEntityTransportObject)) return false;
        AbstractApplicationEntityTransportObject that = (AbstractApplicationEntityTransportObject) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
