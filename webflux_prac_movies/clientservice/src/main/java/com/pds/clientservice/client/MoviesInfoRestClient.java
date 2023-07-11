package com.pds.clientservice.client;

import com.pds.clientservice.domain.MovieInfo;
import com.pds.clientservice.exception.MoviesInfoClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
public class MoviesInfoRestClient {
    private final WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    public MoviesInfoRestClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<MovieInfo> retrieveMovieInfo(String movieId) {
        var url = moviesInfoUrl.concat("/{id}");
        return webClient.get()
                .uri(url, movieId)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        return Mono.error(
                                new MoviesInfoClientException(
                                        "There is no Movie Info by passed id : " + movieId,
                                        clientResponse.statusCode().value()));
                    }
                    return clientResponse.bodyToMono(String.class)
                            .flatMap(s -> Mono.error(
                                    new MoviesInfoClientException(s, clientResponse.statusCode().value())));
                })
                .bodyToMono(MovieInfo.class)
                .retryWhen(Retry
                        .fixedDelay(2, Duration.ofSeconds(1))
                        .filter(ex -> !(ex instanceof MoviesInfoClientException))
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) ->
                                Exceptions.propagate(retrySignal.failure())))
                .log();
    }
}
