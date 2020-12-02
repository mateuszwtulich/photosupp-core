package com.wtulich.photosupp.orderhandling.logic.api;

import com.wtulich.photosupp.orderhandling.logic.api.usecase.*;

public interface OrderHandling extends
        UcDeleteComment,
        UcDeleteMediaContent,
        UcDeleteOrder,
        UcFindComment,
        UcFindMediaContent,
        UcFindOrder,
        UcManageComment,
        UcManageMediaContent,
        UcManageOrder{
}
