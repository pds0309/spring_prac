package com.pds.infoservice.controller;

import com.pds.infoservice.domain.MovieInfo;
import com.pds.infoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MovieInfoControllerTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        var movieInfos =
                List.of(
                        new MovieInfo(null,
                                "Batman Begins",
                                2005,
                                List.of("Bale", "Cane"),
                                LocalDate.parse("2005-06-15")),
                        new MovieInfo("abc",
                                "The Dark Knight",
                                2008,
                                List.of("Bale", "HeathLedger"),
                                LocalDate.parse("2008-07-18"))
                );
        movieInfoRepository.saveAll(movieInfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null,
                "Batman Begins",
                2005,
                List.of("Bale", "Cane"),
                LocalDate.parse("2005-06-15"));
        webTestClient.post().uri("/v1/movieinfos")
                .bodyValue(movieInfo)
                .exchange() // call
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertNotNull(responseBody.getMovieInfoId());
                });
    }

    @Test
    void getAllMovieInfos() {
        webTestClient.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(2);
    }

    @Test
    void getMovieInfoById() {
        webTestClient.get()
                .uri("/v1/movieinfos/{id}", "abc")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.movieInfoId").isEqualTo("abc")
                .jsonPath("$.name").isEqualTo("The Dark Knight");
    }

    @Test
    void updateMovieInfo() {
        var updateMovieInfo = new MovieInfo(null,
                "HELLO",
                2005,
                List.of("Bale", "Cane"),
                LocalDate.parse("2005-06-15"));
        webTestClient.put()
                .uri("/v1/movieinfos/{id}", "abc")
                .bodyValue(updateMovieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    assertEquals("HELLO", Objects.requireNonNull(movieInfoEntityExchangeResult.getResponseBody()).getName());
                });
    }

    @Test
    void deleteMovieInfo() {
        webTestClient.delete()
                .uri("/v1/movieinfos/{id}", "abc")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

}