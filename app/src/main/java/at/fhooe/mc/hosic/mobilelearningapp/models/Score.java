package at.fhooe.mc.hosic.mobilelearningapp.models;

import java.util.Date;

/**
 * Holds information about the score of a quiz attempt.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class Score {
    private int mID;
    private int mAttemptID;
    private int mUserID;
    private int mQuizID;
    private Date mDateTime;
    private double mGrade;

    public Score() {
    }

    public Score(int _id, int _attemptID, int _userID, int _quizID, double _grade, Date _dateTime) {
        mID = _id;
        mAttemptID = _attemptID;
        mUserID = _userID;
        mQuizID = _quizID;
        mGrade = _grade;
        mDateTime = _dateTime;
    }

    public Score(int _attemptID, int _userID, int _quizID, double _grade, Date _dateTime) {
        mAttemptID = _attemptID;
        mUserID = _userID;
        mQuizID = _quizID;
        mGrade = _grade;
        mDateTime = _dateTime;
    }

    public int getID() {
        return mID;
    }

    public void setID(int _id) {
        mID = _id;
    }

    public int getAttemptID() {
        return mAttemptID;
    }

    public void setAttemptID(int _attemptID) {
        mAttemptID = _attemptID;
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int _userID) {
        mUserID = _userID;
    }

    public int getQuizID() {
        return mQuizID;
    }

    public void setQuizID(int _quizID) {
        mQuizID = _quizID;
    }

    public double getGrade() {
        return mGrade;
    }

    public void setGrade(double _grade) {
        mGrade = _grade;
    }

    public Date getDateTime() {
        return mDateTime;
    }

    public void setDateTime(Date _dateTime) {
        mDateTime = _dateTime;
    }
}
