package hello.hyeoni.springproject.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    public void watchNotification(List<Notification> newNotifications) {
        newNotifications.forEach(notification -> {
            notification.setChecked(true);
        });
        notificationRepository.saveAll(newNotifications);
    }
}
