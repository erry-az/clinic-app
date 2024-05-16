package az.erry.clinicrest.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {
    @NotBlank
    @Size(max = 250)
    private String street;

    @NotBlank
    @Size(max = 100)
    private String suburb;

    @NotBlank
    @Size(max = 100)
    private String state;

    @Min(1)
    @Max(9999)
    private Integer postcode;
}
