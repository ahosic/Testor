package at.fhooe.mc.hosic.mobilelearningapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import at.fhooe.mc.hosic.mobilelearningapp.adapters.ScoresAdapter;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.RecyclerViewClickListener;
import at.fhooe.mc.hosic.mobilelearningapp.models.QuizHighscore;
import at.fhooe.mc.hosic.mobilelearningapp.models.QuizModel;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;


/**
 * Fragment that displays all available scores of quizzes to the user.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class ScoresFragment extends Fragment implements Observer, RecyclerViewClickListener {

    private static final String TAG = "ScoresFragment";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

    private List<QuizHighscore> mHighscores;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        // Initialize Recycler View
        mRecyclerView = (RecyclerView) view.findViewById(R.id.scores_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // Initialize Progress bar
        mProgressBar = (ProgressBar) view.findViewById(R.id.scores_progress);
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFE91E63, android.graphics.PorterDuff.Mode.MULTIPLY);


        // Set layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Register observer and load Quizzes
        QuizModel.getInstance().addObserver(this);
        QuizModel.getInstance().loadQuizzes();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();

        QuizModel.getInstance().deleteObserver(this);
    }

    /**
     * Initializes an adapter for the recycler view.
     */
    public void setAdapter() {
        mHighscores = QuizModel.getInstance().getQuizHighscores();

        mAdapter = new ScoresAdapter(mHighscores, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Gets invoked on changes of an observable
     *
     * @param observable The changed observable
     * @param o          Possible arguments passed by the changed observable
     */
    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof ModelChangedMessage) {
            ModelChangedMessage msg = (ModelChangedMessage) o;

            switch (msg.getType()) {
                case LOAD_QUIZZES_SUCCESS:
                    Log.i(TAG, "Get Quizzes OK");
                    mProgressBar.setVisibility(View.GONE);
                    setAdapter();
                    break;
                case LOAD_QUIZZES_FAILED:
                    Log.i(TAG, "Get Quizzes failed");
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(TestorApplication.getContext(), R.string.get_quizzes_failed, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    /**
     * Gets invoked, when an item in a recycler view gets clicked.
     *
     * @param _view     The clicked view
     * @param _position The position of the item in its list.
     */
    @Override
    public void recyclerViewListClicked(View _view, int _position) {
        Log.i(TAG, "Scores View clicked at position " + _position);
        QuizDTO q = mHighscores.get(_position).getQuiz();

        QuizModel.getInstance().deleteObserver(this);

        Intent intent = new Intent(getActivity(), ScoreDetailsActivity.class);
        intent.putExtra("quizname", q.getName());
        intent.putExtra("quizid", q.getID());
        getActivity().startActivity(intent);
    }
}
