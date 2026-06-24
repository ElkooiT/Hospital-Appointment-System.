package com.hospital.exceptions;

/**
 * CHAPTER 4 (Exception Handling)
 *
 * Thrown when a requested appointment time is outside working hours
 * or is set in the past.
 * Demonstrates: passing contextual data through custom exceptions.
 */
public class InvalidTimeSlotException extends Exception {

    private String requestedSlot;
    private String reason;

    public InvalidTimeSlotException(String requestedSlot, String reason) {
        super(String.format("Invalid time slot '%s': %s", requestedSlot, reason));
        this.requestedSlot = requestedSlot;
        this.reason = reason;
    }

    public String getRequestedSlot() { return requestedSlot; }
    public String getReason()        { return reason; }
}
