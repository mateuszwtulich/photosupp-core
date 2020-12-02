package com.wtulich.photosupp.general.security.authentication.logic.impl.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wtulich.photosupp.general.security.authentication.logic.impl.exception.ForbiddenException;
import com.wtulich.photosupp.general.security.authentication.logic.impl.to.ScopePermission;
import com.wtulich.photosupp.general.security.enums.ApplicationPermissions;
import com.wtulich.photosupp.general.utils.annotations.PermissionRestrict;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Aspect
@Component
public class ScopePermissionAspect {

    @Inject
    private UserDao userDao;

    @Before("@annotation(com.wtulich.photosupp.general.utils.annotations.PermissionRestrict)")
    public void hasScopes(final JoinPoint joinPoint) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        PermissionRestrict annotation = signature.getMethod().getAnnotation(PermissionRestrict.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();

        ScopePermission scopePermission = new ObjectMapper().readValue(request.getHeader("Scope"), ScopePermission.class);

        if(!hasAccess(scopePermission, annotation.permissions())) {
            throw new ForbiddenException();
        }
    }

    private boolean hasAccess(ScopePermission scopePermission, ApplicationPermissions[] applicationPermissions) {
        boolean accessed = false;

        if(!scopePermission.getIsAdmin()) {

            List<PermissionEntity> permissions = getUserById(scopePermission.getUserId()).getRole().getPermissions();

            for(ApplicationPermissions applicationPermission : applicationPermissions) {
                for(PermissionEntity userPermission: permissions) {
                    if(userPermission.getName().name().equals(applicationPermission.name())) {
                        accessed = true;
                    }
                }
            }
        } else {
            accessed = true;
        }
        return accessed;
    }

    private UserEntity getUserById(Long userId){
        return userDao.findById(userId).get();
    }
}
