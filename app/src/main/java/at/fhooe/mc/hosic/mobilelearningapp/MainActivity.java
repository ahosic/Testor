package at.fhooe.mc.hosic.mobilelearningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import at.fhooe.mc.hosic.mobilelearningapp.models.AuthenticationModel;
import at.fhooe.mc.hosic.mobilelearningapp.models.QuizModel;

/**
 * Main View of the App. Shows the Quizzes and Scores to the user.
 *
 * @author Almin Hosic
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private BottomNavigationView mBottomNavigationView;
    private QuizzesFragment mQuizzesFragment;
    private ScoresFragment mScoresFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set selection listener
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (findViewById(R.id.fragment_container) != null) {

            // If restored from previous state
            if (savedInstanceState == null) {

                // Create a new Fragment to be placed in the activity layout
                mQuizzesFragment = new QuizzesFragment();
                mScoresFragment = new ScoresFragment();

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mQuizzesFragment).commit();
            }
        }

        // Finish all open quiz attempts
        QuizModel.getInstance().finishAllOpenAttempts();
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
        inflater.inflate(R.menu.main_view_menu, menu);
        return true;
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
            case R.id.logout:
                // Detach fragments
                getSupportFragmentManager().beginTransaction().detach(mQuizzesFragment).commit();
                getSupportFragmentManager().beginTransaction().detach(mScoresFragment).commit();

                // Sign out
                AuthenticationModel.getInstance().signOut();

                // Go back to Login
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Listens for changes in the selection of menu items of bottom navigation listener
     *
     * @param item Selected menu item
     * @return true to display the item as the selected item and false if the item should not
     * be selected.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace fragment
        switch (item.getItemId()) {
            case R.id.action_quizzes:
                Log.i(TAG, "Loading quizzes fragment");
                transaction.replace(R.id.fragment_container, mQuizzesFragment, "quizFragment");
                break;
            case R.id.action_scores:
                Log.i(TAG, "Loading scores fragment");
                transaction.replace(R.id.fragment_container, mScoresFragment, "scoresFragment");
                break;
        }

        // End replacing transaction
        transaction.commit();

        return true;
    }
}
