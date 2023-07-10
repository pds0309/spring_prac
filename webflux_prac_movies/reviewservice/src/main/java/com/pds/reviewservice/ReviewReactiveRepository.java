package com.pds.reviewservice;

import com.pds.reviewservice.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {
}
