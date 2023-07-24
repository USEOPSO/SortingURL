package com.example.urlsorting.common.util.exceprion;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = {UserNotFoundException.class})
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
		return errorResponse(e.getMessage());
	}

	@ExceptionHandler(value = {NotMatchPasswordException.class})
	public ResponseEntity<?> handleNotMatchPasswordException(NotMatchPasswordException e) {
		return errorResponse(e.getMessage());
	}

	@ExceptionHandler(value = {TokenExpiredException.class})
	public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e) {
		return errorResponse(e.getMessage());
	}

	private ResponseEntity<String> errorResponse(String message) {
		return ResponseEntity.ok(message);
	}

}
