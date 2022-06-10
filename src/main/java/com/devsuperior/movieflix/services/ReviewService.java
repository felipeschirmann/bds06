package com.devsuperior.movieflix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository repository;
	
	@Autowired
	private MovieRepository movieRepository;	
	
	@Autowired
	private AuthService authService;
	
	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {		
		Review entity = new Review();
		User user = authService.authenticated();
		try { 	
			Movie movie = movieRepository.findById(dto.getMovieId()).orElseThrow();
			
			entity.setMovie(movie);
			entity.setUser(user);
			entity.setText(dto.getText());
			entity = repository.save(entity);
			return new ReviewDTO(entity, user, movie);
		}catch (Exception e) {
			throw new ResourceNotFoundException("Movie Not Found" + dto.getMovieId());
		}
	}
}
