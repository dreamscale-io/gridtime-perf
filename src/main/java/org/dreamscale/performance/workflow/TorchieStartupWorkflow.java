package org.dreamscale.performance.workflow;

import com.dreamscale.gridtime.api.circuit.LearningCircuitDto;
import com.dreamscale.gridtime.api.team.TeamCircuitDto;
import com.dreamscale.gridtime.client.*;
import org.dreamscale.performance.BenchmarkMetric;
import org.dreamscale.performance.BenchmarkRunner;
import org.dreamscale.performance.client.GridtimeClientFactory;

import java.util.List;

public class TorchieStartupWorkflow {

    private final LearningCircuitClient learningCircuitClient;

    private final JournalClient journalClient;

    private final TeamClient teamClient;

    private final MemberClient memberClient;

    private final TalkToClient talkToClient;

    private final TeamCircuitClient teamCircuitClient;

    public TorchieStartupWorkflow(GridtimeClientFactory gridtimeClientFactory) {

        this.learningCircuitClient = gridtimeClientFactory.createLearningCircuitClient();

        this.teamCircuitClient = gridtimeClientFactory.createTeamCircuitClient();

        this.journalClient = gridtimeClientFactory.createJournalClient();

        this.teamClient = gridtimeClientFactory.createTeamClient();

        this.memberClient = gridtimeClientFactory.createMemberClient();

        this.talkToClient = gridtimeClientFactory.createTalkToClient();
    }

    public List<BenchmarkMetric> runWorkflow() {

        BenchmarkRunner runner = new BenchmarkRunner();

        runner.runAndMeasure(
                "journalClient::getRecentJournal",
                () -> journalClient.getRecentJournal());

        runner.runAndMeasure(
                "learningCircuitClient::getMyParticipatingCircuits",
                () -> learningCircuitClient.getMyParticipatingCircuits());

        runner.runAndMeasure(
                "learningCircuitClient::getMyDoItLaterCircuits",
                () -> learningCircuitClient.getMyDoItLaterCircuits());

        runner.runAndMeasure(
                "learningCircuitClient::getMyRetroCircuits",
                () -> learningCircuitClient.getMyRetroCircuits());

        runner.runAndMeasure(
                "learningCircuitClient::getActiveCircuit",
                () -> learningCircuitClient.getActiveCircuit());

        List<TeamCircuitDto> teamCircuits = runner.runAndMeasure(
                "teamCircuitClient::getAllMyTeamCircuits",
                () -> teamCircuitClient.getAllMyTeamCircuits());

        runner.runAndMeasure(
                "teamClient::getMyTeams",
                () -> teamClient.getMyTeams());

        runner.runAndMeasure(
                "memberClient::getMe",
                () -> memberClient.getMe());


        for (TeamCircuitDto teamCircuit: teamCircuits) {
            runner.runAndMeasure(
                    "talkToClient::joinExistingRoom",
                    () -> talkToClient.joinExistingRoom(teamCircuit.getDefaultRoom().getTalkRoomName()));
        }


        return runner.getMetrics();

    }




}
