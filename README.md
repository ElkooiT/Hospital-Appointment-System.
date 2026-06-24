# Hospital Appointment System
**Java OOP Course Project — All 7 Chapters Covered**

---

## Project Overview

A command-line Hospital Appointment System that integrates every chapter of the Java OOP course into a single, cohesive application. Patients and doctors are registered, appointments are booked with conflict detection, data persists to both binary files and a MySQL database, and text reports are generated on demand.

---

## Chapter Coverage Map

| Chapter | Topic | Files |
|---------|-------|-------|
| Ch1 — OOP / Classes & Objects | `Person`, `Patient`, `Doctor`, `Appointment` with private fields, constructors, getters/setters, static ID counter | `models/Person.java`, `models/Patient.java`, `models/Doctor.java`, `models/Appointment.java` |
| Ch2 — Inheritance | `Person → Patient / Doctor`; `Appointment → ScheduledAppointment / WalkInAppointment`; `super()` calls; abstract methods | `models/ScheduledAppointment.java`, `models/WalkInAppointment.java` |
| Ch3 — Polymorphism & Interfaces | `Notifiable` and `Schedulable` interfaces; polymorphic `displayInfo()` and `getSummary()`; multiple interface implementation on `Doctor` | `interfaces/Notifiable.java`, `interfaces/Schedulable.java` |
| Ch4 — Exception Handling | Three custom checked exceptions; try/catch with multiple catch blocks; exceptions thrown from `AppointmentManager` and caught in `Main` | `exceptions/AppointmentConflictException.java`, `exceptions/PatientNotFoundException.java`, `exceptions/InvalidTimeSlotException.java` |
| Ch5 — Files & Streams | Serialization/deserialization (`ObjectOutputStream` / `ObjectInputStream`); character stream reports (`BufferedWriter`, `PrintWriter`) | `fileio/AppointmentFileHandler.java`, `fileio/ReportGenerator.java` |
| Ch6 — Collections | `HashMap<String, Patient>` and `HashMap<String, Doctor>` for O(1) lookup; `ArrayList<Appointment>` for ordered storage; iteration and filtering | `manager/AppointmentManager.java` |
| Ch7 — JDBC / Database | MySQL connection via `DriverManager`; `PreparedStatement` for all CRUD; `ResultSet` iteration; `ResultSetMetaData`; `DatabaseMetaData`; scrollable `ResultSet` | `database/DBConnection.java`, `database/PatientDAO.java`, `database/DoctorDAO.java`, `database/AppointmentDAO.java` |

> **Note on the JDBC driver:** The course slides use `com.mysql.jdbc.Driver` (legacy). This project uses `com.mysql.cj.jdbc.Driver` — the modern, non-deprecated equivalent introduced in Connector/J 6.x. The behaviour is identical; the old class was removed in version 8.x.

---

## Project Structure

```
Hospital/
├── pom.xml                          Maven build + MySQL dependency
├── db/
│   └── schema.sql                   Run this once to set up MySQL
├── data/                            Auto-created at runtime
│   ├── appointments.dat             Serialized appointments (Ch5)
│   ├── patients.dat                 Serialized patients (Ch5)
│   ├── doctors.dat                  Serialized doctors (Ch5)
│   └── reports/                     Text reports (Ch5)
|hospital/
    ├── Main.java                    Entry point — wires everything
    ├── models/
    │   ├── Person.java              Abstract base (Ch1+Ch2)
    │   ├── Patient.java             Extends Person (Ch2)
    │   ├── Doctor.java              Extends Person, implements 2 interfaces (Ch2+Ch3)
    │   ├── Appointment.java         Abstract base (Ch1+Ch2)
    │   ├── ScheduledAppointment.java
    │   └── WalkInAppointment.java
    ├── interfaces/
    │   ├── Notifiable.java          (Ch3)
    │   └── Schedulable.java         (Ch3)
    ├── exceptions/
    │   ├── AppointmentConflictException.java  (Ch4)
    │   ├── PatientNotFoundException.java       (Ch4)
    │   └── InvalidTimeSlotException.java       (Ch4)
    ├── manager/
    │   └── AppointmentManager.java  ArrayList + HashMap (Ch6)
    ├── fileio/
    │   ├── AppointmentFileHandler.java  Serialization (Ch5)
    │   └── ReportGenerator.java         BufferedWriter/PrintWriter (Ch5)
    └── database/
        ├── DBConnection.java        JDBC connection singleton (Ch7)
        ├── PatientDAO.java          Patient CRUD (Ch7)
        ├── DoctorDAO.java           Doctor CRUD (Ch7)
        └── AppointmentDAO.java      Appointment CRUD + Scrollable ResultSet (Ch7)
```

---

## Setup & Running

### Prerequisites
- Java 11+
- Maven 3.6+
- MySQL 8.x (for Chapter 7 features)

### Step 1 — Clone and build

```bash
git clone <your-repo-url>
cd HospitalAppointmentSystem
mvn clean package
```

### Step 2 — Set up MySQL (Chapter 7)

```bash
mysql -u root -p < db/schema.sql
```

If your MySQL root password is not empty, edit line 14 of `DBConnection.java`:
```java
private static final String PASSWORD = "your_password_here";
```
Then rebuild: `mvn clean package`

### Step 3 — Run

```bash
java -jar target/hospital-appointment-system-1.0.0-jar-with-dependencies.jar
```

**No MySQL? No problem.** The system detects a missing DB connection on startup and continues in memory-only mode. All features except Menu 5 (Database) work without MySQL.

---

## Class Diagram (Text)

```
         ┌──────────────┐
         │   <<abstract>>│
         │    Person     │
         │ - id, name,   │
         │   phone, email│
         │ + getRole()   │  abstract
         │ + displayInfo()│
         └──────┬───────┘
                │ extends
       ┌────────┴────────┐
       ▼                 ▼
  ┌─────────┐       ┌─────────┐
  │ Patient │       │ Doctor  │
  │+Notifiable      │+Notifiable
  │         │       │+Schedulable
  └─────────┘       └─────────┘

         ┌──────────────┐
         │  <<abstract>> │
         │  Appointment  │
         │ - id, patient,│
         │   doctor, dt  │
         │ + getType()   │  abstract
         └──────┬───────┘
                │ extends
       ┌────────┴──────────┐
       ▼                   ▼
┌──────────────┐   ┌──────────────┐
│  Scheduled   │   │   WalkIn     │
│  Appointment │   │  Appointment │
│ + duration   │   │ + queueNum   │
│ + reason     │   │ + urgency    │
└──────────────┘   └──────────────┘

AppointmentManager
  - patients  : HashMap<String, Patient>
  - doctors   : HashMap<String, Doctor>
  - appointments : ArrayList<Appointment>
```

---

## Known Limitations

- The `data/` folder is created relative to where the JAR is run from. Run from the project root for consistency.
- The DB sync (Menu 5) does not handle duplicate key errors if you sync the same data twice. In a production system this would use `INSERT ... ON DUPLICATE KEY UPDATE`. For a course project, flush the tables between runs or use unique IDs.
- No authentication. This is intentional — out of scope for an OOP course project.
