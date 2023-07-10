package com.pds.reviewservice.routes;

import com.pds.reviewservice.ReviewReactiveRepository;
import com.pds.reviewservice.domain.Review;
import com.pds.reviewservice.handler.ReviewHandler;
import com.pds.reviewservice.router.ReviewRouter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class})
@AutoConfigureWebTestClient
class ReviewUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ReviewReactiveRepository reviewReactiveRepository;

    @Test
    void addReview() {
        var review = new Review(null, 1L, "hello", 5.5);
        when(reviewReactiveRepository.save(isA(Review.class)))
                .thenReturn(Mono.just(new Review("abc", 1L, "hello", 5.5)));
        webTestClient
                .post()
                .uri("/v1/reviews")
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    assertNotNull(Objects.requireNonNull(reviewEntityExchangeResult.getResponseBody()).getReviewId());
                });
    }
}
