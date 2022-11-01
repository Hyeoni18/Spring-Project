package hello.hyeoni.springproject.recommendation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest
public class RecommendationControllerTests {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    public void createLocation() throws Exception {
        Location location = Location.builder()
                .city("andong")
                .province("North Gyeongsang")
                .country("korea")
                .description("1,000원 지폐에 그려진 도산서원을 볼 수 있는 지역")
                .createdDateTime(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writer().writeValueAsString(location))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                ;

    }
}
