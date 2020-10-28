package org.dreamscale.shell.core;

import com.dreamscale.gridtime.api.terminal.ActivityContext;
import lombok.Getter;

import java.util.List;

@Getter
public class Shell {

    private final TerminalSession terminalSession;
    private final ShellFactory shellFactory;
    private final String introText;

    private List<String> path;

    private ConsoleIO io;

    private Throwable lastException = null;

    public Shell(ShellFactory shellFactory, List<String> path, String introText, ConsoleIO consoleIO, TerminalSession terminalSession) {
        this.shellFactory = shellFactory;
        this.introText = introText;

        this.path = path;

        this.io = consoleIO;
        this.terminalSession = terminalSession;
    }

    public void commandLoop() {

        io.output(introText);
        String command = "";
        while (!command.trim().equals("exit")) {
            try {
                command = io.readCommand(path);
                processLine(command);
            } catch (TokenException te) {
                lastException = te;
                io.outputException(command, te);
            } catch (CLIException clie) {
                lastException = clie;
                if (!command.trim().equals("exit")) {
                    io.outputException(clie);
                }
            }
        }

    }


    /**
     * You can operate Shell linewise, without entering the command loop.
     * All output is directed to shell's Output.
     *
     * @see asg.cliche.Output
     *
     * @param line Full command line
     * @throws asg.cliche.CLIException This may be TokenException
     */
    public void processLine(String line) throws CLIException {
        if (line.trim().equals("?")) {
            io.output(introText);
        } else {
            List<Token> tokens = Token.tokenize(line);
            if (tokens.size() > 0) {
                String discriminator = tokens.get(0).getString();
                processCommand(discriminator, tokens);
            }
        }
    }

    private void processCommand(String discriminator, List<Token> tokens) throws CLIException {
        assert discriminator != null;
        assert ! discriminator.equals("");


        if (isInRootContext() && isActivityContext(discriminator)) {
            shellFactory.createSubShell(this, discriminator).commandLoop();
        } else {
            String output = terminalSession.run(path, discriminator, tokens);

            io.output(output);
        }

//
//        ShellCommand commandToInvoke = commandTable.lookupCommand(discriminator, tokens);
//
//        Class[] paramClasses = commandToInvoke.getMethod().getParameterTypes();
//        Object[] parameters = inputConverter.convertToParameters(tokens, paramClasses,
//                commandToInvoke.getMethod().isVarArgs());
//
//        outputHeader(commandToInvoke.getHeader(), parameters);
//
//        long timeBefore = Calendar.getInstance().getTimeInMillis();
//        Object invocationResult = commandToInvoke.invoke(parameters);
//        long timeAfter = Calendar.getInstance().getTimeInMillis();
//
//        if (invocationResult != null) {
//            output.output(invocationResult, outputConverter);
//        }
//        if (displayTime) {
//            final long time = timeAfter - timeBefore;
//            if (time != 0L) {
//                output.output(String.format(TIME_MS_FORMAT_STRING, time), outputConverter);
//            }
//        }
    }

    private boolean isActivityContext(String discriminator) {
        boolean isActivityContext = false;

        try {
            ActivityContext.fromString(discriminator);
            isActivityContext = true;
        } catch (IllegalArgumentException ex) {
            //not a context;
        }

        return isActivityContext;
    }

    private boolean isInRootContext() {
        return path.size() == 1;
    }
}
