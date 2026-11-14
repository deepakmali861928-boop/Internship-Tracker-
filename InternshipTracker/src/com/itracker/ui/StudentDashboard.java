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

public class StudentDashboard extends JFrame {

    private final User student;
    private final InternshipDAO internshipDAO = new InternshipDAO();
    private final ApplicationDAO applicationDAO = new ApplicationDAO();

    // Browse tab
    private final JTextField keywordField = new JTextField(15);
    private final JTextField locationField = new JTextField(15);
    private final DefaultTableModel internshipTableModel =
            new DefaultTableModel(new Object[]{"ID", "Title", "Company", "Location", "Stipend", "Description"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
    private final JTable internshipTable = new JTable(internshipTableModel);

    // My Applications tab
    private final DefaultTableModel applicationTableModel =
            new DefaultTableModel(new Object[]{"App ID", "Internship", "Company", "Status", "Applied On"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) { return false; }
            };
    private final JTable applicationTable = new JTable(applicationTableModel);

    public StudentDashboard(User student) {
        this.student = student;
        setTitle("Student Dashboard - " + student.getName());
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Browse Internships", buildBrowsePanel());
        tabs.addTab("My Applications", buildApplicationsPanel());

        add(tabs);

        refreshInternshipTable("", "");
        refreshApplicationTable();
    }

    private JPanel buildBrowsePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Keyword:"));
        searchPanel.add(keywordField);
        searchPanel.add(new JLabel("Location:"));
        searchPanel.add(locationField);
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(internshipTable), BorderLayout.CENTER);

        JButton applyBtn = new JButton("Apply to Selected Internship");
        JPanel bottom = new JPanel();
        bottom.add(applyBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        searchBtn.addActionListener(e ->
                refreshInternshipTable(keywordField.getText().trim(), locationField.getText().trim()));
        clearBtn.addActionListener(e -> {
            keywordField.setText("");
            locationField.setText("");
            refreshInternshipTable("", "");
        });
        applyBtn.addActionListener(e -> handleApply());

        return panel;
    }

    private void handleApply() {
        int row = internshipTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select an internship to apply to.", "No selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int internshipId = (int) internshipTableModel.getValueAt(row, 0);

        try {
            boolean applied = applicationDAO.apply(student.getId(), internshipId);
            if (applied) {
                JOptionPane.showMessageDialog(this, "Application submitted!");
                refreshApplicationTable();
            } else {
                JOptionPane.showMessageDialog(this, "You have already applied to this internship.", "Duplicate application", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshInternshipTable(String keyword, String location) {
        internshipTableModel.setRowCount(0);
        try {
            List<Internship> list = internshipDAO.searchInternships(keyword, location);
            for (Internship i : list) {
                internshipTableModel.addRow(new Object[]{
                        i.getId(), i.getTitle(), i.getCompany(), i.getLocation(), i.getStipend(), i.getDescription()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel buildApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(applicationTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        JPanel bottom = new JPanel();
        bottom.add(refreshBtn);
        panel.add(bottom, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshApplicationTable());

        return panel;
    }

    private void refreshApplicationTable() {
        applicationTableModel.setRowCount(0);
        try {
            List<Application> list = applicationDAO.getApplicationsByStudent(student.getId());
            for (Application a : list) {
                applicationTableModel.addRow(new Object[]{
                        a.getId(), a.getInternshipTitle(), a.getCompany(), a.getStatus(), a.getAppliedDate()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
