package at.fhooe.mc.hosic.mobilelearningapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.MoodleHTMLParser;
import at.fhooe.mc.hosic.mobilelearningapp.models.QuizModel;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptDataDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptInfoDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.AttemptReviewDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuestionDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizSelectionData;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

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

    private View mReviewLayout;
    private AlertDialog mReviewDialog;
    private CircleProgressView mReviewProgress;
    private TextView mReviewQuizTitle;
    private TextView mReviewDescription;

    private int mSelected = -1;
    private boolean mStarted;
    private boolean mFinished = false;

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

        // Review Dialog
        LayoutInflater inflater = getLayoutInflater();
        mReviewLayout = inflater.inflate(R.layout.attempt_review_dialog, null);
        mReviewProgress = (CircleProgressView) mReviewLayout.findViewById(R.id.review_quiz_progress);
        mReviewQuizTitle = (TextView) mReviewLayout.findViewById(R.id.review_quiz_title);
        mReviewDescription = (TextView) mReviewLayout.findViewById(R.id.review_quiz_description);

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
        selectAnswer();

        // Restore member variables
        mFinished = _savedInstanceState.getBoolean("finished");
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

        outState.putBoolean("finished", mFinished);
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

        if (mReviewDialog != null) {
            mReviewDialog.dismiss();
            mReviewDialog = null;
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
     * Specifies the option menu of the app bar.
     *
     * @param menu The menu in which the items are being placed
     * @return true for showing the menu, false for not showing
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_attempt_menu, menu);
        return true;
    }

    /**
     * Selects the clicked answer.
     *
     * @param _view The clicked view.
     */
    public void onClickAnswer(View _view) {
        mSelected = Integer.parseInt((String) _view.getTag());
        Log.i(TAG, "Answer " + mSelected + " selected");

        selectAnswer();
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
                mCardA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mAnswerA.setTextColor(ContextCompat.getColor(this, R.color.plainWhite));
                break;
            case 1:
                mCardB.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mAnswerB.setTextColor(ContextCompat.getColor(this, R.color.plainWhite));
                break;
            case 2:
                mCardC.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mAnswerC.setTextColor(ContextCompat.getColor(this, R.color.plainWhite));
                break;
            case 3:
                mCardD.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
                mAnswerD.setTextColor(ContextCompat.getColor(this, R.color.plainWhite));
                break;
        }
    }

    /**
     * Unselects all answers.
     */
    private void unselectAll() {
        Log.i(TAG, "Unselected all");

        mCardA.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));
        mCardB.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));
        mCardC.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));
        mCardD.setCardBackgroundColor(ContextCompat.getColor(this, R.color.plainWhite));

        mAnswerA.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mAnswerB.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mAnswerC.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mAnswerD.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
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
            mProgressDialog.setMessage(getString(R.string.finishing_quiz));
            mProgressDialog.show();

            mFinished = true;

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

        unselectAll();
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
     * Gets the review of a quiz attempt after finish.
     */
    private void getReview() {
        QuizModel.getInstance().getAttemptFeedback(mAttemptID);
    }

    /**
     * Shows the review of a quiz attempt in a dialog.
     *
     * @param _review The review information of a quiz attempt.
     */
    private void showReview(AttemptReviewDTO _review) {
        if (mReviewLayout != null) {
            // Set Layout of dialog
            AlertDialog.Builder mReview = new AlertDialog.Builder(this);
            mReview.setView(mReviewLayout);
            mReview.setCancelable(false);

            final QuizActivity self = this;
            mReview.setPositiveButton("FINISH", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    self.onBackPressed();
                }
            });

            // Set Progress options
            mReviewProgress.setTextMode(TextMode.PERCENT);

            // Set Quiz Title
            QuizDTO quiz = QuizModel.getInstance().getQuizByID(_review.getAttemptInfo().getQuizID());
            if (quiz != null) {
                mReviewQuizTitle.setText(quiz.getName());
            }

            // Set Review Description
            mReviewDescription.setText(mFinished ? R.string.finished_quiz_description : R.string.quit_quiz_attempt);

            // Create dialog
            mReviewDialog = mReview.create();
            mReviewDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    Button btnPositive = mReviewDialog.getButton(Dialog.BUTTON_POSITIVE);
                    btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    btnPositive.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
                }
            });

            mReviewDialog.show();

            // Set value of the progress
            mReviewProgress.setValueAnimated(0, (float) _review.getGrade(), 2000);
        }
    }

    /**
     * Quits the quiz.
     */
    private void quitQuiz() {
        // Build Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you really want to quit the quiz?").setTitle("Quit");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i(TAG, "Quiz quit");

                mProgressDialog.setMessage(getString(R.string.finishing_quiz));
                mProgressDialog.show();

                // Finish attempt
                QuizModel.getInstance().finishAttempt(mAttemptID);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPositive = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);

                btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                btnPositive.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));

                btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                btnNegative.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
            }
        });

        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /**
     * Invoked, when an options menu item gets clicked.
     *
     * @param item The clicked item
     * @return true to display the item as the selected item and false if the item should not
     * be selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.quiz_quit:
                quitQuiz();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Invoked, when an item in the bottom navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not
     * be selected.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_skip:
                Log.i(TAG, "Answer skipped");

                // Get next data
                getData();
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
                case ATTEMPT_START_SUCCESS:
                    AttemptInfoDTO info = (AttemptInfoDTO) msg.getArgs();
                    Log.i(TAG, "Started attempt " + info.getID());

                    mAttemptID = info.getID();
                    mCurrentPage = info.getID();

                    // Get data
                    getData();
                    break;
                case ATTEMPT_DATA_SUCCESS:
                    // Finish attempt
                    AttemptDataDTO data = (AttemptDataDTO) msg.getArgs();
                    Log.i(TAG, "Attempt data received for attempt " + data.getAttempt().getID());

                    // Process data
                    processData(data);
                    break;
                case ATTEMPT_SAVE_SUCCESS:
                    Log.i(TAG, "Attempt save successful for attempt " + (int) msg.getArgs());

                    // Get next data
                    getData();
                    break;
                case ATTEMPT_FINISH_SUCCESS:
                    Log.i(TAG, "Finished attempt " + (int) msg.getArgs());

                    // Request review
                    getReview();
                    break;
                case ATTEMPT_REVIEW_SUCCESS:
                    AttemptReviewDTO review = (AttemptReviewDTO) msg.getArgs();
                    Log.i(TAG, "Fetched attempt review for attempt " + review.getAttemptInfo().getID());

                    // Show review
                    showReview(review);
                    break;
                case ATTEMPT_START_FAILED:
                    Log.i(TAG, "Starting attempt failed");
                    Toast.makeText(TestorApplication.getContext(), R.string.start_quiz_attempt, Toast.LENGTH_SHORT).show();

                    // Go back to MainActivity
                    this.onBackPressed();
                    break;
                case ATTEMPT_DATA_FAILED:
                    Log.i(TAG, "Attempt data receiving failed for attempt " + (int) msg.getArgs());

                    // Finish attempt
                    QuizModel.getInstance().finishAttempt(mAttemptID);
                    break;
                case ATTEMPT_SAVE_FAILED:
                    Log.i(TAG, "Attempt save failed for attempt " + (int) msg.getArgs());
                    Toast.makeText(TestorApplication.getContext(), R.string.save_failed, Toast.LENGTH_SHORT).show();
                    break;
                case ATTEMPT_FINISH_FAILED:
                    Log.i(TAG, "Finishing attempt failed for attempt " + (int) msg.getArgs());
                    Toast.makeText(TestorApplication.getContext(), R.string.finish_failed, Toast.LENGTH_SHORT).show();

                    // Go back to MainActivity
                    this.onBackPressed();
                    break;
                case ATTEMPT_REVIEW_FAILED:
                    Log.i(TAG, "Fetching attempt review failed for attempt " + (int) msg.getArgs());
                    Toast.makeText(TestorApplication.getContext(), R.string.finish_failed, Toast.LENGTH_SHORT).show();

                    // Go back to MainActivity
                    this.onBackPressed();
                    break;
            }
        }
    }
}
