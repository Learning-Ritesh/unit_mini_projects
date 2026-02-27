package com.insurance.service;

import com.insurance.model.AbstractPolicy;
import com.insurance.model.HealthPolicy;
import com.insurance.model.VehiclePolicy;
import com.insurance.repository.PolicyRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for policy operations.
 */
public class PolicyService {
    private final PolicyRepository repository;
    private final List<AbstractPolicy> inMemoryPolicies;

    public PolicyService(PolicyRepository repository) throws IOException {
        this.repository = repository;
        this.inMemoryPolicies = new ArrayList<>(repository.loadPolicies());
    }

    public List<AbstractPolicy> getAllPolicies() {
        return new ArrayList<>(inMemoryPolicies);
    }

    public Optional<AbstractPolicy> findById(String policyId) {
        return inMemoryPolicies.stream()
                .filter(policy -> policy.getPolicyId().equalsIgnoreCase(policyId))
                .findFirst();
    }

    public void addHealthPolicy(String id, String holder, int term, double basePremium, int age) throws IOException {
        validateNewId(id);
        inMemoryPolicies.add(new HealthPolicy(id, holder, term, basePremium, age));
        persist();
    }

    public void addVehiclePolicy(String id, String holder, int term, double basePremium, int vehicleAge) throws IOException {
        validateNewId(id);
        inMemoryPolicies.add(new VehiclePolicy(id, holder, term, basePremium, vehicleAge));
        persist();
    }

    public boolean deletePolicy(String policyId) throws IOException {
        boolean removed = inMemoryPolicies.removeIf(policy -> policy.getPolicyId().equalsIgnoreCase(policyId));
        if (removed) {
            persist();
        }
        return removed;
    }

    public List<AbstractPolicy> listByHolderName(String holderNamePart) {
        // Lambda expression used for filter behavior.
        return inMemoryPolicies.stream()
                .filter(policy -> policy.getPolicyHolderName().toLowerCase().contains(holderNamePart.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<AbstractPolicy> listSortedByPremiumDesc() {
        return inMemoryPolicies.stream()
                .sorted(Comparator.comparingDouble(AbstractPolicy::calculatePremium).reversed())
                .collect(Collectors.toList());
    }

    public String triggerGarbageCollectionHint() {
        long beforeUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.gc();
        long afterUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return "GC requested. Used memory before: " + beforeUsed + " bytes, after: " + afterUsed + " bytes.";
    }

    private void validateNewId(String id) {
        if (findById(id).isPresent()) {
            throw new IllegalArgumentException("Policy ID already exists: " + id);
        }
    }

    private void persist() throws IOException {
        repository.savePolicies(inMemoryPolicies);
    }
}
