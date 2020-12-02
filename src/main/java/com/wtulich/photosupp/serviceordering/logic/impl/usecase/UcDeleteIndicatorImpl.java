package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.PriceIndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcDeleteIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;

@Validated
@Named
public class UcDeleteIndicatorImpl implements UcDeleteIndicator {
    private static final Logger LOG = LoggerFactory.getLogger(UcDeleteIndicatorImpl.class);
    private static final String DELETE_INDICATOR_LOG = "Delete Indicator with id {} in database.";

    @Inject
    private IndicatorDao indicatorDao;

    @Inject
    private PriceIndicatorDao priceIndicatorDao;

    @Override
    public void deleteIndicator(Long id) throws EntityDoesNotExistException, EntityHasAssignedEntitiesException {
        IndicatorEntity indicatorEntity = indicatorDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Indicator with id " + id + " does not exist."));

        if(priceIndicatorDao.findAllByIndicator_Id(indicatorEntity.getId()).isEmpty()){
            LOG.debug(DELETE_INDICATOR_LOG, indicatorEntity.getId());

            indicatorDao.deleteById(indicatorEntity.getId());
        } else {
            throw new EntityHasAssignedEntitiesException("Indicator with id " + id + " has assigned bookings.");
        }
    }
}
