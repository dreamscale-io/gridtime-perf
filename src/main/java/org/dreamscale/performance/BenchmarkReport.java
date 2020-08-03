package org.dreamscale.performance;

import org.dreamscale.performance.client.GridtimeClientFactory;
import org.dreamscale.performance.workflow.LearningCircuitWorkflow;
import org.springframework.boot.actuate.metrics.Metric;

import java.util.*;

public class BenchmarkReport {

    Map<String, List<BenchmarkMetric>> metricsByWorkflow = new LinkedHashMap<>();


    public String getOutput() {
        String report = "";

        Set<String> workflowKeys = metricsByWorkflow.keySet();

        for (String workflow: workflowKeys) {

            report += "\n\n"+ workflow + " Report\n";
            report += "==========================================================\n\n";

            List<BenchmarkMetric> metrics = metricsByWorkflow.get(workflow);

            for (BenchmarkMetric metric : metrics) {
                report += metric.toString() + "\n";
            }
        }

        return report;

    }

    public void addMetrics(String workflowName, List<BenchmarkMetric> metrics) {
        metricsByWorkflow.put(workflowName, metrics);
    }
}
