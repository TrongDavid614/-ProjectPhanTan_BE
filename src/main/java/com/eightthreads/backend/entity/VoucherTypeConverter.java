package com.eightthreads.backend.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class VoucherTypeConverter implements AttributeConverter<VoucherType, String> {

    @Override
    public String convertToDatabaseColumn(VoucherType attribute) {
        return attribute == null ? null : attribute.getDatabaseValue();
    }

    @Override
    public VoucherType convertToEntityAttribute(String dbData) {
        return VoucherType.fromDatabaseValue(dbData);
    }
}

