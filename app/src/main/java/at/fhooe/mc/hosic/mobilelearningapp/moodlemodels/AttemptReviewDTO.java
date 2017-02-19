package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds the review of a quiz attempt.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class AttemptReviewDTO {
    @SerializedName("grade")
    private double mGrade;

    @SerializedName("attempt")
    private AttemptInfoDTO mAttemptInfo;

    public AttemptReviewDTO(double _grade, AttemptInfoDTO _attemptInfo) {
        mGrade = _grade;
        mAttemptInfo = _attemptInfo;
    }

    public double getGrade() {
        return mGrade;
    }

    public void setGrade(double _grade) {
        mGrade = _grade;
    }

    public AttemptInfoDTO getAttemptInfo() {
        return mAttemptInfo;
    }

    public void setAttemptInfo(AttemptInfoDTO _attemptInfo) {
        mAttemptInfo = _attemptInfo;
    }
}
