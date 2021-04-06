package io.junhyunny;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class GithubApiTest {

	@SuppressWarnings({ "unchecked" })
	@Test
	void test() throws IOException {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<Map<String, Object>> entity = new HttpEntity<Map<String, Object>>(headers);

		RestTemplate restTemplate = new RestTemplate();
		List<Map<String, Object>> repoList = restTemplate.exchange("https://api.github.com/users/junhyunny/repos", HttpMethod.GET, entity, List.class).getBody();
		for (Map<String, Object> repo : repoList) {
			log.info("repo url: " + repo.get("name"));
			log.info("pushed_at: " + repo.get("pushed_at"));
			String time = (String) repo.get("pushed_at");
			time = time.replace("T", " ");
			time = time.replace("Z", "");
			log.info(Timestamp.valueOf(time));
		}
	}
}
