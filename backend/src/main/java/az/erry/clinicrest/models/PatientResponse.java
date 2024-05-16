package az.erry.clinicrest.models;

import az.erry.clinicrest.entities.Address;
import az.erry.clinicrest.entities.PatientGender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse {

    private UUID id;

    private String patientIdentity;

    private String firstName;

    private String lastName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;

    @Enumerated(EnumType.STRING)
    private PatientGender gender;

    private String phoneNumber;

    private AddressResponse address;

    private Date createdAt;

    private Date updatedAt;
}
