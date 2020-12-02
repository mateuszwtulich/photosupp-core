package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
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

import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcDeleteUserTest {

    @Autowired
    private UcDeleteUserImpl ucDeleteUser;

    @MockBean
    private UserDao userDao;

    private UserEntity userEntity;
    private AccountEntity accountEntity;
    private RoleEntity roleEntity;
    private List<PermissionEntity> permissionEntities;

    @BeforeEach
    void setUp() {
        permissionEntities = new ArrayList<>();
        permissionEntities.add(new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC"));
        roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);
        accountEntity = new AccountEntity("USERNAME", "PASS", "USERNAME@test.com", false);
        userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
    }

    @Test
    @DisplayName("Test deleteRole Success")
    void testDeleteRoleSuccess() {
        //Arrange
        when(userDao.findById(1L)).thenReturn(java.util.Optional.of(userEntity));

        //Act Assert
        Assertions.assertDoesNotThrow(() -> ucDeleteUser.deleteUserAndAllRelatedEntities(1L));
    }

    @Test
    @DisplayName("Test deleteRole Failure")
    void testDeleteRoleFailure() {
        //Arrange
        when(userDao.findById(1L)).thenReturn(java.util.Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucDeleteUser.deleteUserAndAllRelatedEntities(1L));
    }
}
