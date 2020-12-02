package com.wtulich.photosupp.userhandling.logic.impl.validator;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.PermissionDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleTo;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class RoleValidator {

    @Inject
    private RoleDao roleDao;

    @Inject
    private PermissionDao permissionDao;

    public void verifyIfRoleNameAlreadyExists(String name) throws EntityAlreadyExistsException {
        if (roleDao.existsByName(name)) {
            throw new EntityAlreadyExistsException("Role with name " + name + " already exists");
        }
    }
}
