package com.ivan.mapper;

import com.ivan.dto.AthleteResponse;
import com.ivan.model.Athlete;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AthleteMapper {

    AthleteResponse toDto(Athlete athlete);

    List<AthleteResponse> toDtoList(List<Athlete> entities);
}