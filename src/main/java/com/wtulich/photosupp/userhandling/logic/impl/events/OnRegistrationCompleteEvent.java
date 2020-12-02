package com.wtulich.photosupp.userhandling.logic.impl.events;

import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private AccountEntity account;
    private String password;

    public OnRegistrationCompleteEvent(
            AccountEntity account, String password, Locale locale, String appUrl) {
        super(account);

        this.account = account;
        this.locale = locale;
        this.appUrl = appUrl;
        this.password = password;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
