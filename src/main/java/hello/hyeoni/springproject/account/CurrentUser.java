package hello.hyeoni.springproject.account;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지하고
@Target(ElementType.PARAMETER) // 파라미터에만 붙일 수 있고
@AuthenticationPrincipal(expression = "#this == 'anonymousUser' ? null : account") // SpEL 사용하여 정의
public @interface CurrentUser {
}
