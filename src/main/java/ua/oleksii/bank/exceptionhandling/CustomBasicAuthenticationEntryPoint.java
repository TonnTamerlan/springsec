package ua.oleksii.bank.exceptionhandling;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("bank-error-reason", "Authentication failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        var currentTimeStamp = LocalDateTime.now();
        var message = (authException != null && authException.getMessage() != null) ? authException.getMessage() : "Unauthorized";
        var path = request.getRequestURI();
        var jsonResponse = String.format("""
                {
                     "timestamp": "%s",
                     "status": %d,
                     "error": "%s",
                     "message": "%s",
                     "path": "%s"
                 }
                """, currentTimeStamp, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), message, path);
        response.getWriter().write(jsonResponse);
    }
}
