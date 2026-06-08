package com.devsuperior.movieflix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private Long existingId;
    private Long nonExistingId;
    private Movie movie;
    private Genre genre;
    private Pageable pageable;
    private Page<Movie> page;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;

        genre = new Genre(1L, "Romance");
        movie = new Movie(existingId, "The Movie", "Sub Title", 2026, "imgUrl", "synopsis");
        movie.setGenre(genre);

        pageable = PageRequest.of(0, 10);
        page = new PageImpl<>(List.of(movie));
    }

    @Test
    public void findByIdShouldReturnMovieDTOWhenIdExists() {
        when(movieRepository.findById(existingId)).thenReturn(Optional.of(movie));

        MovieDTO result = movieService.findById(existingId);

        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(movie.getTitle(), result.getTitle());
        assertEquals(genre.getId(), result.getGenre().getId());

        verify(movieRepository, times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            movieService.findById(nonExistingId);
        });

        verify(movieRepository, times(1)).findById(nonExistingId);
    }

    @Test
    public void findAllByGenrePagedShouldReturnPagedMovieDTOWhenGenreIdIsZero() {
        when(movieRepository.find(null, pageable)).thenReturn(page);

        Page<MovieDTO> result = movieService.findAllByGenrePaged(0L, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(movie.getTitle(), result.getContent().get(0).getTitle());

        verify(movieRepository, times(1)).find(null, pageable);
    }

    @Test
    public void findAllByGenrePagedShouldReturnPagedMovieDTOWhenGenreIdIsNotZero() {
        when(genreRepository.getReferenceById(1L)).thenReturn(genre);
        when(movieRepository.find(genre, pageable)).thenReturn(page);

        Page<MovieDTO> result = movieService.findAllByGenrePaged(1L, pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(movie.getTitle(), result.getContent().get(0).getTitle());

        verify(genreRepository, times(1)).getReferenceById(1L);
        verify(movieRepository, times(1)).find(genre, pageable);
    }

    @Test
    public void findReviewByMovieIdShouldReturnListOfReviewDTOs() {
        User user = new User(1L, "User Name", "user@gmail.com", "pass");
        Review review = new Review(1L, "Review text");
        review.setMovie(movie);
        review.setUser(user);
        
        when(reviewRepository.findByMovieId(existingId)).thenReturn(List.of(review));

        List<ReviewDTO> result = movieService.findReviewByMovieId(existingId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(review.getText(), result.get(0).getText());
        assertEquals(user.getName(), result.get(0).getUser().getName());

        verify(reviewRepository, times(1)).findByMovieId(existingId);
    }
}
