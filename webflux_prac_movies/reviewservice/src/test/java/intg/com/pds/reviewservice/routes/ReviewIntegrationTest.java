package com.pds.reviewservice.routes;

import com.pds.reviewservice.ReviewReactiveRepository;
import com.pds.reviewservice.domain.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class ReviewIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReviewReactiveRepository reviewReactiveRepository;

    @BeforeEach
    void setUp() {
        var reivewList = List.of(
                new Review(null, 1L, "Good movie!", 9.0),
                new Review(null, 2L, "Nice movie!", 9.0),
                new Review(null, 1L, "Excellent movie!", 8.0)
        );
        reviewReactiveRepository.saveAll(reivewList).blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewReactiveRepository.deleteAll().block();
    }

    @Test
    void addReview() {
        Review review = new Review(null, 1L, "Hello movie", 5.5);
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
