package org.dreamscale.shell.core;

import com.dreamscale.gridtime.api.account.SimpleStatusDto;
import com.dreamscale.gridtime.api.circuit.TalkMessageDto;
import com.dreamscale.gridtime.api.grid.GridStatusSummaryDto;
import com.dreamscale.gridtime.api.grid.GridTableResults;
import com.dreamscale.gridtime.api.terminal.*;
import com.dreamscale.gridtime.client.TerminalClient;
import org.apache.commons.lang3.StringUtils;
import org.dreamscale.exception.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TerminalSession {

    private TerminalClient terminalClient;

    private CommandManualDto manual;

    private static final int CMD_PAD_SIZE = 10;

    public TerminalSession(TerminalClient terminalClient) {
        this.terminalClient = terminalClient;
    }

    public String run(List<String> path, String discriminator, List<Token> tokens) {
        List<String> args = toArgs(tokens);

        if (discriminator.equalsIgnoreCase("help")) {
            return showHelp(path, args);
        }

        if (discriminator.equalsIgnoreCase("exit")) {
            return "";
        }

        Command command = null;
        String output = "";

        try {
            command = Command.fromString(discriminator);

            TalkMessageDto response = terminalClient.runCommand(new CommandInputDto(command, args));

            output = toOutputString(response);
        } catch (BadRequestException brex) {
            output = brex.getErrorEntity().getErrorCode() + ": "+ brex.getMessage();
        } catch (IllegalArgumentException ex) {
            output = "Unknown command: "+discriminator;
        }


        return output;
    }

    private String toOutputString(TalkMessageDto response) {
        String output = "";

        if (response.getMessageType().equals(SimpleStatusDto.class.getSimpleName())) {
            SimpleStatusDto status = (SimpleStatusDto) response.getData();
            output = status.getStatus() + ": "+ status.getMessage();
        } else if (response.getMessageType().equals(GridTableResults.class.getSimpleName())) {
            GridTableResults table = (GridTableResults) response.getData();
            output = table.getDisplay();
        } else if (response.getMessageType().equals(GridStatusSummaryDto.class.getSimpleName())) {
            GridStatusSummaryDto summary = (GridStatusSummaryDto)response.getData();

            output = summary.getMessage() + "\n";
            output += summary.getActivitySummary().getDisplay();
        } else {
            output = response.getData().toString();
        }

        return output;
    }

    public String showHelp(List<String> path, List<String> args) {

        if (manual == null) {
            manual = terminalClient.getCommandManual();
        }

        String activityHelp;

        if (isRootPath(path)) {
            activityHelp = createRootContextHelp(args);
        } else {
            activityHelp = createContextSpecificHelp(path, args);
        }

        return activityHelp;
    }

    private String createContextSpecificHelp(List<String> path, List<String> args) {
        String activityHelp = "";
        String context = path.get(path.size() - 1);

        CommandManualPageDto page = manual.getManualPage(ActivityContext.fromString(context));
        List<CommandDescriptorDto> descriptors = page.getCommandDescriptors();

        if (args.size() == 0) {

            for (CommandDescriptorDto descriptor : descriptors) {
                activityHelp += StringUtils.rightPad(descriptor.getCommand().name().toLowerCase(), CMD_PAD_SIZE) + " :: " + descriptor.getDescription() + "\n";
            }
        } else {
            for (CommandDescriptorDto descriptor : descriptors) {
                if (args.get(0).equalsIgnoreCase(descriptor.getCommand().name())) {
                    activityHelp += descriptor.toDisplayString() + "\n";
                }
            }
        }
        return activityHelp;
    }

    private String createRootContextHelp(List<String> args) {
        String activityHelp = "";
        if (args.size() == 0) {
            activityHelp = "Enter the name of a type of activity, " +
                    "to create a subshell that helps you with related tasks.\n\n";

            List<ActivityContext> activityContexts = manual.getActivityContexts();

            for (ActivityContext activity : activityContexts) {
                activityHelp += StringUtils.rightPad(activity.name().toLowerCase(), CMD_PAD_SIZE) + " :: "+activity.getDescription() + "\n";
            }
        } else {
            CommandManualPageDto page = terminalClient.getManualPageForCommand(args.get(0));

            if (page != null) {
                for (CommandDescriptorDto descriptor : page.getCommandDescriptors()) {
                    activityHelp += descriptor.toDisplayString() + "\n";
                }
            }
        }
        return activityHelp;
    }

    private boolean isRootPath(List<String> path) {
        return path.size() == 1;
    }

    private List<String> toArgs(List<Token> tokens) {
        List<String> args = new ArrayList<>();

        for (int i = 1; i < tokens.size(); i++) {
            args.add(tokens.get(i).getString());
        }

        return args;
    }
}
