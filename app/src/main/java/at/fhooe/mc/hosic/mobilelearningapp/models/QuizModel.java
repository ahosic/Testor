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
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptDataDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.ProcessAttemptDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizAttemptDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizSelectionData;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizzesDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.SaveDataResponseDTO;

/**
 * Implements methods of the QuizDTO module of Moodle.
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
    private LinkedList<QuizDTO> quizzes;

    protected QuizModel() {
        queue = Volley.newRequestQueue(TestorApplication.getContext());
        quizzes = new LinkedList<QuizDTO>();
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
    public LinkedList<QuizDTO> getQuizzes() {
        return quizzes;
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

    /**
     * Starts a quiz attempt.
     *
     * @param _quizID The quiz ID
     */
    public void startAttempt(int _quizID) {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", AuthenticationModel.getInstance().getToken().getToken());
        params.put("wsfunction", "mod_quiz_start_attempt");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        HashMap<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("quizid", "" + _quizID);

        // Build request
        GsonRequest<QuizAttemptDTO> request = new GsonRequest<>(Request.Method.POST, url, QuizAttemptDTO.class, bodyParams, new Response.Listener<QuizAttemptDTO>() {
            @Override
            public void onResponse(QuizAttemptDTO response) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO response");

                if (response.getAttempt() == null || (response.getErrorcode() != null && !response.getErrorcode().equals("attemptstillinprogress"))) {
                    Log.i(TAG, "QuizDTO AttemptInfoDTO State invalid");

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_FAILED, null));

                    return;
                }

                Log.i(TAG, "Last attempt: " + response.getAttempt().getID());

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_STARTED, response.getAttempt()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO failure");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_FAILED, null));
            }
        });

        // Send request
        queue.add(request);
    }

    /**
     * Finishes a quiz attempt.
     *
     * @param _attemptID The ID of the attempt
     */
    public void finishAttempt(final int _attemptID) {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", AuthenticationModel.getInstance().getToken().getToken());
        params.put("wsfunction", "mod_quiz_process_attempt");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        HashMap<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("attemptid", "" + _attemptID);
        bodyParams.put("timeup", "0");
        bodyParams.put("finishattempt", "1");

        // Build request
        GsonRequest<ProcessAttemptDTO> request = new GsonRequest<>(Request.Method.POST, url, ProcessAttemptDTO.class, bodyParams, new Response.Listener<ProcessAttemptDTO>() {
            @Override
            public void onResponse(ProcessAttemptDTO response) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO response");

                if (response.getState() == null || (response.getState() != null && !response.getState().equals("finished"))) {
                    Log.i(TAG, "Could not finish quiz attempt " + _attemptID);

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_FINISHED_FAILED, _attemptID));

                    return;
                }

                Log.i(TAG, "Finished attempt: " + _attemptID);

                // TODO: Remove last attempt here

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_FINISHED_SUCCESS, _attemptID));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO failure");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_FINISHED_FAILED, _attemptID));
            }
        });

        // Send request
        queue.add(request);
    }

    /**
     * Gets the data of a quiz attempt.
     *
     * @param _attemptID The ID of the QuizDTO attempt
     * @param _page      The page, which should be loaded
     */
    public void getAttemptData(final int _attemptID, int _page) {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", AuthenticationModel.getInstance().getToken().getToken());
        params.put("wsfunction", "mod_quiz_get_attempt_data");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        HashMap<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("attemptid", "" + _attemptID);
        bodyParams.put("page", "" + _page);

        // Build request
        GsonRequest<AttemptDataDTO> request = new GsonRequest<>(Request.Method.POST, url, AttemptDataDTO.class, bodyParams, new Response.Listener<AttemptDataDTO>() {
            @Override
            public void onResponse(AttemptDataDTO response) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO response");

                if (response.getAttempt() == null || response.getQuestions() == null || response.getQuestions()[0] == null) {
                    Log.i(TAG, "Could not get attempt data for attempt " + _attemptID);

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_FINISHED_FAILED, _attemptID));

                    return;
                }

                Log.i(TAG, "Data for attempt received: " + _attemptID);

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_DATA_RECEIVED, response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Could not get attempt data for attempt " + _attemptID);

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.QUIZ_ATTEMPT_FINISHED_FAILED, _attemptID));
            }
        });

        // Send request
        queue.add(request);
    }

    /**
     * Saves a selected answer of a quiz attempt on the Moodle server.
     *
     * @param _data The data that should be saved on the Moodle server.
     */
    public void saveAttemptData(final QuizSelectionData _data) {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", AuthenticationModel.getInstance().getToken().getToken());
        params.put("wsfunction", "mod_quiz_save_attempt");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        String sCheckName = "q" + (_data.getAttemptID() + 1) + ":" + _data.getQuestionNumber() + "_:sequencecheck";
        String ansName = "q" + (_data.getAttemptID() + 1) + ":" + _data.getQuestionNumber() + "_answer";

        HashMap<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("attemptid", "" + _data.getAttemptID());
        bodyParams.put("data[0][name]", sCheckName);
        bodyParams.put("data[0][value]", "" + _data.getSequenceCheck());
        bodyParams.put("data[1][name]", ansName);
        bodyParams.put("data[1][value]", "" + _data.getAnswerNumber());
        bodyParams.put("data[2][name]", "attempt");
        bodyParams.put("data[2][value]", "" + _data.getAttemptID());

        // Build request
        GsonRequest<SaveDataResponseDTO> request = new GsonRequest<>(Request.Method.POST, url, SaveDataResponseDTO.class, bodyParams, new Response.Listener<SaveDataResponseDTO>() {
            @Override
            public void onResponse(SaveDataResponseDTO response) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO response");

                if (response.getStatus() == null || !response.getStatus().equals("true")) {
                    Log.i(TAG, "Could not save attempt data for attempt " + _data.getAttemptID());

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_SAVE_FAILED, _data.getAttemptID()));

                    return;
                }

                Log.i(TAG, "Data saved for attempt: " + _data.getAttemptID());

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_SAVE_SUCCESS, _data.getAttemptID()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Could not save attempt data for attempt " + _data.getAttemptID());

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_SAVE_FAILED, _data.getAttemptID()));
            }
        });

        // Send request
        queue.add(request);
    }


}
