package com.insurance.model;

/**
 * Abstract base class showing inheritance and encapsulation.
 */
public abstract class AbstractPolicy implements PremiumCalculable {
    private String policyId;
    private String policyHolderName;
    private int policyTermYears;
    private double basePremium;

    protected AbstractPolicy(String policyId, String policyHolderName, int policyTermYears, double basePremium) {
        this.policyId = policyId;
        this.policyHolderName = policyHolderName;
        this.policyTermYears = policyTermYears;
        this.basePremium = basePremium;
    }

    public String getPolicyId() {
        return policyId;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public int getPolicyTermYears() {
        return policyTermYears;
    }

    public double getBasePremium() {
        return basePremium;
    }

    public void setPolicyHolderName(String policyHolderName) {
        this.policyHolderName = policyHolderName;
    }

    public void setPolicyTermYears(int policyTermYears) {
        this.policyTermYears = policyTermYears;
    }

    public void setBasePremium(double basePremium) {
        this.basePremium = basePremium;
    }

    public abstract String getPolicyType();

    @Override
    public String toString() {
        return "Policy{" +
                "id='" + policyId + '\'' +
                ", holder='" + policyHolderName + '\'' +
                ", termYears=" + policyTermYears +
                ", basePremium=" + basePremium +
                ", type='" + getPolicyType() + '\'' +
                ", calculatedPremium=" + String.format("%.2f", calculatePremium()) +
                '}';
    }
}
