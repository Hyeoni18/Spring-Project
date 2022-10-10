package hello.hyeoni.springproject.notification;

import hello.hyeoni.springproject.travel.Travel;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TravelCreatedEvent {

    private Travel travel;

    public TravelCreatedEvent(Travel travel) {
        this.travel = travel;
    }

}
