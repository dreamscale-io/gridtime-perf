package org.dreamscale.performance.workflow;

import com.dreamscale.gridtime.api.account.SimpleStatusDto;
import com.dreamscale.gridtime.api.circuit.LearningCircuitDto;
import com.dreamscale.gridtime.client.LearningCircuitClient;
import lombok.Getter;
import org.dreamscale.performance.BenchmarkMetric;
import org.dreamscale.performance.client.GridtimeClientFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class LearningCircuitWorkflow {

    private final GridtimeClientFactory gridtimeClientFactory;

    private final LearningCircuitClient learningCircuitClient;

    public LearningCircuitWorkflow(GridtimeClientFactory gridtimeClientFactory) {
        this.gridtimeClientFactory = gridtimeClientFactory;

        this.learningCircuitClient = gridtimeClientFactory.createLearningCircuitClient();
    }


    public List<BenchmarkMetric> runWorkflow() {

        RunAndMeasure runner = new RunAndMeasure();

        LearningCircuitDto circuitDto = runner.runAndMeasure(
                "learningCircuitClient.getActiveCircuit()",
                learningCircuitClient::getActiveCircuit);


        if (circuitDto == null) {
            circuitDto = runner.runAndMeasure(
                    "learningCircuitClient.startWTF()",
                    () -> learningCircuitClient.startWTF());
        }

        LearningCircuitDto finalCircuitDto = circuitDto;

        runner.runAndMeasure(
                "learningCircuitClient.getCircuitWithAllDetails()",
                () -> learningCircuitClient.getCircuitWithAllDetails(finalCircuitDto.getCircuitName()));

        runner.runAndMeasure(
                "learningCircuitClient.cancelWTF()",
                () -> learningCircuitClient.cancelWTF(finalCircuitDto.getCircuitName()));


        return runner.getMetrics();

    }

    @Getter
    private static class RunAndMeasure {

        private List<BenchmarkMetric> metrics = new ArrayList<>();

        public <V> V runAndMeasure(String metricName, Callable<V> callable)  {

            long before = System.currentTimeMillis();

            V result = null;
            try {
                result = callable.call();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            long after = System.currentTimeMillis();

            metrics.add(new BenchmarkMetric(metricName, (after - before)));

            return result;
        }
    }
}
