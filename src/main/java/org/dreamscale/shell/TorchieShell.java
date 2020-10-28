/*
 * This example code is in public domain.
 */

package org.dreamscale.shell;


import org.dreamscale.performance.Benchmark;
import org.dreamscale.shell.core.ShellFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 'more advanced' example.
 *
 * @author ASG
 */
public class TorchieShell {

    private static final String PROPERTY_SERVER_TARGET = "shell.server.target";
    private static final String PROPERTY_API_KEY = "shell.user.apikey";

    public static void main(String[] args) throws IOException {
        Logger l = Logger.getLogger("mylogger");
        l.setLevel(Level.OFF);

        new TorchieShell().run();
    }

    private void run() throws IOException {

        InputStream applicationPropsStream = Benchmark.class.getClassLoader().getResourceAsStream("application.properties");

        Properties props = new Properties();
        props.load(applicationPropsStream);

        ShellFactory shellFactory = new ShellFactory(props.getProperty(PROPERTY_SERVER_TARGET), props.getProperty(PROPERTY_API_KEY));

        shellFactory.createRootShell().commandLoop();

    }
}
