package io.junhyunny.chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.junhyunny.chatbot.github.Github;
import io.junhyunny.chatbot.slack.Slack;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LambdaSlackChatBot implements RequestStreamHandler {

	public LambdaSlackChatBot() {}

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("US-ASCII")))) {
			Map<String, Object> event = gson.fromJson(reader, HashMap.class);
			log.info("event: " + event);
			String owner = (String) event.get("owner");
			String slackToken = (String) event.get("slackToken");
			String channelName = (String) event.get("channelName");
			Github github = new Github(owner);
			if (!github.doCommitToday()) {
				Slack slack = new Slack(slackToken);
				slack.sendPushMessage(channelName);
			}
		} catch (Exception exception) {
			log.info(exception.toString(), exception);
		}
	}
}