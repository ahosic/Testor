package at.fhooe.mc.hosic.mobilelearningapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Holds the token information for a service.
 */

public class Token {

    @SerializedName("token")
    private String mToken;

    public Token(String _token) {
        mToken = _token;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String _token) {
        mToken = _token;
    }
}
