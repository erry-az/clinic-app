package az.erry.clinicrest.services;

import az.erry.clinicrest.entities.Address;
import az.erry.clinicrest.entities.Patient;
import az.erry.clinicrest.entities.PatientIdGenerator;
import az.erry.clinicrest.models.RegisterPatientRequest;
import az.erry.clinicrest.models.UpdatePatientRequest;
import az.erry.clinicrest.repositories.AddressRepository;
import az.erry.clinicrest.repositories.PatientRepository;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final AddressRepository addressRepository;

    public PatientService(PatientRepository patientRepository, AddressRepository addressRepository) {
        this.patientRepository = patientRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void register(RegisterPatientRequest request) {
        PatientIdGenerator idGenerator = patientRepository.findTopByOrderByCreatedAtDesc().
                map(patient -> new PatientIdGenerator(patient.getPatientIdentity())).
                orElseGet(PatientIdGenerator::new);

        Patient patient = new Patient();
        patient.setPatientIdentity(idGenerator.generateId());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getParsedDoB());
        patient.setPhoneNumber(request.getParsedPhoneNumber());
        patient.setGender(request.getGender());
        patientRepository.save(patient);

        Address address = new Address();
        address.setStreet(request.getAddress().getStreet());
        address.setSuburb(request.getAddress().getSuburb());
        address.setState(request.getAddress().getState());
        address.setPostcode(request.getAddress().getPostcode());
        address.setPatient(patient);
        addressRepository.save(address);
    }

    @Transactional(readOnly = true)
    public Page<Patient> search(Integer page, Integer size, String q)  {
        Specification<Patient> specification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            List<Order> orders = new ArrayList<>();

            orders.add(builder.desc(root.get("updatedAt")));

            predicates.add(builder.equal(root.get("softDelete"), false));

            if(q != null && !q.isEmpty()) {
                predicates.add(builder.or(
                        builder.like(root.get("firstName"), "%"+q+"%"),
                        builder.like(root.get("lastName"), "%"+q+"%"),
                        builder.like(root.get("patientIdentity"), "%"+q+"%")
                ));
            }

            return query.where(predicates.toArray(new Predicate[]{})).orderBy(orders).getRestriction();
        };

        Pageable pageable = PageRequest.of(page, size);

        return patientRepository.findAll(specification, pageable);
    }

    @Transactional
    public Patient update(UpdatePatientRequest request) {
        Patient patient = patientRepository.findTopByIdAndSoftDeleteFalse(request.getId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDateOfBirth(request.getParsedDoB());
        patient.setPhoneNumber(request.getParsedPhoneNumber());
        patient.setGender(request.getGender());
        patientRepository.save(patient);

        Address address = patient.getAddress();
        address.setStreet(request.getAddress().getStreet());
        address.setSuburb(request.getAddress().getSuburb());
        address.setState(request.getAddress().getState());
        address.setPostcode(request.getAddress().getPostcode());
        address.setPatient(patient);
        addressRepository.save(address);

        patient.setAddress(address);

        return patient;
    }

    @Transactional(readOnly = true)
    public Patient get(UUID id) {
        return patientRepository.findTopByIdAndSoftDeleteFalse(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
    }

    @Transactional
    public Patient delete(UUID id) {
        Patient patient = patientRepository.findTopByIdAndSoftDeleteFalse(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

        patient.setSoftDelete(true);

        patientRepository.save(patient);

        return patient;
    }
}
