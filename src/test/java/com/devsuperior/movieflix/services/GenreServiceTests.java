package com.devsuperior.movieflix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;

@ExtendWith(SpringExtension.class)
public class GenreServiceTests {

    @InjectMocks
    private GenreService genreService;

    @Mock
    private GenreRepository genreRepository;

    private Genre genre;

    @BeforeEach
    void setUp() throws Exception {
        genre = new Genre(1L, "Romance");
    }

    @Test
    public void findAllShouldReturnListOfGenreDTO() {
        when(genreRepository.findAll()).thenReturn(List.of(genre));

        List<GenreDTO> result = genreService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(genre.getName(), result.get(0).getName());

        verify(genreRepository, times(1)).findAll();
    }
}
