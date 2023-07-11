package com.pds.serversentevent.controller;

import com.pds.serversentevent.domain.Post;
import com.pds.serversentevent.domain.PostRepository;
import com.pds.serversentevent.dto.AddPostDto;
import com.pds.serversentevent.dto.RetrievePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequiredArgsConstructor
public class PostController {

    Sinks.Many<RetrievePostDto> manyMulticastSink = Sinks.many().multicast().onBackpressureBuffer();
    private final PostRepository postRepository;

    @PostMapping("/api/post")
    public Mono<RetrievePostDto> addPost(@RequestBody AddPostDto addPostDto) {
        var postMono = postRepository.save(Post.builder().title(addPostDto.getTitle()).build()).log();
        return postMono.map(post -> new RetrievePostDto(post.getPostId(), post.getTitle()))
                .doOnNext(retrievePostDto -> manyMulticastSink.tryEmitNext(retrievePostDto));
    }

    @GetMapping("/api/post")
    public Flux<RetrievePostDto> getAllPosts() {
        Flux<Post> postListMono = postRepository.findAll();
        return postListMono.map(post -> new RetrievePostDto(post.getPostId(), post.getTitle()));
    }

    @GetMapping(value = "/stream/post", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<RetrievePostDto> streamAllPosts() {
        return manyMulticastSink.asFlux().log();
    }

}
