package com.hospital.models;

import java.io.Serializable;


public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    
    private String id;
    private String name;
    private String phoneNumber;
    private String email;

    
    public Person(String id, String name, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    
    public abstract String getRole();

    
    public String displayInfo() {
        return String.format("[%s] ID: %s | Name: %s | Phone: %s | Email: %s",
                getRole(), id, name, phoneNumber, email);
    }

    
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phone) {
        this.phoneNumber = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return displayInfo();
    }
}
