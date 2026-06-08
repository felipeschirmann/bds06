package com.devsuperior.movieflix.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.movieflix.entities.Role;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

@ExtendWith(SpringExtension.class)
public class AuthServiceTests {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User memberUser;
    private User visitorUser;

    @BeforeEach
    void setUp() throws Exception {
        Role memberRole = new Role(1L, "ROLE_MEMBER");
        Role visitorRole = new Role(2L, "ROLE_VISITOR");

        memberUser = new User(1L, "Member", "member@gmail.com", "password");
        memberUser.getRoles().add(memberRole);

        visitorUser = new User(2L, "Visitor", "visitor@gmail.com", "password");
        visitorUser.getRoles().add(visitorRole);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void authenticatedShouldReturnUserWhenAuthenticatedUserExists() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("member@gmail.com");
        when(userRepository.findByEmail("member@gmail.com")).thenReturn(memberUser);

        User result = authService.authenticated();

        assertEquals(memberUser, result);
    }

    @Test
    public void authenticatedShouldThrowUnauthorizedExceptionWhenAuthenticationIsNull() {
        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(UnauthorizedException.class, () -> {
            authService.authenticated();
        });
    }

    @Test
    public void authenticatedShouldReturnNullWhenUserDoesNotExist() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("nonexistent@gmail.com");
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        User result = authService.authenticated();
        org.junit.jupiter.api.Assertions.assertNull(result);
    }

    @Test
    public void authenticatedShouldThrowUnauthorizedExceptionWhenExceptionOccurs() {
        when(securityContext.getAuthentication()).thenThrow(new RuntimeException("Security context exception"));

        assertThrows(UnauthorizedException.class, () -> {
            authService.authenticated();
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenSelfAccess() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("visitor@gmail.com");
        when(userRepository.findByEmail("visitor@gmail.com")).thenReturn(visitorUser);

        assertDoesNotThrow(() -> {
            authService.validateSelfOrAdmin(2L); // 2L matches visitorUser's ID
        });
    }

    @Test
    public void validateSelfOrAdminShouldDoNothingWhenMemberAccessOthers() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("member@gmail.com");
        when(userRepository.findByEmail("member@gmail.com")).thenReturn(memberUser);

        assertDoesNotThrow(() -> {
            authService.validateSelfOrAdmin(2L); // 2L is another user's ID, but memberUser has ROLE_MEMBER
        });
    }

    @Test
    public void validateSelfOrAdminShouldThrowForbiddenExceptionWhenVisitorAccessOthers() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("visitor@gmail.com");
        when(userRepository.findByEmail("visitor@gmail.com")).thenReturn(visitorUser);

        assertThrows(ForbiddenException.class, () -> {
            authService.validateSelfOrAdmin(1L); // 1L is another user's ID, and visitorUser only has ROLE_VISITOR
        });
    }
}
