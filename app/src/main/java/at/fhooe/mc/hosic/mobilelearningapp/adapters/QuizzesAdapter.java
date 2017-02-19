package at.fhooe.mc.hosic.mobilelearningapp.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.fhooe.mc.hosic.mobilelearningapp.R;
import at.fhooe.mc.hosic.mobilelearningapp.helpers.RecyclerViewClickListener;
import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;

/**
 * Defines an adapter for displaying Quizzes in a recycler view
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizzesAdapter extends RecyclerView.Adapter<QuizzesAdapter.QuizViewHolder> {

    private static RecyclerViewClickListener itemListener;
    private List<QuizDTO> quizzes;

    public QuizzesAdapter(List<QuizDTO> _quizzes, RecyclerViewClickListener _listener) {
        quizzes = _quizzes;
        itemListener = _listener;
    }

    /**
     * Specifies which layout should be used for the items of the recycler view.
     *
     * @param _viewGroup The ViewGroup into which the new View will be added after it is bound to
     *                   an adapter position.
     * @param _viewType  The view type of the new View.
     * @return The QuizViewHolder
     */
    @Override
    public QuizViewHolder onCreateViewHolder(ViewGroup _viewGroup, int _viewType) {
        View v = LayoutInflater.from(_viewGroup.getContext()).inflate(R.layout.quiz_card, _viewGroup, false);
        QuizViewHolder qvh = new QuizViewHolder(v);
        return qvh;
    }

    /**
     * Specifies content of each item in the recycler view.
     *
     * @param _holder   The ViewHolder which should be updated to represent the contents of the
     *                  item at the given position in the data set.
     * @param _position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(QuizViewHolder _holder, int _position) {
        _holder.quizName.setText(quizzes.get(_position).getName());
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
    public static class QuizViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView cardView;
        private TextView quizName;

        public QuizViewHolder(View _itemView) {
            super(_itemView);

            cardView = (CardView) _itemView.findViewById(R.id.quiz_card_container);
            quizName = (TextView) _itemView.findViewById(R.id.quiz_name);

            // Set click listener
            cardView.setOnClickListener(this);
        }

        /**
         * Invoked, when the user clicks on the card view.
         *
         * @param _view The clicked view
         */
        @Override
        public void onClick(View _view) {
            itemListener.recyclerViewListClicked(_view, this.getLayoutPosition());
        }
    }
}
