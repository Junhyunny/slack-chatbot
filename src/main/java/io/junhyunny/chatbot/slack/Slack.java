package io.junhyunny.chatbot.slack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Slack {

	private final String slackToken;

	private final RestTemplate restTemplate;

	private final String[] messages = new String[] { "commit 할 시간이야.", "commit은 하고 노는거야?", "commit 안하면 밥도 먹지마!", "어린애처럼 굴래? 열심히 한다며?" };

	public Slack(String slackToken) {
		this.slackToken = slackToken;
		this.restTemplate = new RestTemplate();
	}

	private HttpHeaders getCommonHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json; charset=utf-8");
		headers.set("Authorization", "Bearer " + slackToken);
		return headers;
	}

	@SuppressWarnings({ "unchecked" })
	private String getChannelId(String channelName) {

		String result = null;

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(getCommonHeader());

		Map<String, List<Map<String, Object>>> response = restTemplate.exchange("https://slack.com/api/conversations.list", HttpMethod.GET, entity, Map.class).getBody();
		if (response == null) {
			return result;
		}

		List<Map<String, Object>> channels = response.get("channels");
		for (Map<String, Object> channel : channels) {
			if (channelName.equals(channel.get("name"))) {
				result = channel.get("id") + "";
				break;
			}
		}

		return result;
	}

	public void sendPushMessage(String channelName) {

		String channelId = getChannelId(channelName);
		if(channelId == null) {
			log.error("channel id를 구하지 못하였습니다.");
			return;
		}

		String message = messages[new Random().nextInt(999) % 4];

		Map<String, Object> body = new HashMap<>();
		body.put("text", message);
		body.put("reply_broadcast", true);
		body.put("channel", channelId);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(body, getCommonHeader());

		log.info("api call response message: " + restTemplate.exchange("https://slack.com/api/chat.postMessage", HttpMethod.POST, entity, Map.class).getBody());
	}
}
