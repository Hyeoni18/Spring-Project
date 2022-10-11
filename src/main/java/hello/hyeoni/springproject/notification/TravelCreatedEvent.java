package hello.hyeoni.springproject.notification;

import hello.hyeoni.springproject.travel.Travel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
@RequiredArgsConstructor
public class TravelCreatedEvent {

    private final Travel travel;

}
