package com.hospital.models;

import com.hospital.interfaces.Notifiable;

/**
 * CHAPTER 2 (Inheritance) + CHAPTER 3 (Interfaces)
 * 
 * Patient extends Person (single inheritance).
 * Implements Notifiable — patients receive appointment reminders.
 * Demonstrates: super() constructor call, method overriding, interface
 * implementation.
 */
public class Patient extends Person implements Notifiable {

    private static final long serialVersionUID = 1L;

    private String bloodType;
    private String medicalHistory;
    private int age;

    // CHAPTER 2: super() calls the parent constructor
    public Patient(String id, String name, String phoneNumber, String email,
            int age, String bloodType) {
        super(id, name, phoneNumber, email);
        this.age = age;
        this.bloodType = bloodType;
        this.medicalHistory = "None";
    }

    // CHAPTER 2: Mandatory override of abstract method from Person
    @Override
    public String getRole() {
        return "PATIENT";
    }

    // CHAPTER 3 (Polymorphism): Override displayInfo() to add patient-specific
    // details
    @Override
    public String displayInfo() {
        return super.displayInfo() +
                String.format(" | Age: %d | Blood Type: %s", age, bloodType);
    }

    // CHAPTER 3 (Interfaces): Implementation of Notifiable
    @Override
    public void sendReminder(String message) {
        System.out.println("📱 SMS to " + getName() + " (" + getPhoneNumber() + "): " + message);
    }

    // Getters & Setters
    public int getAge() {
        return age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String history) {
        this.medicalHistory = history;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
