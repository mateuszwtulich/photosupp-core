package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.serviceordering.logic.api.to.IndicatorEto;

import java.util.List;
import java.util.Optional;

public interface UcFindIndicator {

    Optional<List<IndicatorEto>> findAllIndicators();
}
