package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.AccountDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.RoleDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.UserDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.RoleEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.UserEntity;
import com.wtulich.photosupp.userhandling.logic.api.exception.AccountAlreadyExistsException;
import com.wtulich.photosupp.userhandling.logic.api.mapper.AccountMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.PermissionsMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.RoleMapper;
import com.wtulich.photosupp.userhandling.logic.api.mapper.UserMapper;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountEto;
import com.wtulich.photosupp.userhandling.logic.api.to.AccountTo;
import com.wtulich.photosupp.userhandling.logic.api.to.RoleEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserEto;
import com.wtulich.photosupp.userhandling.logic.api.to.UserTo;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcManageUser;
import com.wtulich.photosupp.userhandling.logic.impl.events.OnRegistrationCompleteEvent;
import com.wtulich.photosupp.userhandling.logic.impl.validator.AccountValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Optional;

@Validated
@Named
public class UcManageUserImpl implements UcManageUser {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageUserImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String CREATE_USER_LOG = "Create User with surname {} in database.";
    private static final String UPDATE_USER_LOG = "Update User with id {} in database.";
    private static final String CREATE_ACCOUNT_LOG = "Create Account with username {} in database.";
    private static final String UPDATE_ACCOUNT_LOG = "Update Account with id {} in database.";
    private static final String SEND_EMAIL_FOR_VERIFICATION = "Send mail for verification of username {} in database.";

    @Inject
    private UserDao userDao;

    @Inject
    private AccountDao accountDao;

    @Inject
    private RoleDao roleDao;

    @Inject
    private UserMapper userMapper;

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private RoleMapper roleMapper;

    @Inject
    private PermissionsMapper permissionsMapper;

    @Inject
    private AccountValidator accountValidator;

    @Inject
    private ApplicationEventPublisher applicationEventPublisher;

    @Named("messageSource")
    @Inject
    private MessageSource messages;

    @Inject
    private JavaMailSender mailSender;

    @Inject
    private PasswordEncoder passwordEncoder;


    @Override
    public Optional<UserEto> createUserAndAccountEntities(UserTo userTo, HttpServletRequest request, Errors errors)
            throws AccountAlreadyExistsException, AddressException, EntityDoesNotExistException {
        LOG.debug(CREATE_USER_LOG, userTo.getSurname());
        AccountEntity accountEntity = createAccountEntities(userTo.getAccountTo());

        UserEntity userEntity = userMapper.toUserEntity(userTo);
        userEntity.setRole(getRoleById(userTo.getRoleId()));
        userEntity.setAccount(accountEntity);

        UserEntity userSaved = userDao.save(userEntity);

        sendMailOfAccountCreation(accountEntity, userTo.getAccountTo().getPassword(), request, errors);

        return toUserEto(userSaved);
    }

    @Override
    public Optional<UserEto> updateUser(UserTo userTo, Long userId) throws EntityDoesNotExistException {
        LOG.debug(UPDATE_USER_LOG, userId);
        UserEntity userEntity = getUserById(userId);

        userEntity.setRole(getRoleById(userTo.getRoleId()));
        userEntity.setName(userTo.getName());
        userEntity.setSurname(userTo.getSurname());

        return toUserEto(userEntity);
    }

    @Override
    public Optional<AccountEto> updateUserAccount(AccountTo accountTo, Long userId)
            throws AccountAlreadyExistsException, AddressException, EntityDoesNotExistException {
        LOG.debug(UPDATE_ACCOUNT_LOG, userId);
        UserEntity userEntity = getUserById(userId);

        AccountEntity accountEntity = userEntity.getAccount();

        if(!accountEntity.getEmail().equals(accountTo.getEmail())){
            verifyAccount(accountTo);
            accountEntity.setEmail(accountTo.getEmail());
            accountEntity.setUsername(extractUsername(accountTo.getEmail()));
            sendMailWithNewUsername(accountEntity);
        }

        if(!accountEntity.getPassword().equals(accountTo.getPassword())){
            accountEntity.setPassword(passwordEncoder.encode(accountTo.getPassword()));
            sendMailWithNewPassword(accountEntity, accountTo.getPassword());
        }

        userEntity.setAccount(accountEntity);
        return Optional.of(accountMapper.toAccountEto(accountEntity));
    }

