package hello.hyeoni.springproject.recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/recommendations", produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationRepository recommendationRepository;

    @PostMapping
    public ResponseEntity createLocation(@RequestBody Location location) {
        Location newLocation = this.recommendationRepository.save(location);

        URI createdUri = linkTo(RecommendationController.class).slash(newLocation.getId()).toUri();
        return ResponseEntity.created(createdUri).body(location);
    }
}
