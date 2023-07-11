package com.pds.serversentevent;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
public class PracSseController {

    Sinks.Many<Integer> manyReplayIntegerSink = Sinks.many().replay().latest();

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(1)).log();
    }

    @PostMapping("/sink/many/replay")
    public Mono<Integer> publishSinkManyReplay(@RequestBody Integer integer) {
        return Mono.just(integer).doOnNext(itg -> manyReplayIntegerSink.tryEmitNext(itg));
    }

    @GetMapping(value = "/sink/many/replay", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Integer> subscribeSinkManyReplay() {
        return manyReplayIntegerSink.asFlux();
    }

}
