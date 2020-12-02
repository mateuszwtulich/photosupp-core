package com.wtulich.photosupp.orderhandling.logic.api.mapper;

import com.wtulich.photosupp.orderhandling.dataaccess.api.entity.MediaContentEntity;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentEto;
import com.wtulich.photosupp.orderhandling.logic.api.to.MediaContentTo;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-12-02T17:23:02+0100",
    comments = "version: 1.4.0.Final, compiler: javac, environment: Java 1.8.0_271 (Oracle Corporation)"
)
@Component
public class MediaContentMapperImpl implements MediaContentMapper {

    @Override
    public MediaContentEntity toMediaContentEntity(MediaContentTo mediaContentTo) {
        if ( mediaContentTo == null ) {
            return null;
        }

        MediaContentEntity mediaContentEntity = new MediaContentEntity();

        mediaContentEntity.setType( mediaContentTo.getType() );
        mediaContentEntity.setUrl( mediaContentTo.getUrl() );

        return mediaContentEntity;
    }

    @Override
    public MediaContentEto toMediaContentEto(MediaContentEntity mediaContentEntity) {
        if ( mediaContentEntity == null ) {
            return null;
        }

        MediaContentEto mediaContentEto = new MediaContentEto();

        mediaContentEto.setId( mediaContentEntity.getId() );
        mediaContentEto.setType( mediaContentEntity.getType() );
        mediaContentEto.setUrl( mediaContentEntity.getUrl() );

        return mediaContentEto;
    }
}
