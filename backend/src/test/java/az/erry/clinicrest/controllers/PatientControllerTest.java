package az.erry.clinicrest.controllers;

//import az.erry.clinicrest.entities.Address;
//import az.erry.clinicrest.entities.Patient;
//import az.erry.clinicrest.entities.PatientGender;
//import az.erry.clinicrest.entities.PatientIdGenerator;
//import az.erry.clinicrest.models.*;
//import az.erry.clinicrest.repositories.AddressRepository;
//import az.erry.clinicrest.repositories.PatientRepository;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.i18n.phonenumbers.NumberParseException;
//import com.google.i18n.phonenumbers.PhoneNumberUtil;
//import com.google.i18n.phonenumbers.Phonenumber;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class PatientControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private PatientRepository patientRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//
//    @BeforeEach
//    void setUp()  {
//        patientRepository.deleteAll();
//        addressRepository.deleteAll();
//    }
//
//    @Test
//    void testRegisterSuccess() throws Exception {
//        RegisterPatientRequest request = getPatientRequest();
//
//        mockMvc.perform(
//                post("/api/patients").
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(request))
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals("OK", response.getData());
//        });
//
//        mockMvc.perform(
//                post("/api/patients").
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(request))
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals("OK", response.getData());
//        });
//    }
//
//    @Test
//    void testRegisterBadRequest() throws Exception {
//        RegisterPatientRequest request = getInvalidPatientRequest();
//
//        mockMvc.perform(
//                post("/api/patients").
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(request))
//        ).andExpectAll(
//                status().isBadRequest()
//        );
//    }
//
//    @Test
//    void testSearchNotFound() throws Exception {
//        mockMvc.perform(
//                get("/api/patients").
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals(0, response.getData().size());
//            assertEquals(0, response.getPaging().getTotalPage());
//            assertEquals(0, response.getPaging().getPage());
//            assertEquals(10, response.getPaging().getSize());
//        });
//    }
//
//    @Test
//    void testSearchFound() throws Exception {
//        storePatient(100);
//
//        mockMvc.perform(
//                get("/api/patients").
//                        queryParam("query", "first").
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals(10, response.getData().size());
//            assertEquals(10, response.getPaging().getTotalPage());
//            assertEquals(0, response.getPaging().getPage());
//            assertEquals(10, response.getPaging().getSize());
//        });
//
//        mockMvc.perform(
//                get("/api/patients").
//                        queryParam("query", "last").
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals(10, response.getData().size());
//            assertEquals(10, response.getPaging().getTotalPage());
//            assertEquals(0, response.getPaging().getPage());
//            assertEquals(10, response.getPaging().getSize());
//        });
//
//        mockMvc.perform(
//                get("/api/patients").
//                        queryParam("query", "A000").
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals(9, response.getData().size());
//            assertEquals(1, response.getPaging().getTotalPage());
//            assertEquals(0, response.getPaging().getPage());
//            assertEquals(10, response.getPaging().getSize());
//        });
//    }
//
//    private void storePatient(Integer max) {
//        try {
//            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse("+6281211290399", "");
//
//            for (int i = 0; i < max; i++) {
//                PatientIdGenerator idGenerator = patientRepository.findTopByOrderByCreatedAtDesc().
//                        map(patient -> new PatientIdGenerator(patient.getPatientIdentity())).
//                        orElseGet(PatientIdGenerator::new);
//
//                Patient patient = new Patient();
//                patient.setFirstName("first" + i);
//                patient.setLastName("last" + i);
//                patient.setPatientIdentity(idGenerator.generateId());
//                patient.setGender(PatientGender.MALE);
//                patient.setPhoneNumber(phoneNumber);
//                patient.setDateOfBirth(new Date());
//
//                patientRepository.save(patient);
//
//                Address address = new Address();
//                address.setStreet("street" + i);
//                address.setSuburb("suburb" + i);
//                address.setState("state" + i);
//                address.setPostcode(1010);
//                address.setPatient(patient);
//
//                addressRepository.save(address);
//            }
//        } catch (NumberParseException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    void testUpdateSuccess() throws Exception {
//        storePatient(1);
//
//        UUID latestPatientID = patientRepository.findTopByOrderByCreatedAtDesc().
//                map(Patient::getId).
//                orElseThrow();
//
//        AddressRequest addressRequest = AddressRequest.builder().
//                street("update-street").
//                suburb("update-suburb").
//                state("update-state").
//                postcode(7070).
//                build();
//
//        UpdatePatientRequest request = UpdatePatientRequest.builder().
//                firstName("update-first-name").
//                lastName("update-last-name").
//                address(addressRequest).
//                gender(PatientGender.FEMALE).
//                phoneNumber("+62812112903970").
//                dateOfBirth("2019-01-02").
//                build();
//
//        mockMvc.perform(
//                put("/api/patients/"+ latestPatientID).
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(request))
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<PatientResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals(response.getData().getFirstName(), request.getFirstName());
//            assertEquals(response.getData().getLastName(), request.getLastName());
//            assertEquals(response.getData().getGender(), request.getGender());
//            assertEquals(response.getData().getPhoneNumber(), "+62 812-1129-03970");
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String strDate = dateFormat.format(response.getData().getDateOfBirth());
//            assertEquals(strDate, request.getDateOfBirth());
//
//            assertEquals(response.getData().getAddress().getStreet(), request.getAddress().getStreet());
//            assertEquals(response.getData().getAddress().getSuburb(), request.getAddress().getSuburb());
//            assertEquals(response.getData().getAddress().getState(), request.getAddress().getState());
//            assertEquals(response.getData().getAddress().getPostcode(), request.getAddress().getPostcode());
//        });
//    }
//
//    @Test
//    void testUpdateBadRequest() throws Exception {
//        storePatient(1);
//
//        UUID latestPatientID = patientRepository.findTopByOrderByCreatedAtDesc().
//                map(Patient::getId).
//                orElseThrow();
//
//        AddressRequest addressRequest = AddressRequest.builder().
//                suburb("update-suburb").
//                state("update-state").
//                postcode(7070).
//                build();
//
//        UpdatePatientRequest request = UpdatePatientRequest.builder().
//                firstName("update-first-name").
//                lastName("update-last-name").
//                address(addressRequest).
//                gender(PatientGender.FEMALE).
//                phoneNumber("+62812112903970").
//                dateOfBirth("2019-01-02").
//                build();
//
//        mockMvc.perform(
//                put("/api/patients/"+ latestPatientID).
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON).
//                        content(objectMapper.writeValueAsString(request))
//        ).andExpectAll(
//                status().isBadRequest()
//        );
//    }
//
//    @Test
//    void testGetSuccess() throws Exception {
//        storePatient(1);
//
//        Patient patient = patientRepository.findTopByOrderByCreatedAtDesc().
//                orElseThrow();
//
//        mockMvc.perform(
//                get("/api/patients/"+ patient.getId()).
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<PatientResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals(response.getData().getId(), patient.getId());
//        });
//    }
//
//    @Test
//    void testDeleteSuccess() throws Exception {
//        storePatient(1);
//
//        Patient patient = patientRepository.findTopByOrderByCreatedAtDesc().
//                orElseThrow();
//
//        mockMvc.perform(
//                delete("/api/patients/"+ patient.getId()).
//                        accept(MediaType.APPLICATION_JSON).
//                        contentType(MediaType.APPLICATION_JSON)
//        ).andExpectAll(
//                status().isOk()
//        ).andDo(result -> {
//            RestResponse<PatientResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(),
//                    new TypeReference<>() {});
//
//            assertEquals(response.getData().getId(), patient.getId());
//        });
//    }
//
//    private static RegisterPatientRequest getPatientRequest() {
//        AddressRequest addressRequest = AddressRequest.builder().
//                street("street").
//                suburb("suburb").
//                state("state").
//                postcode(1010).
//                build();
//
//        return RegisterPatientRequest.builder().
//                firstName("test").
//                lastName("test").
//                address(addressRequest).
//                gender(PatientGender.MALE).
//                phoneNumber("+6281211290399").
//                dateOfBirth("2019-01-01").
//                build();
//    }
//
//    private static RegisterPatientRequest getInvalidPatientRequest() {
//        AddressRequest addressRequest = new AddressRequest();
//        addressRequest.setStreet("street");
//        addressRequest.setSuburb("suburb");
//        addressRequest.setState("state");
//        addressRequest.setPostcode(10100);
//
//        RegisterPatientRequest request = new RegisterPatientRequest();
//        request.setFirstName("test");
//        request.setLastName("test");
//        request.setAddress(addressRequest);
//        request.setGender(PatientGender.MALE);
//        request.setPhoneNumber("+6281211290399");
//        request.setDateOfBirth("2019-01-01");
//        return request;
//    }
//}

