package org.dreamscale.performance;

import org.dreamscale.performance.client.GridtimeClientFactory;
import org.dreamscale.performance.workflow.LearningCircuitWorkflow;

import java.util.List;

public class BenchmarkSuite {

    private GridtimeClientFactory gridtimeClientFactory;
    private LearningCircuitWorkflow learningCircuitWorkflow;

    public void setup(String serverUri, String apiKey) {
        gridtimeClientFactory = new GridtimeClientFactory(serverUri, apiKey);

        learningCircuitWorkflow = new LearningCircuitWorkflow(gridtimeClientFactory);
    }

    public BenchmarkReport run() {

        BenchmarkReport report = new BenchmarkReport();

        report.addMetrics("'Learning Circuit Workflow'", learningCircuitWorkflow.runWorkflow());


        return report;
    }



}
