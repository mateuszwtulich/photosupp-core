package com.wtulich.photosupp.userhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.userhandling.dataaccess.api.dao.VerificationTokenDao;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.AccountEntity;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.VerificationTokenEntity;
import com.wtulich.photosupp.userhandling.logic.api.usecase.UcManageRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;
import java.util.Optional;

@Validated
@Named
public class UcManageRegistrationImpl implements UcManageRegistration {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageRegistrationImpl.class);
    private static final String VERIFICATION_TOKEN_NOT_FOUND = "Verification token not found!";
    private static final String CONFIRM_REGISTRATION_LOG = "Confirm registration of account with id {} in database.";

    @Inject
    private VerificationTokenDao verificationTokenDao;

    @Named("messageSource")
    @Inject
    private MessageSource messages;

    @Override
    public Optional<RedirectView> confirmRegistration(String token) throws EntityDoesNotExistException {
        VerificationTokenEntity verificationTokenEntity = verificationTokenDao.findByToken(token).orElseThrow(() ->
                new EntityDoesNotExistException(VERIFICATION_TOKEN_NOT_FOUND));
        AccountEntity accountEntity = verificationTokenEntity.getAccount();

        LOG.debug(CONFIRM_REGISTRATION_LOG, verificationTokenEntity.getAccount().getId());
        accountEntity.setActivated(true);

        return Optional.of(new RedirectView(messages.getMessage("frontend", null, Locale.getDefault()) + "/login"));
    }
}
