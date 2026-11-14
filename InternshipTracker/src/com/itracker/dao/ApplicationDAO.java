package com.itracker.dao;

import com.itracker.db.DBConnection;
import com.itracker.model.Application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {

    /** Returns false if the student already applied to this internship (duplicate). */
    public boolean apply(int studentId, int internshipId) throws SQLException {
        String checkSql = "SELECT id FROM applications WHERE student_id = ? AND internship_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement check = con.prepareStatement(checkSql)) {
            check.setInt(1, studentId);
            check.setInt(2, internshipId);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    return false; // already applied
                }
            }
        }

        String sql = "INSERT INTO applications (student_id, internship_id, status) VALUES (?, ?, 'APPLIED')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, internshipId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateStatus(int applicationId, String status) throws SQLException {
        String sql = "UPDATE applications SET status = ? WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, applicationId);
            return ps.executeUpdate() > 0;
        }
    }

    /** All applications submitted by one student, joined with internship info. */
    public List<Application> getApplicationsByStudent(int studentId) throws SQLException {
        String sql = "SELECT a.*, i.title AS internship_title, i.company AS company " +
                "FROM applications a JOIN internships i ON a.internship_id = i.id " +
                "WHERE a.student_id = ? ORDER BY a.applied_date DESC";
        List<Application> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithInternship(rs));
                }
            }
        }
        return list;
    }

    /** All applications received for internships posted by one admin, joined with student + internship info. */
    public List<Application> getApplicationsForAdmin(int adminId) throws SQLException {
        String sql = "SELECT a.*, i.title AS internship_title, i.company AS company, u.name AS student_name " +
                "FROM applications a " +
                "JOIN internships i ON a.internship_id = i.id " +
                "JOIN users u ON a.student_id = u.id " +
                "WHERE i.posted_by = ? ORDER BY a.applied_date DESC";
        List<Application> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Application app = mapRowWithInternship(rs);
                    app.setStudentName(rs.getString("student_name"));
                    list.add(app);
                }
            }
        }
        return list;
    }

    private Application mapRowWithInternship(ResultSet rs) throws SQLException {
        Application app = new Application(
                rs.getInt("id"),
                rs.getInt("student_id"),
                rs.getInt("internship_id"),
                rs.getString("status"),
                rs.getDate("applied_date")
        );
        app.setInternshipTitle(rs.getString("internship_title"));
        app.setCompany(rs.getString("company"));
        return app;
    }
}
