package com.ivan.mapper;

import com.ivan.dto.TrainingTypeResponse;
import com.ivan.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingTypeMapper {

    List<TrainingTypeResponse> toDtoList(List<TrainingType> entities);
}