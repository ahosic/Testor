package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information about a quiz attempt
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class Attempt {

    @SerializedName("id")
    private int mID;

    @SerializedName("quiz")
    private int mQuizID;

    @SerializedName("userid")
    private int mUserID;

    @SerializedName("currentpage")
    private int mCurrentPage;

    @SerializedName("state")
    private String mState;

    public Attempt(int _id, int _quizID, int _userID, int _currentPage, String _state) {
        mID = _id;
        mQuizID = _quizID;
        mUserID = _userID;
        mCurrentPage = _currentPage;
        mState = _state;
    }

    public int getID() {
        return mID;
    }

    public void setID(int _id) {
        mID = _id;
    }

    public int getQuizID() {
        return mQuizID;
    }

    public void setQuizID(int _quizID) {
        mQuizID = _quizID;
    }

    public int getUserID() {
        return mUserID;
    }

    public void setmUserID(int _userID) {
        mUserID = _userID;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int _currentPage) {
        mCurrentPage = _currentPage;
    }

    public String getState() {
        return mState;
    }

    public void setState(String _state) {
        mState = _state;
    }
}
