package com.wtulich.photosupp.general.security.authentication.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.security.authentication.logic.impl.to.AuthenticationPayload;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.AccountDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UcLoginImpl implements UserDetailsService {

    @Inject
    private AccountDao accountDao;

    @Inject
    private UserDao userDao;

    private UserEntity userEntity;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        AccountEntity accountEntity = accountDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No account found with username: " + username));
        userEntity = userDao.findByAccount_Username(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                accountEntity.getUsername(),
                accountEntity.getPassword(),
                accountEntity.isActivated(),
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                getGrantedAuthorities());    }

    private List<GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>(0);

        this.userEntity.getRole().getPermissions().stream().forEach(permission -> {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(permission.getName());
                authorities.add(new SimpleGrantedAuthority(stringBuilder.toString()));
        });

        return authorities;
    }

    public Long getUserIdByUserName(String username) {
        return userDao.findByAccount_Username(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username: " + username)).getId();
    }

//    public AuthenticationPayload getAuthenticationPayload(Long userId) {
//        List<String> permissions;
//
//        try {
//            permissions = getPermissions(getUserEntityById(userId).getRole().getPermissions());
//        } catch (EntityDoesNotExistException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//
//        return new AuthenticationPayload(permissions);
//    }

    private List<String> getPermissions(List<PermissionEntity> permissions) {
        return permissions.stream().map(permission -> permission.getName().name())
                .collect(Collectors.toList());
    }

    private UserEntity getUserEntityById(Long userId) throws EntityDoesNotExistException {
        return userDao.findById(userId)
                .orElseThrow(() -> new EntityDoesNotExistException("No user found with id " + userId));
    }
}
