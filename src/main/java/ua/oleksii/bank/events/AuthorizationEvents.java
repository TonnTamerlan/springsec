package ua.oleksii.bank.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvents {

    @EventListener
    public void onFail(AuthorizationDeniedEvent event) {
        log.info("Authorization denied: {}. Due to: {}",
                event.getAuthentication().get().getName(), event.getAuthorizationDecision());
    }
}
