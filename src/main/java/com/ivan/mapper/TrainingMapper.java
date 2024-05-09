package com.ivan.mapper;

import com.ivan.dto.TrainingResponse;
import com.ivan.model.Training;
import com.ivan.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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