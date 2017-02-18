package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information for requesting attempt data.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class AttemptDataDTO {
    @SerializedName("attempt")
    private Attempt mAttempt;

    @SerializedName("nextpage")
    private int mNextPage;

    @SerializedName("questions")
    private Question[] mQuestions;

    public AttemptDataDTO(Attempt _attempt, int _nextPage, Question[] _questions) {
        mAttempt = _attempt;
        mNextPage = _nextPage;
        mQuestions = _questions;
    }

    public Attempt getAttempt() {
        return mAttempt;
    }

    public void setAttempt(Attempt _attempt) {
        mAttempt = _attempt;
    }

    public int getNextPage() {
        return mNextPage;
    }

    public void setNextPage(int _nextPage) {
        mNextPage = _nextPage;
    }

    public Question[] getQuestions() {
        return mQuestions;
    }

    public void setQuestions(Question[] _questions) {
        mQuestions = _questions;
    }
}
