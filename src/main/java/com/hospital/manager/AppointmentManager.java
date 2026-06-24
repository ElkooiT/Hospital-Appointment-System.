package com.hospital.manager;

import com.hospital.exceptions.AppointmentConflictException;
import com.hospital.exceptions.InvalidTimeSlotException;
import com.hospital.exceptions.PatientNotFoundException;
import com.hospital.models.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * CHAPTER 6 (Collections) + CHAPTER 4 (Exception Handling)
 *
 * Central manager for all in-memory data.
 * Demonstrates:
 *   - ArrayList: dynamic list of appointments
 *   - HashMap: fast patient/doctor lookup by ID
 *   - Iteration over collections
 *   - Throwing and propagating custom exceptions
 */
public class AppointmentManager {

    // CHAPTER 6: HashMap for O(1) lookup by ID
    private Map<String, Patient>     patients     = new HashMap<>();
    private Map<String, Doctor>      doctors      = new HashMap<>();

    // CHAPTER 6: ArrayList for ordered list of all appointments
    private List<Appointment>        appointments = new ArrayList<>();

    private static final LocalTime OPENING_TIME = LocalTime.of(8, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(17, 0);

    // ─── Patient Operations ───────────────────────────────────────────────

    public void registerPatient(Patient patient) {
        patients.put(patient.getId(), patient);
        System.out.println("✅ Patient registered: " + patient.getName());
    }

    public Patient getPatient(String id) throws PatientNotFoundException {
        // CHAPTER 4: throw custom exception if not found
        Patient p = patients.get(id);
        if (p == null) throw new PatientNotFoundException(id);
        return p;
    }

    // CHAPTER 6: Returns a new list — callers can't mutate internal state
    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients.values());
    }

    // ─── Doctor Operations ────────────────────────────────────────────────

    public void registerDoctor(Doctor doctor) {
        doctors.put(doctor.getId(), doctor);
        System.out.println("✅ Doctor registered: Dr. " + doctor.getName());
    }

    public Doctor getDoctor(String id) {
        return doctors.get(id);
    }

    public List<Doctor> getAllDoctors() {
        return new ArrayList<>(doctors.values());
    }

    public List<Doctor> getDoctorsBySpecialization(String spec) {
        // CHAPTER 6: Iterating a collection with filtering
        List<Doctor> result = new ArrayList<>();
        for (Doctor d : doctors.values()) {
            if (d.getSpecialization().equalsIgnoreCase(spec)) {
                result.add(d);
            }
        }
        return result;
    }

    // ─── Appointment Operations ───────────────────────────────────────────

    /**
     * Books a scheduled appointment.
     * CHAPTER 4: Throws checked exceptions the caller must handle.
     */
    public ScheduledAppointment bookScheduledAppointment(
            String patientId, String doctorId,
            LocalDateTime dateTime, int durationMinutes,
            String reason)
            throws PatientNotFoundException, AppointmentConflictException,
                   InvalidTimeSlotException {

        // CHAPTER 4: Validate time slot — throws InvalidTimeSlotException
        validateTimeSlot(dateTime);

        // CHAPTER 4: Retrieve patient — throws PatientNotFoundException
        Patient patient = getPatient(patientId);
        Doctor doctor = doctors.get(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor ID not found: " + doctorId);
        }

        // CHAPTER 4: Check for conflicts — throws AppointmentConflictException
        checkForConflict(doctor, dateTime);

        ScheduledAppointment appt = new ScheduledAppointment(
                patient, doctor, dateTime, durationMinutes, reason);
        appointments.add(appt);

        // CHAPTER 3 (Polymorphism via Notifiable interface): both objects notified
        patient.sendReminder("Your appointment with Dr. " + doctor.getName() +
                             " is confirmed for " + appt.getFormattedDateTime());
        doctor.sendReminder("New appointment scheduled: Patient " + patient.getName() +
                            " at " + appt.getFormattedDateTime());

        doctor.schedule(appt.getFormattedDateTime());
        return appt;
    }

    /**
     * Registers a walk-in appointment.
     */
    public WalkInAppointment registerWalkIn(
            String patientId, String doctorId, String urgency)
            throws PatientNotFoundException {

        Patient patient = getPatient(patientId);
        Doctor doctor = doctors.get(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor ID not found: " + doctorId);
        }

        int queueNum = getNextQueueNumber();
        WalkInAppointment appt = new WalkInAppointment(
                patient, doctor, LocalDateTime.now(), queueNum, urgency);
        appointments.add(appt);

        patient.sendReminder("Walk-in registered. Queue number: " + queueNum);
        return appt;
    }

    public void cancelAppointment(String appointmentId) {
        // CHAPTER 6: Iterating ArrayList to find by ID
        for (Appointment appt : appointments) {
            if (appt.getAppointmentId().equals(appointmentId)) {
                appt.setStatus(Appointment.Status.CANCELLED);
                // CHAPTER 3 (Polymorphism): Notifiable.sendReminder called on both
                appt.getPatient().sendReminder("Your appointment " + appointmentId + " has been cancelled.");
                appt.getDoctor().cancel(appt.getFormattedDateTime());
                System.out.println("❌ Appointment " + appointmentId + " cancelled.");
                return;
            }
        }
        System.out.println("⚠️ Appointment not found: " + appointmentId);
    }

    public void completeAppointment(String appointmentId) {
        for (Appointment appt : appointments) {
            if (appt.getAppointmentId().equals(appointmentId)) {
                appt.setStatus(Appointment.Status.COMPLETED);
                appt.getDoctor().setAvailable(true);
                System.out.println("✅ Appointment " + appointmentId + " marked as completed.");
                return;
            }
        }
        System.out.println("⚠️ Appointment not found: " + appointmentId);
    }

    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatient().getId().equals(patientId)) {
                result.add(a);
            }
        }
        return result;
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getDoctor().getId().equals(doctorId)) {
                result.add(a);
            }
        }
        return result;
    }

    // ─── Private Helpers ──────────────────────────────────────────────────

    private void validateTimeSlot(LocalDateTime dateTime)
            throws InvalidTimeSlotException {
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new InvalidTimeSlotException(
                    dateTime.toString(), "Appointment time is in the past.");
        }
        LocalTime time = dateTime.toLocalTime();
        if (time.isBefore(OPENING_TIME) || time.isAfter(CLOSING_TIME)) {
            throw new InvalidTimeSlotException(
                    dateTime.toString(),
                    "Outside working hours (08:00 - 17:00).");
        }
    }

    private void checkForConflict(Doctor doctor, LocalDateTime requestedTime)
            throws AppointmentConflictException {
        for (Appointment appt : appointments) {
            if (appt.getDoctor().getId().equals(doctor.getId())
                    && appt.getStatus() != Appointment.Status.CANCELLED
                    && appt.getStatus() != Appointment.Status.COMPLETED) {
                // Conflict if within 30-minute window
                long diffMinutes = Math.abs(
                        java.time.Duration.between(appt.getDateTime(), requestedTime).toMinutes());
                if (diffMinutes < 30) {
                    throw new AppointmentConflictException(
                            doctor.getName(), appt.getFormattedDateTime());
                }
            }
        }
    }

    private int getNextQueueNumber() {
        int max = 0;
        for (Appointment a : appointments) {
            if (a instanceof WalkInAppointment) {
                int q = ((WalkInAppointment) a).getQueueNumber();
                if (q > max) max = q;
            }
        }
        return max + 1;
    }

    // Allow loading appointments from file (used by AppointmentFileHandler)
    public void loadAppointments(List<Appointment> loaded) {
        appointments.clear();
        appointments.addAll(loaded);
    }

    public void loadPatients(List<Patient> loaded) {
        patients.clear();
        for (Patient p : loaded) patients.put(p.getId(), p);
    }

    public void loadDoctors(List<Doctor> loaded) {
        doctors.clear();
        for (Doctor d : loaded) doctors.put(d.getId(), d);
    }
}
