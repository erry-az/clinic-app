package az.erry.clinicrest.controllers;

import az.erry.clinicrest.entities.Patient;
import az.erry.clinicrest.models.*;
import az.erry.clinicrest.services.PatientService;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@CrossOrigin
public class PatientController {

    private final PatientService patientService;

    private final Validator validator;

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private PatientResponse toPatientResponse(Patient patient) {
        AddressResponse addressResponse = AddressResponse.builder().
                id(patient.getAddress().getId()).
                street(patient.getAddress().getStreet()).
                state(patient.getAddress().getState()).
                suburb(patient.getAddress().getSuburb()).
                postcode(patient.getAddress().getPostcode()).
                createdAt(patient.getAddress().getCreatedAt()).
                updatedAt(patient.getAddress().getUpdatedAt()).
                build();

        return PatientResponse.builder().
                id(patient.getId()).
                patientIdentity(patient.getPatientIdentity()).
                firstName(patient.getFirstName()).
                lastName(patient.getLastName()).
                dateOfBirth(formatter.format(patient.getDateOfBirth())).
                gender(patient.getGender()).
                phoneNumber(phoneNumberUtil.format(patient.getPhoneNumber(), PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)).
                createdAt(patient.getCreatedAt()).
                updatedAt(patient.getUpdatedAt()).
                address(addressResponse).
                build();
    }

    public PatientController(PatientService patientService, Validator validator) {
        this.patientService = patientService;
        this.validator = validator;
    }

    @GetMapping(
            path = "/api/patients",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse<List<PatientResponse>> search(@RequestParam(name = "query", required = false) String query,
                                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        Page<Patient> patientPage = patientService.search(page, size, query);

        List<PatientResponse> patientResponses = patientPage.getContent().stream().
                map(this::toPatientResponse).
                toList();

        PagingResponse paging = PagingResponse.builder().
                page(patientPage.getNumber()).
                totalPage(patientPage.getTotalPages()).
                size(patientPage.getSize()).
                build();

        return RestResponse.<List<PatientResponse>>builder().
                data(patientResponses).
                paging(paging).
                build();
    }

    @GetMapping(
            path = "/api/patients/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse<PatientResponse> get(@PathVariable String id) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id format");
        }

        Patient patient = patientService.get(uuid);

        return RestResponse.<PatientResponse>builder().
                data(toPatientResponse(patient)).
                build();
    }

    @DeleteMapping(
            path = "/api/patients/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse<PatientResponse> delete(@PathVariable String id) {
        UUID uuid;

        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id format");
        }

        Patient patient = patientService.delete(uuid);

        return RestResponse.<PatientResponse>builder().
                data(toPatientResponse(patient)).
                build();
    }

    @PostMapping(
            path = "/api/patients",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse<String> register(@RequestBody RegisterPatientRequest request) {
        Set<ConstraintViolation<RegisterPatientRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        try {
            request.setParsedPhoneNumber(phoneNumberUtil.parse(request.getPhoneNumber(), ""));
            request.setParsedDoB(formatter.parse(request.getDateOfBirth()));
        } catch (NumberParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid phone number");
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid dob format");
        }

        patientService.register(request);

        return RestResponse.<String>builder().data("OK").build();
    }

    @PutMapping(
            path = "/api/patients/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse<PatientResponse> update(@RequestBody UpdatePatientRequest request, @PathVariable String id) {
        Set<ConstraintViolation<UpdatePatientRequest>> constraintViolations = validator.validate(request);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }

        try {
            request.setId(UUID.fromString(id));
            request.setParsedPhoneNumber(phoneNumberUtil.parse(request.getPhoneNumber(), ""));
            request.setParsedDoB(formatter.parse(request.getDateOfBirth()));
        } catch (NumberParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid phone number");
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid dob format");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid id format");
        }

        Patient patient = patientService.update(request);

        return RestResponse.<PatientResponse>builder().data(toPatientResponse(patient)).build();
    }
}
