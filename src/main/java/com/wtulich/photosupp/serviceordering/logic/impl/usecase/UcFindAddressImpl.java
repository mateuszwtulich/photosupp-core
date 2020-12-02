package com.wtulich.photosupp.serviceordering.logic.impl.usecase;

import com.wtulich.photosupp.serviceordering.dataaccess.api.dao.AddressDao;
import com.wtulich.photosupp.serviceordering.logic.api.usecase.UcFindAddress;
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
public class UcFindAddressImpl implements UcFindAddress {

    private static final Logger LOG = LoggerFactory.getLogger(UcFindAddressImpl.class);
    private static final String GET_ALL_CITIES_LOG = "Get all cities from database.";
    private static final String GET_ALL_STREETS_LOG = "Get all streets from database.";

    @Inject
    private AddressDao addressDao;

    @Override
    public Optional<List<String>> findAllCities() {
        LOG.debug(GET_ALL_CITIES_LOG);

        return Optional.of(addressDao.findAll().stream()
                .map(addressEntity -> addressEntity.getCity())
                .collect(Collectors.toList()));
    }

    @Override
    public Optional<List<String>> findAllStreets() {
        LOG.debug(GET_ALL_STREETS_LOG);

        return Optional.of(addressDao.findAll().stream()
                .map(addressEntity -> addressEntity.getStreet())
                .collect(Collectors.toList()));
    }
}
