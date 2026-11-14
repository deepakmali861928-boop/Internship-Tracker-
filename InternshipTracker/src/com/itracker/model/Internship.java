package com.itracker.model;

import java.sql.Date;

public class Internship {
    private int id;
    private String title;
    private String company;
    private String description;
    private String location;
    private double stipend;
    private int postedBy;
    private Date postedDate;

    public Internship() {
    }

    public Internship(int id, String title, String company, String description,
                       String location, double stipend, int postedBy, Date postedDate) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.description = description;
        this.location = location;
        this.stipend = stipend;
        this.postedBy = postedBy;
        this.postedDate = postedDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getStipend() { return stipend; }
    public void setStipend(double stipend) { this.stipend = stipend; }

    public int getPostedBy() { return postedBy; }
    public void setPostedBy(int postedBy) { this.postedBy = postedBy; }

    public Date getPostedDate() { return postedDate; }
    public void setPostedDate(Date postedDate) { this.postedDate = postedDate; }

    @Override
    public String toString() {
        return title + " @ " + company;
    }
}
