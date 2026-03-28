package za.ac.cput.goldenconnect.service;

import za.ac.cput.goldenconnect.model.Match;
import za.ac.cput.goldenconnect.model.User;
import java.util.List;

public interface MatchingService {
    Match createMatch(int volunteerId, int elderlyId, String matchType);
    Match updateMatch(Match match);
    boolean deleteMatch(int matchId);
    Match findMatchById(int matchId);
    List<Match> findMatchesByVolunteer(int volunteerId);
    List<Match> findMatchesByElderly(int elderlyId);
    List<Match> findMatchesByStatus(String status);
    List<Match> findMatchesByType(String matchType);
    List<Match> findActiveMatches();
    List<Match> findPreferredMatches();
    boolean updateMatchStatus(int matchId, String status);
    boolean updateCompatibilityScore(int matchId, double score);
    boolean incrementInteractions(int matchId);
    List<User> findCompatibleVolunteers(int elderlyId, String preferences);
    List<User> findCompatibleElderly(int volunteerId, String skills);
    double calculateCompatibilityScore(int volunteerId, int elderlyId);
    List<Match> findMatchesByCompatibilityRange(double minScore, double maxScore);
}
