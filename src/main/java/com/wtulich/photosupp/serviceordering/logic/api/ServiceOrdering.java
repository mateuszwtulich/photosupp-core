package com.wtulich.photosupp.serviceordering.logic.api;

import com.wtulich.photosupp.serviceordering.logic.api.usecase.*;

public interface ServiceOrdering extends
        UcCalculateService,
        UcDeleteBooking,
        UcDeleteIndicator,
        UcDeleteService,
        UcFindAddress,
        UcFindIndicator,
        UcFindBooking,
        UcFindService,
        UcManageBooking,
        UcManageIndicator,
        UcManageService {
}
