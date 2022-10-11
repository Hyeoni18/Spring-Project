package hello.hyeoni.springproject.notification;

import hello.hyeoni.springproject.account.Account;
import hello.hyeoni.springproject.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String getNotifications(@CurrentUser Account account, Model model) {
        List<Notification> newNotifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, false);
        List<Notification> watchNotifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, true);
        model.addAttribute("newNotificationList", newNotifications);
        model.addAttribute("watchNotificationList", watchNotifications);
        model.addAttribute("count", newNotifications.size());
        model.addAttribute(account);
        notificationService.watchNotification(newNotifications);
        return "notification";
    }
}
