package com.hospital.models;

import java.time.LocalDateTime;


public class ScheduledAppointment extends Appointment {

    private static final long serialVersionUID = 1L;

    private int durationMinutes;
    private String reasonForVisit;

    public ScheduledAppointment(Patient patient, Doctor doctor,
                                LocalDateTime dateTime, int durationMinutes,
                                String reasonForVisit) {
        super(patient, doctor, dateTime);
        this.durationMinutes = durationMinutes;
        this.reasonForVisit = reasonForVisit;
    }

    @Override
    public String getType() {
        return "Scheduled";
    }

    // Extends parent getSummary() with scheduled-specific info
    @Override
    public String getSummary() {
        return super.getSummary() +
               String.format("\nDuration       : %d minutes\nReason         : %s",
                       durationMinutes, reasonForVisit);
    }

    public int getDurationMinutes()           { return durationMinutes; }
    public String getReasonForVisit()         { return reasonForVisit; }
    public void setDurationMinutes(int mins)  { this.durationMinutes = mins; }
}
