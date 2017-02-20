package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds information about the current signed in user.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class UserInfoDTO {
    @SerializedName("userid")
    private int mID;

    @SerializedName("username")
    private String mUsername;

    @SerializedName("firstname")
    private String mFirstname;

    @SerializedName("lastname")
    private String mLastname;

    public UserInfoDTO(int _id, String _username, String _firstname, String _lastname) {
        mID = _id;
        mUsername = _username;
        mFirstname = _firstname;
        mLastname = _lastname;
    }

    public int getID() {
        return mID;
    }

    public void setID(int _id) {
        mID = _id;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String _username) {
        mUsername = _username;
    }

    public String getFirstname() {
        return mFirstname;
    }

    public void setFirstname(String _firstname) {
        mFirstname = _firstname;
    }

    public String getLastname() {
        return mLastname;
    }

    public void setLastname(String _lastname) {
        mLastname = _lastname;
    }
}
