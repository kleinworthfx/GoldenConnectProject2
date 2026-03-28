package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.VideoTutorial;
import java.util.List;

public interface VideoTutorialService {
    VideoTutorial createTutorial(VideoTutorial tutorial);
    VideoTutorial updateTutorial(VideoTutorial tutorial);
    boolean deleteTutorial(int tutorialId);
    VideoTutorial findTutorialById(int tutorialId);
    List<VideoTutorial> findAllTutorials();
    List<VideoTutorial> findTutorialsByCategory(String category);
    List<VideoTutorial> findTutorialsByDifficulty(String difficulty);
    List<VideoTutorial> findTutorialsByStatus(String status);
    List<VideoTutorial> findTutorialsByInstructor(int instructorId);
    List<VideoTutorial> findTutorialsByTargetAudience(String targetAudience);
    List<VideoTutorial> findActiveTutorials();
    List<VideoTutorial> findTutorialsByLanguage(String language);
    List<VideoTutorial> findTutorialsByTags(String tags);
    List<VideoTutorial> findTopRatedTutorials(int limit);
    List<VideoTutorial> findMostViewedTutorials(int limit);
    List<VideoTutorial> findTutorialsByDurationRange(int minDuration, int maxDuration);
    boolean updateTutorialStatus(int tutorialId, String status);
    boolean updateTutorialRating(int tutorialId, double rating);
    boolean incrementViewCount(int tutorialId);
    boolean incrementTotalRatings(int tutorialId);
    List<VideoTutorial> searchTutorials(String searchTerm);
    List<VideoTutorial> findRecommendedTutorials(int userId);
    boolean validateTutorial(VideoTutorial tutorial);
}
