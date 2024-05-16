package az.erry.clinicrest.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {
    private UUID id;

    private String street;

    private String suburb;

    private String state;

    private Integer postcode;

    private Date createdAt;

    private Date updatedAt;
}
