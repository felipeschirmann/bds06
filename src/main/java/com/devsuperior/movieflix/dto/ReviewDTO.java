package com.devsuperior.movieflix.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;

public class ReviewDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@NotBlank
	private String text;
	private Long movieId;
	private UserDTO user;

	public ReviewDTO() {

	}

	public ReviewDTO(Review entity, User user, Movie movie) {
		id = entity.getId();
		text = entity.getText();
		movieId = movie.getId();
		this.setUser(new UserDTO(user));
	}
	
	public ReviewDTO(Review review) {
		id = review.getId();
		text = review.getText();
		movieId = review.getMovie().getId();
		this.setUser(new UserDTO(review.getUser()));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public Long getMovieId() {
		return movieId;
	}

	public void setMovieId(Long movieId) {
		this.movieId = movieId;
	}
}
