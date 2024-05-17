package com.ivan.trainingdiary.mapper;

import com.ivan.trainingdiary.dto.TrainingTypeResponse;
import com.ivan.trainingdiary.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingTypeMapper {

    List<TrainingTypeResponse> toDtoList(List<TrainingType> entities);
}