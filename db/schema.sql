-- ============================================================
-- Hospital Appointment System — Database Schema
-- Chapter 7: JDBC / Database Programming
-- Run this script once before starting the application.
-- ============================================================

-- Create the database
CREATE DATABASE IF NOT EXISTS hospital_db;
USE hospital_db;

-- ─── Patients ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS patients (
    patient_id       VARCHAR(20)  PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    phone            VARCHAR(20),
    email            VARCHAR(100),
    age              INT,
    blood_type       VARCHAR(5),
    medical_history  TEXT
);

-- ─── Doctors ─────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id        VARCHAR(20)  PRIMARY KEY,
    name             VARCHAR(100) NOT NULL,
    phone            VARCHAR(20),
    email            VARCHAR(100),
    specialization   VARCHAR(100),
    license_number   VARCHAR(50),
    available        BOOLEAN DEFAULT TRUE
);

-- ─── Appointments ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id   VARCHAR(20)  PRIMARY KEY,
    patient_id       VARCHAR(20)  NOT NULL,
    doctor_id        VARCHAR(20)  NOT NULL,
    date_time        DATETIME     NOT NULL,
    status           VARCHAR(20)  DEFAULT 'PENDING',
    notes            TEXT,
    type             VARCHAR(20),
    extra_info       TEXT,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id)  REFERENCES doctors(doctor_id)
);

-- ─── Verify ───────────────────────────────────────────────────
SHOW TABLES;
