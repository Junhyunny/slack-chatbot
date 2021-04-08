package io.junhyunny.chatbot.github;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Github {

	private final String owner;

	private final RestTemplate restTemplate;

	public Github(String owner) {
		this.owner = owner;
		this.restTemplate = new RestTemplate();
	}

	@SuppressWarnings({ "unchecked" })
	public boolean doCommitToday() throws IOException {

		boolean result = false;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(headers);

		List<Map<String, Object>> repoList = restTemplate.exchange("https://api.github.com/users/" + owner + "/repos", HttpMethod.GET, entity, List.class).getBody();
		for (Map<String, Object> repo : repoList) {

			String time = (String) repo.get("pushed_at");
			time = time.replace("T", " ");
			time = time.replace("Z", "");

			SimpleDateFormat seoulTz = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			seoulTz.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

			String currentTime = seoulTz.format(new Date()).toString();
			log.info(seoulTz.format(new Date()).toString());

			Timestamp lastPushTime = Timestamp.valueOf(time);
			Timestamp todayStartTime = Timestamp.valueOf(currentTime.substring(0, 10) + " 00:00:00");
			Timestamp todayEndTime = Timestamp.valueOf(currentTime.substring(0, 10) + " 23:59:59");

			log.info("lastPushTime: " + lastPushTime + ", todayStartTime: " + todayStartTime + ", todayEndTime: " + todayEndTime);

			if (todayStartTime.getTime() <= lastPushTime.getTime() && lastPushTime.getTime() <= todayEndTime.getTime()) {
				log.info("금일 push를 수행한 저장소가 존재합니다. " + repo.get("name"));
				result = true;
				break;
			}
		}

		return result;
	}
}
