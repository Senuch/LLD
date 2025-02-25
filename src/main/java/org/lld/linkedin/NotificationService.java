package org.lld.linkedin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class NotificationService {
    private static NotificationService instance;

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    private NotificationService() {
    }

    private final ConcurrentHashMap<Account, ArrayList<Notification>> notifications = new ConcurrentHashMap<>();

    public boolean sendNotification(Notification notification) {
        ArrayList<Notification> notificationList = notifications.getOrDefault(notification.getReceiver(), new ArrayList<>());
        notificationList.add(notification);

        notifications.put(notification.getReceiver(), notificationList);

        return true;
    }

    public List<Notification> getNotificationsByAccount(Account account) {
        return notifications.getOrDefault(account, new ArrayList<>()).stream().filter(x -> !x.isRead()).toList();
    }

    public List<Notification> getNotificationByType(Account account, NotificationType type) {
        return getNotificationsByAccount(account).stream().filter(x -> x.getType() == type).toList();
    }

    public boolean markNotificationAsReadById(int notificationId) {
        Notification target = null;
        for (List<Notification> notificationList : notifications.values()) {
            target = notificationList.stream().filter(x -> x.getId() == notificationId).findFirst().orElse(null);
            if (target != null) {
                break;
            }
        }

        if (target != null) {
            target.setNotificationStatus(true);
            return true;
        }
        return false;
    }

    public boolean markAllAccountNotificationsAsRead(Account account) {
        List<Notification> notifications = getNotificationsByAccount(account);
        for (Notification notification : notifications) {
            notification.setNotificationStatus(true);
        }

        return true;
    }
}
