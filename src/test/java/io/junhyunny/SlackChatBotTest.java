package io.junhyunny;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
class SlackChatBotTest {

	@Value("${client-slackToken.slack}")
	private String slackToken;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	void getChannel() {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + slackToken);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(headers);

		RestTemplate restTemplate = new RestTemplate();
		List<Map<String, Object>> channels = (List) restTemplate.exchange("https://slack.com/api/conversations.list", HttpMethod.GET, entity, Map.class).getBody().get("channels");
		for (Map<String, Object> channel : channels) {
			log.info(channel);
		}
	}

	@Test
	void getChannelThreadTs() {
		//
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");
		headers.set("Authorization", "Bearer " + slackToken);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(headers);

		RestTemplate restTemplate = new RestTemplate();
		log.info(restTemplate.exchange("https://slack.com/api/conversations.history?channel=C01TD73AZEF", HttpMethod.GET, entity, Map.class).getBody());
	}

	@Test
	void postSomeMessage() {

		Map<String, Object> body = new HashMap<>();
		body.put("text", "Hello slack-chatbot");
		body.put("reply_broadcast", true);
		body.put("thread_ts", 1617703059.000600);
		body.put("channel", "C01TD73AZEF");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json; charset=utf-8");
		headers.set("Authorization", "Bearer " + slackToken);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		log.info(restTemplate.exchange("https://slack.com/api/chat.postMessage", HttpMethod.POST, entity, Map.class).getBody());
	}
}
