package at.fhooe.mc.hosic.mobilelearningapp.models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import at.fhooe.mc.hosic.mobilelearningapp.TestorApplication;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.GsonRequest;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.MessageType;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.Quiz;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizzesDTO;

/**
 * Implements methods of the Quiz module of Moodle.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizModel extends BaseModel {
    // Static variables
    private static final String TAG = "QuizModel";
    private static QuizModel instance = null;


    // Members
    private RequestQueue queue;
    private LinkedList<Quiz> quizzes;

    protected QuizModel() {
        queue = Volley.newRequestQueue(TestorApplication.getContext());
    }

    /**
     * Returns an instance of the QuizModel.
     *
     * @return an object of type QuizModel
     */
    public static QuizModel getInstance() {
        if (instance == null) {
            instance = new QuizModel();
        }
        return instance;
    }

    /**
     * Gets the quizzes available for the user.
     *
     * @return A collection of quizzes.
     */
    public LinkedList<Quiz> getQuizzes() {
        return quizzes;
    }

    /**
     * Filters quizzes that were attempted by the user.
     *
     * @return A collection of attempted quizzes.
     */
    public LinkedList<Quiz> getQuizzesWithRealGrades() {
        LinkedList<Quiz> quizzesWithGrades = new LinkedList<>();

        for (Quiz q : quizzes) {
            if (q.getAttemptCount() > 0) {
                quizzesWithGrades.add(q);
            }
        }

        return quizzesWithGrades;
    }

    /**
     * Requests all quizzes that are available to the user from the Moodle server.
     */
    public void loadQuizzes() {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", AuthenticationModel.getInstance().getToken().getToken());
        params.put("wsfunction", "mod_quiz_get_quizzes_by_courses");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        // Build request
        GsonRequest<QuizzesDTO> request = new GsonRequest<>(Request.Method.POST, url, QuizzesDTO.class, null, new Response.Listener<QuizzesDTO>() {
            @Override
            public void onResponse(QuizzesDTO response) {
                Log.i(TAG, "Get Quizzes response");

                if (response.getQuizzes() == null) {
                    Log.i(TAG, "Quizzes null");

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.GET_QUIZZES_FAILED, null));

                    return;
                }

                // Create list from Array
                quizzes = new LinkedList<>(Arrays.asList(response.getQuizzes()));

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.GET_QUIZZES_SUCCESS, null));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Get Quizzes failure");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.GET_QUIZZES_FAILED, null));
            }
        });

        // Send request
        queue.add(request);
    }
}
