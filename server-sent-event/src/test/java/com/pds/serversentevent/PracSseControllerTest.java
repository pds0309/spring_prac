package com.pds.serversentevent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest(controllers = PracSseController.class)
@AutoConfigureWebTestClient
class PracSseControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void stream() {
        var flux = webTestClient
                .get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();
        StepVerifier.create(flux)
                .expectNext(0L, 1L, 2L, 3L)
                .thenCancel()
                .verify();
    }

    @Test
    void publishSinkManyReplay() {
        webTestClient
                .post()
                .uri("/sink/many/replay")
                .bodyValue(1)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Integer.class)
                .consumeWith(integerEntityExchangeResult ->
                        assertEquals(1, integerEntityExchangeResult.getResponseBody()));
    }

    @Test
    void subscribeSinkManyReplay() {
        publishSinkManyReplay();
        var flux = webTestClient
                .get()
                .uri("/sink/many/replay")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();
        StepVerifier.create(flux)
                .assertNext(integer -> {
                    assertEquals(1, integer);
                })
                .thenCancel()
                .verify();
    }
}
