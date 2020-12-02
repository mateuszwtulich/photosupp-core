package com.wtulich.photosupp.userhandling.dataaccess.api;

import com.wtulich.photosupp.config.H2JpaConfig;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = {H2JpaConfig.class})
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    @Test
    void findByRole_Id_OK_HAS_ANY() {
        //Arrange
        AccountEntity accountEntity = new AccountEntity("user1", "passw0rd", "user1@test.com", false);
        accountEntity.setId(1L);

        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.AUTH_USER, "Standard user with no special permissions.");
        permissionEntity.setId(6L);

        List<PermissionEntity> permissionEntityList = new ArrayList<>();
        permissionEntityList.add(permissionEntity);

        RoleEntity roleEntity = new RoleEntity("USER", "Standard user with no special permissions", permissionEntityList);
        roleEntity.setId(2L);

        UserEntity userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
        userEntity.setId(1L);

        //Act
        List<UserEntity> userEntityList = userDao.findAllByRole_Id(2L);

        // Assert
        assertThat(userEntityList).hasSize(2);
        assertThat(userEntity).isEqualToIgnoringGivenFields(userEntityList.get(0), "account", "role", "orderList", "bookingList");
    }

    @Test
    void findByRole_Id_OK_EMPTY() {
        //Act Assert
        Assertions.assertEquals(new ArrayList(), userDao.findAllByRole_Id(1L));
    }
}
