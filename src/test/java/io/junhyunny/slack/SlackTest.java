package io.junhyunny.slack;

import static org.assertj.core.api.Assertions.assertThat;
import io.junhyunny.chatbot.slack.Slack;
import org.junit.jupiter.api.Test;

public class SlackTest {

    private final String slackToken = "your slack token";

    @Test
    public void getMembersInChannel_normalChannelName_memberListSizeNotZero() {
        Slack slack = new Slack(slackToken);
        assertThat(slack.getMembersInChannel(slack.getChannelId("one-day-one-commit"))).isNotEmpty();
    }
}
