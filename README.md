# Spring-Project

<h3>로그인/로그아웃</h3>

```java
//SecurityConfig.java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authz) -> authz
                               .mvcMatchers("/","/login","/sign-up") // 권한 확인 없이 접근
                               .permitAll()
                               .mvcMatchers(HttpMethod.GET, "/profile/*").permitAll() 
                               .anyRequest().authenticated()
                              )
        .httpBasic(withDefaults());

    http.formLogin()
        .loginPage("/login")  //로그인 화면
        .permitAll();

    http.logout()
        .logoutSuccessUrl("/"); //로그아웃 성공 후 이동 URL

    http.rememberMe()
        .userDetailsService(userDetailsService) // tokenRepository 사용할 때 설정
        .tokenRepository(tokenRepository()); 
    // 유저정보, 토큰, 시리즈 값을 조합해서 만든 값을 디비에 저장
    return http.build();
}

//AccountService.java
public class AccountService implements UserDetailsService { 
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 로그인에 필요한 정보 조회
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new UsernameNotFoundException(email);
        }

        return new UserAccount(account); // 위에서 만든 principal에 해당하는 객체를 넘기면 됨
    }
}
// service는 bean으로 등록되어 있기에 security에 설정하지 않아도 자동으로 가져다 사용
```



<h3>회원가입</h3>

```html
<!-- form-validation -->
<script type="application/javascript" th:fragment="form-validation">
    (function () {
        'use strict';

        window.addEventListener('load', function () {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            var forms = document.getElementsByClassName('needs-validation');

            // Loop over them and prevent submission
            Array.prototype.filter.call(forms, function (form) {
                form.addEventListener('submit', function (event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated')
                }, false)
            })
        }, false)
    }())
</script>
```

```java
//AccountController.java
@InitBinder("signUpForm")
public void initBinder(WebDataBinder webDataBinder) {
    webDataBinder.addValidators(signUpFormValidator);
}
@PostMapping("/sign-up")
public String singUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
    if (errors.hasErrors()) {
        return "account/sign-up";
    }

    Account account = accountService.processNewAccount(signUpForm);
    accountService.login(account);
    return "redirect:/";
}

//SignUpForm.java
@Data
public class SignUpForm {
    @NotBlank
    @Length(min = 3, max = 20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Length(min = 8, max = 50)
    private String password;
}

//SignUpFormValidator.java
@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm = (SignUpForm) target;
        if(accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용 중인 이메일 입니다.");
        }

        if(accountRepository.existsByNickname(signUpForm.getNickname())) {
            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpForm.getNickname()}, "이미 사용 중인 닉네임 입니다.");
        }
    }
}

//AccountService.java
public Account processNewAccount(SignUpForm signUpForm) {
    Account newAccount = saveNewAccount(signUpForm);
    sendSignUpConfirmEmail(newAccount);
    return newAccount;
}

private Account saveNewAccount(SignUpForm signUpForm) {
    signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
    Account account = modelMapper.map(signUpForm, Account.class);
    account.generateEmailCheckToken();
    return accountRepository.save(account);
}
```

<h3>CURD </h3>

```java
//AccountSettingsController.java
@PostMapping("/profile")
public String updateProfile(@CurrentUser Account account, 
                            @Valid Profile profile, Errors errors, Model model, 
                            RedirectAttributes attributes) {
    if(errors.hasErrors()) {
        model.addAttribute(account);
        return "settings/profile";
    }

    accountService.updateProfile(account,profile);
    attributes.addFlashAttribute("message","프로필을 수정했습니다.");
    return "redirect:/settings/profile";
}

//AccountService.java
public void updateProfile(Account account, Profile profile) {
    modelMapper.map(profile, account);
    accountRepository.save(account);
}

public void completeSignUp(Account account) {
    account.setEmailVerified(true);
    account.setJoinedAt(LocalDateTime.now());
}
```

```java
//MainController.java
@GetMapping("/")
public String home(@CurrentUser Account account, Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
    if(account != null) {
        model.addAttribute(account);
    }
    Page<Travel> travelList = travelRepository.findByPublished(pageable);
    model.addAttribute("travelList", travelList);
    return "index";
}

//TravelRepositoryExtensionImpl.java
@Override
public Page<Travel> findByPublished(Pageable pageable) {
    QTravel travel = QTravel.travel;
    JPQLQuery<Travel> query = from(travel).where(travel.published.isTrue())
        .leftJoin(travel.members, QAccount.account).fetchJoin()
        .leftJoin(travel.managers, QAccount.account).fetchJoin();
    JPQLQuery<Travel> pageableQuery = getQuerydsl().applyPagination(pageable, query);
    QueryResults<Travel> travelQueryResults = pageableQuery.fetchResults();
    return new PageImpl<>(travelQueryResults.getResults(), pageable, travelQueryResults.getTotal());
}
```

```java
//TravelController.java
@PostMapping("/travel/{path}/remove")
public String removeTravel(@CurrentUser Account account, @PathVariable String path) {
    Travel travel = travelService.getTravelStatus(path);
    travelService.remove(travel);
    return "redirect:/";
}

//TravelService.java
public Travel getTravelStatus(String path) {
    Travel byPath = travelRepository.findByPath(path);
    return byPath;
}

public void remove(Travel travel) {
    travelRepository.delete(travel);
}
```

<h3>테스트 코드</h3>

```java
//MainControllerTest.java
@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTest {
    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("usernick");
        signUpForm.setEmail("user@gmail.com");
        signUpForm.setPassword("asdfasdf");
        accountService.processNewAccount(signUpForm);
    }
    @AfterEach
    void AfterEach() {
        accountRepository.deleteAll();
    }
    @DisplayName("이메일 로그인 성공")
    @Test
    void login_with_email() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "user@gmail.com")
                        .param("password","asdfasdf")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("usernick"))
        ;
    }
    @DisplayName("닉네임 로그인 성공")
    @Test
    void login_with_nickname() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "usernick")
                        .param("password","asdfasdf")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("usernick"))
        ;
    }
    @DisplayName("로그인 실패")
    @Test
    void login_fail() throws Exception {

        mockMvc.perform(post("/login")
                        .param("username", "111111111")
                        .param("password","22222222222222")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated())
        ;
    }
    @WithMockUser
    @DisplayName("로그아웃")
    @Test
    void logout() throws Exception {
        mockMvc.perform(post("/logout")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(unauthenticated());
    }
}
```

<h3>인터페이스</h3>

```java
//EmailService.java
public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
}

//EmailMessage.java
@Data
@Builder
public class EmailMessage {
    private String to;
    private String subject;
    private String message;
}

//AccountService.java
public void sendSignUpConfirmEmail(Account newAccount) {
    EmailMessage emailMessage = EmailMessage.builder()
        .to(newAccount.getEmail())
        .subject("트래블위드, 회원 가입 인증")
        .message(message)
        .build();

    emailService.sendEmail(emailMessage);
}

//ConsoleEmailService.java
@Slf4j
@Profile({"local","test"})
@Component
public class ConsoleEmailService implements EmailService{
    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("sent email: {}"+emailMessage.getMessage());
    }
}

//HtmlEmailService.java
@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class HtmlEmailService implements EmailService {

```

