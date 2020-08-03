package org.dreamscale.performance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Getter
public class BenchmarkMetric {

    private String measurement;

    private long durationInMillis;

    public String toString() {
        return StringUtils.rightPad(measurement, 50) + "| "+ durationInMillis ;
    }
}
