package com.pds.infoservice.repository;

import com.pds.infoservice.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {
    @Autowired
    private MovieInfoRepository movieInfoRepository;

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
    void findAll() {
        var movieInfoFlux = movieInfoRepository.findAll().log();
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findById() {
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.findById("abc").log();
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals("The Dark Knight", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void save() {
        MovieInfo movieInfo = new MovieInfo(null,
                "범죄도시1",
                2019,
                List.of("마동석", "윤계상"),
                LocalDate.parse("2019-01-01"));
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(movieInfo).log();
        StepVerifier.create(movieInfoMono)
                .assertNext(movie -> {
                    assertEquals("범죄도시1", movie.getName());
                    assertNotNull(movie.getMovieInfoId());
                })
                .verifyComplete();
    }

    @Test
    void update() {
        // block means get actual Object, not Mono or Flux type
        MovieInfo movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setName("깔깔");
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(movieInfo).log();
        StepVerifier.create(movieInfoMono)
                .assertNext(info -> {
                    assertEquals("깔깔", info.getName());
                })
                .verifyComplete();
    }

    @Test
    void delete() {
        movieInfoRepository.deleteById("abc").block();
        Flux<MovieInfo> movieInfoFlux = movieInfoRepository.findAll();
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(1)
                .verifyComplete();
    }

}