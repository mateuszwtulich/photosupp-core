package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcFindUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Optional;

@Validated
@Named
public class UcFindUserImpl implements UcFindUser {

    private static final Logger LOG = LoggerFactory.getLogger(UcFindUserImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String GET_USER_LOG = "Get User with id {} from database.";
    private static final String GET_ALL_USERS_LOG = "Get all Users from database.";
    private static final String GET_USERS_ROLE_LOG = "Get Users with Role id {} from database.";

    @Inject
    private UserDao userDao;

    @Inject
    private UserMapper userMapper;

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private PermissionsMapper permissionsMapper;

    @Override
    public Optional<UserEto> findUser(Long id) throws EntityDoesNotExistException{

        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);
        LOG.debug(GET_USER_LOG, id);

        return Optional.of(toUserEto(userDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("User with id " + id + " does not exist."))));
    }

    @Override
    public Optional<List<UserEto>> findAllUsers() {
        LOG.debug(GET_ALL_USERS_LOG);

        return Optional.of(userDao.findAll().stream()
                .map(userEntity -> toUserEto(userEntity))
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<UserEto>> findAllUsersByRoleId(Long roleId) {

        Objects.requireNonNull(roleId, ID_CANNOT_BE_NULL);
        LOG.debug(GET_USERS_ROLE_LOG, roleId);

        return Optional.of(userDao.findAllByRole_Id(roleId).stream()
                .map(this::toUserEto).collect(Collectors.toList()));
    }

    private UserEto toUserEto(UserEntity userEntity){
        UserEto userEto = userMapper.toUserEto(userEntity);
        userEto.setAccountEto(accountMapper.toAccountEto(userEntity.getAccount()));

        RoleEto roleEto = roleMapper.toRoleEto(userEntity.getRole());
        roleEto.setPermissionEtoList(userEntity.getRole().getPermissions().stream()
                .map(p -> permissionsMapper.toPermissionEto(p))
                .collect(Collectors.toList()));
        userEto.setRoleEto(roleEto);
        return userEto;
    }
}
