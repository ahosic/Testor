package at.fhooe.mc.hosic.mobilelearningapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import at.fhooe.mc.hosic.mobilelearningapp.R;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.RecyclerViewClickListener;
import at.fhooe.mc.hosic.mobilelearningapp.models.QuizHighscore;

/**
 * Defines an adapter for displaying the scores of quizzes in a recycler view.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {

    private static RecyclerViewClickListener itemListener;
    private List<QuizHighscore> highscores;

    public ScoresAdapter(List<QuizHighscore> _highscores, RecyclerViewClickListener _listener) {
        highscores = _highscores;
        itemListener = _listener;
    }

    /**
     * Specifies which layout should be used for the items of the recycler view.
     *
     * @param _viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                   an adapter position.
     * @param _viewType  The view type of the new View.
     * @return The ScoreViewHolder
     */
    @Override
    public ScoreViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _viewType) {
        View v = LayoutInflater.from(_viewGroup.getContext()).inflate(R.layout.score_item, _viewGroup, false);
        ScoresAdapter.ScoreViewHolder svh = new ScoresAdapter.ScoreViewHolder(v);
        return svh;
    }

    /**
     * Specifies content of each item in the recycler view.
     *
     * @param _holder   The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param _position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ScoreViewHolder _holder, int _position) {
        QuizHighscore qh = highscores.get(_position);

        // Set values
        _holder.quizName.setText(qh.getQuiz().getName());
        _holder.attemptsCount.setText("" + qh.getAttemptCount());
        _holder.grade.setText("" + (int) qh.getHighscore().getGrade() + " %");
    }

    /**
     * Called by RecyclerView when it starts observing this Adapter.
     *
     * @param recyclerView The RecyclerView instance which started observing this adapter.
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    /**
     * Gets the item count of the list.
     *
     * @return The total number of items in the list.
     */
    @Override
    public int getItemCount() {
        return highscores.size();
    }

    /**
     * Holds the views that are used for displaying the content of the score items.
     */
    public static class ScoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout container;
        private TextView quizName;
        private TextView attemptsCount;
        private TextView grade;

        public ScoreViewHolder(View _itemView) {
            super(_itemView);

            container = (LinearLayout) _itemView.findViewById(R.id.score_item_container);
            quizName = (TextView) _itemView.findViewById(R.id.quiz_name);
            attemptsCount = (TextView) _itemView.findViewById(R.id.score_attempts_cnt);
            grade = (TextView) _itemView.findViewById(R.id.score_grade);

            // Set click listener
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View _view) {
            itemListener.recyclerViewListClicked(_view, this.getLayoutPosition());
        }
    }
}
