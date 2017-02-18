package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information for finishing a quiz attempt.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class ProcessAttemptDTO {

    @SerializedName("state")
    public String mState;

    public ProcessAttemptDTO(String _state) {
        mState = _state;
    }

    public String getState() {
        return mState;
    }

    public void setState(String _state) {
        mState = _state;
    }
}
