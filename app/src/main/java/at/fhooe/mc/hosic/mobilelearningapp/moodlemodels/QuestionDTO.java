package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information about a quiz question.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuestionDTO {

    @SerializedName("type")
    private String mQuestionType;

    @SerializedName("slot")
    private int mSlot;

    @SerializedName("number")
    private int mQuestionNumber;

    @SerializedName("html")
    private String mHTML;

    @SerializedName("sequencecheck")
    private int mSequencecheck;

    public QuestionDTO(String _questionType, int _slot, int _questionNumber, String _html, int _sequencecheck) {
        mQuestionType = _questionType;
        mSlot = _slot;
        mQuestionNumber = _questionNumber;
        mHTML = _html;
        mSequencecheck = _sequencecheck;
    }

    public String getQuestionType() {
        return mQuestionType;
    }

    public void setQuestionType(String _questionType) {
        mQuestionType = _questionType;
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
