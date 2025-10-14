package example.cashcard;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {

   @Bean
   SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
      http.authorizeHttpRequests(request -> request.requestMatchers("/cashcards/**").hasRole("CARD-OWNER")).httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable());
      return http.build();
   }

   @Bean
   UserDetailsService testOnlyUsers(final PasswordEncoder passwordEncoder) {
      final User.UserBuilder users = User.builder();
      final UserDetails sarah = users.username("sarah1").password(passwordEncoder.encode("abc123")).roles("CARD-OWNER").build();
      final UserDetails hank = users.username("hank").password(passwordEncoder.encode("qwe123")).roles("NON-OWNER").build();
      return new InMemoryUserDetailsManager(sarah, hank);

   }

   @Bean
   PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}