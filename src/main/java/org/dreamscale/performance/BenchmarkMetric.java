package org.dreamscale.performance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.dreamscale.performance.client.GridtimeClientFactory;
import org.dreamscale.performance.workflow.LearningCircuitWorkflow;

import java.time.Duration;

@AllArgsConstructor
@Getter
public class BenchmarkMetric {

    private String measurement;

    private long durationInMillis;

    public String toString() {
        return StringUtils.rightPad(measurement, 50) + "| "+ durationInMillis ;
    }
}
