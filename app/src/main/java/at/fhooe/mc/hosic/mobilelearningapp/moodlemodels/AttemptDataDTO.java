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
    private AttemptInfoDTO mAttemptInfo;

    @SerializedName("nextpage")
    private int mNextPage;

    @SerializedName("questions")
    private QuestionDTO[] mQuestions;

    public AttemptDataDTO(AttemptInfoDTO _attemptInfo, int _nextPage, QuestionDTO[] _questions) {
        mAttemptInfo = _attemptInfo;
        mNextPage = _nextPage;
        mQuestions = _questions;
    }

    public AttemptInfoDTO getAttempt() {
        return mAttemptInfo;
    }

    public void setAttempt(AttemptInfoDTO _attemptInfo) {
        mAttemptInfo = _attemptInfo;
    }

    public int getNextPage() {
        return mNextPage;
    }

    public void setNextPage(int _nextPage) {
        mNextPage = _nextPage;
    }

    public QuestionDTO[] getQuestions() {
        return mQuestions;
    }

    public void setQuestions(QuestionDTO[] _questions) {
        mQuestions = _questions;
    }
}
