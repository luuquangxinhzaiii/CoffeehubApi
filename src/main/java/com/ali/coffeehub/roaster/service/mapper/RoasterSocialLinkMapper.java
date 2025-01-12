package com.ali.coffeehub.roaster.service.mapper;

import com.ali.coffeehub.roaster.domain.RoasterSocialLinkEntity;
import com.ali.coffeehub.roaster.service.dto.RoasterSocialLinkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoasterSocialLinkEntity} and its DTO {@link RoasterSocialLinkDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoasterSocialLinkMapper extends EntityMapper<RoasterSocialLinkDTO, RoasterSocialLinkEntity> {}
