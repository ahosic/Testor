package at.fhooe.mc.hosic.mobilelearningapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.LinkedList;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.MoodleAnswerType;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.MoodleHTMLParser;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuestionDTO;
import me.grantland.widget.AutofitTextView;


/**
 * Displays matching answers for a quiz.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class MatchingQuestionFragment extends Fragment implements MoodleAnswerType, AdapterView.OnItemSelectedListener {

    private static final String TAG = "MatchingFragment";

    private LayoutInflater mInflater;
    private LinearLayout mContainer;

    private int mAttemptID;
    private int mQuestionNumber;
    private HashMap<String, String> mSelections;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_matching_question, container, false);

        // Initialize container
        mContainer = (LinearLayout) view.findViewById(R.id.matching_answers_layout);

        // Initialize selections map
        mSelections = new HashMap<>();

        return view;
    }

    /**
     * Adds a matching question's layout to the parent layout.
     *
     * @param _text         Text of the answer
     * @param _options      Items of the spinner
     * @param _tag          Name of the spinner (use appropriate name for transmitting to Moodle)
     * @param _layoutWeight Weight of the layout based on the count of matching questions.
     */
    private void addMatchingQuestion(String _text, ArrayAdapter<String> _options, String _tag, float _layoutWeight) {
        Log.i(TAG, "addMatchingQuestion");

        // Get container of item
        LinearLayout answer = (LinearLayout) mInflater.inflate(R.layout.matching_item, null, false);
        answer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                _layoutWeight
        ));

        // Text of answer question
        AutofitTextView text = (AutofitTextView) answer.findViewById(R.id.matching_question);
        text.setText(_text);

        // Available options
        Spinner optionSpinner = (Spinner) answer.findViewById(R.id.matching_option);
        optionSpinner.setTag(_tag);
        optionSpinner.setAdapter(_options);
        optionSpinner.setOnItemSelectedListener(this);

        // Set initial selection
        mSelections.put(_tag, "0");

        // Add matching answer to view
        mContainer.addView(answer);
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

        // Set question attempt values
        mAttemptID = _attemptID;
        mQuestionNumber = _question.getSlot();

        // Get data
        MoodleHTMLParser parser = new MoodleHTMLParser(_question.getHTML());
        LinkedList<String>[] lists = parser.getMatchingAnswers();
        LinkedList<String> questions = lists[0];
        LinkedList<String> options = lists[1];

        // Set adapter
        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                options.toArray(new String[options.size()]));

        // Calculate layout weight based on item count
        float layoutWeight = 1.0f / questions.size();

        // Add answers
        int idx = 0;
        for (String s : questions) {
            // Compute name for answer
            String tag = "q" + (mAttemptID + 1) + ":" + mQuestionNumber + "_sub" + idx;
            idx++;

            addMatchingQuestion(s, optionsAdapter, tag, layoutWeight);
        }
    }

    /**
     * Gets the answer that has been selected by the user.
     *
     * @return A Hashmap containing a key and the value used for transmitting to the Moodle server.
     */
    @Override
    public HashMap<String, String> getSelectedAnswer() {
        return mSelections;
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

    /**
     * Invoked, when a user changes the selection of a spinner.
     *
     * @param _adapterView Spinner
     * @param _view        View
     * @param _pos         Position of the selected item
     * @param _id          ID of the view
     */
    @Override
    public void onItemSelected(AdapterView<?> _adapterView, View _view, int _pos, long _id) {
        Log.i(TAG, "Item selected");
        String tag = (String) _adapterView.getTag();
        mSelections.put(tag, String.valueOf(_pos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> _adapterView) {
        // Method not needed
    }
}
