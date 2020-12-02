package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.userhandling.dataaccess.api.dao.AccountDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcFindAccountTest {

    @Autowired
    private UcFindAccountImpl ucFindAccount;

    @Autowired
    private AccountMapper accountMapper;

    @MockBean
    private AccountDao accountDao;

    private AccountEntity accountEntity;
    private List<AccountEntity> accountEntities;
    private List<AccountEto> accountEtoList;

    @BeforeEach
    void setUp() {
        accountEntity = new AccountEntity("USERNAME", "PASS", "USERNAME@test.com", false);

        accountEntities = new ArrayList<>();
        accountEntities.add(accountEntity);
        accountEtoList = new ArrayList<>();
    }

    @Test
    @DisplayName("Test findAllAccounts Success")
    void testFindAllAccountsSuccess() {
        //Arrange
        when(accountDao.findAll()).thenReturn(accountEntities);
        accountEtoList.add(accountMapper.toAccountEto(accountEntity));

        //Act
        Optional<List<AccountEto>> result = ucFindAccount.findAllAccounts();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(accountEtoList.get(0)).isEqualToComparingFieldByField(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllAccounts No content")
    void testFindAllAccountsNoContent() {
        //Arrange
        when(accountDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<AccountEto>> result = ucFindAccount.findAllAccounts();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }
}
