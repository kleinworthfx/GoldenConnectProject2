@Service
public class EmergencyAlertService {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmergencyContactRepository contactRepository;

    public void triggerEmergencyAlert(Long userId, String alertType) {
        User user = userService.getUser(userId);
        List<EmergencyContact> contacts = 
            contactRepository.findByUserId(userId);

        contacts.forEach(contact -> {
            notificationService.sendEmergencyAlert(
                contact.getContactInfo(),
                createEmergencyMessage(user, alertType)
            );
        });
        
        logEmergencyEvent(userId, alertType);
    }

    private String createEmergencyMessage(User user, String alertType) {
        return String.format(
            "EMERGENCY ALERT: %s needs immediate assistance. " +
            "Location: %s. Medical Info: %s",
            user.getName(),
            user.getCurrentLocation(),
            user.getMedicalInfo()
        );
    }
}