package com.wtulich.photosupp.userhandling.logic.api.to;

import com.sun.istack.NotNull;
import com.wtulich.photosupp.general.dataaccess.api.entity.AbstractApplicationEntityTransportObject;
import net.bytebuddy.implementation.bind.annotation.Default;

import java.util.Objects;

public class AccountEto extends AbstractApplicationEntityTransportObject {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private boolean isActivated;

    public AccountEto() {
    }

    public AccountEto(Long id, String username, String password, String email, boolean isActivated) {
        super(id);
        this.username = username;
        this.password = password;
        this.email = email;
        this.isActivated = isActivated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEto)) return false;
        if (!super.equals(o)) return false;
        AccountEto that = (AccountEto) o;
        return isActivated == that.isActivated &&
                username.equals(that.username) &&
                password.equals(that.password) &&
                email.equals(that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, email, isActivated);
    }
}
