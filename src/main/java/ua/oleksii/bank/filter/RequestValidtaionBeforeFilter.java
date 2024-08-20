package ua.oleksii.bank.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidtaionBeforeFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var res = (HttpServletResponse) response;
        var header = StringUtils.trimToNull(req.getHeader(HttpHeaders.AUTHORIZATION));
        if (StringUtils.startsWith(header, "Basic ")) {
            var base64Token = header.replace("Basic ", "").getBytes(StandardCharsets.UTF_8);
            byte[] decoded;
            try {
                decoded = Base64.getDecoder().decode(base64Token);
                var token = new String(decoded, StandardCharsets.UTF_8);
                var delim = token.indexOf(':');
                if (delim == -1) {
                    throw new BadCredentialsException("Invalid basic token");
                }
                var email = token.substring(0, delim);
                if (email.toLowerCase().contains("test")) {
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            } catch (IllegalArgumentException e) {
                throw new BadCredentialsException("Invalid basic token");
            }
        }
        chain.doFilter(request, response);
    }
}
