package com.devsuperior.movieflix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ReviewServiceTests {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private AuthService authService;

    private Long existingMovieId;
    private Long nonExistingMovieId;
    private User user;
    private Movie movie;
    private Review review;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingMovieId = 1L;
        nonExistingMovieId = 2L;

        user = new User(1L, "Ana", "ana@gmail.com", "password");
        Genre genre = new Genre(1L, "Romance");
        movie = new Movie(existingMovieId, "The Movie", "Sub Title", 2026, "imgUrl", "synopsis");
        movie.setGenre(genre);
        review = new Review(1L, "Review text");
        review.setMovie(movie);
        review.setUser(user);

        reviewDTO = new ReviewDTO(review, user, movie);
    }

    @Test
    public void insertShouldReturnReviewDTOWhenMovieIdExists() {
        when(authService.authenticated()).thenReturn(user);
        when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewDTO result = reviewService.insert(reviewDTO);

        assertNotNull(result);
        assertEquals(review.getText(), result.getText());
        assertEquals(existingMovieId, result.getMovieId());
        assertEquals(user.getId(), result.getUser().getId());

        verify(authService, times(1)).authenticated();
        verify(movieRepository, times(1)).findById(existingMovieId);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void insertShouldThrowResourceNotFoundExceptionWhenMovieIdDoesNotExist() {
        when(authService.authenticated()).thenReturn(user);
        when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

        reviewDTO.setMovieId(nonExistingMovieId);

        assertThrows(ResourceNotFoundException.class, () -> {
            reviewService.insert(reviewDTO);
        });

        verify(authService, times(1)).authenticated();
        verify(movieRepository, times(1)).findById(nonExistingMovieId);
        verify(reviewRepository, times(0)).save(any(Review.class));
    }
}
