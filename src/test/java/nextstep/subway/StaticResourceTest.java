package nextstep.subway;

import static nextstep.subway.WebMvcConfig.PREFIX_STATIC_RESOURCES;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.CacheControl;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class StaticResourceTest {

    private static Logger logger = LoggerFactory.getLogger(StaticResourceTest.class);

    @Autowired
    private WebTestClient client;
    @LocalServerPort
    private int port;

    @Test
    void test_cache_control() {
        String uri = PREFIX_STATIC_RESOURCES + "/" + "js/main.js";
        EntityExchangeResult<String> response = client
            .get()
            .uri(uri)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .cacheControl(CacheControl.maxAge(60 * 60 * 24 * 365, TimeUnit.SECONDS).cachePrivate())
            .expectBody(String.class)
            .returnResult();

        logger.debug("body - {}", response.getResponseBody());

        String etag = response.getResponseHeaders()
            .getETag();

        client
            .get()
            .uri(uri)
            .header("If-None-Match", etag)
            .exchange()
            .expectStatus()
            .isNotModified();
    }
}
