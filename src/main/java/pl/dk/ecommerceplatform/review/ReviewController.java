package pl.dk.ecommerceplatform.review;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.review.dtos.CreateReviewDto;
import pl.dk.ecommerceplatform.review.dtos.SingleReviewDto;
import pl.dk.ecommerceplatform.security.SecurityService;

import java.net.URI;

@RestController
@RequestMapping("/reviews")
@AllArgsConstructor
class ReviewController {

    private final ReviewService reviewService;
    private final SecurityService securityService;

    @PostMapping("")
    @PreAuthorize(value = "hasAnyRole('ROLE_CUSTOMER')")
    public ResponseEntity<SingleReviewDto> createReview(@Valid @RequestBody CreateReviewDto createReviewDto) {
        Long idFromSecurityContext = securityService.getIdFromSecurityContextOrThrowException();
        SingleReviewDto review = reviewService.createReview(idFromSecurityContext, createReviewDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(review.id()).toUri();
        return ResponseEntity.created(uri).body(review);
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<SingleReviewDto> getReview(@PathVariable Long id) {
        SingleReviewDto reviewDto = reviewService.getReview(id);
        return ResponseEntity.ok(reviewDto);
    }
}
