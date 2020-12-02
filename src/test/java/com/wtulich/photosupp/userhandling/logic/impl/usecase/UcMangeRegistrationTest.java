package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.VerificationTokenDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.VerificationTokenEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcMangeRegistrationTest {

    @Autowired
    private UcManageRegistrationImpl ucManageRegistration;

    @MockBean
    private VerificationTokenDao verificationTokenDao;

    @Test
    @DisplayName("Test confirmRegistration Success")
    void testConfirmRegistrationSuccess() throws EntityDoesNotExistException {
        //Arrange
        AccountEntity accountEntity = new AccountEntity("USERNAME", "PASS", "USERNAME@test.com", false);
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity("token", accountEntity);

        when(verificationTokenDao.findByToken(verificationTokenEntity.getToken())).thenReturn(Optional.of(verificationTokenEntity));

        //Act
        Optional<RedirectView> result = ucManageRegistration.confirmRegistration(verificationTokenEntity.getToken());

        // Assert
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Test confirmRegistration Failure")
    void testConfirmRegistrationFailure() {
        //Arrange
        when(verificationTokenDao.findByToken("token")).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageRegistration.confirmRegistration("token"));
    }
}
