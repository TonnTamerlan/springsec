package ua.oleksii.bank.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class AuthorityLoggingAfterFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("Authenticated user: {}. Authority: {}",
                    authentication.getName(), authentication.getAuthorities());
        }
        chain.doFilter(request, response);
    }
}
