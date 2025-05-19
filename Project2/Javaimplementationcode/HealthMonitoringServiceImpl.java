@Service
@Transactional
public class HealthMonitoringServiceImpl implements HealthMonitoringService {
    private final HealthRepository healthRepository;
    private final NotificationService notificationService;
    private final EmergencyService emergencyService;

    @Autowired
    public HealthMonitoringServiceImpl(
        HealthRepository healthRepository,
        NotificationService notificationService,
        EmergencyService emergencyService
    ) {
        this.healthRepository = healthRepository;
        this.notificationService = notificationService;
        this.emergencyService = emergencyService;
    }

    @Override
    public void recordHealthMetrics(HealthMetrics metrics) {
        validateMetrics(metrics);

        HealthRecord record = healthRepository.save(
            convertToHealthRecord(metrics)
        );

        AlertLevel alertLevel = analyzeMetrics(metrics);

        if (alertLevel != AlertLevel.NORMAL) {
            handleHealthAlert(record, alertLevel);
        }

        updateHealthHistory(record);
    }

    private void handleHealthAlert(HealthRecord record, AlertLevel level) {
        switch (level) {
            case WARNING:
                notificationService.sendHealthWarning(record);
                break;
            case CRITICAL:
                emergencyService.triggerEmergencyProtocol(record);
                break;
        }
    }

    private AlertLevel analyzeMetrics(HealthMetrics metrics) {
        if (isMetricsCritical(metrics)) {
            return AlertLevel.CRITICAL;
        } else if (isMetricsWarning(metrics)) {
            return AlertLevel.WARNING;
        }
        return AlertLevel.NORMAL;
    }
}