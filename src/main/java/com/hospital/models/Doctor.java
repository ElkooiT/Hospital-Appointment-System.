package com.hospital.models;

import com.hospital.interfaces.Notifiable;
import com.hospital.interfaces.Schedulable;


public class Doctor extends Person implements Notifiable, Schedulable {

    private static final long serialVersionUID = 1L;

    private String specialization;
    private String licenseNumber;
    private boolean available;

    public Doctor(String id, String name, String phoneNumber, String email,
            String specialization, String licenseNumber) {
        super(id, name, phoneNumber, email);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.available = true;
    }

    @Override
    public String getRole() {
        return "DOCTOR";
    }

    
    @Override
    public String displayInfo() {
        return super.displayInfo() +
                String.format(" | Specialization: %s | License: %s | Available: %s",
                        specialization, licenseNumber, available ? "Yes" : "No");
    }

    
    @Override
    public void sendReminder(String message) {
        System.out.println("📧 Email to Dr. " + getName() + " (" + getEmail() + "): " + message);
    }

    
    @Override
    public void schedule(String timeSlot) {
        System.out.println("📅 Dr. " + getName() + " scheduled for: " + timeSlot);
        this.available = false;
    }

    @Override
    public void cancel(String timeSlot) {
        System.out.println("❌ Dr. " + getName() + "'s slot cancelled: " + timeSlot);
        this.available = true;
    }

    @Override
    public void reschedule(String oldSlot, String newSlot) {
        System.out.println("🔄 Dr. " + getName() + " rescheduled from " + oldSlot + " to " + newSlot);
    }

    // Getters & Setters
    public String getSpecialization() {
        return specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setSpecialization(String spec) {
        this.specialization = spec;
    }
}
