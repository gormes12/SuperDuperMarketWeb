package my.project.user;

import feedback.Feedback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StoreOwner extends User{

    private List<Feedback> feedbacks;

    public StoreOwner(int id, String username) {
        super(id, username, eUserType.StoreOwner);
        feedbacks = new ArrayList<>();
    }

    @Override
    public eUserType getUserType() {
        return eUserType.StoreOwner;
    }

    public void addFeedback(String userGiverFeedback, LocalDate date, int rate, String textRate) {
        feedbacks.add(new Feedback(userGiverFeedback, date, rate, textRate));
    }
}
