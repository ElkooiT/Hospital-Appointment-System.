package com.hospital.interfaces;

/**
 * CHAPTER 3 (Polymorphism & Interfaces)
 *
 * Any entity that can receive notifications implements this.
 * Both Patient and Doctor implement Notifiable, but each sends
 * reminders differently — demonstrating interface-based polymorphism.
 *
 * This is the "multiple inheritance solution" discussed in Ch3:
 * Java doesn't allow extending multiple classes, but a class
 * can implement multiple interfaces.
 */
public interface Notifiable {
    void sendReminder(String message);
}
