package ua.oleksii.bank.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.oleksii.bank.repository.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BankUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var customer = customerRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User details was not found for: " + email));
        List<GrantedAuthority> authorities = customer.getAuthorities().stream()
                .map(authority->new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return User.withUsername(customer.getEmail())
                .password(customer.getPwd())
                .authorities(authorities)
                .build();
    }
}
