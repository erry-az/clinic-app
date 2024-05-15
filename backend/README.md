# Patient Table Specification for PostgreSQL Database

## Overview

This document outlines the specifications for the `patient` table to be used in a PostgreSQL database. The table is designed to store information about patients, ensuring each entry is unique, comprehensive, and complies with relevant data protection regulations.

## Table Definition

The `patient` table will have the following fields:

| Field            | Type       | Description                                            | Constraints                           |
|------------------|------------|--------------------------------------------------------|---------------------------------------|
| `id`             | UUID       | Unique identifier for each patient                     | Primary Key                           |
| `patient_identity`| VARCHAR   | Unique patient identity string in the format `CLN-YYYY-XXXXXX` | Unique, Not Null                     |
| `first_name`     | VARCHAR    | Patient's first name                                   | Not Null                              |
| `last_name`      | VARCHAR    | Patient's last name                                    | Not Null                              |
| `date_of_birth`  | DATE       | Patient's date of birth                                | Not Null                              |
| `gender`         | VARCHAR    | Patient's gender                                       | Not Null                              |
| `address`        | TEXT       | Patient's street address                               | Not Null                              |
| `address_suburb` | VARCHAR    | Suburb of the patient's address                        | Not Null                              |
| `address_state`  | VARCHAR    | State of the patient's address                         | Not Null                              |
| `address_postcode` | VARCHAR    | Postcode of the patient's address                      | Not Null                              |
| `phone_no`       | VARCHAR    | Patient's phone number                                 | Not Null                              |
| `soft_delete`    | BOOLEAN    | Indicates if the record is soft deleted                | Default FALSE                         |
| `created_at`     | TIMESTAMP  | Timestamp when the record was created                  | Not Null, Default to current timestamp|
| `updated_at`     | TIMESTAMP  | Timestamp when the record was last updated             | Not Null, Default to current timestamp|

## SQL Statement

```sql
CREATE TABLE patient (
    id UUID PRIMARY KEY,
    patient_identity VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(255) NOT NULL,
    address TEXT NOT NULL,
    address_suburb VARCHAR(255) NOT NULL,
    address_state VARCHAR(255) NOT NULL,
    address_postcode VARCHAR(255) NOT NULL,
    phone_no VARCHAR(255) NOT NULL,
    soft_delete BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

# Patient Data REST API Specification

## List of Patient
able to search by patient identity and name pagination

- endpoint : GET /api/patients
- query params : pid, name, page, and limit
- response :
```json
{
  "data": {
    "patients": [
      {
        "id": "",
        "patientIdentity": "",
        "firstName":  "",
        "lastName": "",
        "dateOfBirth":  "",
        "gender": "",
        "address":  "",
        "addressSuburb": "",
        "addressState":  "",
        "addressPostcode": "",
        "phoneNo":  "",
        "softDelete": "",
        "createdAt":  "",
        "updatedAt": ""
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 10,
      "maxPage": 10,
      "count": 100
    }
  }
}
```

## Create New Patient
- endpoint : POST /api/patients
- request
```json
{
  "firstName":  "",
  "lastName": "",
  "dateOfBirth":  "",
  "gender": "",
  "address":  "",
  "addressSuburb": "",
  "addressState":  "",
  "addressPostcode": "",
  "phoneNo":  "",
  "softDelete": "",
  "createdAt":  "",
  "updatedAt": ""
}
```
- response
```json
{
  "status": "ok"
}
```

## Update Patient
- endpoint : PUT /api/patients/{id}
- request
```json
{
  "firstName":  "",
  "lastName": "",
  "dateOfBirth":  "",
  "gender": "",
  "address":  "",
  "addressSuburb": "",
  "addressState":  "",
  "addressPostcode": "",
  "phoneNo":  "",
  "softDelete": "",
  "createdAt":  "",
  "updatedAt": ""
}
```
- response
```json
{
  "status": "ok"
}
```

## Delete Patient
- endpoint : DELETE /api/patients/{id}
- response
```json
{
  "status": "ok"
}
```