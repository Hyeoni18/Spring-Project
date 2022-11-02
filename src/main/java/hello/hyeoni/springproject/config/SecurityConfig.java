package hello.hyeoni.springproject.config;

import hello.hyeoni.springproject.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity // Security 설정을 직접할 때 선언
@RequiredArgsConstructor
public class SecurityConfig { // extends WebSecurityConfigurerAdapter를 상속 받으면 WebSecurity 설정을 손쉽게 할 수 있도록 도와줌. 그리고 configure 메소드를 Override 하여 설정하였지만, 개정이 되어 SecurityFilterChain을 bean 등록하는 것으로 바뀜.

    private final AccountService userDetailsService;
    private final DataSource dataSource; // JPA를 사용하고 있으니 bean으로 등록되어 있어 가져다 사용하면 됨

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .mvcMatchers("/","/login","/sign-up",
                                "/check-email-token", "/email-login", "/check-email-login",
                                "/login-link","/login-by-email","/search/travel","/api/recommendations") // 권한 확인 없이 접근
                        .permitAll()
                        .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll() // GET만 허용
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        http.formLogin()
                .loginPage("/login")
                .permitAll();

        http.logout()
                .logoutSuccessUrl("/");

        http.rememberMe()
                .userDetailsService(userDetailsService)
                .tokenRepository(tokenRepository());
        return http.build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/js/**","/css/**","/fonts/**","/images/**","/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
