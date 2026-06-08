package com.devsuperior.movieflix.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    private Long existingId;
    private Long nonExistingId;
    private String existingEmail;
    private String nonExistingEmail;
    private User user;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        existingEmail = "ana@gmail.com";
        nonExistingEmail = "nobody@gmail.com";

        user = new User(existingId, "Ana", existingEmail, "password");
    }

    @Test
    public void findByIdShouldReturnUserDTOWhenIdExists() {
        doNothing().when(authService).validateSelfOrAdmin(existingId);
        when(userRepository.findById(existingId)).thenReturn(Optional.of(user));

        UserDTO result = userService.findById(existingId);

        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        verify(authService, times(1)).validateSelfOrAdmin(existingId);
        verify(userRepository, times(1)).findById(existingId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        doNothing().when(authService).validateSelfOrAdmin(nonExistingId);
        when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findById(nonExistingId);
        });

        verify(authService, times(1)).validateSelfOrAdmin(nonExistingId);
        verify(userRepository, times(1)).findById(nonExistingId);
    }

    @Test
    public void findByIdShouldThrowForbiddenExceptionWhenAuthServiceThrowsForbiddenException() {
        doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(nonExistingId);

        assertThrows(ForbiddenException.class, () -> {
            userService.findById(nonExistingId);
        });

        verify(authService, times(1)).validateSelfOrAdmin(nonExistingId);
        verify(userRepository, times(0)).findById(any());
    }

    @Test
    public void loadUserByUsernameShouldReturnUserDetailsWhenUsernameExists() {
        when(userRepository.findByEmail(existingEmail)).thenReturn(user);

        UserDetails result = userService.loadUserByUsername(existingEmail);

        assertNotNull(result);
        assertEquals(existingEmail, result.getUsername());

        verify(userRepository, times(1)).findByEmail(existingEmail);
    }

    @Test
    public void loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUsernameDoesNotExist() {
        when(userRepository.findByEmail(nonExistingEmail)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(nonExistingEmail);
        });

        verify(userRepository, times(1)).findByEmail(nonExistingEmail);
    }

    @Test
    public void profileForCurrentUserShouldReturnUserDTO() {
        when(authService.authenticated()).thenReturn(user);

        UserDTO result = userService.profileForCurrentUser();

        assertNotNull(result);
        assertEquals(existingId, result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());

        verify(authService, times(1)).authenticated();
    }
}
