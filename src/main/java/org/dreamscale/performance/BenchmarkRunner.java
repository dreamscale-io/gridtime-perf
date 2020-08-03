package org.dreamscale.performance;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Getter
public class BenchmarkRunner {

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