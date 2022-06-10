package com.devsuperior.movieflix.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.MovieDTO;
import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class MovieService {
	@Autowired
	private MovieRepository repository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;

	@Transactional(readOnly = true)
	public MovieDTO findById(Long id) {

		Optional<Movie> obj = repository.findById(id);
		Movie entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new MovieDTO(entity, entity.getGenre());
	}

	@Transactional(readOnly = true)
	public Page<MovieDTO> findAllByGenrePaged(Long genreId, Pageable pageable) {
		Genre genre = (genreId == 0) ? null : genreRepository.getOne(genreId);
		Page<Movie> list = repository.find(genre, pageable);	
		return list.map(x -> new MovieDTO(x));
	}

	public List<ReviewDTO> findReviewByMovieId(Long id) {
		List<Review> list = reviewRepository.findByMovieId(id);
		return list.stream().map(x -> new ReviewDTO(x)).toList();
	}
}
