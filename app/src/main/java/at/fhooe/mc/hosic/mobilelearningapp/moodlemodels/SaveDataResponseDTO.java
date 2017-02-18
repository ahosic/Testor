package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information for saving data of a quiz attempt.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class SaveDataResponseDTO {

    @SerializedName("status")
    private String mStatus;

    public SaveDataResponseDTO(String _status) {
        mStatus = _status;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setState(String _status) {
        mStatus = _status;
    }
}
