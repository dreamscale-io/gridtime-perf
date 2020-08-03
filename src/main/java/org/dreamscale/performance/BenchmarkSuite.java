package org.dreamscale.performance;

import org.dreamscale.performance.client.GridtimeClientFactory;
import org.dreamscale.performance.workflow.LearningCircuitStateWorkflow;
import org.dreamscale.performance.workflow.TorchieStartupWorkflow;

public class BenchmarkSuite {

    private GridtimeClientFactory gridtimeClientFactory;
    private LearningCircuitStateWorkflow learningCircuitWorkflow;
    private TorchieStartupWorkflow torchieStartupWorkflow;

    public void setup(String serverUri, String apiKey) {
        gridtimeClientFactory = new GridtimeClientFactory(serverUri, apiKey);

        learningCircuitWorkflow = new LearningCircuitStateWorkflow(gridtimeClientFactory);

        torchieStartupWorkflow = new TorchieStartupWorkflow(gridtimeClientFactory);
    }

    public BenchmarkReport run() {

        BenchmarkReport report = new BenchmarkReport();

        report.addMetrics("'Learning Circuit Workflow'", learningCircuitWorkflow.runWorkflow());

        report.addMetrics("'Torchie Startup Workflow'", torchieStartupWorkflow.runWorkflow());

        return report;
    }



}
