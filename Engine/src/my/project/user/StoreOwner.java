package my.project.user;

import feedback.Feedback;
import my.project.order.Order;

import java.time.LocalDate;
import java.util.*;

public class StoreOwner extends User{

    private HashMap<String, List<Feedback>> feedbacks;

    public StoreOwner(int id, String username) {
        super(id, username, eUserType.StoreOwner);
        feedbacks = new HashMap<>();
    }

    @Override
    public eUserType getUserType() {
        return eUserType.StoreOwner;
    }

    public void addFeedback(String zoneName, String userGiverFeedback, LocalDate date, int rate, String textRate) {
        List<Feedback> feedbackList = feedbacks.getOrDefault(zoneName, null);
        if (feedbackList == null){
            feedbackList = new LinkedList<>();
        }

        if (textRate.isEmpty()) {
            textRate = "No Rating text entered";
        }
        feedbackList.add(new Feedback(userGiverFeedback, date, rate, textRate));
        feedbacks.put(zoneName, feedbackList);
    }

    public Collection<Feedback> getFeedbackFromZone(String zoneName) {
        return feedbacks.get(zoneName);
    }
}
