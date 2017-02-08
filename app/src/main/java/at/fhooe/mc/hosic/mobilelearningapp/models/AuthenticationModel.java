package at.fhooe.mc.hosic.mobilelearningapp.models;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.MessageType;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implements methods for authentication at the server.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class AuthenticationModel extends BaseModel {

    private static final String TAG = "AuthenticationModel";
    private static AuthenticationModel instance = null;
    private Retrofit retrofit;
    private APIEndpoint service;

    protected AuthenticationModel() {
        // Initializes the API service
        initService();
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
     * Initializes the API service used for interaction with the server
     */
    private void initService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIEndpoint.class);
    }

    /**
     * Authenticates a user at the server and requests a service token.
     *
     * @param _username The username of the user
     * @param _password The password of the user
     */
    public void authenticate(String _username, String _password) {
        Call<Token> call = service.getToken(_username, _password, SERVICE_NAME);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Log.i(TAG, "Authentication response");

                Token token = response.body();

                if (token.getToken() == null) {
                    Log.i(TAG, "Token null");

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_FAILED, null));

                    return;
                }

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_OK, token));
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i(TAG, "Authentication failure");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.AUTHENTICATION_FAILED, null));
            }
        });
    }
}
