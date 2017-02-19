package at.fhooe.mc.hosic.mobilelearningapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.MoodleHTMLParser;
import at.fhooe.mc.hosic.mobilelearningapp.models.QuizModel;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptDataDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptInfoDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuestionDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizSelectionData;

public class QuizActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, Observer {
    private static final String TAG = "QuizActivity";

    private ProgressDialog mProgressDialog;

    private BottomNavigationView mBottomNavigationView;
    private TextView mQuestion;
    private CardView mCardA;
    private CardView mCardB;
    private CardView mCardC;
    private CardView mCardD;
    private TextView mAnswerA;
    private TextView mAnswerB;
    private TextView mAnswerC;
    private TextView mAnswerD;

    private int mSelected = -1;
    private boolean mStarted;

    // QuizDTO data
    private int mQuizID;
    private int mAttemptID;
    private int mCurrentPage;
    private int mNextPage = 0;
    private int mSequenceCheck;
    private int mQuestionNumber;
    private int mSlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize Views
        mCardA = (CardView) findViewById(R.id.quiz_card_A);
        mCardB = (CardView) findViewById(R.id.quiz_card_B);
        mCardC = (CardView) findViewById(R.id.quiz_card_C);
        mCardD = (CardView) findViewById(R.id.quiz_card_D);
        mQuestion = (TextView) findViewById(R.id.question);
        mAnswerA = (TextView) findViewById(R.id.answerA);
        mAnswerB = (TextView) findViewById(R.id.answerB);
        mAnswerC = (TextView) findViewById(R.id.answerC);
        mAnswerD = (TextView) findViewById(R.id.answerD);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Initialize Progress dialog
        mProgressDialog = new ProgressDialog(QuizActivity.this, R.style.QuizDialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);

        Intent i = getIntent();

        // Get Intent content
        if (i != null) {
            mQuizID = i.getIntExtra("quizid", -1);
            mStarted = i.getBooleanExtra("started", false);
        }

