package com.eightthreads.backend.entity;

import com.eightthreads.backend.common.enums.EventStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EventStatusConverter implements AttributeConverter<EventStatus, String> {

    @Override
    public String convertToDatabaseColumn(EventStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public EventStatus convertToEntityAttribute(String dbData) {
        return EventStatus.fromValue(dbData);
    }
}

