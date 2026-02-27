package com.insurance.model;

/**
 * Child class demonstrating inheritance and polymorphism.
 */
public class VehiclePolicy extends AbstractPolicy {
    private final int vehicleAge;

    public VehiclePolicy(String policyId, String policyHolderName, int policyTermYears, double basePremium, int vehicleAge) {
        super(policyId, policyHolderName, policyTermYears, basePremium);
        this.vehicleAge = vehicleAge;
    }

    public int getVehicleAge() {
        return vehicleAge;
    }

    @Override
    public double calculatePremium() {
        // Older vehicles get slightly higher premium.
        double ageFactor = vehicleAge > 10 ? 1.25 : 1.05;
        return getBasePremium() * ageFactor;
    }

    @Override
    public String getPolicyType() {
        return "VEHICLE";
    }
}
