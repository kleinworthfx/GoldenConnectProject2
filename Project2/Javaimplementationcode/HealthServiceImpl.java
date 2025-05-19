@Service
public class HealthServiceImpl implements HealthService {
    @Autowired
    private HealthRepository healthRepository;
    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public void saveHealthMetrics(HealthMetrics metrics) {
        validateMetrics(metrics);
        HealthMetrics saved = healthRepository.save(metrics);
        if (isMetricsConcerning(metrics)) {
            notificationService.notifyCaregivers(metrics);
        }
        updateHealthHistory(saved);
    }

    private void validateMetrics(HealthMetrics metrics) {
        if (metrics.getSystolic() < 70 || metrics.getSystolic() > 190) {
            throw new InvalidMetricsException("Invalid systolic pressure");
        }
    }
}