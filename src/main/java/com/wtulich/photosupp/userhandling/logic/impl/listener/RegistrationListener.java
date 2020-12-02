package com.wtulich.photosupp.userhandling.logic.impl.listener;

import com.wtulich.photosupp.userhandling.dataaccess.api.dao.VerificationTokenDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.VerificationTokenEntity;
import com.wtulich.photosupp.userhandling.logic.impl.events.OnRegistrationCompleteEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Named("messageSource")
    @Inject
    private MessageSource messages;

    @Inject
    private JavaMailSender mailSender;

    @Inject
    private VerificationTokenDao verificationTokenDao;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        AccountEntity account = event.getAccount();
        String token = UUID.randomUUID().toString();
        VerificationTokenEntity verificationTokenEntity = createVerificationToken(account, token);

        mailSender.send(createMailMessage(verificationTokenEntity, account, event));
    }

    private VerificationTokenEntity createVerificationToken(AccountEntity accountEntity, String token) {
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity(token, accountEntity);
        return verificationTokenDao.save(verificationTokenEntity);
    }

    private SimpleMailMessage createMailMessage(VerificationTokenEntity verificationTokenEntity, AccountEntity account,
                                                OnRegistrationCompleteEvent event){
        String recipientAddress = account.getEmail();
        String subject = messages.getMessage("message.regTitle", null, event.getLocale());

        StringBuilder confirmationUrl = new StringBuilder();
        confirmationUrl.append(event.getAppUrl() + "/user/v1/user/account/registrationConfirm?token=" + verificationTokenEntity.getToken());

        StringBuilder message = new StringBuilder();
        message.append(messages.getMessage("message.regSucc", null, event.getLocale()));
        message.append("\r\n\n" + messages.getMessage("api", null, event.getLocale()) + confirmationUrl.toString());
        message.append("\n\nNazwa użytkownika: " + account.getUsername()+ "\nHasło: " + event.getPassword() + "\n\n");

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message.toString());

        return email;
    }
}
