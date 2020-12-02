package com.wtulich.photosupp.orderhandling.logic.api.mapper;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentTo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentEntity toCommentEntity(CommentTo commentTo);

    CommentEto toCommentEto(CommentEntity commentEntity);
}
