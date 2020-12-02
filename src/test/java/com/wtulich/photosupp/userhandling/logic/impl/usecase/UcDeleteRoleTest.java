package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.exception.RoleHasAssignedUsersException;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
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
public class UcDeleteRoleTest {

    @Autowired
    private UcDeleteRoleImpl ucDeleteRole;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private UserDao userDao;

    private RoleEntity roleEntity;
    private List<PermissionEntity> permissionEntities;

    @BeforeEach
    void setUp() {
        permissionEntities = new ArrayList<>();
        permissionEntities.add(new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC"));
        roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);
    }

    @Test
    @DisplayName("Test deleteRole Success")
    void testDeleteRoleSuccess() {
        //Arrange
        when(roleDao.findById(1L)).thenReturn(java.util.Optional.of(roleEntity));
        when(userDao.findAllByRole_Id(1L)).thenReturn(new ArrayList<>());

        //Act Assert
        Assertions.assertDoesNotThrow(() -> ucDeleteRole.deleteRole(1L));
    }

    @Test
    @DisplayName("Test deleteRole Failure")
    void testDeleteRoleFailure() {
        //Arrange
        when(roleDao.findById(1L)).thenReturn(java.util.Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucDeleteRole.deleteRole(1L));
    }

    @Test
    @DisplayName("Test deleteRole Failure2")
    void testDeleteRoleFailure2() {
        //Arrange
        when(roleDao.findById(1L)).thenReturn(java.util.Optional.of(roleEntity));

        List<UserEntity> userEntities = new ArrayList<>();
        userEntities.add(new UserEntity());
        when(userDao.findAllByRole_Id(1L)).thenReturn(userEntities);

        //Act Assert
        Assertions.assertThrows(RoleHasAssignedUsersException.class, () ->
                ucDeleteRole.deleteRole(1L));    }
}
