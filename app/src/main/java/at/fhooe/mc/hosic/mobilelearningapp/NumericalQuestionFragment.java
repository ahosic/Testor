package at.fhooe.mc.hosic.mobilelearningapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnlin.numberpicker.NumberPicker;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.MoodleAnswerType;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuestionDTO;


/**
 * Displays numerical answers for a quiz.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class NumericalQuestionFragment extends Fragment implements MoodleAnswerType {

    private static final String TAG = "NumericalFragment";

    private NumberPicker mNumberPicker;

    private int mAttemptID;
    private int mQuestionNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_numerical_question, container, false);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);

        if (savedInstanceState != null) {
            mAttemptID = savedInstanceState.getInt("attemptid");
            mQuestionNumber = savedInstanceState.getInt("qno");
        }

        return view;
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("attemptid", mAttemptID);
        outState.putInt("qno", mQuestionNumber);
    }

    /**
     * Sets the possible answers for displaying in the view.
     *
     * @param _question  Question data with answer possibilities
     * @param _attemptID ID of the quiz attempt
     */
    @Override
    public void setAnswers(QuestionDTO _question, int _attemptID) {
        Log.i(TAG, "setAnswers");

        mAttemptID = _attemptID;
        mQuestionNumber = _question.getQuestionNumber();
    }

    /**
     * Gets the answer that has been selected by the user.
     *
     * @return A String array containing a key and the value used for transmitting to the Moodle server.
     */
    @Override
    public String[] getSelectedAnswer() {
        Log.i(TAG, "getSelectedAnswer");

        int selectedValue = mNumberPicker.getValue();
        String ansName = "q" + (mAttemptID + 1) + ":" + mQuestionNumber + "_answer";
        String[] answer = new String[]{ansName, String.valueOf(selectedValue)};

        return answer;
    }

    /**
     * Checks, whether the user has selected an answer, or not.
     *
     * @return True, if the user has selected an answer, false if not.
     */
    @Override
    public boolean isAnswerSelected() {
        return true;
    }
}
