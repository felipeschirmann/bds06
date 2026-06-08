package com.devsuperior.movieflix.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class EntityCoverageTests {

    @Test
    public void movieCoverage() {
        Genre genre1 = new Genre(1L, "Romance");
        Genre genre2 = new Genre(2L, "Comedy");
        Movie m1 = new Movie(1L, "T1", "ST1", 2020, "img1", "syn1");
        m1.setGenre(genre1);
        Movie m2 = new Movie(1L, "T1", "ST1", 2020, "img1", "syn1");
        m2.setGenre(genre1);
        Movie m3 = new Movie(2L, "T2", "ST2", 2021, "img2", "syn2");
        m3.setGenre(genre2);
        Movie mNull = new Movie();

        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertNotEquals(m1, null);
        assertNotEquals(m1, new Object());
        assertEquals(m1.hashCode(), m2.hashCode());

        mNull.setId(5L);
        mNull.setTitle("T");
        mNull.setSubTitle("ST");
        mNull.setYear(2022);
        mNull.setImgUrl("img");
        mNull.setSynopsis("syn");
        mNull.setGenre(genre2);

        assertEquals(5L, mNull.getId());
        assertEquals("T", mNull.getTitle());
        assertEquals("ST", mNull.getSubTitle());
        assertEquals(2022, mNull.getYear());
        assertEquals("img", mNull.getImgUrl());
        assertEquals("syn", mNull.getSynopsis());
        assertEquals(genre2, mNull.getGenre());
        assertEquals(0, mNull.getReviews().size());
    }

    @Test
    public void userCoverage() {
        User u1 = new User(1L, "Bob", "bob@gmail.com", "pass");
        User u2 = new User(1L, "Bob", "bob@gmail.com", "pass");
        User u3 = new User(2L, "Alice", "alice@gmail.com", "pass");
        
        assertEquals(u1, u2);
        assertNotEquals(u1, u3);
        assertNotEquals(u1, null);
        assertNotEquals(u1, new Object());
        assertEquals(u1.hashCode(), u2.hashCode());

        Role r = new Role(1L, "ROLE_MEMBER");
        u1.getRoles().add(r);
        assertTrue(u1.hasRole("ROLE_MEMBER"));
        assertFalse(u1.hasRole("ROLE_ADMIN"));

        Collection<? extends GrantedAuthority> authorities = u1.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_MEMBER", authorities.iterator().next().getAuthority());

        assertEquals("bob@gmail.com", u1.getUsername());
        assertTrue(u1.isAccountNonExpired());
        assertTrue(u1.isAccountNonLocked());
        assertTrue(u1.isCredentialsNonExpired());
        assertTrue(u1.isEnabled());
    }

    @Test
    public void roleCoverage() {
        Role r1 = new Role(1L, "ROLE_MEMBER");
        Role r2 = new Role(1L, "ROLE_MEMBER");
        Role r3 = new Role(2L, "ROLE_VISITOR");
        Role rNull = new Role();

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertNotEquals(r1, null);
        assertNotEquals(r1, new Object());
        assertEquals(r1.hashCode(), r2.hashCode());

        rNull.setId(5L);
        rNull.setAuthority("ROLE_TEST");
        assertEquals(5L, rNull.getId());
        assertEquals("ROLE_TEST", rNull.getAuthority());
    }

    @Test
    public void genreCoverage() {
        Genre g1 = new Genre(1L, "Romance");
        Genre g2 = new Genre(1L, "Romance");
        Genre g3 = new Genre(2L, "Comedy");
        Genre gNull = new Genre();

        assertEquals(g1, g2);
        assertNotEquals(g1, g3);
        assertNotEquals(g1, null);
        assertNotEquals(g1, new Object());
        assertEquals(g1.hashCode(), g2.hashCode());

        gNull.setId(5L);
        gNull.setName("Action");
        assertEquals(5L, gNull.getId());
        assertEquals("Action", gNull.getName());
        assertEquals(0, gNull.getMovies().size());
    }

    @Test
    public void reviewCoverage() {
        Movie m = new Movie(1L, "T", "ST", 2026, "img", "syn");
        User u = new User(1L, "Bob", "bob@gmail.com", "pass");
        Review r1 = new Review(1L, "Good");
        r1.setMovie(m);
        r1.setUser(u);
        Review r2 = new Review(1L, "Good");
        r2.setMovie(m);
        r2.setUser(u);
        Review r3 = new Review(2L, "Bad");
        r3.setMovie(m);
        r3.setUser(u);
        Review rNull = new Review();

        assertEquals(r1, r2);
        assertNotEquals(r1, r3);
        assertNotEquals(r1, null);
        assertNotEquals(r1, new Object());
        assertEquals(r1.hashCode(), r2.hashCode());

        rNull.setId(5L);
        rNull.setText("Nice");
        rNull.setMovie(m);
        rNull.setUser(u);

        assertEquals(5L, rNull.getId());
        assertEquals("Nice", rNull.getText());
        assertEquals(m, rNull.getMovie());
        assertEquals(u, rNull.getUser());
    }
}
