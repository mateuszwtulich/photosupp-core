package com.wtulich.photosupp.orderhandling.logic.api.mapper;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.CommentEntity;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.CommentTo;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-11-30T19:18:31+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 15 (Oracle Corporation)"
)
@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentEntity toCommentEntity(CommentTo commentTo) {
        if ( commentTo == null ) {
            return null;
        }

        CommentEntity commentEntity = new CommentEntity();

        commentEntity.setContent( commentTo.getContent() );

        return commentEntity;
    }

    @Override
    public CommentEto toCommentEto(CommentEntity commentEntity) {
        if ( commentEntity == null ) {
            return null;
        }

        CommentEto commentEto = new CommentEto();

        commentEto.setId( commentEntity.getId() );
        commentEto.setContent( commentEntity.getContent() );
        if ( commentEntity.getCreatedAt() != null ) {
            commentEto.setCreatedAt( DateTimeFormatter.ISO_LOCAL_DATE.format( commentEntity.getCreatedAt() ) );
        }

        return commentEto;
    }
}
