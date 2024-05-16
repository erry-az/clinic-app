package az.erry.clinicrest.models;

import az.erry.clinicrest.entities.PatientGender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterPatientRequest {
    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String dateOfBirth;

    @NotNull
    @Valid
    private PatientGender gender;

    @NotBlank
    @Size(max = 32)
    private String phoneNumber;

    @NotNull
    @Valid
    private AddressRequest address;

    @JsonIgnore
    private Date parsedDoB;

    @JsonIgnore
    private Phonenumber.PhoneNumber parsedPhoneNumber;
}
