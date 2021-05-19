package io.junhyunny.chatbot.slack;

import io.junhyunny.entity.GeneuinMember;
import io.junhyunny.utils.JsonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class Slack {


    private final RestTemplate restTemplate;

    private final HttpHeaders headers;

    private final String[] messages = new String[]{"commit 할 시간이에요.", "오늘 commit 하고 놀고 있는거에요?", "commit 안하면 밥도 먹지마세요.", "미래를 위해 공부해야죠. 어린애처럼 굴래요?"};

    public Slack(String slackToken) {
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Authorization", "Bearer " + slackToken);
    }

    @SuppressWarnings({"unchecked"})
    public String getChannelId(String channelName) {

        String result = null;

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        Map<String, List<Map<String, Object>>> response = restTemplate.exchange("https://slack.com/api/conversations.list", HttpMethod.GET, entity, Map.class).getBody();
        if (response == null) {
            return null;
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

    public Map<String, GeneuinMember> getMembersInChannel(String channelId) {

        Map<String, GeneuinMember> result = new HashMap<>();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);

        StringBuffer url = new StringBuffer("https://slack.com/api/conversations.members");
        url.append("?channel=").append(channelId);

        Map<String, List<Map<String, Object>>> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, Map.class).getBody();
        if (response == null) {
            return null;
        }

        url.delete(0, url.length());
        url.append("https://slack.com/api/users.info");
        url.append("?user=");

        List<String> memberKeys = (ArrayList) response.get("members");
        for (String memberKey : memberKeys) {
            Map<String, Object> mapMember = restTemplate.exchange(url + memberKey, HttpMethod.GET, entity, Map.class).getBody();
            result.put(memberKey, JsonUtils.fromJson(JsonUtils.toJson(mapMember.get("user")), GeneuinMember.class));
        }

        return result;
    }

    public void sendPushMessage(String channelName) {

        String channelId = getChannelId(channelName);
        if (channelId == null) {
            log.error("channel id를 구하지 못하였습니다.");
            return;
        }

        String message = messages[new Random().nextInt(999) % 4];

        Map<String, Object> body = new HashMap<>();
        body.put("text", message);
        body.put("reply_broadcast", true);
        body.put("channel", channelId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(body, headers);

        log.info("api call response message: " + restTemplate.exchange("https://slack.com/api/chat.postMessage", HttpMethod.POST, entity, Map.class).getBody());
    }
}
