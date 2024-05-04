package com.ivan.mapper;

import com.ivan.dto.AthleteResponse;
import com.ivan.model.Athlete;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AthleteMapper {

    AthleteMapper INSTANCE = Mappers.getMapper(AthleteMapper.class);

    List<AthleteResponse> toDtoList(List<Athlete> entities);
}