package com.ivan.mapper;

import com.ivan.dto.TrainingResponse;
import com.ivan.model.Training;
import com.ivan.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper
public interface TrainingMapper {

    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);

    @Mapping(source = "trainingType", target = "typeName", qualifiedByName = "mapTypeName")
    List<TrainingResponse> toDtoList(List<Training> entities);

    default String mapTypeName(TrainingType value) {
        return value.getTypeName();
    }
}