# Internship Opportunity & Application Tracker

A Java Swing + MySQL (JDBC) desktop application for tracking internship postings and applications.

## Features
- **Two roles:** Student and Admin (recruiter), chosen at registration.
- **Admin:** post internships, view/delete internships they posted, view applications received, update each application's status (APPLIED / SHORTLISTED / SELECTED / REJECTED).
- **Student:** browse all internships, search/filter by keyword (title or company) and location, apply to internships, view own applications and their current status.
- Data is stored in MySQL — nothing is lost when the app closes.

## Project Structure
```
InternshipTracker/
├── sql/
│   └── schema.sql              -- run this once to create the database and tables
├── src/com/itracker/
│   ├── Main.java                -- entry point
│   ├── db/DBConnection.java     -- JDBC connection helper
│   ├── model/                   -- User, Internship, Application (POJOs)
│   ├── dao/                     -- UserDAO, InternshipDAO, ApplicationDAO (all SQL lives here)
│   └── ui/                      -- LoginFrame, RegisterFrame, AdminDashboard, StudentDashboard
└── README.md
```

## Prerequisites
1. **JDK 8+** installed (`javac -version` to check).
2. **MySQL Server** installed and running.
3. **MySQL Connector/J** (JDBC driver) — download the `.jar` from
   https://dev.mysql.com/downloads/connector/j/ (choose "Platform Independent").

## Setup

### 1. Create the database
Open a MySQL client (Workbench, or the `mysql` CLI) and run:
```sql
source sql/schema.sql;
```
This creates the `internship_tracker` database with `users`, `internships`, and `applications` tables,
plus a default admin login: `admin@tracker.com` / `admin123`.

### 2. Configure the connection
Open `src/com/itracker/db/DBConnection.java` and update if needed:
```java
private static final String URL = "jdbc:mysql://localhost:3306/internship_tracker?useSSL=false&serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "root"; // <-- change to your MySQL password
```

### 3. Compile
From the project root, with the MySQL connector jar downloaded alongside the project (adjust the path/filename to match what you downloaded):
```bash
javac -d out $(find src -name "*.java")
```

### 4. Run
Include the MySQL connector jar on the classpath when running:
```bash
java -cp "out:mysql-connector-j-8.x.x.jar" com.itracker.Main
```
On Windows, use a semicolon instead of a colon in the classpath:
```bash
java -cp "out;mysql-connector-j-8.x.x.jar" com.itracker.Main
```

### Using an IDE instead (Eclipse / IntelliJ / NetBeans)
1. Import the `src` folder as a Java project.
2. Add the MySQL connector `.jar` to the project's build path / libraries.
3. Run `com.itracker.Main`.

## Using the App
1. Launch the app — you'll see the Login screen.
2. Click **Create Account** to register as a Student or Admin, or log in with the seeded admin
   account (`admin@tracker.com` / `admin123`).
3. **As Admin:** go to "Post Internship" to add a listing, check "My Internships" to manage what
   you've posted, and use "Applications Received" to shortlist/select/reject candidates.
4. **As Student:** use "Browse Internships" to search and apply, and "My Applications" to track
   status changes.

## Notes for extending the project
- Passwords are stored in plain text for simplicity — for a stronger project, hash them (e.g. with
  `MessageDigest` / `BCrypt`) before saving.
- You could add: email notifications on status change, resume upload (store file path), pagination
  for large lists, or an "edit internship" feature.
