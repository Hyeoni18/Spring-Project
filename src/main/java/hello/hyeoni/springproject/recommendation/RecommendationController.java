package hello.hyeoni.springproject.recommendation;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value="/api/recommendations", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class RecommendationController {

    @PostMapping
    public ResponseEntity createLocation(@RequestBody Location location) {
        URI createdUri = linkTo(RecommendationController.class).slash("{id}").toUri();
        location.setId(10L);
        return ResponseEntity.created(createdUri).body(location);
    }
}
