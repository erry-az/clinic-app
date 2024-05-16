import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PatientsService } from './patients.service';

@Component({
  selector: 'app-patients',
  templateUrl: './patients.component.html',
  styleUrls: ['./patients.component.css']
})
export class PatientsComponent implements OnInit {
  patients: any[] = [];
  searchQuery: string = '';
  isModalOpen: boolean = false;
  editingPatient: any = null;
  patientForm: FormGroup;
  paging: { page: number; size: number; totalPage: number } = {page: 0, size: 10, totalPage: 0};
  pages: any[] = [];

  constructor(private patientsService: PatientsService, private fb: FormBuilder) {
    this.patientForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      gender: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      street: ['', Validators.required],
      suburb: ['', Validators.required],
      state: ['', Validators.required],
      postcode: ['', Validators.required],      
    });
  }

  ngOnInit(): void {
    this.searchPatients(0);
  }

  prev(): void {
    if(this.paging.page > 0) {
      this.searchPatients(this.paging.page - 1);
    }
  }

  next(): void {
    if(this.paging.page < this.paging.totalPage - 1) {
      this.searchPatients(this.paging.page + 1);
    }
  }

  searchPatients(page: number): void {
    this.patientsService.searchPatients(this.searchQuery, page, this.paging.size).subscribe(response => {
      this.patients = response.data;
      this.paging = {
        page: response.paging.page,
        size: response.paging.size,
        totalPage: response.paging.totalPage,
      };

      this.pages = [];
      for (let i = 0; i < this.paging.totalPage; i++) {
        this.pages.push(i == response.paging.page ? 'active' : '');  
      }
    });
  }

  openAddModal(): void {
    this.editingPatient = null;
    this.patientForm.reset();
    this.isModalOpen = true;
  }

  openEditModal(patient: any): void {
    const newPatient = {
      firstName: patient.firstName,
      lastName: patient.lastName,
      dateOfBirth: patient.dateOfBirth,
      gender: patient.gender,
      phoneNumber: patient.phoneNumber,
      street: patient.address.street,
      suburb: patient.address.suburb,
      state: patient.address.state,
      postcode: patient.address.postcode,
    };

    this.editingPatient = patient;
    this.patientForm.patchValue(newPatient);
    this.isModalOpen = true;
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  savePatient(): void {
    console.log(this.patientForm.invalid);
    console.log(this.patientForm.value);
    if (this.patientForm.invalid) {
      return;
    }

    const patient = this.patientForm.value;
    const patientData = {
      firstName: patient.firstName,
      lastName: patient.lastName,
      dateOfBirth: patient.dateOfBirth,
      gender: patient.gender,
      phoneNumber: patient.phoneNumber,
      address: {
        street: patient.street,
        suburb: patient.suburb,
        state: patient.state,
        postcode: patient.postcode,
      }
    };


    if (this.editingPatient) {
      this.patientsService.updatePatient(this.editingPatient.id, patientData).subscribe({
        next: () => {
          this.searchPatients(0);
          this.closeModal();
        },
        error: (e) => {
          console.table(e.error.errors);
        }
      });
    } else {
      this.patientsService.createPatient(patientData).subscribe({
        next: () => {
          this.searchPatients(0);
          this.closeModal();
        },
        error: (e) => {
          console.table(e.error.errors);
        }
      });
    }
  }

  deletePatient(id: string): void {
    this.patientsService.deletePatient(id).subscribe(() => {
      this.searchPatients(0);
    });
  }
}
