package com.hospital.models;

import com.hospital.interfaces.Notifiable;


public class Patient extends Person implements Notifiable {

    private static final long serialVersionUID = 1L;

    private String bloodType;
    private String medicalHistory;
    private int age;

    
    public Patient(String id, String name, String phoneNumber, String email,
            int age, String bloodType) {
        super(id, name, phoneNumber, email);
        this.age = age;
        this.bloodType = bloodType;
        this.medicalHistory = "None";
    }

    
    @Override
    public String getRole() {
        return "PATIENT";
    }

    
    @Override
    public String displayInfo() {
        return super.displayInfo() +
                String.format(" | Age: %d | Blood Type: %s", age, bloodType);
    }

    
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
