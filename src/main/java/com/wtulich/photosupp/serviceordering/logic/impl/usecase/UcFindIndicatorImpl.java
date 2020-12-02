package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.IndicatorDao;
import com.wtulich.photosupp.serviceordering.logic.api.mapper.IndicatorMapper;
import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcFindIndicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@Named
public class UcFindIndicatorImpl implements UcFindIndicator {

    private static final Logger LOG = LoggerFactory.getLogger(UcFindIndicatorImpl.class);
    private static final String GET_ALL_INDICATORS_LOG = "Get all Indicators from database.";

    @Inject
    private IndicatorDao indicatorDao;

    @Inject
    private IndicatorMapper indicatorMapper;

    @Override
    public Optional<List<IndicatorEto>> findAllIndicators() {
        LOG.debug(GET_ALL_INDICATORS_LOG);

        return Optional.of(indicatorDao.findAll().stream()
                .map(indicatorEntity -> indicatorMapper.toIndicatorEto(indicatorEntity))
                .collect(Collectors.toList()));
    }
}
