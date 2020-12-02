package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityAlreadyExistsException;
import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceEto;
import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceTo;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcManageService;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.ServiceValidator;
import com.wtulich.photosupp.userhandling.dataaccess.api.entity.PermissionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Named
public class UcManageServiceImpl implements UcManageService {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageServiceImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String CREATE_SERVICE_LOG = "Create Service with name {} in database.";
    private static final String UPDATE_SERVICE_LOG = "Update Service with id {} from database.";

    @Inject
    private ServiceDao serviceDao;

    @Inject
    private IndicatorDao indicatorDao;

    @Inject
    private ServiceMapper serviceMapper;

    @Inject
    private IndicatorMapper indicatorMapper;

    @Inject
    private ServiceValidator serviceValidator;

    @Override
    public Optional<ServiceEto> createService(ServiceTo serviceTo) throws EntityAlreadyExistsException, EntityDoesNotExistException {
        serviceValidator.verifyIfServiceNameAlreadyExists(serviceTo.getName());
        LOG.debug(CREATE_SERVICE_LOG, serviceTo.getName());

        ServiceEntity serviceEntity = serviceMapper.toServiceEntity(serviceTo);
        if(serviceTo.getIndicatorsIds().size() > 0){
            serviceEntity.setIndicatorList(getIndicatorsByIds(serviceTo.getIndicatorsIds()));
        }

        return Optional.of(toServiceEto(serviceDao.save(serviceEntity)));
    }

    @Override
    public Optional<ServiceEto> updateService(ServiceTo serviceTo, Long id) throws EntityDoesNotExistException,
            EntityAlreadyExistsException {

        Objects.requireNonNull(id, ID_CANNOT_BE_NULL);

        ServiceEntity serviceEntity = serviceDao.findById(id).orElseThrow(() ->
                new EntityDoesNotExistException("Service with id " + id + " does not exist."));

        if(!serviceEntity.getName().equals(serviceTo.getName())){
            serviceValidator.verifyIfServiceNameAlreadyExists(serviceTo.getName());
            serviceEntity.setName(serviceTo.getName());
        }

        LOG.debug(UPDATE_SERVICE_LOG, id);
        serviceEntity.setDescription(serviceTo.getDescription());
        serviceEntity.setBasePrice(serviceTo.getBasePrice());

        if(serviceTo.getIndicatorsIds().size() > 0){
            serviceEntity.setIndicatorList(getIndicatorsByIds(serviceTo.getIndicatorsIds()));
        } else {
            serviceEntity.setIndicatorList(new ArrayList<>());
        }

        return Optional.of(toServiceEto(serviceEntity));
    }

    private List<IndicatorEntity> getIndicatorsByIds(List<Long> indicatorsIds) throws EntityDoesNotExistException {
        List<IndicatorEntity> indicatorEntities = indicatorDao.findAllById(indicatorsIds);

        if(indicatorEntities.size() == 0){
            throw new EntityDoesNotExistException("Indicators do not exist");
        }
        return indicatorEntities;
    }

    private ServiceEto toServiceEto(ServiceEntity serviceEntity){
        ServiceEto serviceEto = serviceMapper.toServiceEto(serviceEntity);
        serviceEto.setIndicatorEtoList(serviceEntity.getIndicatorList().stream()
                .map(i -> indicatorMapper.toIndicatorEto(i))
                .collect(Collectors.toList()));
        return serviceEto;
    }
}
