package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import java.util.List;
import java.util.Optional;

public interface UcFindAddress {

    Optional<List<String>> findAllCities();

    Optional<List<String>> findAllStreets();
}
