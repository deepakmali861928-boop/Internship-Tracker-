package com.itracker.model;

import java.sql.Date;

public class Application {
    private int id;
    private int studentId;
    private int internshipId;
    private String status; // APPLIED, SHORTLISTED, REJECTED, SELECTED
    private Date appliedDate;

    // Extra display-only fields populated by joined DAO queries
    private String studentName;
    private String internshipTitle;
    private String company;

    public Application() {
    }

    public Application(int id, int studentId, int internshipId, String status, Date appliedDate) {
        this.id = id;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getInternshipId() { return internshipId; }
    public void setInternshipId(int internshipId) { this.internshipId = internshipId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getAppliedDate() { return appliedDate; }
    public void setAppliedDate(Date appliedDate) { this.appliedDate = appliedDate; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getInternshipTitle() { return internshipTitle; }
    public void setInternshipTitle(String internshipTitle) { this.internshipTitle = internshipTitle; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
}
