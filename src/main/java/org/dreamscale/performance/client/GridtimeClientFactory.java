package org.dreamscale.performance.client;

import com.dreamscale.gridtime.client.*;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.dreamscale.feign.DefaultFeignConfig;
import org.dreamscale.logging.RequestResponseLoggerFactory;

public class GridtimeClientFactory {

    private static final int connectTimeoutMillis = 5000;
    private static final int readTimeoutMillis = 30000;

    private final String serverUri;
    private final String apiKey;

    public GridtimeClientFactory(String serverUri, String apiKey) {
        this.serverUri = serverUri;
        this.apiKey = apiKey;
    }

    public AccountClient createAccountClient() {
        return createClient(serverUri, apiKey, AccountClient.class);
    }

    public JournalClient createJournalClient() {
        return createClient(serverUri, apiKey, JournalClient.class);
    }
    public LearningCircuitClient createLearningCircuitClient() {
        return createClient(serverUri, apiKey, LearningCircuitClient.class);
    }

    private <T> T createClient(String serverUri, String apiKey, Class<T> clientClazz) {
        return new DefaultFeignConfig()
                .jacksonFeignBuilder()
                .requestResponseLoggerFactory(new RequestResponseLoggerFactory())
                .requestInterceptor(new StaticAuthHeaderRequestInterceptor(apiKey))
                .options(new Request.Options(connectTimeoutMillis, readTimeoutMillis))
                .target(clientClazz, serverUri);
    }

    public TeamClient createTeamClient() {
        return createClient(serverUri, apiKey, TeamClient.class);
    }

    public MemberClient createMemberClient() {
        return createClient(serverUri, apiKey, MemberClient.class);
    }

    public TalkToClient createTalkToClient() {
        return createClient(serverUri, apiKey, TalkToClient.class);
    }

    public TeamCircuitClient createTeamCircuitClient() {
        return createClient(serverUri, apiKey, TeamCircuitClient.class);
    }


    private static class StaticAuthHeaderRequestInterceptor implements RequestInterceptor {

        private String apiKey;

        public StaticAuthHeaderRequestInterceptor(String apiKey) {
            this.apiKey = apiKey;
        }

        @Override
        public void apply(RequestTemplate template) {
            template.header("X-API-KEY", apiKey);
        }

    }
}
