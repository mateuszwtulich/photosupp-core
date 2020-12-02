package com.wtulich.photosupp.general.security.authentication.logic.impl.to;

public class ScopePermission {

    private Long userId;

    private boolean isAdmin;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public boolean setIsAdmin() {
        return isAdmin;
    }
}
