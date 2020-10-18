package feedback;

import java.time.LocalDate;

public class Feedback {
    private final String userGiverFeedback;
    private final String date;
    private final int numericalRating;
    private final String textRating;

    public Feedback(String userGiverFeedback, LocalDate date, int numericalRating, String textRating) {
        this.userGiverFeedback = userGiverFeedback;
        this.date = date.toString();
        this.numericalRating = numericalRating;
        this.textRating = textRating;
    }

    public String getUserGiverFeedback() {
        return userGiverFeedback;
    }

    public String getDate() {
        return date;
    }

    public int getNumericalRating() {
        return numericalRating;
    }

    public String getTextRating() {
        return textRating;
    }
}
