package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Holds the token information for a service.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class TokenDTO {

    @SerializedName("token")
    private String mToken;

    public TokenDTO(String _token) {
        mToken = _token;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String _token) {
        mToken = _token;
    }
}
