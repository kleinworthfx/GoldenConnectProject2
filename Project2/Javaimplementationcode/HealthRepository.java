@Repository
public interface HealthRepository extends JpaRepository<HealthMetrics, Long> {
    List<HealthMetrics> findByUserIdOrderByRecordedAtDesc(Long userId);
    
    @Query("SELECT h FROM HealthMetrics h WHERE h.userId = :userId " +
           "AND h.recordedAt >= :startDate AND h.recordedAt <= :endDate")
    List<HealthMetrics> getHealthHistory(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}