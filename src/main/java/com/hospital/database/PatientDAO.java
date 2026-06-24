package com.hospital.database;

import com.hospital.models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PatientDAO {

    
    public void insertPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (patient_id, name, phone, email, age, blood_type, medical_history) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, patient.getId());
            pstmt.setString(2, patient.getName());
            pstmt.setString(3, patient.getPhoneNumber());
            pstmt.setString(4, patient.getEmail());
            pstmt.setInt(5, patient.getAge());
            pstmt.setString(6, patient.getBloodType());
            pstmt.setString(7, patient.getMedicalHistory());
            pstmt.executeUpdate();
            System.out.println("✅ Patient inserted into DB: " + patient.getName());
        }
    }

    
    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient p = new Patient(
                        rs.getString("patient_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getInt("age"),
                        rs.getString("blood_type")
                );
                p.setMedicalHistory(rs.getString("medical_history"));
                list.add(p);
            }
        }
        return list;
    }

    
    public Patient getPatientById(String id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Patient p = new Patient(
                            rs.getString("patient_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getInt("age"),
                            rs.getString("blood_type")
                    );
                    p.setMedicalHistory(rs.getString("medical_history"));
                    return p;
                }
            }
        }
        return null;
    }

    
    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET name=?, phone=?, email=?, age=?, " +
                     "blood_type=?, medical_history=? WHERE patient_id=?";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setString(2, patient.getPhoneNumber());
            pstmt.setString(3, patient.getEmail());
            pstmt.setInt(4, patient.getAge());
            pstmt.setString(5, patient.getBloodType());
            pstmt.setString(6, patient.getMedicalHistory());
            pstmt.setString(7, patient.getId());
            int rows = pstmt.executeUpdate();
            System.out.println("✅ Patient updated (" + rows + " row affected).");
        }
    }

    
    public void deletePatient(String id) throws SQLException {
        String sql = "DELETE FROM patients WHERE patient_id = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("🗑️ Patient " + id + " deleted from DB.");
        }
    }

    
    public void printDatabaseInfo() throws SQLException {
        DatabaseMetaData meta = DBConnection.getConnection().getMetaData();
        System.out.println("DB Product : " + meta.getDatabaseProductName());
        System.out.println("DB Version : " + meta.getDatabaseProductVersion());
        System.out.println("Driver     : " + meta.getDriverName());
    }
}
