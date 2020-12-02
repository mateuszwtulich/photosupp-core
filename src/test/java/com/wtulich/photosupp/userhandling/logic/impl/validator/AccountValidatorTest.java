package com.wtulich.photosupp.userhandling.logic.impl.validator;

import com.wtulich.photosupp.userhandling.dataaccess.api.dao.AccountDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.logic.api.exception.AccountAlreadyExistsException;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountTo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.internet.AddressException;

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class AccountValidatorTest {

    @Autowired
    private AccountValidator accountValidator;

    @MockBean
    private AccountDao accountDao;

    private AccountEntity accountEntity;
    private AccountTo accountTo;

    @BeforeEach
    void setUp() {
        accountEntity = new AccountEntity("USERNAME", "PASS", "USERNAME@test.com", false);
        accountEntity.setId(1L);
        accountTo = new AccountTo("PASS", "USERNAME@test.com");
    }

    @Test
    @DisplayName("Test verifyIfAccountAlreadyExists Success")
    void verifyIfAccountAlreadyExistsSuccess() {
        //Arrange
        when(accountDao.existsByEmail(accountEntity.getEmail())).thenReturn(true);

        //Act Assert
        Assertions.assertThrows(AccountAlreadyExistsException.class, () ->
                accountValidator.verifyIfAccountAlreadyExists(accountTo));
    }

    @Test
    @DisplayName("Test verifyIfRoleAlreadyExists Failure")
    void verifyIfRoleAlreadyExistsFailure() {
        //Arrange
        when(accountDao.existsByEmail(accountEntity.getEmail())).thenReturn(false);

        //Act Assert
        Assertions.assertDoesNotThrow(() ->  accountValidator.verifyIfAccountAlreadyExists(accountTo));
    }

    @Test
    @DisplayName("Test verifyIfValidEmailAddress Success")
    void verifyIfValidEmailAddressSuccess() {
        //Act Assert
        Assertions.assertThrows(AddressException.class, () ->
                accountValidator.verifyIfValidEmailAddress("TEST"));
    }

    @Test
    @DisplayName("Test verifyIfValidEmailAddress Failure")
    void verifyIfValidEmailAddressFailure() {
        //Act Assert
        Assertions.assertDoesNotThrow(() -> accountValidator.verifyIfValidEmailAddress(accountTo.getEmail()));
    }
}
