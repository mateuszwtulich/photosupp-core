package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcFindUserTest {

    @Autowired
    private UcFindUserImpl ucFindUser;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private PermissionsMapper permissionsMapper;

    @MockBean
    private UserDao userDao;

    private UserEntity userEntity;
    private AccountEntity accountEntity;
    private RoleEntity roleEntity;
    private List<PermissionEntity> permissionEntities;
    private List<UserEntity> userEntityList;
    private List<UserEto> userEtoList;
    private UserEto userEto;
    private RoleEto roleEto;
    private AccountEto accountEto;

    @BeforeEach
    void setUp() {
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC");
        permissionEntity.setId(1L);
        permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);

        roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);
        roleEntity.setId(1L);

        accountEntity = new AccountEntity("USERNAME", "PASS", "USERNAME@test.com", false);
        accountEntity.setId(1L);
        userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
        userEntity.setId(1L);

        userEntityList = new ArrayList<>();
        userEntityList.add(userEntity);

        userEtoList = new ArrayList<>();
        userEto = userMapper.toUserEto(userEntity);

        accountEto = accountMapper.toAccountEto(accountEntity);
        userEto.setAccountEto(accountEto);

        roleEto = roleMapper.toRoleEto(roleEntity);
        userEto.setRoleEto(roleEto);
        userEto.getRoleEto().setPermissionEtoList(permissionEntities.stream().map(p ->
                permissionsMapper.toPermissionEto(p)).collect(Collectors.toList()));
        userEtoList.add(userEto);
    }

    @Test
    @DisplayName("Test findAllUsers Success")
    void testFindAllUsersSuccess() {
        //Arrange
        when(userDao.findAll()).thenReturn(userEntityList);

        //Act
        Optional<List<UserEto>> result = ucFindUser.findAllUsers();

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(userEtoList.get(0)).isEqualToComparingFieldByField(result.get().get(0));
    }

    @Test
    @DisplayName("Test findAllUsers No content")
    void testFindAllUsersNoContent() {
        //Arrange
        when(userDao.findAll()).thenReturn(new ArrayList<>());

        //Act
        Optional<List<UserEto>> result = ucFindUser.findAllUsers();

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }

    @Test
    @DisplayName("Test findUserById Success")
    void testFindUserByIdSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        //Act
        Optional<UserEto> result = ucFindUser.findUser(userEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(userEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test findUserById Failure")
    void testFindUserByIdFailure() {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.ofNullable(null));

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucFindUser.findUser(userEntity.getId()));
    }

    @Test
    @DisplayName("Test findUserByRoleId Success")
    void testFindUserByRoleIdSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(userDao.findAllByRole_Id(roleEntity.getId())).thenReturn(userEntityList);

        //Act
        Optional<List<UserEto>> result = ucFindUser.findAllUsersByRoleId(roleEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(userEtoList.get(0)).isEqualToComparingFieldByField(result.get().get(0));
    }

    @Test
    @DisplayName("Test findUserByRoleId Failure")
    void testFindUserByRoleIdFailure() {
        //Arrange
        when(userDao.findAllByRole_Id(roleEntity.getId())).thenReturn(new ArrayList<>());

        //Act
        Optional<List<UserEto>> result = ucFindUser.findAllUsersByRoleId(roleEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        Assertions.assertTrue(result.get().size() == 0);
    }
}
