package org.dreamscale.performance;

import org.dreamscale.performance.client.GridtimeClientFactory;
import org.dreamscale.performance.config.UserAccountDto;
import org.dreamscale.performance.workflow.LearningCircuitStateWorkflow;
import org.dreamscale.performance.workflow.TorchieStartupWorkflow;

import java.util.List;

public class BenchmarkSuite {

    private GridtimeClientFactory gridtimeClientFactory;
    private LearningCircuitStateWorkflow learningCircuitWorkflow;
    private TorchieStartupWorkflow torchieStartupWorkflow;

    public void setup(String serverUri, List<UserAccountDto> users) {
        gridtimeClientFactory = new GridtimeClientFactory(serverUri, users);

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
