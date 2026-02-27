package com.insurance;

import com.insurance.repository.JsonPolicyRepository;
import com.insurance.repository.PolicyRepository;
import com.insurance.service.PolicyService;
import com.insurance.ui.SwingUI;
import com.insurance.ui.TerminalUI;

import java.nio.file.Path;

/**
 * Entry point for the mini project.
 *
 * Set USE_GUI = true for Swing UI, false for terminal UI.
 */
public class InsurancePolicyManagementApp {
    private static final boolean USE_GUI = false;

    public static void main(String[] args) {
        try {
            Path dataFile = Path.of("data", "policies.json");
            PolicyRepository repository = new JsonPolicyRepository(dataFile);
            PolicyService policyService = new PolicyService(repository);

            if (USE_GUI) {
                new SwingUI(policyService).start();
            } else {
                new TerminalUI(policyService).start();
            }
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
