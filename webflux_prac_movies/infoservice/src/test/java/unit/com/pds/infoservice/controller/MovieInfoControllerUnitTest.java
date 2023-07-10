package com.pds.infoservice.controller;

import com.pds.infoservice.domain.MovieInfo;
import com.pds.infoservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MovieInfoController.class)
@AutoConfigureWebTestClient
class MovieInfoControllerUnitTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private MovieInfoService movieInfoService;

    @Test
    void addMovieInfo() {
        var movieInfo = new MovieInfo(null,
                "Batman Begins",
                2005,
                List.of("Bale", "Cane"),
                LocalDate.parse("2005-06-15"));
        when(movieInfoService.addMovieInfo(isA(MovieInfo.class))).thenReturn(Mono.just(new MovieInfo("ABCD",
                "Batman Begins",
                2005,
                List.of("Bale", "Cane"),
                LocalDate.parse("2005-06-15"))));
        webTestClient.post().uri("/v1/movieinfos")
                .bodyValue(movieInfo)
                .exchange() // call
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertEquals("ABCD", responseBody.getMovieInfoId());
                });
    }

    @Test
    void getAllMovieInfos() {
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
                                LocalDate.parse("2008-07-18")));
        when(movieInfoService.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));
        webTestClient.get()
                .uri("/v1/movieinfos")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(2);
    }

}
