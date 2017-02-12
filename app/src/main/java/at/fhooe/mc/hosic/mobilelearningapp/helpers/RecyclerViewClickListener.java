package at.fhooe.mc.hosic.mobilelearningapp.helpers;

import android.view.View;

/**
 * An interface for a click listener for a RecyclerView.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public interface RecyclerViewClickListener {

    /**
     * Gets invoked, when an item in a recycler view gets clicked.
     *
     * @param _view     The clicked view
     * @param _position The position of the item in its list.
     */
    public void recyclerViewListClicked(View _view, int _position);
}
