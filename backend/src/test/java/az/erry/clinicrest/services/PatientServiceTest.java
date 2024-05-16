package az.erry.clinicrest.services;

import az.erry.clinicrest.entities.Address;
import az.erry.clinicrest.entities.Patient;
import az.erry.clinicrest.entities.PatientGender;
import az.erry.clinicrest.models.AddressRequest;
import az.erry.clinicrest.models.RegisterPatientRequest;
import az.erry.clinicrest.models.UpdatePatientRequest;
import az.erry.clinicrest.repositories.AddressRepository;
import az.erry.clinicrest.repositories.PatientRepository;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private PatientService patientService;

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister() {
        AddressRequest addressRequest = AddressRequest.builder()
                .street("123 Street")
                .build();
        RegisterPatientRequest request = RegisterPatientRequest.builder().
                firstName("John").
                lastName("Doe").
                parsedDoB(new Date()).
                parsedPhoneNumber(phoneNumberUtil.getExampleNumber("ID")).
                gender(PatientGender.MALE).
                address(addressRequest).
                build();

        Address address = new Address();
        address.setStreet("123 Street");
        address.setSuburb("Suburb");
        address.setState("State");
        address.setPostcode(1234);

        when(patientRepository.findTopByPatientIdentityIsNotNullOrderByPatientIdentityDesc()).thenReturn(Optional.empty());

        patientService.register(request);

        ArgumentCaptor<Patient> patientCaptor = ArgumentCaptor.forClass(Patient.class);
        verify(patientRepository, times(1)).save(patientCaptor.capture());

        Patient savedPatient = patientCaptor.getValue();
        assertEquals("John", savedPatient.getFirstName());
        assertEquals("Doe", savedPatient.getLastName());

        ArgumentCaptor<Address> addressCaptor = ArgumentCaptor.forClass(Address.class);
        verify(addressRepository, times(1)).save(addressCaptor.capture());

        Address savedAddress = addressCaptor.getValue();
        assertEquals("123 Street", savedAddress.getStreet());
        assertEquals(savedPatient, savedAddress.getPatient());
    }

    @Test
    public void testSearch() {
        Patient patient = new Patient();
        Page<Patient> patientPage = new PageImpl<>(Collections.singletonList(patient));
        when(patientRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(patientPage);

        Page<Patient> result = patientService.search(0, 10, "John");

        assertEquals(1, result.getContent().size());
        verify(patientRepository, times(1)).findAll(any(Specification.class), eq(PageRequest.of(0, 10)));
    }

    @Test
    public void testUpdate() {
        UUID patientId = UUID.randomUUID();
        AddressRequest addressRequest = AddressRequest.builder().build();
        UpdatePatientRequest request = UpdatePatientRequest.builder().
                id(patientId).
                firstName("John").
                lastName("Doe").
                parsedDoB(new Date()).
                parsedPhoneNumber(phoneNumberUtil.getExampleNumber("ID")).
                gender(PatientGender.MALE).
                address(addressRequest).
                build();

        Address address = new Address();
        address.setStreet("123 Street");
        address.setSuburb("Suburb");
        address.setState("State");
        address.setPostcode(12345);

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setAddress(new Address());

        when(patientRepository.findTopByIdAndSoftDeleteFalse(eq(patientId))).thenReturn(Optional.of(patient));

        patientService.update(request);

        verify(patientRepository, times(1)).save(patient);
        verify(addressRepository, times(1)).save(patient.getAddress());
    }

    @Test
    public void testGet() {
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepository.findTopByIdAndSoftDeleteFalse(eq(patientId))).thenReturn(Optional.of(patient));

        Patient result = patientService.get(patientId);

        assertEquals(patient, result);
        verify(patientRepository, times(1)).findTopByIdAndSoftDeleteFalse(patientId);
    }

    @Test
    public void testDelete() {
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(patientId);

        when(patientRepository.findTopByIdAndSoftDeleteFalse(eq(patientId))).thenReturn(Optional.of(patient));

        Patient result = patientService.delete(patientId);

        assertEquals(patient, result);
        assertTrue(patient.getSoftDelete());
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    public void testGetPatientNotFound() {
        UUID patientId = UUID.randomUUID();

        when(patientRepository.findTopByIdAndSoftDeleteFalse(eq(patientId))).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            patientService.get(patientId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
