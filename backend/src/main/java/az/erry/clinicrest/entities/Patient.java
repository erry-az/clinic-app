package az.erry.clinicrest.entities;

import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "patient_identity", unique = true)
    private String patientIdentity;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private PatientGender gender;

    @Column(name = "phone_no")
    @Convert(converter = PhoneNumberConverter.class)
    private Phonenumber.PhoneNumber phoneNumber;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private Address address;

    @Column(name = "soft_delete")
    private Boolean softDelete = false;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}
