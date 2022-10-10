package hello.hyeoni.springproject.notification;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.AccountPredicates;
import hello.hyeoni.springproject.account.AccountRepository;
import hello.hyeoni.springproject.config.AppProperties;
import hello.hyeoni.springproject.mail.EmailMessage;
import hello.hyeoni.springproject.mail.EmailService;
import hello.hyeoni.springproject.travel.Travel;
import hello.hyeoni.springproject.travel.TravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Properties;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class TravelEventListener {

    private final TravelRepository travelRepository;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final TemplateEngine templateEngine;
    private final AppProperties properties;
    private final NotificationRepository notificationRepository;

    @EventListener
    public void handleTravelCreatedEvent(TravelCreatedEvent event) {
        Travel travel = travelRepository.findTravelWithTagsAndZonesById(event.getTravel().getId());
        Iterable<Account> accounts = accountRepository.findAll(AccountPredicates.findByTagsAndZones(travel.getTags(), travel.getZones()));
        accounts.forEach(account -> {
            if(account.isTravelCreatedByEmail()) {
                sendEmailForCreatedTravel(travel, account);
            }

            if(account.isTravelCreatedByWeb()) {
                sendWebNotificationForCreatedTravel(travel, account);
            }
        });

    }

    private void sendWebNotificationForCreatedTravel(Travel travel, Account account) {
        Notification notification = new Notification();
        notification.setTitle(travel.getTitle());
        notification.setLink("/travel/"+ travel.getEncodedPath());
        notification.setChecked(false);
        notification.setCreatedDateTime(LocalDateTime.now());
        notification.setShortDescription(travel.getShortDescription());
        notification.setAccount(account);
        notification.setNotificationType(NotificationType.TRAVEL_CREATED);
        notificationRepository.save(notification);
    }

    private void sendEmailForCreatedTravel(Travel travel, Account account) {
        Context context = new Context();
        context.setVariable("nickname", account.getNickname());
        context.setVariable("link", "/travel/"+ travel.getEncodedPath());
        context.setVariable("linkName", travel.getTitle());
        context.setVariable("message", "새로운 동행자 모집이 생성 되었습니다.");
        context.setVariable("host", properties.getHost());

        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .subject("트래블 위드, '"+ travel.getTitle()+"' 새로운 동행자 모집이 생성 되었습니다.")
                .to(account.getEmail())
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }
}
