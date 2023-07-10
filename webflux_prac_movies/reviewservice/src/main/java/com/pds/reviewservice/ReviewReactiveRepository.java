package com.pds.reviewservice;

import com.pds.reviewservice.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {
    Flux<Review> findReviewsByMovieInfoId(Long movieInfoId);
}
