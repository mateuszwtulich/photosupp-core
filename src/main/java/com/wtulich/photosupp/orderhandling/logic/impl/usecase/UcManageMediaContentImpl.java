package com.wtulich.photosupp.orderhandling.logic.impl.usecase;

import com.wtulich.photosupp.general.logic.api.exception.EntityDoesNotExistException;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.MediaContentDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.dao.OrderDao;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.MediaContentEntity;
import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.OrderEntity;
import com.wtulich.photosupp.orderhandling.logic.api.mapper.MediaContentMapper;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentTo;
import com.wtulich.photosupp.orderhandling.logic.api.usecase.UcManageMediaContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Optional;

@Validated
@Named
public class UcManageMediaContentImpl implements UcManageMediaContent {

    private static final Logger LOG = LoggerFactory.getLogger(UcManageMediaContentImpl.class);
    private static final String CREATE_MEDIA_CONTENT_LOG = "Create Media Content with url {} in database.";

    @Inject
    private MediaContentDao mediaContentDao;

    @Inject
    private OrderDao orderDao;

    @Inject
    private MediaContentMapper mediaContentMapper;

    @Override
    public Optional<MediaContentEto> addMediaContent(MediaContentTo mediaContentTo) throws EntityDoesNotExistException {
        LOG.debug(CREATE_MEDIA_CONTENT_LOG, mediaContentTo.getUrl());

        MediaContentEntity mediaContentEntity = mediaContentMapper.toMediaContentEntity(mediaContentTo);
        mediaContentEntity.setOrder(getOrderByOrderNumber(mediaContentTo.getOrderNumber()));

        return Optional.of(toMediaContentEto(mediaContentDao.save(mediaContentEntity)));
    }

    private MediaContentEto toMediaContentEto(MediaContentEntity mediaContentEntity){
        MediaContentEto mediaContentEto = mediaContentMapper.toMediaContentEto(mediaContentEntity);
        mediaContentEto.setOrderNumber(mediaContentEntity.getOrder().getOrderNumber());

        return mediaContentEto;
    }

    private OrderEntity getOrderByOrderNumber(String orderNumber) throws EntityDoesNotExistException {
        return orderDao.findByOrderNumber(orderNumber).orElseThrow(() ->
                new EntityDoesNotExistException("Order with order number " + orderNumber + " does not exist."));
    }
}
