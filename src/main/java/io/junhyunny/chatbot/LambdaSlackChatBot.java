package io.junhyunny.chatbot;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.junhyunny.chatbot.github.Github;
import io.junhyunny.chatbot.slack.Slack;
import io.junhyunny.entity.GeneuinMember;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.client.HttpClientErrorException;

@Log4j2
public class LambdaSlackChatBot implements RequestStreamHandler {

    public LambdaSlackChatBot() {
    }

    void sendOneDayOneCommitMessage(String slackToken, String oneDayOneCommitChannelName) {
        Github github = new Github();
        Slack slack = new Slack(slackToken);
        String channelId = slack.getChannelId(oneDayOneCommitChannelName);
        Map<String, GeneuinMember> members = slack.getMembersInChannel(channelId);
        for (String key : members.keySet()) {
            String memberName = members.get(key).getProfile().getDisplayName();
            if (memberName == null || memberName.isEmpty()) {
                log.warn("'memberName' 이 존재하지 않습니다.");
                continue;
            }
            try {
                if (!github.doCommitToday(memberName)) {
                    slack.sendPushMessage(memberName, channelId);
                }
            } catch (HttpClientErrorException.NotFound exception) {
                slack.sendNotFoundErrorMessage(memberName, channelId);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")))) {
            Map<String, Object> event = gson.fromJson(reader, HashMap.class);
            log.info("event: " + event);
            String slackToken = (String) event.get("slackToken");
            String oneDayOneCommitChannelName = (String) event.get("oneDayOneCommitChannelName");
            sendOneDayOneCommitMessage(slackToken, oneDayOneCommitChannelName);
        } catch (IOException exception) {
            log.info(exception.toString(), exception);
            throw exception;
        } catch (Exception exception) {
            log.info(exception.toString(), exception);
        }
    }
}