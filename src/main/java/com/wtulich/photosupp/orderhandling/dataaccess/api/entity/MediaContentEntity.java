package com.wtulich.photosupp.orderhandling.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;
import com.wtulich.photosupp.general.utils.enums.MediaType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "MEDIA_CONTENT")
public class MediaContentEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @Column(name = "URL", nullable = false)
    private String url;

    @ManyToOne(targetEntity = OrderEntity.class)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "order_number", nullable = false)
    private OrderEntity order;

    public MediaContentEntity() {
    }

    public MediaContentEntity(MediaType type, String url, OrderEntity order) {
        this.type = type;
        this.url = url;
        this.order = order;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaContentEntity)) return false;
        MediaContentEntity that = (MediaContentEntity) o;
        return type == that.type &&
                url.equals(that.url) &&
                order.equals(that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, url, order);
    }
}
