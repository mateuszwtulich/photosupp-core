package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.AccountDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.VerificationTokenDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.*;
import com.wtulich.photosupp.userhandling.logic.api.exception.AccountAlreadyExistsException;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.*;
import com.wtulich.photosupp.userhandling.logic.impl.listener.RegistrationListener;
import com.wtulich.photosupp.userhandling.logic.impl.validator.AccountValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class UcManageUserTest {

    @Autowired
    private UcManageUserImpl ucManageUser;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionsMapper permissionsMapper;

    @MockBean
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private RegistrationListener registrationListener;

    @MockBean
    private UserDao userDao;

    @MockBean
    private AccountDao accountDao;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private AccountValidator accountValidator;

    @MockBean
    private VerificationTokenDao verificationTokenDao;

    private UserEntity userEntity;
    private AccountEntity accountEntity;
    private RoleEntity roleEntity;
    private List<PermissionEntity> permissionEntities;
    private VerificationTokenEntity verificationTokenEntity;
    private UserEto userEto;
    private RoleEto roleEto;
    private AccountEto accountEto;
    private List<Long> permissionsIds;
    private UserTo userTo;
    private RoleTo roleTo;
    private AccountTo accountTo;

    @BeforeEach
    void setUp() {
        PermissionEntity permissionEntity = new PermissionEntity(ApplicationPermissions.A_CRUD_SUPER, "DESC");
        permissionEntity.setId(1L);
        permissionEntities = new ArrayList<>();
        permissionEntities.add(permissionEntity);

        permissionsIds = new ArrayList<>();
        permissionsIds.add(1L);

        roleEntity = new RoleEntity("ADMIN", "DESC1", permissionEntities);
        roleEntity.setId(1L);

        accountEntity = new AccountEntity("USERNAME", "PASS", "USERNAME@test.com", false);
        accountEntity.setId(1L);
        accountTo = new AccountTo("PASS", "USERNAME@test.com");
        verificationTokenEntity = new VerificationTokenEntity("TOKEN", accountEntity);
        verificationTokenEntity.setId(1L);

        userEntity = new UserEntity("NAME", "SURNAME", roleEntity, accountEntity);
        userEntity.setId(1L);
        userTo = new UserTo("NAME", "SURNAME", accountTo, roleEntity.getId());

        userEto = userMapper.toUserEto(userEntity);

        accountEto = accountMapper.toAccountEto(accountEntity);
        userEto.setAccountEto(accountEto);

        roleEto = roleMapper.toRoleEto(roleEntity);
        userEto.setRoleEto(roleEto);
        userEto.getRoleEto().setPermissionEtoList(permissionEntities.stream().map(p ->
                permissionsMapper.toPermissionEto(p)).collect(Collectors.toList()));
    }

    @Test
    @DisplayName("Test createUser Success")
    void testCreateUserSuccess() throws EntityDoesNotExistException, AccountAlreadyExistsException, AddressException {
        //Arrange
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));
        when(accountDao.save(any())).thenReturn(accountEntity);
        when(userDao.save(any())).thenReturn(userEntity);

        //Act
        Optional<UserEto> result = ucManageUser.createUserAndAccountEntities(userTo, mock(HttpServletRequest.class), null);

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(userEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test createUser AccountAlreadyExistsException")
    void testCreateUserAccountAlreadyExistsException() throws AccountAlreadyExistsException {
        //Arrange
        doThrow(AccountAlreadyExistsException.class).when(accountValidator).verifyIfAccountAlreadyExists(accountTo);

        //Act Assert
        Assertions.assertThrows(AccountAlreadyExistsException.class, () ->
                ucManageUser.createUserAndAccountEntities(userTo, mock(HttpServletRequest.class), null));
    }

    @Test
    @DisplayName("Test createUser EntityDoesNotExistException")
    void testCreateUserEntityDoesNotExistException() {
        //Arrange
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.empty());
        when(accountDao.save(accountEntity)).thenReturn(accountEntity);

        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageUser.createUserAndAccountEntities(userTo, mock(HttpServletRequest.class), null));
    }

    @Test
    @DisplayName("Test createUser AddressException")
    void testCreateUserAddressException() throws AddressException {
        //Arrange
        doThrow(AddressException.class).when(accountValidator).verifyIfValidEmailAddress(accountTo.getEmail());


        //Act Assert
        Assertions.assertThrows(AddressException.class, () ->
                ucManageUser.createUserAndAccountEntities(userTo, mock(HttpServletRequest.class), null));
    }

    @Test
    @DisplayName("Test updateUser Success")
    void testUpdateUserSuccess() throws EntityDoesNotExistException {
        //Arrange
        when(roleDao.findById(roleEntity.getId())).thenReturn(Optional.of(roleEntity));
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        //Act
        Optional<UserEto> result = ucManageUser.updateUser(userTo, userEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(userEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test updateUser EntityDoesNotExistException")
    void testUpdateUserEntityDoesNotExistException() {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.empty());


        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageUser.updateUser(userTo, userEntity.getId()));
    }

    @Test
    @DisplayName("Test updateUserAccount Success")
    void testUpdateUserAccountSuccess() throws EntityDoesNotExistException, AccountAlreadyExistsException, AddressException {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

        //Act
        Optional<AccountEto> result = ucManageUser.updateUserAccount(accountTo, userEntity.getId());

        // Assert
        Assertions.assertTrue(result.isPresent());
        assertThat(accountEto).isEqualToComparingFieldByField(result.get());
    }

    @Test
    @DisplayName("Test updateUserAccount EntityDoesNotExistException")
    void testUpdateUserAccountEntityDoesNotExistException() {
        //Arrange
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.empty());


        //Act Assert
        Assertions.assertThrows(EntityDoesNotExistException.class, () ->
                ucManageUser.updateUserAccount(accountTo, accountEntity.getId()));
    }

    @Test
    @DisplayName("Test updateUserAccount AccountAlreadyExistsException")
    void testUpdateUserAccountAccountAlreadyExistsException() throws AccountAlreadyExistsException {
        //Arrange
        accountTo.setEmail("user2@test.com");
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        doThrow(AccountAlreadyExistsException.class).when(accountValidator).verifyIfAccountAlreadyExists(accountTo);

        //Act Assert
        Assertions.assertThrows(AccountAlreadyExistsException.class, () ->
                ucManageUser.updateUserAccount(accountTo, userEntity.getId()));
    }

    @Test
    @DisplayName("Test updateUserAccount AddressException")
    void testUpdateUserAccountAddressException() throws AddressException {
        //Arrange
        accountTo.setEmail("test");
        when(userDao.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));
        doThrow(AddressException.class).when(accountValidator).verifyIfValidEmailAddress(accountTo.getEmail());

        //Act Assert
        Assertions.assertThrows(AddressException.class, () ->
                ucManageUser.updateUserAccount(accountTo, userEntity.getId()));
    }
}
