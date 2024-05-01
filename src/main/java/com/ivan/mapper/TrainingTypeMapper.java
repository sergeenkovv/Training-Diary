package com.ivan.mapper;

import com.ivan.dto.TrainingTypeResponse;
import com.ivan.model.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TrainingTypeMapper {

    TrainingTypeMapper INSTANCE = Mappers.getMapper(TrainingTypeMapper.class);

    List<TrainingTypeResponse> toDtoList(List<TrainingType> entities);
}