    @Override
    public void updatePassword(AccountTo accountTo) throws EntityDoesNotExistException {
        AccountEntity accountEntity = accountDao.findByEmail(accountTo.getEmail()).orElseThrow(() ->
                new EntityDoesNotExistException("Account with id " + accountTo.getEmail() + " does not exist."));

        accountEntity.setPassword(passwordEncoder.encode(accountTo.getPassword()));
        sendMailWithNewPassword(accountEntity, accountTo.getPassword());
    }

    private AccountEntity createAccountEntities(AccountTo accountTo) throws AccountAlreadyExistsException, AddressException {
        verifyAccount(accountTo);

        AccountEntity accountEntity = toAccountEntity(accountTo);
        accountEntity.setPassword(passwordEncoder.encode(accountTo.getPassword()));
        LOG.debug(CREATE_ACCOUNT_LOG, accountEntity.getUsername());

        return accountDao.save(accountEntity);
    }

    private void verifyAccount(AccountTo accountTo) throws AccountAlreadyExistsException, AddressException {
            accountValidator.verifyIfAccountAlreadyExists(accountTo);
            accountValidator.verifyIfValidEmailAddress(accountTo.getEmail());
    }

    private void sendMailOfAccountCreation(AccountEntity accountEntity, String password,
                                           HttpServletRequest request, Errors errors) {
        String appUrl = request.getContextPath();

        LOG.debug(SEND_EMAIL_FOR_VERIFICATION, accountEntity.getUsername());
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(accountEntity, password,
                request.getLocale(), appUrl));
    }


    private AccountEntity toAccountEntity(AccountTo accountTo) {
        String username = extractUsername(accountTo.getEmail());
        AccountEntity accountEntity = accountMapper.toAccountEntity(accountTo);
        accountEntity.setUsername(username);
        accountEntity.setActivated(false);

        return accountEntity;
    }

    private Optional<UserEto> toUserEto(UserEntity userEntity){
        UserEto userEto = userMapper.toUserEto(userEntity);
        userEto.setAccountEto(accountMapper.toAccountEto(userEntity.getAccount()));

        RoleEto roleEto = roleMapper.toRoleEto(userEntity.getRole());
        roleEto.setPermissionEtoList(userEntity.getRole().getPermissions().stream()
                .map(p -> permissionsMapper.toPermissionEto(p))
                .collect(Collectors.toList()));
        userEto.setRoleEto(roleEto);
        return Optional.of(userEto);
    }

    private UserEntity getUserById(Long userId) throws EntityDoesNotExistException {
        Objects.requireNonNull(userId, ID_CANNOT_BE_NULL);

        return userDao.findById(userId).orElseThrow(() ->
                new EntityDoesNotExistException("User with id " + userId + " does not exist."));
    }

    private RoleEntity getRoleById(Long roleId) throws EntityDoesNotExistException {
        Objects.requireNonNull(roleId, ID_CANNOT_BE_NULL);

        return roleDao.findById(roleId).orElseThrow(() ->
                new EntityDoesNotExistException("Role with id " + roleId + " does not exist."));
    }

    private String extractUsername(String email){
        return email.split("@")[0];
    }

    private void sendMailWithNewPassword(AccountEntity account, String password){
        String recipientAddress = account.getEmail();
        String subject = messages.getMessage("message.passwordTitle", null, Locale.getDefault());
        StringBuilder generatePasswordMessage = new StringBuilder();
        StringBuilder message = new StringBuilder();
        message.append(messages.getMessage("message.passwordSucc", null, Locale.getDefault()));
        message.append("\n\nHasło: " + password + "\n");
        message.append("\r\n\n" + messages.getMessage("frontend", null, Locale.getDefault()) + "/login" + generatePasswordMessage.toString());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message.toString());
        mailSender.send(email);
    }

    private void sendMailWithNewUsername(AccountEntity account) {
        String recipientAddress = account.getEmail();
        String subject = messages.getMessage("message.usernameTitle", null, Locale.getDefault());
        StringBuilder generatePasswordMessage = new StringBuilder();
        StringBuilder message = new StringBuilder();
        message.append(messages.getMessage("message.usernameSucc", null, Locale.getDefault()));
        message.append("\n\nNazwa użytkownika: " + account.getUsername() + "\n");
        message.append("\r\n\n" + messages.getMessage("frontend", null, Locale.getDefault()) + "/login" + generatePasswordMessage.toString());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message.toString());
        mailSender.send(email);
    }
}
