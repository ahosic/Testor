package at.fhooe.mc.hosic.mobilelearningapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.MoodleAnswerType;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.MoodleHTMLParser;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuestionDTO;
import me.grantland.widget.AutofitTextView;


/**
 * Displays multiple choice answers for a quiz.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class MultipleChoiceQuestionFragment extends Fragment implements MoodleAnswerType, View.OnClickListener {

    private static final String TAG = "MultipleChoiceFragment";

    private CardView mCardA;
    private CardView mCardB;
    private CardView mCardC;
    private CardView mCardD;
    private AutofitTextView mAnswerA;
    private AutofitTextView mAnswerB;
    private AutofitTextView mAnswerC;
    private AutofitTextView mAnswerD;

    private int mSelected = -1;
    private int mAttemptID;
    private int mQuestionNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multiple_choice_question, container, false);

        mCardA = (CardView) view.findViewById(R.id.quiz_card_A);
        mCardB = (CardView) view.findViewById(R.id.quiz_card_B);
        mCardC = (CardView) view.findViewById(R.id.quiz_card_C);
        mCardD = (CardView) view.findViewById(R.id.quiz_card_D);

        mAnswerA = (AutofitTextView) view.findViewById(R.id.answerA);
        mAnswerB = (AutofitTextView) view.findViewById(R.id.answerB);
        mAnswerC = (AutofitTextView) view.findViewById(R.id.answerC);
        mAnswerD = (AutofitTextView) view.findViewById(R.id.answerD);

        mCardA.setOnClickListener(this);
        mCardB.setOnClickListener(this);
        mCardC.setOnClickListener(this);
        mCardD.setOnClickListener(this);

        if (savedInstanceState != null) {
            mSelected = savedInstanceState.getInt("selected");
            mAttemptID = savedInstanceState.getInt("attemptid");
            mQuestionNumber = savedInstanceState.getInt("qno");
            selectAnswer();
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

        outState.putInt("selected", mSelected);
        outState.putInt("attemptid", mAttemptID);
        outState.putInt("qno", mQuestionNumber);
    }

    /**
     * Unselects all answers.
     */
    private void unselectAll() {
        Log.i(TAG, "Unselected all");

        mCardA.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));
        mCardB.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));
        mCardC.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));
        mCardD.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));

        mAnswerA.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
        mAnswerB.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
        mAnswerC.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
        mAnswerD.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
    }

    /**
     * Selects a specific answer and changes the background and text color.
     */
    private void selectAnswer() {
        Log.i(TAG, "Select Answer");

        unselectAll();

        // Change Background and Text Color of selected answer
        switch (mSelected) {
            case 0:
                mCardA.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
                mAnswerA.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));
                break;
            case 1:
                mCardB.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
                mAnswerB.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));
                break;
            case 2:
                mCardC.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
                mAnswerC.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));
                break;
            case 3:
                mCardD.setCardBackgroundColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
                mAnswerD.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.plainWhite));
                break;
        }
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
        mQuestionNumber = _question.getSlot();

        MoodleHTMLParser parser = new MoodleHTMLParser(_question.getHTML());
        String[] answers = parser.getMultiChoiceAnswers(mAttemptID, mQuestionNumber);

        // Set values
        mAnswerA.setText(answers[0]);
        mAnswerB.setText(answers[1]);
        mAnswerC.setText(answers[2]);
        mAnswerD.setText(answers[3]);

        unselectAll();
    }

    /**
     * Gets the answer that has been selected by the user.
     *
     * @return A Hashmap containing a key and the value used for transmitting to the Moodle server.
     */
    @Override
    public HashMap<String, String> getSelectedAnswer() {
        Log.i(TAG, "getSelectedAnswer");

        // Compute answer name
        String ansName = "q" + (mAttemptID + 1) + ":" + mQuestionNumber + "_answer";

        // Add selection
        HashMap<String, String> answer = new HashMap<String, String>();
        answer.put(ansName, String.valueOf(mSelected));

        return answer;
    }

    /**
     * Checks, whether the user has selected an answer, or not.
     *
     * @return True, if the user has selected an answer, false if not.
     */
    @Override
    public boolean isAnswerSelected() {
        return mSelected != -1;
    }

    /**
     * Gets invoked, when a user clicks on an answer
     *
     * @param _view The clicked view
     */
    @Override
    public void onClick(View _view) {
        mSelected = Integer.parseInt((String) _view.getTag());
        Log.i(TAG, "Answer " + mSelected + " selected");

        selectAnswer();
    }
}
