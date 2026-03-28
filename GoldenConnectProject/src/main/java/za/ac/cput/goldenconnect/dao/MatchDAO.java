package za.ac.cput.goldenconnect.dao;

import za.ac.cput.goldenconnect.model.Match;
import java.util.List;

public interface MatchDAO {
    Match findById(int id);
    List<Match> findAll();
    List<Match> findByVolunteerId(int volunteerId);
    List<Match> findByElderlyId(int elderlyId);
    List<Match> findByStatus(String status);
    List<Match> findByMatchType(String matchType);
    List<Match> findActiveMatches();
    List<Match> findPreferredMatches();
    Match findByVolunteerAndElderly(int volunteerId, int elderlyId);
    boolean save(Match match);
    boolean update(Match match);
    boolean delete(int id);
    boolean updateStatus(int matchId, String status);
    boolean updateCompatibilityScore(int matchId, double score);
    boolean incrementInteractions(int matchId);
    List<Match> findMatchesByCompatibilityRange(double minScore, double maxScore);
}
