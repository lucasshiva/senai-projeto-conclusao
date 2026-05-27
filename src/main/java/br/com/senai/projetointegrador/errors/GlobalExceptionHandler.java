package br.com.senai.projetointegrador.errors;

import br.com.senai.projetointegrador.features.auth.register.InvalidLoginException;
import br.com.senai.projetointegrador.features.students.register.exceptions.InvalidPictureException;
import br.com.senai.projetointegrador.features.users.UserNotFoundException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthException(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String title = "Authentication Failed";
        String detail = ex.getMessage();

        if (ex.getCause() instanceof TokenExpiredException) {
            title = "Token Expired";
            detail = "The provided JWT has expired. Please re-authenticate.";
        }

        var problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidLoginException.class)
    public ProblemDetail handleInvalidLoginException(InvalidLoginException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @Override
    protected ResponseEntity<@NonNull Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        var pd = ProblemDetail.forStatusAndDetail(status, "One or more fields failed validation.");
        pd.setTitle("Validation Failed");

        List<FieldErrorDetail> errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();
        pd.setProperty("errors", errors);

        return handleExceptionInternal(ex, pd, headers, status, request);
    }

    @ExceptionHandler(InvalidPictureException.class)
    public ProblemDetail handleInvalidPictureException(InvalidPictureException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
