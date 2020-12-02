package com.wtulich.photosupp.userhandling.dataaccess.api;

import com.wtulich.photosupp.config.H2JpaConfig;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.VerificationTokenDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.VerificationTokenEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {H2JpaConfig.class})
public class VerificationTokenDaoTest {

    @Autowired
    private VerificationTokenDao verificationTokenDao;

    @Test
    void findByToken_OK() {
        //Arrange
        AccountEntity accountEntity = new AccountEntity("user1", "passw0rd", "user1@test.com", false);
        accountEntity.setId(1L);
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity("token", accountEntity);
        verificationTokenEntity.setId(1L);

        //Act Assert
        assertThat(verificationTokenEntity).isEqualToIgnoringGivenFields(verificationTokenDao.findByToken("token").get(), "account");
    }

    @Test
    void findByToken_EMPTY() {
        //Act Assert
        Assertions.assertEquals(Optional.empty(), verificationTokenDao.findByToken("str"));
    }
}
