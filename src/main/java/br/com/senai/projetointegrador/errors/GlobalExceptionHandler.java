package br.com.senai.projetointegrador.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthException(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String title = "Authentication Failed";
        String detail = ex.getMessage();

//        if (ex.getCause() instanceof TokenExpiredException) {
//            title = "Token Expired";
//            detail = "The provided JWT has expired. Please re-authenticate.";
//        }

        var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
