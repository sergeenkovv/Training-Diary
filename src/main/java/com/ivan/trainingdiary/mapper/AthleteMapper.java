package com.ivan.trainingdiary.mapper;

import com.ivan.trainingdiary.dto.AthleteResponse;
import com.ivan.trainingdiary.model.Athlete;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AthleteMapper {

    AthleteResponse toDto(Athlete athlete);

    List<AthleteResponse> toDtoList(List<Athlete> entities);
}