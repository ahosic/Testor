package at.fhooe.mc.hosic.mobilelearningapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import at.fhooe.mc.hosic.mobilelearningapp.R;
import at.fhooe.mc.hosic.mobilelearningapp.models.AuthenticationModel;
import at.fhooe.mc.hosic.mobilelearningapp.models.Score;
import at.fhooe.mc.hosic.mobilelearningapp.models.ScoreDatabaseHandler;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;

/**
 * Defines an adapter for displaying the scores of quizzes in a recycler view
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ScoreViewHolder> {

    private List<QuizDTO> quizzes;

    public ScoresAdapter(List<QuizDTO> _quizzes) {
        quizzes = _quizzes;
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
        QuizDTO q = quizzes.get(_position);

        // Get score and attempt count
        Score score = ScoreDatabaseHandler.getInstance().getScoreForQuiz(
                q.getID(),
                AuthenticationModel.getInstance().getUserID());

        int count = ScoreDatabaseHandler.getInstance().getAttemptsCount(
                q.getID(),
                AuthenticationModel.getInstance().getUserID());

        // Set values
        if (score != null && count > 0) {
            _holder.quizName.setText(q.getName());
            _holder.attemptsCount.setText("" + count);
            _holder.grade.setText("" + (int) score.getGrade() + " %");
        } else {
            _holder.quizName.setText(q.getName());
            _holder.attemptsCount.setText("0");
            _holder.grade.setText("-");
        }
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
        return quizzes.size();
    }

    /**
     * Holds the views that are used for displaying the content of the quiz items.
     */
    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
