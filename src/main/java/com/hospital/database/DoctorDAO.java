package com.hospital.database;

import com.hospital.models.Doctor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DoctorDAO {

    public void insertDoctor(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO doctors (doctor_id, name, phone, email, specialization, license_number, available) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, doctor.getId());
            pstmt.setString(2, doctor.getName());
            pstmt.setString(3, doctor.getPhoneNumber());
            pstmt.setString(4, doctor.getEmail());
            pstmt.setString(5, doctor.getSpecialization());
            pstmt.setString(6, doctor.getLicenseNumber());
            pstmt.setBoolean(7, doctor.isAvailable());
            pstmt.executeUpdate();
            System.out.println("✅ Doctor inserted into DB: " + doctor.getName());
        }
    }

    public List<Doctor> getAllDoctors() throws SQLException {
        List<Doctor> list = new ArrayList<>();
        String sql = "SELECT * FROM doctors";

        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            
            ResultSetMetaData rsMeta = rs.getMetaData();
            System.out.println("Columns in doctors table: " + rsMeta.getColumnCount());

            while (rs.next()) {
                Doctor d = new Doctor(
                        rs.getString("doctor_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("specialization"),
                        rs.getString("license_number")
                );
                d.setAvailable(rs.getBoolean("available"));
                list.add(d);
            }
        }
        return list;
    }

    public Doctor getDoctorById(String id) throws SQLException {
        String sql = "SELECT * FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Doctor d = new Doctor(
                            rs.getString("doctor_id"),
                            rs.getString("name"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("specialization"),
                            rs.getString("license_number")
                    );
                    d.setAvailable(rs.getBoolean("available"));
                    return d;
                }
            }
        }
        return null;
    }

    public void updateAvailability(String doctorId, boolean available) throws SQLException {
        String sql = "UPDATE doctors SET available = ? WHERE doctor_id = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setBoolean(1, available);
            pstmt.setString(2, doctorId);
            pstmt.executeUpdate();
        }
    }

    public void deleteDoctor(String id) throws SQLException {
        String sql = "DELETE FROM doctors WHERE doctor_id = ?";
        try (PreparedStatement pstmt = DBConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
            System.out.println("🗑️ Doctor " + id + " deleted from DB.");
        }
    }
}
