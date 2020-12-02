package com.wtulich.photosupp.orderhandling.dataaccess.api.entity;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationPersistenceEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PHOTOSUPP_COMMENT")
public class CommentEntity extends AbstractApplicationPersistenceEntity {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "CONTENT", nullable = false)
    private String content;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, referencedColumnName = "ID")
    private UserEntity user;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_ID", nullable = false, referencedColumnName = "order_number")
    private OrderEntity order;

    @Column(name = "CREATED_AT", updatable = false)
    private LocalDate createdAt;

    public CommentEntity() {
    }

    public CommentEntity(String content, UserEntity user, OrderEntity order, LocalDate createdAt) {
        this.content = content;
        this.user = user;
        this.order = order;
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentEntity)) return false;
        CommentEntity that = (CommentEntity) o;
        return content.equals(that.content) &&
                user.equals(that.user) &&
                order.equals(that.order) &&
                createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, user, order, createdAt);
    }
}
