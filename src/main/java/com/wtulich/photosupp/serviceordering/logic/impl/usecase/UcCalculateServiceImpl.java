package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.general.logic.api.exception.UnprocessableEntityException;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.ServiceDao;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.IndicatorEntity;
import com.wtulich.photosupp.serviceordering.dataaccess.api.entity.ServiceEntity;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.ServiceMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.*;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcCalculateService;
import com.wtulich.photosupp.serviceordering.logic.impl.validator.BookingValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Validated
@Named
public class UcCalculateServiceImpl implements UcCalculateService {
    private static final Logger LOG = LoggerFactory.getLogger(UcManageBookingImpl.class);
    private static final String ID_CANNOT_BE_NULL = "id cannot be a null value";
    private static final String CALCULATE_SERVICE_LOG = "Calculate service with id {}.";

    @Inject
    private ServiceDao serviceDao;

    @Inject
    private IndicatorDao indicatorDao;

    @Inject
    private ServiceMapper serviceMapper;

    @Inject
    private IndicatorMapper indicatorMapper;

    @Inject
    private BookingValidator bookingValidator;

    @Override
    public Optional<CalculateCto> calculateService(CalculateTo calculateTo) throws EntityDoesNotExistException, UnprocessableEntityException {
        Objects.requireNonNull(calculateTo.getServiceId(), ID_CANNOT_BE_NULL);

        LOG.debug(CALCULATE_SERVICE_LOG, calculateTo.getServiceId());
        CalculateCto calculateCto = new CalculateCto();

        calculateCto.setServiceEto(serviceMapper.toServiceEto(getServiceById(calculateTo.getServiceId())));

        if( calculateTo.getPriceIndicatorToList() != null ){
            calculateCto.setPriceIndicatorEtoList(getPriceIndicatorList(calculateTo.getPriceIndicatorToList()));
        }

        bookingValidator.verifyIfDatesAreValid(LocalDate.parse(calculateTo.getStart()), LocalDate.parse(calculateTo.getEnd()));
        calculateCto.setStart(calculateTo.getStart());
        calculateCto.setEnd(calculateTo.getEnd());
        calculateCto.setPredictedPrice(calculatePredictedPrice(calculateCto));

        return Optional.of(calculateCto);
    }

    private Double calculatePredictedPrice(CalculateCto calculateCto){
        Double predictedPrice = calculateCto.getServiceEto().getBasePrice();

        Integer days = 1 + LocalDate.parse(calculateCto.getEnd()).getDayOfYear() -
                LocalDate.parse(calculateCto.getStart()).getDayOfYear();

        predictedPrice = predictedPrice * days;

        if (calculateCto.getPriceIndicatorEtoList() != null){
            for (PriceIndicatorEto priceIndicator : calculateCto.getPriceIndicatorEtoList()) {
                predictedPrice += priceIndicator.getPrice();
            }
        }

        return  predictedPrice;
    }

    private List<PriceIndicatorEto> getPriceIndicatorList
            (List<PriceIndicatorTo> priceIndicatorToList) throws EntityDoesNotExistException {
        List<PriceIndicatorEto> priceIndicatorEtoList = new ArrayList<>();

        for (PriceIndicatorTo priceIndicatorTo : priceIndicatorToList) {

            priceIndicatorEtoList.add(toPriceIndicatorEto(priceIndicatorTo));
        }

        return priceIndicatorEtoList;
    }

    private PriceIndicatorEto toPriceIndicatorEto(PriceIndicatorTo priceIndicatorTo)
            throws EntityDoesNotExistException {

        PriceIndicatorEto priceIndicatorEto = new PriceIndicatorEto();
        priceIndicatorEto.setIndicatorEto(indicatorMapper.toIndicatorEto(getIndicatorById(priceIndicatorTo.getIndicatorId())));
        priceIndicatorEto.setPrice(priceIndicatorTo.getPrice());
        priceIndicatorEto.setAmount(priceIndicatorTo.getAmount());

        return priceIndicatorEto;
    }

    private ServiceEntity getServiceById(Long serviceId) throws EntityDoesNotExistException {
        return serviceDao.findById(serviceId).orElseThrow(() ->
                new EntityDoesNotExistException("Service with id " + serviceId + " does not exist."));
    }

    private IndicatorEntity getIndicatorById(Long indicatorId) throws EntityDoesNotExistException {
        return indicatorDao.findById(indicatorId).orElseThrow(() ->
                new EntityDoesNotExistException("Indicator with id " + indicatorId + " does not exist."));
    }

}
