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
    private AttemptInfoDTO mAttemptInfo;

    @SerializedName("errorcode")
    private String mErrorcode;

    public QuizAttemptDTO(AttemptInfoDTO _attemptInfo, String _errorcode) {
        mAttemptInfo = _attemptInfo;
        mErrorcode = _errorcode;
    }

    public AttemptInfoDTO getAttempt() {
        return mAttemptInfo;
    }

    public void setAttempt(AttemptInfoDTO _attemptInfo) {
        mAttemptInfo = _attemptInfo;
    }

    public String getErrorcode() {
        return mErrorcode;
    }

    public void setErrorcode(String _errorcode) {
        mErrorcode = _errorcode;
    }
}
