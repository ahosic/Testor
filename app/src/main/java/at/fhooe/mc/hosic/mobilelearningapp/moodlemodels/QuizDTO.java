package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information about a quiz.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizDTO {

    @SerializedName("id")
    private int mID;

    @SerializedName("course")
    private int mCourse;

    @SerializedName("name")
    private String mName;

    @SerializedName("attempts")
    private int mAttemptCount;

    @SerializedName("grade")
    private int mGrade;

    public QuizDTO(int _id, int _course, String _name, int _attemptCount, int _grade) {
        mID = _id;
        mCourse = _course;
        mName = _name;
        mAttemptCount = _attemptCount;
        mGrade = _grade;
    }

    public int getCourse() {
        return mCourse;
    }

    public void setCourse(int _course) {
        mCourse = _course;
    }

    public int getID() {
        return mID;
    }

    public void setID(int _id) {
        mID = _id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String _name) {
        mName = _name;
    }

    public int getAttemptCount() {
        return mAttemptCount;
    }

    public void setAttemptCount(int _attemptCount) {
        mAttemptCount = _attemptCount;
    }

    public int getGrade() {
        return mGrade;
    }

    public void setGrade(int _grade) {
        mGrade = _grade;
    }
}
