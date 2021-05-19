package io.junhyunny.chatbot;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

@Log4j2
public class LambdaSlackChatBotTest {

    private final String slackToken = "your slack token";

    private final String channelName = "one-day-one-commit";

    @Test
    public void sendOneDayOneCommitMessage_normalInformation_withoutException() {
        LambdaSlackChatBot chatBot = new LambdaSlackChatBot();
        chatBot.sendOneDayOneCommitMessage(slackToken, channelName);
    }
}