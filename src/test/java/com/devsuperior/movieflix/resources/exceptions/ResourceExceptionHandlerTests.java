package com.devsuperior.movieflix.resources.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

public class ResourceExceptionHandlerTests {

    private ResourceExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new ResourceExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/test-uri");
    }

    @Test
    public void entityNotFoundShouldReturnNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found message");
        ResponseEntity<StandardError> response = handler.entityNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Resource not found", response.getBody().getError());
        assertEquals("Not found message", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void databaseShouldReturnBadRequest() {
        DatabaseException ex = new DatabaseException("Database message");
        ResponseEntity<StandardError> response = handler.database(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Database exception", response.getBody().getError());
        assertEquals("Database message", response.getBody().getMessage());
        assertEquals("/test-uri", response.getBody().getPath());
    }

    @Test
    public void forbiddenShouldReturnForbidden() {
        ForbiddenException ex = new ForbiddenException("Forbidden message");
        ResponseEntity<OAuthCustomError> response = handler.forbidden(ex, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Forbidden", response.getBody().getError());
        assertEquals("Forbidden message", response.getBody().getErrorDescription());
    }

    @Test
    public void unauthorizedShouldReturnUnauthorized() {
        UnauthorizedException ex = new UnauthorizedException("Unauthorized message");
        ResponseEntity<OAuthCustomError> response = handler.unauthorized(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unauthorized", response.getBody().getError());
        assertEquals("Unauthorized message", response.getBody().getErrorDescription());
    }

    @Test
    public void validationShouldReturnUnprocessableEntity() throws NoSuchMethodException {
        java.lang.reflect.Method method = this.getClass().getMethod("validationShouldReturnUnprocessableEntity");
        org.springframework.core.MethodParameter parameter = new org.springframework.core.MethodParameter(method, -1);
        
        org.springframework.validation.BeanPropertyBindingResult bindingResult = 
            new org.springframework.validation.BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "fieldName", "field message"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ValidationError> response = handler.validation(ex, request);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation exception", response.getBody().getError());
        assertEquals("/test-uri", response.getBody().getPath());
        assertEquals(1, response.getBody().getErrors().size());
        assertEquals("fieldName", response.getBody().getErrors().get(0).getFieldName());
        assertEquals("field message", response.getBody().getErrors().get(0).getMessage());
    }

    @Test
    public void oAuthCustomErrorAccessors() {
        OAuthCustomError err = new OAuthCustomError();
        assertNull(err.getError());
        assertNull(err.getErrorDescription());

        err.setError("Err");
        err.setErrorDescription("Desc");

        assertEquals("Err", err.getError());
        assertEquals("Desc", err.getErrorDescription());
    }

    @Test
    public void fieldMessageAccessors() {
        FieldMessage msg = new FieldMessage();
        assertNull(msg.getFieldName());
        assertNull(msg.getMessage());

        msg.setFieldName("field");
        msg.setMessage("msg");

        assertEquals("field", msg.getFieldName());
        assertEquals("msg", msg.getMessage());
    }

    @Test
    public void standardErrorAccessors() {
        StandardError err = new StandardError();
        Instant now = Instant.now();
        err.setTimestamp(now);
        err.setStatus(200);
        err.setError("Err");
        err.setMessage("Msg");
        err.setPath("/path");

        assertEquals(now, err.getTimestamp());
        assertEquals(200, err.getStatus());
        assertEquals("Err", err.getError());
        assertEquals("Msg", err.getMessage());
        assertEquals("/path", err.getPath());
    }
}
