package org.dreamscale.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dreamscale.performance.config.UserAccountsDto;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Benchmark {

    private static final String PROPERTY_SERVER_TARGET = "benchmark.test.server.target";
    private static final String PROPERTY_API_KEY = "benchmark.test.apikey";


    public static void main(String [] args ) throws IOException {

        new Benchmark().run();
    }

    private void run() throws IOException {

        InputStream applicationPropsStream = Benchmark.class.getClassLoader().getResourceAsStream("application.properties");
        InputStream usersJsonStream = Benchmark.class.getClassLoader().getResourceAsStream("users.json");

        Properties props = new Properties();
        props.load(applicationPropsStream);

        UserAccountsDto userAccounts = new ObjectMapper().readValue(usersJsonStream, UserAccountsDto.class);

        System.out.println("server = "+props.getProperty(PROPERTY_SERVER_TARGET));

        BenchmarkSuite benchmarkSuite = new BenchmarkSuite();

        benchmarkSuite.setup(props.getProperty(PROPERTY_SERVER_TARGET), userAccounts.getUsers());
        BenchmarkReport report = benchmarkSuite.run();

        System.out.println(report.getOutput());

    }




}
