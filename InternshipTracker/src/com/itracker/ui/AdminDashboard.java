package com.itracker.ui;

import com.itracker.dao.ApplicationDAO;
import com.itracker.dao.InternshipDAO;
import com.itracker.model.Application;
import com.itracker.model.Internship;
import com.itracker.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final User admin;
    private final InternshipDAO internshipDAO = new InternshipDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    // Post Internship tab fields
    private final JTextField titleField = new JTextField(20);
    private final JTextField companyField = new JTextField(20);
    private final JTextField locationField = new JTextField(20);
    private final JTextField stipendField = new JTextField(20);
    private final JTextArea descriptionArea = new JTextArea(4, 20);

    // My Internships tab
    private final DefaultTableModel internshipTableModel =
            new DefaultTableModel(new Object[]{"ID", "Title", "Company", "Location", "Stipend", "Posted"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
    private final JTable internshipTable = new JTable(internshipTableModel);

    // Applications tab
    private final DefaultTableModel applicationTableModel =
            new DefaultTableModel(new Object[]{"App ID", "Student", "Internship", "Company", "Status", "Applied"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
    private final JTable applicationTable = new JTable(applicationTableModel);
    private final JComboBox<String> statusBox =
            new JComboBox<>(new String[]{"APPLIED", "SHORTLISTED", "SELECTED", "REJECTED"});

    public AdminDashboard(User admin) {
        this.admin = admin;
        setTitle("Admin Dashboard - " + admin.getName());
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Post Internship", buildPostPanel());
        tabs.addTab("My Internships", buildMyInternshipsPanel());
        tabs.addTab("Applications Received", buildApplicationsPanel());

        add(tabs);

        refreshInternshipTable();
        refreshApplicationTable();
    }

    private JPanel buildPostPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1; panel.add(titleField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1; panel.add(companyField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1; panel.add(locationField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Stipend:"), gbc);
        gbc.gridx = 1; panel.add(stipendField, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; panel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; panel.add(new JScrollPane(descriptionArea), gbc);

        row++;
        JButton postBtn = new JButton("Post Internship");
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(postBtn, gbc);

        postBtn.addActionListener(e -> handlePost());

        return panel;
    }

    private void handlePost() {
        String title = titleField.getText().trim();
        String company = companyField.getText().trim();
        String location = locationField.getText().trim();
        String stipendText = stipendField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || company.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title and Company are required.", "Missing info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double stipend = 0;
        if (!stipendText.isEmpty()) {
            try {
                stipend = Double.parseDouble(stipendText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Stipend must be a number.", "Invalid input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Internship internship = new Internship();
        internship.setTitle(title);
        internship.setCompany(company);
        internship.setLocation(location);
        internship.setStipend(stipend);
        internship.setDescription(description);
        internship.setPostedBy(admin.getId());

        try {
            boolean ok = internshipDAO.addInternship(internship);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Internship posted!");
                titleField.setText("");
                companyField.setText("");
                locationField.setText("");
                stipendField.setText("");
                descriptionArea.setText("");
                refreshInternshipTable();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildMyInternshipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(internshipTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        JButton deleteBtn = new JButton("Delete Selected");
        JPanel buttons = new JPanel();
        buttons.add(refreshBtn);
        buttons.add(deleteBtn);
        panel.add(buttons, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshInternshipTable());
        deleteBtn.addActionListener(e -> handleDeleteInternship());

        return panel;
    }

    private void handleDeleteInternship() {
        int row = internshipTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an internship to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) internshipTableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this internship? This also removes its applications.",
                "Confirm delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            internshipDAO.deleteInternship(id);
            refreshInternshipTable();
            refreshApplicationTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshInternshipTable() {
        internshipTableModel.setRowCount(0);
        try {
            List<Internship> list = internshipDAO.getInternshipsByAdmin(admin.getId());
            for (Internship i : list) {
                internshipTableModel.addRow(new Object[]{
                        i.getId(), i.getTitle(), i.getCompany(), i.getLocation(), i.getStipend(), i.getPostedDate()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(applicationTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton refreshBtn = new JButton("Refresh");
        JButton updateBtn = new JButton("Update Status To:");
        bottom.add(refreshBtn);
        bottom.add(updateBtn);
        bottom.add(statusBox);
        panel.add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshApplicationTable());
        updateBtn.addActionListener(e -> handleUpdateStatus());

        return panel;
    }

    private void handleUpdateStatus() {
        int row = applicationTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an application first.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int appId = (int) applicationTableModel.getValueAt(row, 0);
        String newStatus = (String) statusBox.getSelectedItem();

        try {
            applicationDAO.updateStatus(appId, newStatus);
            refreshApplicationTable();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshApplicationTable() {
        applicationTableModel.setRowCount(0);
        try {
            List<Application> list = applicationDAO.getApplicationsForAdmin(admin.getId());
            for (Application a : list) {
                applicationTableModel.addRow(new Object[]{
                        a.getId(), a.getStudentName(), a.getInternshipTitle(), a.getCompany(), a.getStatus(), a.getAppliedDate()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
