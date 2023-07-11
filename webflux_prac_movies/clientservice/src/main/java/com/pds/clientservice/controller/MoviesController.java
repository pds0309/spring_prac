package com.pds.clientservice.controller;

import com.pds.clientservice.client.MoviesInfoRestClient;
import com.pds.clientservice.client.ReviewRestClient;
import com.pds.clientservice.domain.Movie;
import com.pds.clientservice.domain.Review;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MoviesController {
    /*
     * WebClient:
     *  - reactive non-blocking rest client
     *  - allow application to interact with other services in a non-blocking
     */

    private final MoviesInfoRestClient moviesInfoRestClient;
    private final ReviewRestClient reviewRestClient;

    public MoviesController(MoviesInfoRestClient moviesInfoRestClient, ReviewRestClient reviewRestClient) {
        this.moviesInfoRestClient = moviesInfoRestClient;
        this.reviewRestClient = reviewRestClient;
    }

    @GetMapping("/{id}")
    public Mono<Movie> retrieveMovieById(@PathVariable("id") String movieId) {
        return moviesInfoRestClient.retrieveMovieInfo(movieId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsListMono = reviewRestClient.retrieveReviews(movieId)
                            .collectList();
                    return reviewsListMono.map(reviews -> new Movie(movieInfo, reviews));
                });
    }
}
