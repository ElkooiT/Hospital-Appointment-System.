package com.hospital.interfaces;


public interface Schedulable {
    void schedule(String timeSlot);
    void cancel(String timeSlot);
    void reschedule(String oldSlot, String newSlot);
}
