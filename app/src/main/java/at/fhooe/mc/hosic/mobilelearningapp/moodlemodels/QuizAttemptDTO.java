package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information for requesting a quiz attempt.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizAttemptDTO {
    @SerializedName("attempt")
    private Attempt mAttempt;

    @SerializedName("errorcode")
    private String mErrorcode;

    public QuizAttemptDTO(Attempt _attempt, String _errorcode) {
        mAttempt = _attempt;
        mErrorcode = _errorcode;
    }

    public Attempt getAttempt() {
        return mAttempt;
    }

    public void setAttempt(Attempt _attempt) {
        mAttempt = _attempt;
    }

    public String getErrorcode() {
        return mErrorcode;
    }

    public void setErrorcode(String _errorcode) {
        mErrorcode = _errorcode;
    }
}
