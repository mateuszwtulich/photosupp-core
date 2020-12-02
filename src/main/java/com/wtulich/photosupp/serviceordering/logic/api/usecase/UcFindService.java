package com.wtulich.photosupp.serviceordering.logic.api.usecase;

import com.wtulich.photosupp.serviceordering.logic.api.to.ServiceEto;

import java.util.List;
import java.util.Optional;

public interface UcFindService {

    Optional<List<ServiceEto>> findAllServices();
}
