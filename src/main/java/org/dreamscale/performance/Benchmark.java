package org.dreamscale.performance;

import java.io.IOException;
import java.util.Properties;

public class Benchmark {

    private static final String PROPERTY_SERVER_TARGET = "benchmark.test.server.target";
    private static final String PROPERTY_API_KEY = "benchmark.test.apikey";


    public static void main(String [] args ) throws IOException {

        new Benchmark().run();
    }

    private void run() throws IOException {

        Properties props = new Properties();
        props.load(Benchmark.class.getClassLoader().getResourceAsStream("application.properties"));

        System.out.println("server = "+props.getProperty(PROPERTY_SERVER_TARGET));


        BenchmarkSuite benchmarkSuite = new BenchmarkSuite();

        benchmarkSuite.setup(props.getProperty(PROPERTY_SERVER_TARGET), props.getProperty(PROPERTY_API_KEY));
        BenchmarkReport report = benchmarkSuite.run();

        System.out.println(report.getOutput());

    }




}
