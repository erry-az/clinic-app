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