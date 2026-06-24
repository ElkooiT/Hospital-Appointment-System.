package com.hospital.models;

import java.io.Serializable;

/**
 * CHAPTER 1 (OOP - Classes & Objects) + CHAPTER 2 (Inheritance)
 * 
 * Abstract base class representing any person in the hospital system.
 * Demonstrates: abstract classes, encapsulation (private fields +
 * getters/setters),
 * constructors, and serves as the parent for Patient and Doctor (inheritance).
 */
public abstract class Person implements Serializable {

    private static final long serialVersionUID = 1L;

    // CHAPTER 1: Encapsulation — private fields, exposed via public methods
    private String id;
    private String name;
    private String phoneNumber;
    private String email;

    // CHAPTER 1: Constructor
    public Person(String id, String name, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // CHAPTER 2: Abstract method — subclasses MUST provide their own implementation
    public abstract String getRole();

    // CHAPTER 3 (Polymorphism): overridden differently in Patient vs Doctor
    public String displayInfo() {
        return String.format("[%s] ID: %s | Name: %s | Phone: %s | Email: %s",
                getRole(), id, name, phoneNumber, email);
    }

    // CHAPTER 1: Getters and Setters
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
