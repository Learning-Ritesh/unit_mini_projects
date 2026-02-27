package com.insurance.repository;

import com.insurance.model.AbstractPolicy;

import java.io.IOException;
import java.util.List;

/**
 * Repository abstraction to keep storage logic decoupled from business logic.
 */
public interface PolicyRepository {
    List<AbstractPolicy> loadPolicies() throws IOException;
    void savePolicies(List<AbstractPolicy> policies) throws IOException;
}
