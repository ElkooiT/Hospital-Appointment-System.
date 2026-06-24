package com.hospital.interfaces;

/**
 * CHAPTER 3 (Polymorphism & Interfaces)
 *
 * Defines the contract for anything that can be scheduled,
 * cancelled, and rescheduled. Doctor implements this.
 *
 * Demonstrates: interface as a contract, separation of behaviour
 * from class hierarchy, and the "program to interfaces" principle.
 */
public interface Schedulable {
    void schedule(String timeSlot);
    void cancel(String timeSlot);
    void reschedule(String oldSlot, String newSlot);
}
