package az.erry.clinicrest;

import az.erry.clinicrest.entities.Address;
import az.erry.clinicrest.entities.Patient;
import az.erry.clinicrest.entities.PatientGender;
import az.erry.clinicrest.repositories.AddressRepository;
import az.erry.clinicrest.repositories.PatientRepository;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;


@SpringBootTest
class ClinicRestApplicationTests {
//    @Autowired
//    private PatientRepository PatientRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
//
//    @Test
//    public void seedPatient() {
//        for (int i = 0; i < 100; i++) {
//            Patient patient = new Patient();
//            patient.setFirstName("First Test" + i);
//            patient.setLastName("Last Test" + i);
//            patient.setDateOfBirth(new Date());
//            patient.setGender(i % 2 == 0 ? PatientGender.MALE : PatientGender.FEMALE);
//            patient.setPhoneNumber(phoneNumberUtil.getExampleNumber("ID"));
//
//            PatientRepository.save(patient);
//
//
//            Address address = new Address();
//            address.setPatient(patient);
//            address.setStreet("Street Test" + i);
//            address.setSuburb("Suburb Test" + i);
//            address.setState("State Test" + i);
//            address.setPostcode(1000 + i);
//
//            addressRepository.save(address);
//        }
//    }
}
