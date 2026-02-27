package com.insurance.ui;

import com.insurance.model.AbstractPolicy;
import com.insurance.service.PolicyService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;

/**
 * Swing-based user interface.
 */
public class SwingUI {
    private final PolicyService policyService;
    private final JTextArea outputArea = new JTextArea();

    public SwingUI(PolicyService policyService) {
        this.policyService = policyService;
    }

    public void start() {
        SwingUtilities.invokeLater(this::createAndShowUI);
    }

    private void createAndShowUI() {
        JFrame frame = new JFrame("Insurance Policy Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(880, 520);

        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Policy Input"));

        JTextField idField = new JTextField();
        JTextField holderField = new JTextField();
        JTextField termField = new JTextField();
        JTextField basePremiumField = new JTextField();
        JTextField riskField = new JTextField();
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"HEALTH", "VEHICLE"});

        inputPanel.add(new JLabel("Policy Type"));
        inputPanel.add(typeBox);
        inputPanel.add(new JLabel("Policy ID"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Holder Name"));
        inputPanel.add(holderField);
        inputPanel.add(new JLabel("Term (Years)"));
        inputPanel.add(termField);
        inputPanel.add(new JLabel("Base Premium"));
        inputPanel.add(basePremiumField);
        inputPanel.add(new JLabel("Age / Vehicle Age"));
        inputPanel.add(riskField);

        JButton addButton = new JButton("Add Policy");
        JButton listButton = new JButton("List Policies");
        JButton sortedButton = new JButton("Sort by Premium");
        JButton deleteButton = new JButton("Delete by ID");
        JButton searchButton = new JButton("Search by Holder");
        JButton gcButton = new JButton("Request GC");

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 8, 8));
        buttonPanel.add(addButton);
        buttonPanel.add(listButton);
        buttonPanel.add(sortedButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(gcButton);

        outputArea.setEditable(false);

        addButton.addActionListener(e -> {
            try {
                String id = idField.getText().trim();
                String holder = holderField.getText().trim();
                int term = Integer.parseInt(termField.getText().trim());
                double basePremium = Double.parseDouble(basePremiumField.getText().trim());
                int risk = Integer.parseInt(riskField.getText().trim());

                if ("HEALTH".equals(typeBox.getSelectedItem())) {
                    policyService.addHealthPolicy(id, holder, term, basePremium, risk);
                } else {
                    policyService.addVehiclePolicy(id, holder, term, basePremium, risk);
                }

                showMessage("Policy added successfully.");
                appendPolicies(policyService.getAllPolicies());
            } catch (Exception ex) {
                showError(ex);
            }
        });

        listButton.addActionListener(e -> appendPolicies(policyService.getAllPolicies()));
        sortedButton.addActionListener(e -> appendPolicies(policyService.listSortedByPremiumDesc()));

        deleteButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(frame, "Enter Policy ID to delete:");
            if (id == null || id.isBlank()) {
                return;
            }
            try {
                boolean removed = policyService.deletePolicy(id.trim());
                showMessage(removed ? "Policy deleted." : "Policy not found.");
                appendPolicies(policyService.getAllPolicies());
            } catch (IOException ex) {
                showError(ex);
            }
        });

        searchButton.addActionListener(e -> {
            String query = JOptionPane.showInputDialog(frame, "Enter holder name search text:");
            if (query != null) {
                appendPolicies(policyService.listByHolderName(query));
            }
        });

        gcButton.addActionListener(e -> showMessage(policyService.triggerGarbageCollectionHint()));

        JPanel northContainer = new JPanel(new BorderLayout(8, 8));
        northContainer.add(inputPanel, BorderLayout.CENTER);
        northContainer.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(northContainer, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        appendPolicies(policyService.getAllPolicies());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void appendPolicies(List<AbstractPolicy> policies) {
        StringBuilder builder = new StringBuilder();
        if (policies.isEmpty()) {
            builder.append("No policies found.\n");
        } else {
            policies.forEach(policy -> builder.append(policy).append("\n"));
        }
        outputArea.setText(builder.toString());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
