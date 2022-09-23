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
                                "/login-link","/login-by-email","/search/study","/board") // 권한 확인 없이 접근
                        .permitAll()
                        .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll() // GET만 허용
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults());

        http.formLogin()
                .loginPage("/login")  //로그인 화면 커스텀
                .permitAll();

        http.logout()
                .logoutSuccessUrl("/"); //로그아웃 성공 후 이동 URL

        http.rememberMe()
                .userDetailsService(userDetailsService) // tokenRepository 사용할 때 설정
                .tokenRepository(tokenRepository()); // 유저정보, 토큰, 시리즈 값을 조합해서 만든 값을 디비에 저장
        return http.build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        // JDBC 기반의 token repository 구현체
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl(); // JdbcTokenRepositoryImpl가 사용하는 테이블 필요함
        jdbcTokenRepository.setDataSource(dataSource); // jdbc는 dataSource가 필요
        return jdbcTokenRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/js/**","/css/**","/fonts/**","/images/**","/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
