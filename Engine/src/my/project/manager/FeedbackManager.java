package my.project.manager;

import dto.ShoppingCartDTO;
import feedback.Feedback;

import java.time.LocalDate;
import java.util.*;

public class FeedbackManager {
    private HashMap<String, List<Feedback>> feedbacks = new HashMap<>();
    private final List<Feedback> FeedbackAlerts = new ArrayList<>();

    public void addFeedback(String zoneName, String userGiverFeedback, LocalDate date, int rate, String textRate) {
        List<Feedback> feedbackList = feedbacks.getOrDefault(zoneName, null);
        if (feedbackList == null){
            feedbackList = new LinkedList<>();
        }

        if (textRate.isEmpty()) {
            textRate = "No Description Entered";
        }

        Feedback newFeedback = new Feedback(userGiverFeedback, date, rate, textRate);
        FeedbackAlerts.add(newFeedback);
        feedbackList.add(newFeedback);
        feedbacks.put(zoneName, feedbackList);
    }

    public List<Feedback> getFeedbacksEntries(int fromIndex){
        if (fromIndex < 0 || fromIndex > FeedbackAlerts.size()) {
            fromIndex = 0;
        }
        return FeedbackAlerts.subList(fromIndex, FeedbackAlerts.size());
    }

    public int getVersion() {
        return FeedbackAlerts.size();
    }

    public Collection<Feedback> getFeedbackFromZone(String zoneName) {
        return feedbacks.get(zoneName);
    }

}
