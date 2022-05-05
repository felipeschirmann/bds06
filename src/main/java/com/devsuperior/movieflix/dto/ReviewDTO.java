package com.devsuperior.movieflix.dto;

import java.io.Serializable;

public class ReviewDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String text;
	private Long MovieId;
	private Long UserId;

	public ReviewDTO() {

	}

	public ReviewDTO(Long id, String text, Long movieId, Long userId) {
		this.id = id;
		this.text = text;
		this.MovieId = movieId;
		this.UserId = userId;
	}

	public ReviewDTO(ReviewDTO entity) {
		id = getId();
		text = getText();
		MovieId = getMovieId();
		UserId = getUserId();
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

	public Long getMovieId() {
		return MovieId;
	}

	public void setMovieId(Long movieId) {
		MovieId = movieId;
	}

	public Long getUserId() {
		return UserId;
	}

	public void setUserId(Long userId) {
		UserId = userId;
	}

}
