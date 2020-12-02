package com.wtulich.photosupp.userhandling.logic.api.to;

import com.sun.istack.NotNull;

import java.util.Objects;

public class AccountTo {

    public AccountTo() {
    }

    public AccountTo(String password, String email) {
        this.password = password;
        this.email = email;
    }

    @NotNull
    private String password;

    @NotNull
    private  String email;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountTo)) return false;
        AccountTo accountTo = (AccountTo) o;
        return password.equals(accountTo.password) &&
                email.equals(accountTo.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, email);
    }
}
