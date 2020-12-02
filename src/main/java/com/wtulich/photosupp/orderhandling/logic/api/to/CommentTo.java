package com.wtulich.photosupp.orderhandling.logic.api.to;

import com.sun.istack.NotNull;

import java.util.Objects;

public class CommentTo {

    @NotNull
    private String content;

    @NotNull
    private String orderNumber;

    @NotNull
    private Long userId;

    public CommentTo() {
    }

    public CommentTo(String content, String orderNumber, Long userId) {
        this.content = content;
        this.orderNumber = orderNumber;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentTo)) return false;
        CommentTo commentTo = (CommentTo) o;
        return content.equals(commentTo.content) &&
                orderNumber.equals(commentTo.orderNumber) &&
                userId.equals(commentTo.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, orderNumber, userId);
    }
}
