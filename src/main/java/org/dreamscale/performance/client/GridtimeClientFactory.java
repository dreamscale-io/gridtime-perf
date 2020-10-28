package org.dreamscale.performance.client;

import com.dreamscale.gridtime.client.*;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.dreamscale.feign.DefaultFeignConfig;
import org.dreamscale.logging.RequestResponseLoggerFactory;
import org.dreamscale.performance.config.UserAccountDto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GridtimeClientFactory {

    private static final int connectTimeoutMillis = 5000;
    private static final int readTimeoutMillis = 30000;

    private final String serverUri;
    private final Map<String, UserAccountDto> usersByUsername;

    private UserAccountDto activeUser;

    public GridtimeClientFactory(String serverUri, List<UserAccountDto> users) {
        this.serverUri = serverUri;
        this.usersByUsername = new LinkedHashMap<>();

        for (UserAccountDto user : users) {
            usersByUsername.put(user.getUsername(), user);
        }

        this.activeUser = users.get(0);
    }

    public GridtimeClientFactory(String serverUri, UserAccountDto defaultUser) {
        this.serverUri = serverUri;
        this.usersByUsername = new LinkedHashMap<>();

        this.activeUser = defaultUser;

        usersByUsername.put(defaultUser.getUsername(), defaultUser);
    }

    public void changeActiveUser(String userName) {
        activeUser = usersByUsername.get(userName);
    }

    public AccountClient createAccountClient() {
        return createClient(serverUri, activeUser.getApiKey(), AccountClient.class);
    }

    public JournalClient createJournalClient() {
        return createClient(serverUri, activeUser.getApiKey(), JournalClient.class);
    }

    public TerminalClient createTerminalClient() {
        return createClient(serverUri, activeUser.getApiKey(), TerminalClient.class);
    }

    public LearningCircuitClient createLearningCircuitClient() {
        return createClient(serverUri, activeUser.getApiKey(), LearningCircuitClient.class);
    }

    private <T> T createClient(String serverUri, String apiKey, Class<T> clientClazz) {
        return new DefaultFeignConfig()
                .jacksonFeignBuilder()
                .requestResponseLoggerFactory(new RequestResponseLoggerFactory())
                .requestInterceptor(new ActiveUserAuthHeaderRequestInterceptor())
                .options(new Request.Options(connectTimeoutMillis, readTimeoutMillis))
                .target(clientClazz, serverUri);
    }

    public TeamClient createTeamClient() {
        return createClient(serverUri, activeUser.getApiKey(), TeamClient.class);
    }

    public MemberClient createMemberClient() {
        return createClient(serverUri, activeUser.getApiKey(), MemberClient.class);
    }

    public TalkToClient createTalkToClient() {
        return createClient(serverUri, activeUser.getApiKey(), TalkToClient.class);
    }

    public TeamCircuitClient createTeamCircuitClient() {
        return createClient(serverUri, activeUser.getApiKey(), TeamCircuitClient.class);
    }

    private class ActiveUserAuthHeaderRequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {

            template.header("X-API-KEY", activeUser.getApiKey());
        }

    }

}
