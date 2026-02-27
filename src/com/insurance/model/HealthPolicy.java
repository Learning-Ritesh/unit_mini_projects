package com.insurance.model;

/**
 * Child class demonstrating inheritance and polymorphism.
 */
public class HealthPolicy extends AbstractPolicy {
    private final int age;

    public HealthPolicy(String policyId, String policyHolderName, int policyTermYears, double basePremium, int age) {
        super(policyId, policyHolderName, policyTermYears, basePremium);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    @Override
    public double calculatePremium() {
        // Age-based increase to base premium.
        double ageFactor = age > 50 ? 1.35 : 1.10;
        return getBasePremium() * ageFactor;
    }

    @Override
    public String getPolicyType() {
        return "HEALTH";
    }
}
