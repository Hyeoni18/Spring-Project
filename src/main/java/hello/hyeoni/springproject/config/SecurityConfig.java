package hello.hyeoni.springproject.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .mvcMatchers("/","/login","/sign-up",
                                "/check-email-token", "/email-login", "/check-email-login",
                                "/login-link") // 권한 확인 없이 접근
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
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/js/**","/css/**","/fonts/**","/images/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
