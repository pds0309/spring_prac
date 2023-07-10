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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
                new Review("abc", 1L, "Excellent movie!", 8.0)
        );
        reviewReactiveRepository.saveAll(reivewList).blockLast();
    }

    @AfterEach
    void tearDown() {
        reviewReactiveRepository.deleteAll().block();
    }

    @Test
    void getReviews() {
        webTestClient
                .get()
                .uri("/v1/reviews")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .value(reviews -> {
                    assertEquals(3, reviews.size());
                });
    }

    @Test
    void getReviewsByMovieInfoId() {
        webTestClient
                .get()
                .uri(uriBuilder -> {
                    return uriBuilder.path("/v1/reviews")
                            .queryParam("movieInfoId", "1")
                            .build();
                })
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .value(reviewList -> {
                    System.out.println("reviewList : " + reviewList);
                    assertEquals(2, reviewList.size());
                });
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

    @Test
    void updateReview() {
        String requestReviewId = "abc";
        Review review = new Review(null, 1L, "Hello movie", 5.5);
        webTestClient
                .put()
                .uri("/v1/reviews/{id}", requestReviewId)
                .bodyValue(review)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    assertEquals("Hello movie", Objects.requireNonNull(reviewEntityExchangeResult.getResponseBody()).getComment());
                });
    }

    @Test
    void deleteReview() {
        String requestReviewId = "abc";
        webTestClient
                .delete()
                .uri("/v1/reviews/{id}", requestReviewId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    void deleteReview_not_exists() {
        String requestReviewId = "a121bc";
        webTestClient
                .delete()
                .uri("/v1/reviews/{id}", requestReviewId)
                .exchange()
                .expectStatus()
                .isNoContent();
    }
}
