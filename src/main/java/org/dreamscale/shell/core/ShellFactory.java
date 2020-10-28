package org.dreamscale.shell.core;

import com.dreamscale.gridtime.client.TerminalClient;
import org.dreamscale.performance.client.GridtimeClientFactory;
import org.dreamscale.performance.config.UserAccountDto;

import java.util.ArrayList;
import java.util.List;

public class ShellFactory {

    private final String serverUri;
    private final TerminalClient terminalClient;
    private final GridtimeClientFactory gridtimeClientFactory;

    private static final String APP_NAME = "Torchie Shell";

    private static final String HINT_FORMAT = "This is %1$s, running on %2$s\n" +
            "For more information on commands, enter 'help', or type 'exit' to exit.";

    private static final String SUBSHELL_CONTEXT = "You are now in a %1$s subshell, Type 'exit' to exit.\n";

    public ShellFactory(String serverUri, String apiKey) {
        UserAccountDto account = new UserAccountDto();
        account.setUsername("default");
        account.setApiKey(apiKey);

        this.serverUri = serverUri;
        gridtimeClientFactory = new GridtimeClientFactory(serverUri, account);

        terminalClient = gridtimeClientFactory.createTerminalClient();
    }

    public Shell createRootShell() {
        ConsoleIO io = new ConsoleIO();

        TerminalSession terminalSession = new TerminalSession(terminalClient);

        List<String> path = new ArrayList<>();
        path.add("torchie");

        return new Shell(this, path, String.format(HINT_FORMAT, APP_NAME, serverUri), io, terminalSession);
    }

    public Shell createSubShell(Shell parent, String subpath) {
        List<String> newPath = new ArrayList<>(parent.getPath());
        newPath.add(subpath);

        return new Shell(this, newPath, String.format(SUBSHELL_CONTEXT, subpath), parent.getIo(), parent.getTerminalSession() );
    }
}
