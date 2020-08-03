package org.dreamscale.performance.workflow;

import com.dreamscale.gridtime.api.circuit.LearningCircuitDto;
import com.dreamscale.gridtime.client.LearningCircuitClient;
import org.dreamscale.performance.BenchmarkMetric;
import org.dreamscale.performance.BenchmarkRunner;
import org.dreamscale.performance.client.GridtimeClientFactory;

import java.util.List;

public class LearningCircuitStateWorkflow {

    private final LearningCircuitClient learningCircuitClient;

    public LearningCircuitStateWorkflow(GridtimeClientFactory gridtimeClientFactory) {

        this.learningCircuitClient = gridtimeClientFactory.createLearningCircuitClient();
    }

    public List<BenchmarkMetric> runWorkflow() {

        BenchmarkRunner runner = new BenchmarkRunner();

        LearningCircuitDto circuitDto = runner.runAndMeasure(
                "learningCircuitClient::getActiveCircuit",
                learningCircuitClient::getActiveCircuit);


        if (circuitDto == null) {
            circuitDto = runner.runAndMeasure(
                    "learningCircuitClient::startWTF",
                    () -> learningCircuitClient.startWTF());
        }

        LearningCircuitDto finalCircuitDto = circuitDto;

        //pause, unpause, solve, retro, close

        runner.runAndMeasure(
                "learningCircuitClient::getCircuitWithAllDetails",
                () -> learningCircuitClient.getCircuitWithAllDetails(finalCircuitDto.getCircuitName()));

        runner.runAndMeasure(
                "learningCircuitClient::pauseWTFWithDoItLater",
                () -> learningCircuitClient.pauseWTFWithDoItLater(finalCircuitDto.getCircuitName()));

        runner.runAndMeasure(
                "learningCircuitClient::resumeWTF",
                () -> learningCircuitClient.resumeWTF(finalCircuitDto.getCircuitName()));

        runner.runAndMeasure(
                "learningCircuitClient::solveWTF",
                () -> learningCircuitClient.solveWTF(finalCircuitDto.getCircuitName()));

        runner.runAndMeasure(
                "learningCircuitClient::markForReview",
                () -> learningCircuitClient.markForReview(finalCircuitDto.getCircuitName()));

        runner.runAndMeasure(
                "learningCircuitClient::markForClose",
                () -> learningCircuitClient.markForClose(finalCircuitDto.getCircuitName()));



        return runner.getMetrics();

    }


}
