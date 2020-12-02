package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.EntityHasAssignedEntitiesException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.BookingDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcDeleteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;

@Validated
@Named
public class UcDeleteServiceImpl implements UcDeleteService {

    private static final Logger LOG = LoggerFactory.getLogger(UcDeleteServiceImpl.class);
    private static final String DELETE_SERVICE_LOG = "Delete Service with id {} in database.";

    @Inject
    private ServiceDao serviceDao;

    @Inject
    private BookingDao bookingDao;

    @Override
    public void deleteService(Long id) throws EntityDoesNotExistException, EntityHasAssignedEntitiesException {
        ServiceEntity serviceEntity = serviceDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Service with id " + id + " does not exist."));

        if(bookingDao.findAllByService_Id(serviceEntity.getId()).isEmpty()){
            LOG.debug(DELETE_SERVICE_LOG, serviceEntity.getId());

            serviceDao.deleteById(serviceEntity.getId());
        } else {
            throw new EntityHasAssignedEntitiesException("Service with id " + id + " has assigned bookings.");
        }
    }
}
