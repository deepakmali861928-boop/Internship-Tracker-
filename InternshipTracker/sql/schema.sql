-- Internship Opportunity & Application Tracker
-- Database schema

CREATE DATABASE IF NOT EXISTS internship_tracker;
USE internship_tracker;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('STUDENT','ADMIN') NOT NULL
);

CREATE TABLE IF NOT EXISTS internships (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    company VARCHAR(150) NOT NULL,
    description TEXT,
    location VARCHAR(100),
    stipend DOUBLE DEFAULT 0,
    posted_by INT,
    posted_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (posted_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    internship_id INT NOT NULL,
    status ENUM('APPLIED','SHORTLISTED','REJECTED','SELECTED') DEFAULT 'APPLIED',
    applied_date DATE DEFAULT (CURRENT_DATE),
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (student_id, internship_id)
);

-- Optional: a default admin account (password: admin123)
INSERT INTO users (name, email, password, role)
VALUES ('Admin', 'admin@tracker.com', 'admin123', 'ADMIN')
ON DUPLICATE KEY UPDATE email = email;
