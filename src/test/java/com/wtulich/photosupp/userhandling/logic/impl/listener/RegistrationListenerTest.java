package com.wtulich.photosupp.userhandling.logic.impl.listener;

import com.wtulich.photosupp.userhandling.dataaccess.api.dao.VerificationTokenDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.VerificationTokenEntity;
import com.wtulich.photosupp.userhandling.logic.impl.events.OnRegistrationCompleteEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationListenerTest {

    @Autowired
    private RegistrationListener registrationListener;

    @Autowired
    @Named("messageSource")
    private MessageSource messages;

    @Autowired
    private JavaMailSender mailSender;

    @MockBean
    private VerificationTokenDao verificationTokenDao;

    @Test
    @DisplayName("Test onApplicationEvent Success")
    void testOnApplicationEventSuccess() {
        //Arrange
        AccountEntity accountEntity = new AccountEntity("USERNAME", "PASS", "USERNAME@test.com", false);
        accountEntity.setId(1L);
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity("TOKEN", accountEntity);
        HttpServletRequest request = mock(HttpServletRequest.class);

        doReturn(verificationTokenEntity).when(verificationTokenDao).save(any());

        //Act Assert
        Assertions.assertDoesNotThrow(() -> registrationListener.onApplicationEvent(
                new OnRegistrationCompleteEvent(accountEntity, accountEntity.getPassword(), request.getLocale(), request.getContextPath())));
    }
}
