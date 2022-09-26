package hello.hyeoni.springproject.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.AccountRepository;
import hello.hyeoni.springproject.account.AccountService;
import hello.hyeoni.springproject.account.SettingsController;
import hello.hyeoni.springproject.account.form.SignUpForm;
import hello.hyeoni.springproject.tag.Tag;
import hello.hyeoni.springproject.tag.TagForm;
import hello.hyeoni.springproject.tag.TagRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SettingsControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired AccountService accountService;
    @Autowired AccountRepository accountRepository;
    @Autowired ObjectMapper objectMapper;
    @Autowired TagRepository tagRepository;

    @BeforeEach
    void beforeEach() {
        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setNickname("usernick");
        signUpForm.setEmail("user@gmail.com");
        signUpForm.setPassword("asdfasdf");
        accountService.processNewAccount(signUpForm);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @WithUserDetails(value="usernick", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("프로필 수정하기 - 입력값 정상")
    @Test
    void updateProfile() throws Exception {
        String bio = "짧은 소개 수정하는 경우";
        mockMvc.perform(post("/settings/profile")
                        .param("bio",bio)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("message"));

        Account account = accountRepository.findByNickname("usernick");
        assertEquals(bio, account.getBio());
    }

    @WithUserDetails(value="usernick", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정의 태그 수정 폼")
    @Test
    void updateTagsForm() throws Exception {
        mockMvc.perform(get("/settings/tags"))
                .andExpect(view().name("settings/tags"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }

    @WithUserDetails(value="usernick", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계정에 태그 추가")
    @Test
    void addTag() throws Exception {
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post("/settings/tags/add")
                .contentType(MediaType.APPLICATION_JSON) // 요청 안에 파라미터가 아닌 본문으로 들어옴, 타입은 JSON 문자열 (TagForm 객체가 JSON 으로 변환한 모습)
                .content(objectMapper.writeValueAsString(tagForm)) // {"tagTitle" : "newTag"} 이렇게 확인을 해야하는데, 프로퍼티가 많아지면 "\ 이런식으로 작성하기 힘드니 objectMapper를 사용
                .with(csrf()) // post 요청에 필요
        ).andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle("newTag");
        assertNotNull(newTag);
        assertTrue(accountRepository.findByNickname("usernick").getTags().contains(newTag));
    }
}
