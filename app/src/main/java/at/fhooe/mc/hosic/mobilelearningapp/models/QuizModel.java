package at.fhooe.mc.hosic.mobilelearningapp.models;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import at.fhooe.mc.hosic.mobilelearningapp.TestorApplication;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.GsonRequest;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.MessageType;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptDataDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptReviewDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.ProcessAttemptDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizAttemptDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizzesDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.SaveDataResponseDTO;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implements methods of the QuizDTO module of Moodle.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizModel extends BaseModel {
    // Static variables
    private static final String TAG = "QuizModel";
    private static final String QUIZ_PREFS = "QuizPrefs";
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
     * Gets a Quiz based on the ID.
     *
     * @param _id ID of the quiz
     * @return The quiz based on the provided ID.
     */
    public QuizDTO getQuizByID(int _id) {
        for (QuizDTO q : quizzes) {
            if (q.getID() == _id) {
                return q;
            }
        }

        return null;
    }

    /**
     * Gets a list of all quizzes with the total count of attempts and the highscore for each quiz.
     *
     * @return A list of objects of type QuizHighscore
     */
    public List<QuizHighscore> getQuizHighscores() {
        LinkedList<QuizHighscore> highscores = new LinkedList<QuizHighscore>();

        for (QuizDTO q : quizzes) {
            // Get highscore
            Score score = ScoreDatabaseHandler.getInstance().getHighscoreForQuiz(
                    q.getID(),
                    AuthenticationModel.getInstance().getUserID());

            // Get attempt count
            int count = ScoreDatabaseHandler.getInstance().getAttemptsCount(
                    q.getID(),
                    AuthenticationModel.getInstance().getUserID());

            if (score != null && count > 0) {
                highscores.add(new QuizHighscore(q, score, count));
            }
        }

        return highscores;
    }

    /**
     * Saves the ID of an open quiz attempt to shared preferences.
     *
     * @param _attemptID ID of the quiz attempt
     */
    private void saveOpenAttempt(int _attemptID) {
        Log.i(TAG, "Saving open attempt " + _attemptID);

        SharedPreferences sharedPref = TestorApplication.getContext().getSharedPreferences(QUIZ_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Get all ids
        String userid = String.valueOf(AuthenticationModel.getInstance().getUserID());
        HashSet<String> ids = (HashSet<String>) sharedPref.getStringSet(userid, null);

        // Check, if null
        if (ids == null) {
            ids = new HashSet<String>();
        }

        // Add id
        ids.add(String.valueOf(_attemptID));

        // Save changes
        editor.putStringSet(userid, ids);
        editor.commit();
    }

    /**
     * Removes the ID of an open attempt from shared preferences.
     *
     * @param _attemptID ID of the quiz attempt
     */
    private void removeOpenAttempt(int _attemptID) {
        Log.i(TAG, "Removing open attempt " + _attemptID);

        SharedPreferences sharedPref = TestorApplication.getContext().getSharedPreferences(QUIZ_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Get all ids
        String userid = String.valueOf(AuthenticationModel.getInstance().getUserID());
        HashSet<String> ids = (HashSet<String>) sharedPref.getStringSet(userid, null);

        // Check, if null
        if (ids != null && !ids.isEmpty()) {
            ids.remove(String.valueOf(_attemptID));
        }

        // Save changes
        if (!ids.isEmpty()) {
            editor.putStringSet(userid, ids);
        } else {
            editor.remove(userid);
        }

        editor.commit();
    }

    /**
     * Checks, if there are any open quiz attempts and finishes them.
     */
    public void finishAllOpenAttempts() {
        Log.i(TAG, "Finishing all open attempts");
        SharedPreferences sharedPref = TestorApplication.getContext().getSharedPreferences(QUIZ_PREFS, MODE_PRIVATE);

        // Get all ids
        String userid = String.valueOf(AuthenticationModel.getInstance().getUserID());
        HashSet<String> ids = (HashSet<String>) sharedPref.getStringSet(userid, null);

        // Check, if null
        if (ids != null && !ids.isEmpty()) {
            for (String id : ids) {
                // Finish all open attempts
                finishAttempt(Integer.parseInt(id), true);
            }
        }
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
                    instance.notifyObservers(new ModelChangedMessage(MessageType.LOAD_QUIZZES_FAILED, null));

                    return;
                }

                // Create list from Array
                quizzes = new LinkedList<>(Arrays.asList(response.getQuizzes()));

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.LOAD_QUIZZES_SUCCESS, null));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Get Quizzes failure");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.LOAD_QUIZZES_FAILED, null));
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
                Log.i(TAG, "Start Attempt response");

                if (response.getAttempt() == null || (response.getErrorcode() != null && !response.getErrorcode().equals("attemptstillinprogress"))) {
                    Log.i(TAG, "Attempt State invalid");

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_START_FAILED, null));

                    return;
                }

                Log.i(TAG, "Last attempt: " + response.getAttempt().getID());

                // Save open attempt id
                saveOpenAttempt(response.getAttempt().getID());

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_START_SUCCESS, response.getAttempt()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO failure");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_START_FAILED, null));
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
    public void finishAttempt(final int _attemptID, final boolean silentFlag) {
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

                    // Remove open attempt id
                    removeOpenAttempt(_attemptID);

                    // Notify observers
                    if (!silentFlag) {
                        instance.setChanged();
                        instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_FINISH_FAILED, _attemptID));
                    }

                    return;
                }

                Log.i(TAG, "Finished attempt: " + _attemptID);

                // Remove open attempt id
                removeOpenAttempt(_attemptID);

                // Notify observers
                if (!silentFlag) {
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_FINISH_SUCCESS, _attemptID));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO failure");

                // Notify observers
                if (!silentFlag) {
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_FINISH_FAILED, _attemptID));
                }
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
                    instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_DATA_FAILED, _attemptID));

                    return;
                }

                Log.i(TAG, "Data for attempt received: " + _attemptID);

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_DATA_SUCCESS, response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Could not get attempt data for attempt " + _attemptID);

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_DATA_FAILED, _attemptID));
            }
        });

        // Send request
        queue.add(request);
    }

    /**
     * Saves a selected answer of a quiz attempt on the Moodle server.
     *
     * @param _attemptID ID of quiz attempt
     * @param _qno       Number of question
     * @param _answer    Array of key and value of the selected answer
     * @param _sCheck    Sequence Check Number of question
     */
    public void saveAttemptData(final int _attemptID, int _qno, HashMap<String, String> _answer, int _sCheck) {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", AuthenticationModel.getInstance().getToken().getToken());
        params.put("wsfunction", "mod_quiz_save_attempt");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        String sCheckName = "q" + (_attemptID + 1) + ":" + _qno + "_:sequencecheck";

        HashMap<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("attemptid", "" + _attemptID);
        bodyParams.put("data[0][name]", sCheckName);
        bodyParams.put("data[0][value]", "" + _sCheck);
        bodyParams.put("data[1][name]", "attempt");
        bodyParams.put("data[1][value]", "" + _attemptID);

        // Add answers
        Iterator it = _answer.entrySet().iterator();
        int idx = 2;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            bodyParams.put("data[" + idx + "][name]", String.valueOf(pair.getKey()));
            bodyParams.put("data[" + idx + "][value]", String.valueOf(pair.getValue()));

            idx++;
            it.remove();
        }

        // Build request
        GsonRequest<SaveDataResponseDTO> request = new GsonRequest<>(Request.Method.POST, url, SaveDataResponseDTO.class, bodyParams, new Response.Listener<SaveDataResponseDTO>() {
            @Override
            public void onResponse(SaveDataResponseDTO response) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO response");

                if (response.getStatus() == null || !response.getStatus().equals("true")) {
                    Log.i(TAG, "Could not save attempt data for attempt " + _attemptID);

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_SAVE_FAILED, _attemptID));

                    return;
                }

                Log.i(TAG, "Data saved for attempt: " + _attemptID);

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_SAVE_SUCCESS, _attemptID));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Could not save attempt data for attempt " + _attemptID);

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_SAVE_FAILED, _attemptID));
            }
        });

        // Send request
        queue.add(request);
    }

    /**
     * Gets the feedback of a quiz attempt.
     *
     * @param _attemptID ID of the attempt
     */
    public void getAttemptFeedback(final int _attemptID) {
        // Define parameters
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("wstoken", AuthenticationModel.getInstance().getToken().getToken());
        params.put("wsfunction", "mod_quiz_get_attempt_review");
        params.put("moodlewsrestformat", "json");

        // Encode URL
        String url = encodeURLWithParams(BASE_URL, params);

        // Body parameters
        HashMap<String, String> bodyParams = new HashMap<String, String>();
        bodyParams.put("attemptid", "" + _attemptID);
        bodyParams.put("page", "-1");

        // Build request
        final GsonRequest<AttemptReviewDTO> request = new GsonRequest<>(Request.Method.POST, url, AttemptReviewDTO.class, bodyParams, new Response.Listener<AttemptReviewDTO>() {
            @Override
            public void onResponse(AttemptReviewDTO response) {
                Log.i(TAG, "Start QuizDTO AttemptInfoDTO response");

                if (response.getAttemptInfo() == null) {
                    Log.i(TAG, "Attempt Review: Attempt Info null");

                    // Notify observers
                    instance.setChanged();
                    instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_REVIEW_FAILED, _attemptID));

                    return;
                }

                Log.i(TAG, "Attempt Review received for attempt " + _attemptID);

                // Create score
                Score score = new Score(
                        response.getAttemptInfo().getID(),
                        response.getAttemptInfo().getUserID(),
                        response.getAttemptInfo().getQuizID(),
                        response.getGrade(),
                        Calendar.getInstance().getTime());

                // Save score
                ScoreDatabaseHandler.getInstance().addScore(score);

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_REVIEW_SUCCESS, response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Attempt Review Request failed.");

                // Notify observers
                instance.setChanged();
                instance.notifyObservers(new ModelChangedMessage(MessageType.ATTEMPT_REVIEW_FAILED, _attemptID));
            }
        });

        // Send request
        queue.add(request);
    }
}
