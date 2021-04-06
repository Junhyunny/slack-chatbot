package io.junhyunny;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class GithubApiTest {

	@Value("${client-token.github}")
	private String githubToken;

	@Test
	void test() throws IOException {
		GitHub github = new GitHubBuilder().withOAuthToken(githubToken).build();
		github.checkApiUrlValidity();
		log.info(github.getRepository("Junhyunny/junhyunny.github.io"));
	}

}
