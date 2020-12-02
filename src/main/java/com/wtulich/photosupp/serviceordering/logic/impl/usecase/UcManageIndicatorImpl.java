package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorTo;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcManageIndicator;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.IndicatorValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;
import java.util.Optional;

@Validated
@Named
public class UcManageIndicatorImpl implements UcManageIndicator {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageIndicatorImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String CREATE_INDICATOR_LOG = "Create Indicator with name {} in database.";
    private static final String UPDATE_INDICATOR_LOG = "Update Indicator with id {} from database.";

    @Inject
    private IndicatorDao indicatorDao;

    @Inject
    private IndicatorMapper indicatorMapper;

    @Inject
    private IndicatorValidator indicatorValidator;

    @Override
    public Optional<IndicatorEto> createIndicator(IndicatorTo indicatorTo) throws EntityAlreadyExistsException {
        indicatorValidator.verifyIfIndicatorNameAlreadyExists(indicatorTo.getName());
        LOG.debug(CREATE_INDICATOR_LOG, indicatorTo.getName());

        IndicatorEntity indicatorEntity = indicatorMapper.toIndicatorEntity(indicatorTo);
        return Optional.of(indicatorMapper.toIndicatorEto(indicatorDao.save(indicatorEntity)));
    }

    @Override
    public Optional<IndicatorEto> updateIndicator(IndicatorTo indicatorTo, Long id)
            throws EntityDoesNotExistException, EntityAlreadyExistsException {
        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);

        IndicatorEntity indicatorEntity = indicatorDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Indicator with id " + id + " does not exist."));

        if(!indicatorEntity.getName().equals(indicatorTo.getName())){
            indicatorValidator.verifyIfIndicatorNameAlreadyExists(indicatorTo.getName());
            indicatorEntity.setName(indicatorTo.getName());
        }

        LOG.debug(UPDATE_INDICATOR_LOG, id);
        indicatorEntity.setDescription(indicatorTo.getDescription());
        indicatorEntity.setBaseAmount(indicatorTo.getBaseAmount());

        return Optional.of(indicatorMapper.toIndicatorEto(indicatorEntity));
    }
}
