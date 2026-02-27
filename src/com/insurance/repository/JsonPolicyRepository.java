package com.insurance.repository;

import com.insurance.model.AbstractPolicy;
import com.insurance.model.HealthPolicy;
import com.insurance.model.VehiclePolicy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JSON repository using a lightweight parser suitable for this mini project.
 * Format is stable because we control both write and read paths.
 */
public class JsonPolicyRepository implements PolicyRepository {
    private final Path filePath;

    public JsonPolicyRepository(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<AbstractPolicy> loadPolicies() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        String content = Files.readString(filePath).trim();
        if (content.isEmpty() || content.equals("[]")) {
            return new ArrayList<>();
        }

        List<AbstractPolicy> policies = new ArrayList<>();
        Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
        Matcher objectMatcher = objectPattern.matcher(content);

        while (objectMatcher.find()) {
            String objectBody = objectMatcher.group(1);
            Map<String, String> fields = parseObjectFields(objectBody);

            String type = fields.get("type");
            String id = fields.get("policyId");
            String holder = fields.get("policyHolderName");
            int term = Integer.parseInt(fields.get("policyTermYears"));
            double basePremium = Double.parseDouble(fields.get("basePremium"));
            int riskValue = Integer.parseInt(fields.get("riskValue"));

            if ("HEALTH".equals(type)) {
                policies.add(new HealthPolicy(id, holder, term, basePremium, riskValue));
            } else if ("VEHICLE".equals(type)) {
                policies.add(new VehiclePolicy(id, holder, term, basePremium, riskValue));
            }
        }

        return policies;
    }

    @Override
    public void savePolicies(List<AbstractPolicy> policies) throws IOException {
        Files.createDirectories(filePath.getParent());
        StringBuilder builder = new StringBuilder();
        builder.append("[\n");

        for (int i = 0; i < policies.size(); i++) {
            AbstractPolicy policy = policies.get(i);
            int riskValue = extractRiskValue(policy);

            builder.append("  {\n")
                    .append("    \"type\": \"").append(policy.getPolicyType()).append("\",\n")
                    .append("    \"policyId\": \"").append(escape(policy.getPolicyId())).append("\",\n")
                    .append("    \"policyHolderName\": \"").append(escape(policy.getPolicyHolderName())).append("\",\n")
                    .append("    \"policyTermYears\": ").append(policy.getPolicyTermYears()).append(",\n")
                    .append("    \"basePremium\": ").append(policy.getBasePremium()).append(",\n")
                    .append("    \"riskValue\": ").append(riskValue).append("\n")
                    .append("  }");

            if (i < policies.size() - 1) {
                builder.append(",");
            }
            builder.append("\n");
        }

        builder.append("]\n");
        Files.writeString(filePath, builder.toString());
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static int extractRiskValue(AbstractPolicy policy) {
        if (policy instanceof HealthPolicy healthPolicy) {
            return healthPolicy.getAge();
        }
        if (policy instanceof VehiclePolicy vehiclePolicy) {
            return vehiclePolicy.getVehicleAge();
        }
        return 0;
    }

    private static Map<String, String> parseObjectFields(String objectBody) {
        Pattern fieldPattern = Pattern.compile("\"(.*?)\"\\s*:\\s*(\".*?\"|[0-9.]+)");
        Matcher fieldMatcher = fieldPattern.matcher(objectBody);
        Map<String, String> fields = new HashMap<>();

        while (fieldMatcher.find()) {
            String key = fieldMatcher.group(1);
            String rawValue = fieldMatcher.group(2);
            String cleanValue = rawValue.startsWith("\"")
                    ? rawValue.substring(1, rawValue.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\")
                    : rawValue;
            fields.put(key, cleanValue);
        }
        return fields;
    }
}