        // Check, if there are saved states
        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
        } else {
            // Register observer
            QuizModel.getInstance().addObserver(this);
        }

        if (!mStarted) {
            mProgressDialog.setMessage(getString(R.string.start_quiz));
            mProgressDialog.show();

            // Start attempt
            QuizModel.getInstance().startAttempt(mQuizID);
        }
    }

    /**
     * Restores data from a bundle.
     *
     * @param _savedInstanceState Bundle, where data is saved.
     */
    private void restoreData(Bundle _savedInstanceState) {
        // Restore selection
        mSelected = _savedInstanceState.getInt("selected");
        switch (mSelected) {
            case 0:
                selectAnswer(mCardA, mAnswerA);
                break;
            case 1:
                selectAnswer(mCardB, mAnswerB);
                break;
            case 2:
                selectAnswer(mCardC, mAnswerC);
                break;
            case 3:
                selectAnswer(mCardD, mAnswerD);
                break;

        }

        // Restore member variables
        mStarted = _savedInstanceState.getBoolean("started");
        mQuizID = _savedInstanceState.getInt("quizid");
        mAttemptID = _savedInstanceState.getInt("attemptid");
        mCurrentPage = _savedInstanceState.getInt("currentpage");
        mNextPage = _savedInstanceState.getInt("nextpage");
        mSequenceCheck = _savedInstanceState.getInt("sequencecheck");
        mQuestionNumber = _savedInstanceState.getInt("questionnumber");
        mSlot = _savedInstanceState.getInt("slot");
    }

    /**
     * Saves all relevant member variables to a bundle for later restoration.
     *
     * @param outState Bundle, where member variables get saved.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("selected", mSelected);
        outState.putBoolean("started", mStarted);
        outState.putInt("quizid", mQuizID);

        outState.putInt("attemptid", mAttemptID);
        outState.putInt("currentpage", mCurrentPage);
        outState.putInt("nextpage", mNextPage);
        outState.putInt("sequencecheck", mSequenceCheck);
        outState.putInt("questionnumber", mQuestionNumber);
        outState.putInt("slot", mSlot);
    }

    /**
     * Quits observation of QuizModel, finishes QuizActivity and returns back to MainActivity.
     */
    @Override
    public void onBackPressed() {
        // Remove from observers
        QuizModel.getInstance().deleteObserver(this);

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * Selects the clicked answer.
     *
     * @param _view The clicked view.
     */
    public void onClickAnswerA(View _view) {
        Log.i(TAG, "Answer A selected");
        mSelected = 0;
        selectAnswer(mCardA, mAnswerA);
    }

    /**
     * Selects the clicked answer.
     *
     * @param _view The clicked view.
     */
    public void onClickAnswerB(View _view) {
        Log.i(TAG, "Answer B selected");
        mSelected = 1;
        selectAnswer(mCardB, mAnswerB);
    }

    /**
     * Selects the clicked answer.
     *
     * @param _view The clicked view.
     */
    public void onClickAnswerC(View _view) {
        Log.i(TAG, "Answer C selected");
        mSelected = 2;
        selectAnswer(mCardC, mAnswerC);
    }

    /**
     * Selects the clicked answer.
     *
     * @param _view The clicked view.
     */
    public void onClickAnswerD(View _view) {
        Log.i(TAG, "Answer D selected");
        mSelected = 3;
        selectAnswer(mCardD, mAnswerD);
    }

    /**
     * Selects a specific answer and changes the background and text color.
     *
     * @param _card The selected card
     * @param _text The selected answer text
     */
    private void selectAnswer(CardView _card, TextView _text) {
        Log.i(TAG, "Select Answer");

        mCardA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));
        mCardB.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));
        mCardC.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));
        mCardD.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));

        _card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));

        mAnswerA.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mAnswerB.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mAnswerC.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mAnswerD.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        _text.setTextColor(ContextCompat.getColor(this, R.color.plainWhite));
    }

    /**
     * Requests the quiz data from the QuizModel.
     */
    private void getData() {
        if (mNextPage != -1) {
            mProgressDialog.setMessage(getString(R.string.get_question));
            mProgressDialog.show();

            // Get attempt data
            QuizModel.getInstance().getAttemptData(mAttemptID, mNextPage);
        } else {
            mProgressDialog.setMessage("Finishing QuizDTO ...");
            mProgressDialog.show();

            // Finish QuizDTO
            QuizModel.getInstance().finishAttempt(mAttemptID);
        }
    }

    /**
     * Processes the received quiz data and displays it to the user.
     *
     * @param _data The received quiz data
     */
    private void processData(AttemptDataDTO _data) {
        QuestionDTO q = _data.getQuestions()[0];

        // Set QuizDTO variables
        mCurrentPage = _data.getAttempt().getCurrentPage();
        mNextPage = _data.getNextPage();
        mSequenceCheck = q.getSequencecheck();
        mSlot = q.getSlot();
        mQuestionNumber = q.getQuestionNumber();

        MoodleHTMLParser parser = new MoodleHTMLParser(q.getHTML());
        String qText = parser.getQuestion();
        String[] answers = parser.getAnswers(mAttemptID, mQuestionNumber);

        // Set values
        mQuestion.setText(qText);
        mAnswerA.setText(answers[0]);
        mAnswerB.setText(answers[1]);
        mAnswerC.setText(answers[2]);
        mAnswerD.setText(answers[3]);

        mProgressDialog.hide();
    }

    /**
     * Saves the selected Answer.
     */
    private void saveData() {
        if (mSelected != -1) {
            QuizSelectionData data = new QuizSelectionData(mAttemptID, mQuestionNumber, mSelected, mSequenceCheck, mCurrentPage, mNextPage, mSlot);

            // Save AttemptInfoDTO Data
            QuizModel.getInstance().saveAttemptData(data);
        }
    }


    /**
     * Called when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not
     * be selected. Consider setting non-selectable items as disabled preemptively to
     * make them appear non-interactive.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_skip:
                Log.i(TAG, "Answer skipped");

                // TODO: Get next data

                // Finish attempt
                QuizModel.getInstance().finishAttempt(mAttemptID);

                break;
            case R.id.action_select:
                Log.i(TAG, "Answer selected");
                saveData();
                break;
        }

        return true;
    }

    /**
     * Invoked, when the QuizModel changes.
     *
     * @param observable The observed QuizModel
     * @param o          The ModelChangedMessage sent out by the QuizModel on its change.
     */
    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof ModelChangedMessage) {
            ModelChangedMessage msg = (ModelChangedMessage) o;

            // Hide dialog
            mProgressDialog.hide();

            switch (msg.getType()) {
                case QUIZ_ATTEMPT_STARTED:
                    Log.i(TAG, "Started attempt");

                    AttemptInfoDTO a = (AttemptInfoDTO) msg.getArgs();
                    mAttemptID = a.getID();
                    mCurrentPage = a.getID();

                    // Get data
                    getData();
                    break;
                case ATTEMPT_DATA_RECEIVED:
                    // Finish attempt
                    AttemptDataDTO data = (AttemptDataDTO) msg.getArgs();
                    Log.i(TAG, "AttemptInfoDTO data received.");

                    // Process data
                    processData(data);
                    break;
                case ATTEMPT_SAVE_SUCCESS:
                    Log.i(TAG, "Save successful: " + (int) msg.getArgs());

                    // Get next data
                    getData();
                    break;
                case QUIZ_ATTEMPT_FINISHED_SUCCESS:
                    Log.i(TAG, "Finished attempt: " + (int) msg.getArgs());

                    // Go back to MainActivity
                    this.onBackPressed();
                    break;
                case QUIZ_ATTEMPT_FAILED:
                    Log.i(TAG, "Starting attempt failed");
                    Toast.makeText(TestorApplication.getContext(), R.string.get_quizzes_failed, Toast.LENGTH_SHORT).show();

                    // Go back to MainActivity
                    this.onBackPressed();
                    break;
                case ATTEMPT_DATA_FAILED:
                    Log.i(TAG, "AttemptInfoDTO data receiving failed.");

                    // Finish attempt
                    QuizModel.getInstance().finishAttempt(mAttemptID);
                    break;
                case ATTEMPT_SAVE_FAILED:
                    Log.i(TAG, "Save failed " + (int) msg.getArgs());
                    Toast.makeText(TestorApplication.getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
                    break;
                case QUIZ_ATTEMPT_FINISHED_FAILED:
                    Log.i(TAG, "Finishing attempt " + (int) msg.getArgs() + " failed");
                    Toast.makeText(TestorApplication.getContext(), R.string.finish_failed, Toast.LENGTH_SHORT).show();

                    // Go back to MainActivity
                    this.onBackPressed();
                    break;
            }
        }
    }
}
