package at.fhooe.mc.hosic.mobilelearningapp.models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

import at.fhooe.mc.hosic.mobilelearningapp.TestorApplication;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.GsonRequest;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.MessageType;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.TokenDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.UserInfoDTO;

/**
 * Implements methods for authentication at the server.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class AuthenticationModel extends BaseModel {

    // Static variables
    private static final String TAG = "AuthenticationModel";
    private static AuthenticationModel instance = null;

    // Members
    private RequestQueue queue;
    private TokenDTO token;
    private UserInfoDTO userInfo;

    protected AuthenticationModel() {
        queue = Volley.newRequestQueue(TestorApplication.getContext());
    }

    /**
     * Returns an instance of the AuthenticationModel.
     *
     * @return an object of type AuthenticationModel
     */
    public static AuthenticationModel getInstance() {
        if (instance == null) {
            instance = new AuthenticationModel();
        }
        return instance;
    }

    /**
     * Authenticates a user at the moodle server and requests a service token.
     *
     * @param _username The username of the user
     * @param _password The password of the user
     */
    public void authenticate(String _username, String _password) {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", _username);
        params.put("password", _password);
        params.put("service", SERVICE_NAME);

        // Encode URL
        String url = encodeURLWithParams(LOGIN_URL, params);

        // Build request
        GsonRequest<TokenDTO> request = new GsonRequest<>(Request.Method.GET, url, TokenDTO.class, null, new Response.Listener<TokenDTO>() {
            @Override
            public void onResponse(TokenDTO response) {
                Log.i(TAG, "Authentication response");

                if (response.getToken() == null) {
                    Log.i(TAG, "TokenDTO null");

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_FAILED, null));

                    return;
                }

                // Set token
                token = response;

                // Get user info
                requestUserInfo();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Authentication failure");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_FAILED, null));
            }
        });

        // Send request
        queue.add(request);
    }

    /**
     * Signs out the current signed in user.
     */
    public void signOut() {
        Log.i(TAG, "Sign out of user " + userInfo.getUsername());

        token = null;
        userInfo = null;
    }

    /**
     * Requests info about the user from moodle.
     */
    private void requestUserInfo() {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", token.getToken());
        params.put("wsfunction", "core_webservice_get_site_info");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        // Build request
        GsonRequest<UserInfoDTO> request = new GsonRequest<>(Request.Method.GET, url, UserInfoDTO.class, null, new Response.Listener<UserInfoDTO>() {
            @Override
            public void onResponse(UserInfoDTO response) {
                Log.i(TAG, "Request User Info response");

                if (response.getUsername() == null) {
                    Log.i(TAG, "Username null");

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_FAILED, null));

                    return;
                }

                // Set user info
                userInfo = response;

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_OK, token));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Request User Info failed");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_FAILED, null));
            }
        });

        // Send request
        queue.add(request);
    }

    public TokenDTO getToken() {
        return token;
    }

    public int getUserID() {
        return userInfo.getID();
    }
}
