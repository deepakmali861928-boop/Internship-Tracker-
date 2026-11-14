package com.itracker.dao;

import com.itracker.db.DBConnection;
import com.itracker.model.Internship;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipDAO {

    public boolean addInternship(Internship internship) throws SQLException {
        String sql = "INSERT INTO internships (title, company, description, location, stipend, posted_by) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, internship.getTitle());
            ps.setString(2, internship.getCompany());
            ps.setString(3, internship.getDescription());
            ps.setString(4, internship.getLocation());
            ps.setDouble(5, internship.getStipend());
            ps.setInt(6, internship.getPostedBy());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteInternship(int id) throws SQLException {
        String sql = "DELETE FROM internships WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public List<Internship> getAllInternships() throws SQLException {
        String sql = "SELECT * FROM internships ORDER BY posted_date DESC";
        List<Internship> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Internship> getInternshipsByAdmin(int adminId) throws SQLException {
        String sql = "SELECT * FROM internships WHERE posted_by = ? ORDER BY posted_date DESC";
        List<Internship> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    /** Search/filter by keyword (title or company) and/or location. Pass "" to ignore a filter. */
    public List<Internship> searchInternships(String keyword, String location) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM internships WHERE 1=1");
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (title LIKE ? OR company LIKE ?)");
        }
        if (location != null && !location.isBlank()) {
            sql.append(" AND location LIKE ?");
        }
        sql.append(" ORDER BY posted_date DESC");

        List<Internship> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            if (keyword != null && !keyword.isBlank()) {
                ps.setString(idx++, "%" + keyword + "%");
                ps.setString(idx++, "%" + keyword + "%");
            }
            if (location != null && !location.isBlank()) {
                ps.setString(idx++, "%" + location + "%");
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Internship getInternshipById(int id) throws SQLException {
        String sql = "SELECT * FROM internships WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    private Internship mapRow(ResultSet rs) throws SQLException {
        return new Internship(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("company"),
                rs.getString("description"),
                rs.getString("location"),
                rs.getDouble("stipend"),
                rs.getInt("posted_by"),
                rs.getDate("posted_date")
        );
    }
}
