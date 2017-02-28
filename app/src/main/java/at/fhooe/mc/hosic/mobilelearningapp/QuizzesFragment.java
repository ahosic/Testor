package at.fhooe.mc.hosic.mobilelearningapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import at.fhooe.mc.hosic.mobilelearningapp.adapters.QuizzesAdapter;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.RecyclerViewClickListener;
import at.fhooe.mc.hosic.mobilelearningapp.models.QuizModel;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;


/**
 * Fragment that displays all available Quizzes to the user.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class QuizzesFragment extends Fragment implements Observer, RecyclerViewClickListener {

    private static final String TAG = "QuizzesFragment";

    private Activity mActivity;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quizzes, container, false);

        // Initialize Recycler View
        mRecyclerView = (RecyclerView) view.findViewById(R.id.quizzes_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);

        // Initialize Progress bar
        mProgressBar = (ProgressBar) view.findViewById(R.id.quizzes_progress);
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
        mAdapter = new QuizzesAdapter(QuizModel.getInstance().getQuizzes(), this);
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
        Log.i(TAG, "Quizzes View clicked at position " + _position);
        QuizDTO q = QuizModel.getInstance().getQuizzes().get(_position);

        QuizModel.getInstance().deleteObserver(this);

        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra("quizid", q.getID());
        intent.putExtra("started", false);
        intent.putExtra("quizname", q.getName());
        getActivity().startActivity(intent);
    }
}
