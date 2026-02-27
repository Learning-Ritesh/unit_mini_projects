package com.insurance.ui;

import com.insurance.model.AbstractPolicy;
import com.insurance.service.PolicyService;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Terminal-based user interface.
 */
public class TerminalUI {
    private final PolicyService policyService;

    public TerminalUI(PolicyService policyService) {
        this.policyService = policyService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> displayPolicies(policyService.getAllPolicies());
                    case "2" -> addHealthPolicy(scanner);
                    case "3" -> addVehiclePolicy(scanner);
                    case "4" -> deletePolicy(scanner);
                    case "5" -> searchByHolder(scanner);
                    case "6" -> displayPolicies(policyService.listSortedByPremiumDesc());
                    case "7" -> System.out.println(policyService.triggerGarbageCollectionHint());
                    case "0" -> {
                        System.out.println("Exiting terminal UI.");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println("\n==== Insurance Policy Management System ====");
        System.out.println("1. View all policies");
        System.out.println("2. Add health policy");
        System.out.println("3. Add vehicle policy");
        System.out.println("4. Delete policy by ID");
        System.out.println("5. Search by holder name");
        System.out.println("6. View policies sorted by premium (desc)");
        System.out.println("7. Request garbage collection");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }

    private void addHealthPolicy(Scanner scanner) throws IOException {
        System.out.print("Policy ID: ");
        String id = scanner.nextLine();
        System.out.print("Holder name: ");
        String holder = scanner.nextLine();
        System.out.print("Term (years): ");
        int term = Integer.parseInt(scanner.nextLine());
        System.out.print("Base premium: ");
        double premium = Double.parseDouble(scanner.nextLine());
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());

        policyService.addHealthPolicy(id, holder, term, premium, age);
        System.out.println("Health policy added.");
    }

    private void addVehiclePolicy(Scanner scanner) throws IOException {
        System.out.print("Policy ID: ");
        String id = scanner.nextLine();
        System.out.print("Holder name: ");
        String holder = scanner.nextLine();
        System.out.print("Term (years): ");
        int term = Integer.parseInt(scanner.nextLine());
        System.out.print("Base premium: ");
        double premium = Double.parseDouble(scanner.nextLine());
        System.out.print("Vehicle age: ");
        int vehicleAge = Integer.parseInt(scanner.nextLine());

        policyService.addVehiclePolicy(id, holder, term, premium, vehicleAge);
        System.out.println("Vehicle policy added.");
    }

    private void deletePolicy(Scanner scanner) throws IOException {
        System.out.print("Policy ID to delete: ");
        String id = scanner.nextLine();
        boolean removed = policyService.deletePolicy(id);
        System.out.println(removed ? "Policy deleted." : "Policy not found.");
    }

    private void searchByHolder(Scanner scanner) {
        System.out.print("Search holder name: ");
        String query = scanner.nextLine();
        displayPolicies(policyService.listByHolderName(query));
    }

    private void displayPolicies(List<AbstractPolicy> policies) {
        if (policies.isEmpty()) {
            System.out.println("No policies found.");
            return;
        }
        policies.forEach(System.out::println);
    }
}
