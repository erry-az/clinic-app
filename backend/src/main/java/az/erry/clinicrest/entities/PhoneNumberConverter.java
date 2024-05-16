package az.erry.clinicrest.entities;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PhoneNumberConverter implements AttributeConverter<Phonenumber.PhoneNumber, String> {

    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @Override
    public String convertToDatabaseColumn(Phonenumber.PhoneNumber attribute) {
        if (attribute == null) {
            return null;
        }
        return phoneUtil.format(attribute, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }

    @Override
    public Phonenumber.PhoneNumber convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        try {
            return phoneUtil.parse(dbData, "");
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("Invalid phone number format: " + dbData);
        }
    }
}