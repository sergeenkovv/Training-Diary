package com.ivan.trainingdiary.mapper;

import com.ivan.trainingdiary.dto.TrainingResponse;
import com.ivan.trainingdiary.model.Training;
import com.ivan.trainingdiary.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TrainingMapper {

    @Mapping(source = "trainingType", target = "typeName", qualifiedByName = "mapTypeName")
    List<TrainingResponse> toDtoList(List<Training> entities);

    default String mapTypeName(TrainingType value) {
        return value.getTypeName();
    }
}