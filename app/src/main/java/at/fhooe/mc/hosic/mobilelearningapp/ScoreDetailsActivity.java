package at.fhooe.mc.hosic.mobilelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import at.fhooe.mc.hosic.mobilelearningapp.adapters.ScoreDetailsAdapter;
import at.fhooe.mc.hosic.mobilelearningapp.models.AuthenticationModel;
import at.fhooe.mc.hosic.mobilelearningapp.models.Score;
import at.fhooe.mc.hosic.mobilelearningapp.models.ScoreDatabaseHandler;

/**
 * Activity for showing all attempts with grades of a specific quiz.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class ScoreDetailsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mQuizID;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_details);

        // Initialize Recycler View
        mRecyclerView = (RecyclerView) findViewById(R.id.scoredetails_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);

        // Set dividers
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Set layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (savedInstanceState != null) {
            mQuizID = savedInstanceState.getInt("quizid");
            mTitle = savedInstanceState.getString("quizname");
        } else {

            // Get intent
            Intent i = getIntent();
            mQuizID = i.getIntExtra("quizid", -1);
            mTitle = i.getStringExtra("quizname");

            if (mQuizID == -1) {
                Toast.makeText(this, R.string.scoredetails_load, Toast.LENGTH_SHORT).show();
                finish();

                return;
            }
        }

        // Set title of app bar
        setTitle(mTitle);

        // Load scores
        loadScores();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("quizid", mQuizID);
        outState.putString("quizname", mTitle);
    }

    /**
     * Loads the scores of a quiz and sets the adapter of the recycle view.
     */
    private void loadScores() {
        int userid = AuthenticationModel.getInstance().getUserID();
        List<Score> scores = ScoreDatabaseHandler.getInstance().getScoresForQuiz(mQuizID, userid);

        mAdapter = new ScoreDetailsAdapter(scores);
        mRecyclerView.setAdapter(mAdapter);
    }
}
