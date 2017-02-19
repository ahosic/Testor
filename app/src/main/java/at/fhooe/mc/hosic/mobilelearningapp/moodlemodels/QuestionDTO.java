package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information about a quiz question.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuestionDTO {

    @SerializedName("slot")
    private int mSlot;

    @SerializedName("number")
    private int mQuestionNumber;

    @SerializedName("html")
    private String mHTML;

    @SerializedName("sequencecheck")
    private int mSequencecheck;

    public QuestionDTO(int _slot, int _questionNumber, String _html, int _sequencecheck) {
        mSlot = _slot;
        mQuestionNumber = _questionNumber;
        mHTML = _html;
        mSequencecheck = _sequencecheck;
    }

    public int getSlot() {
        return mSlot;
    }

    public void setSlot(int _slot) {
        mSlot = _slot;
    }

    public int getQuestionNumber() {
        return mQuestionNumber;
    }

    public void setQuestionNumber(int _questionNumber) {
        mQuestionNumber = _questionNumber;
    }

    public String getHTML() {
        return mHTML;
    }

    public void setHTML(String _html) {
        mHTML = _html;
    }

    public int getSequencecheck() {
        return mSequencecheck;
    }

    public void setSequencecheck(int _sequencecheck) {
        mSequencecheck = _sequencecheck;
    }
}
