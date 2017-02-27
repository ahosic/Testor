package at.fhooe.mc.hosic.mobilelearningapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.List;

import at.fhooe.mc.hosic.mobilelearningapp.R;
import at.fhooe.mc.hosic.mobilelearningapp.models.Score;

/**
 * Defines an adapter for displaying the score details of quizzes in a recycler view.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class ScoreDetailsAdapter extends RecyclerView.Adapter<ScoreDetailsAdapter.ScoreDetailsViewHolder> {

    private List<Score> mScores;

    public ScoreDetailsAdapter(List<Score> _scores) {
        mScores = _scores;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent
     * an item.
     *
     * @param _viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                   an adapter position.
     * @param _viewType  The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ScoreDetailsViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _viewType) {
        View v = LayoutInflater.from(_viewGroup.getContext()).inflate(R.layout.score_detail_item, _viewGroup, false);
        ScoreDetailsAdapter.ScoreDetailsViewHolder svh = new ScoreDetailsAdapter.ScoreDetailsViewHolder(v);
        return svh;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the itemView to reflect the item at the given
     * position.
     *
     * @param _holder   The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param _position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ScoreDetailsViewHolder _holder, int _position) {
        Score score = mScores.get(_position);

        // Convert to current timezone
        DateTime dt = new DateTime(score.getDateTime().getTime(), DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault());

        // Set values
        _holder.date.setText(dt.toString("MMM d, yyyy"));
        _holder.time.setText(dt.toString("H:m"));
        _holder.grade.setText("" + (int) score.getGrade() + " %");
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mScores.size();
    }

    /**
     * Holds the views that are used for displaying the content of the score details items.
     */
    public static class ScoreDetailsViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout container;
        private TextView date;
        private TextView time;
        private TextView grade;

        public ScoreDetailsViewHolder(View _itemView) {
            super(_itemView);

            container = (RelativeLayout) _itemView.findViewById(R.id.score_detail_container);
            date = (TextView) _itemView.findViewById(R.id.score_detail_date);
            time = (TextView) _itemView.findViewById(R.id.score_detail_time);
            grade = (TextView) _itemView.findViewById(R.id.score_detail_grade);
        }
    }
}
