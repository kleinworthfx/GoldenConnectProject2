package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.VideoTutorial;
import java.util.List;

public interface VideoTutorialDAO {
    VideoTutorial findById(int id);
    List<VideoTutorial> findAll();
    List<VideoTutorial> findByCategory(String category);
    List<VideoTutorial> findByDifficulty(String difficulty);
    List<VideoTutorial> findByStatus(String status);
    List<VideoTutorial> findByInstructorId(int instructorId);
    List<VideoTutorial> findByTargetAudience(String targetAudience);
    List<VideoTutorial> findActiveTutorials();
    List<VideoTutorial> findByLanguage(String language);
    List<VideoTutorial> findByTags(String tags);
    List<VideoTutorial> findTopRatedTutorials(int limit);
    List<VideoTutorial> findMostViewedTutorials(int limit);
    List<VideoTutorial> findByDurationRange(int minDuration, int maxDuration);
    boolean save(VideoTutorial tutorial);
    boolean update(VideoTutorial tutorial);
    boolean delete(int id);
    boolean updateStatus(int tutorialId, String status);
    boolean updateRating(int tutorialId, double rating);
    boolean incrementViewCount(int tutorialId);
    boolean incrementTotalRatings(int tutorialId);
    List<VideoTutorial> searchTutorials(String searchTerm);
}