import az.erry.clinicrest.entities.Address;
import az.erry.clinicrest.entities.Patient;
import az.erry.clinicrest.models.RegisterPatientRequest;
import az.erry.clinicrest.models.UpdatePatientRequest;
import az.erry.clinicrest.services.PatientService;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;

    @Mock
    private Validator validator;

    @InjectMocks
    private PatientController patientController;

    private PhoneNumberUtil phoneNumberUtil;
    private SimpleDateFormat formatter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
        phoneNumberUtil = PhoneNumberUtil.getInstance();
        formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Test
    public void testSearch() throws Exception {
        Address mockAddr = new Address();
        Patient mockPatient = new Patient();
        mockPatient.setAddress(mockAddr);
        mockPatient.setPhoneNumber(phoneNumberUtil.getExampleNumber("ID"));
        mockPatient.setDateOfBirth(formatter.parse("2020-10-10"));

        List<Patient> patients = Arrays.asList(mockPatient, mockPatient);
        Page<Patient> patientPage = new PageImpl<>(patients);

        when(patientService.search(anyInt(), anyInt(), anyString())).thenReturn(patientPage);

        mockMvc.perform(get("/api/patients")
                        .param("query", "John")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService, times(1)).search(0, 10, "John");
    }

    @Test
    public void testGet() throws Exception {
        UUID patientId = UUID.randomUUID();
        Address mockAddr = new Address();
        Patient mockPatient = new Patient();
        mockPatient.setAddress(mockAddr);
        mockPatient.setPhoneNumber(phoneNumberUtil.getExampleNumber("ID"));
        mockPatient.setDateOfBirth(formatter.parse("2020-10-10"));

        when(patientService.get(eq(patientId))).thenReturn(mockPatient);

        mockMvc.perform(get("/api/patients/{id}", patientId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService, times(1)).get(patientId);
    }

    @Test
    public void testDelete() throws Exception {
        UUID patientId = UUID.randomUUID();
        Address mockAddr = new Address();
        Patient mockPatient = new Patient();
        mockPatient.setAddress(mockAddr);
        mockPatient.setPhoneNumber(phoneNumberUtil.getExampleNumber("ID"));
        mockPatient.setDateOfBirth(formatter.parse("2020-10-10"));

        when(patientService.delete(eq(patientId))).thenReturn(mockPatient);

        mockMvc.perform(delete("/api/patients/{id}", patientId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(patientService, times(1)).delete(patientId);
    }

    @Test
    public void testRegister() throws Exception {
        RegisterPatientRequest request = new RegisterPatientRequest();
        request.setPhoneNumber("+1234567890");
        request.setDateOfBirth("2000-01-01");

        when(validator.validate(any(RegisterPatientRequest.class))).thenReturn(Collections.emptySet());

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\": \"+1234567890\", \"dateOfBirth\": \"2000-01-01\"}"))
                .andExpect(status().isOk());

        verify(patientService, times(1)).register(any(RegisterPatientRequest.class));
    }

    @Test
    public void testUpdate() throws Exception {
        UUID patientId = UUID.randomUUID();
        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setPhoneNumber("+1234567890");
        request.setDateOfBirth("2000-01-01");

        Address mockAddr = new Address();
        Patient mockPatient = new Patient();
        mockPatient.setAddress(mockAddr);
        mockPatient.setPhoneNumber(phoneNumberUtil.getExampleNumber("ID"));
        mockPatient.setDateOfBirth(formatter.parse("2020-10-10"));

        when(validator.validate(any(UpdatePatientRequest.class))).thenReturn(Collections.emptySet());
        when(patientService.update(any(UpdatePatientRequest.class))).thenReturn(mockPatient);

        mockMvc.perform(put("/api/patients/{id}", patientId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\": \"+1234567890\", \"dateOfBirth\": \"2000-01-01\"}"))
                .andExpect(status().isOk());

        verify(patientService, times(1)).update(any(UpdatePatientRequest.class));
    }
}
