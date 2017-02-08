package at.fhooe.mc.hosic.mobilelearningapp.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Defines an interface for interaction with the server.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public interface APIEndpoint {
    @GET("https://fh-mlearning.moodlecloud.com/login/token.php")
    Call<Token> getToken(@Query("username") String username, @Query("password") String password, @Query("service") String service);
}
