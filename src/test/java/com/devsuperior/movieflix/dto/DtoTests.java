package com.devsuperior.movieflix.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;

public class DtoTests {

    @Test
    public void userDTOAccessorsAndConstructors() {
        UserDTO dto1 = new UserDTO();
        assertNull(dto1.getId());
        
        UserDTO dto2 = new UserDTO(1L, "Bob", "bob@gmail.com");
        assertEquals(1L, dto2.getId());
        assertEquals("Bob", dto2.getName());
        assertEquals("bob@gmail.com", dto2.getEmail());

        dto1.setId(2L);
        dto1.setName("Alice");
        dto1.setEmail("alice@gmail.com");
        
        assertEquals(2L, dto1.getId());
        assertEquals("Alice", dto1.getName());
        assertEquals("alice@gmail.com", dto1.getEmail());

        User entity = new User(3L, "Charlie", "charlie@gmail.com", "pass");
        UserDTO dto3 = new UserDTO(entity);
        assertEquals(3L, dto3.getId());
        assertEquals("Charlie", dto3.getName());
    }

    @Test
    public void genreDTOAccessorsAndConstructors() {
        GenreDTO dto1 = new GenreDTO();
        assertNull(dto1.getId());

        GenreDTO dto2 = new GenreDTO(1L, "Romance");
        assertEquals(1L, dto2.getId());
        assertEquals("Romance", dto2.getName());

        dto1.setId(2L);
        dto1.setName("Comedy");

        assertEquals(2L, dto1.getId());
        assertEquals("Comedy", dto1.getName());

        Genre entity = new Genre(3L, "Action");
        GenreDTO dto3 = new GenreDTO(entity);
        assertEquals(3L, dto3.getId());
        assertEquals("Action", dto3.getName());
    }

    @Test
    public void movieDTOAccessorsAndConstructors() {
        MovieDTO dto1 = new MovieDTO();
        assertNull(dto1.getId());

        MovieDTO dto2 = new MovieDTO(1L, "Title", "SubTitle", 2026, "imgUrl", "synopsis");
        assertEquals(1L, dto2.getId());
        assertEquals("Title", dto2.getTitle());
        assertEquals("SubTitle", dto2.getSubTitle());
        assertEquals(2026, dto2.getYear());
        assertEquals("imgUrl", dto2.getImgUrl());
        assertEquals("synopsis", dto2.getSynopsis());

        GenreDTO genreDTO = new GenreDTO(1L, "Romance");
        MovieDTO dto3 = new MovieDTO(1L, "Title", "SubTitle", 2026, "imgUrl", "synopsis", genreDTO);
        assertEquals(genreDTO, dto3.getGenre());

        dto1.setId(2L);
        dto1.setTitle("T");
        dto1.setSubTitle("ST");
        dto1.setYear(2000);
        dto1.setImgUrl("img");
        dto1.setSynopsis("syn");
        dto1.setGenre(genreDTO);

        assertEquals(2L, dto1.getId());
        assertEquals("T", dto1.getTitle());
        assertEquals("ST", dto1.getSubTitle());
        assertEquals(2000, dto1.getYear());
        assertEquals("img", dto1.getImgUrl());
        assertEquals("syn", dto1.getSynopsis());
        assertEquals(genreDTO, dto1.getGenre());

        Genre genreEntity = new Genre(1L, "Romance");
        Movie movieEntity = new Movie(3L, "Title3", "SubTitle3", 2026, "img3", "syn3");
        movieEntity.setGenre(genreEntity);
        MovieDTO dto4 = new MovieDTO(movieEntity);
        assertEquals(3L, dto4.getId());
        assertNotNull(dto4.getGenre());
        assertEquals(1L, dto4.getGenre().getId());
    }

    @Test
    public void reviewDTOAccessorsAndConstructors() {
        ReviewDTO dto1 = new ReviewDTO();
        assertNull(dto1.getId());

        dto1.setId(1L);
        dto1.setText("nice");
        dto1.setMovieId(2L);
        UserDTO userDTO = new UserDTO(1L, "User", "user@gmail.com");
        dto1.setUser(userDTO);

        assertEquals(1L, dto1.getId());
        assertEquals("nice", dto1.getText());
        assertEquals(2L, dto1.getMovieId());
        assertEquals(userDTO, dto1.getUser());

        User userEntity = new User(1L, "User", "user@gmail.com", "pass");
        Genre genreEntity = new Genre(1L, "Romance");
        Movie movieEntity = new Movie(2L, "Movie", "Sub", 2026, "img", "syn");
        movieEntity.setGenre(genreEntity);
        Review reviewEntity = new Review(3L, "cool");
        reviewEntity.setMovie(movieEntity);
        reviewEntity.setUser(userEntity);

        ReviewDTO dto2 = new ReviewDTO(reviewEntity, userEntity, movieEntity);
        assertEquals(3L, dto2.getId());
        assertEquals("cool", dto2.getText());
        assertEquals(2L, dto2.getMovieId());
        assertNotNull(dto2.getUser());
        assertEquals(1L, dto2.getUser().getId());

        ReviewDTO dto3 = new ReviewDTO(reviewEntity);
        assertEquals(3L, dto3.getId());
        assertEquals("cool", dto3.getText());
        assertEquals(2L, dto3.getMovieId());
        assertNotNull(dto3.getUser());
    }
}
