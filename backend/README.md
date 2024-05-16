# Patient Specification

## Overview

This document outlines the specifications for the `patient` table to be used in a PostgreSQL database. The table is designed to store information about patients, ensuring each entry is unique, comprehensive, and complies with relevant data protection regulations.

## Table Definition

- Gender is enum (MALE, FEMALE)

```sql
CREATE TABLE patients (
    id UUID PRIMARY KEY,
    patient_identity VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(255) NOT NULL,
    phone_no VARCHAR(255) NOT NULL,
    soft_delete BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    patient_id UUID,
    street TEXT NOT NULL,
    suburb VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    postcode SMALLINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

# Patient Data REST API Specification

## List of Patient
able to search by patient identity and name pagination

- endpoint : GET /api/patients
- query params : query, page, and size
- response :
```json
{
  "data": [
    {
      "id": "",
      "patientIdentity": "",
      "firstName":  "",
      "lastName": "",
      "dateOfBirth":  "",
      "gender": "",
      "phoneNo":  "",
      "address":  {
        "street": "",
        "suburb": "",
        "state":  "",
        "postcode": "0000"
      },
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
  "phoneNo":  "",
  "address": {
    "street":  "",
    "suburb": "",
    "state":  "",
    "postcode": ""
  }
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
  "phoneNo":  "",
  "address": {
    "street":  "",
    "suburb": "",
    "state":  "",
    "postcode": ""
  }
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