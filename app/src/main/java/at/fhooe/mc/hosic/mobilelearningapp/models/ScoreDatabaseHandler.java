package at.fhooe.mc.hosic.mobilelearningapp.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import at.fhooe.mc.hosic.mobilelearningapp.TestorApplication;

/**
 * Implements methods for handling all database operations for score items.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class ScoreDatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "ScoreDatabaseHandler";
    // Database
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "testorQuizzes";
    // Scores: Table Name
    private static final String TABLE_SCORES = "scores";
    // Scores: Column Names
    private static final String KEY_ID = "id";
    private static final String KEY_ATTEMPT_ID = "attemptid";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_QUIZ_ID = "quizid";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_DATETIME = "datetime";
    private static ScoreDatabaseHandler instance = null;

    protected ScoreDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Returns an instance of the ScoreDatabaseHandler.
     *
     * @return Instance of ScoreDatabaseHandler
     */
    public static ScoreDatabaseHandler getInstance() {
        if (instance == null) {
            instance = new ScoreDatabaseHandler(TestorApplication.getContext());
        }
        return instance;
    }

    /**
     * Creates all tables of a database.
     *
     * @param _db The SQLite Database
     */
    @Override
    public void onCreate(SQLiteDatabase _db) {
        Log.i(TAG, "onCreate");

        // SQL statement
        String create_scores_table =
                "CREATE TABLE " + TABLE_SCORES + " ("
                        + KEY_ID + " INTEGER PRIMARY KEY, "
                        + KEY_ATTEMPT_ID + " INTEGER, "
                        + KEY_USER_ID + " INTEGER, "
                        + KEY_QUIZ_ID + " INTEGER, "
                        + KEY_GRADE + " REAL, "
                        + KEY_DATETIME + " INTEGER)";

        // Create table
        _db.execSQL(create_scores_table);
    }

    /**
     * Drops existing tables and creates new ones.
     *
     * @param _db         The SQLite Database
     * @param _oldVersion The old version
     * @param _newVersion The new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
        Log.i(TAG, "onUpgrade");

        // SQL statement
        String drop_scores_table = "DROP TABLE IF EXISTS " + TABLE_SCORES;

        // Drop old table
        _db.execSQL(drop_scores_table);

        // Create new table
        onCreate(_db);
    }

    /**
     * Adds a score to the scores table of the database.
     *
     * @param _score The score of a quiz attempt
     */
    public void addScore(Score _score) {
        Log.i(TAG, "Add score for attempt " + _score.getAttemptID());

        SQLiteDatabase db = this.getWritableDatabase();

        long millis = _score.getDateTime().getTime();

        ContentValues vals = new ContentValues();
        vals.put(KEY_ATTEMPT_ID, _score.getAttemptID());
        vals.put(KEY_USER_ID, _score.getUserID());
        vals.put(KEY_QUIZ_ID, _score.getQuizID());
        vals.put(KEY_GRADE, _score.getGrade());
        vals.put(KEY_DATETIME, _score.getDateTime().getTime());

        db.insert(TABLE_SCORES, null, vals);
    }

    /**
     * Gets all scores for a quiz.
     *
     * @param _quizid ID of the quiz
     * @param _userid ID of the user
     * @return A list of all scores of a quiz
     */
    public List<Score> getScoresForQuiz(int _quizid, int _userid) {
        Log.i(TAG, "Get scores for quiz " + _quizid + " and user " + _userid);

        LinkedList<Score> scores = new LinkedList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL statement
        String get_scores =
                "SELECT * "
                        + "FROM " + TABLE_SCORES + " WHERE "
                        + KEY_QUIZ_ID + " = " + _quizid + " AND "
                        + KEY_USER_ID + " = " + _userid;

        // Build object
        Cursor cursor = db.rawQuery(get_scores, null);
        if (cursor.moveToFirst()) {

            do {

                Score score = new Score(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getDouble(4),
                        new Date(cursor.getLong(5)));

                scores.add(score);
            } while (cursor.moveToNext());


            cursor.close();
        }

        return scores;
    }

    /**
     * Gets the highest score of a user for a quiz.
     *
     * @param _quizid ID of the quiz
     * @param _userid ID of the user
     * @return Highest score
     */
    public Score getHighscoreForQuiz(int _quizid, int _userid) {
        Log.i(TAG, "Get highscore for quiz " + _quizid + " and user " + _userid);

        SQLiteDatabase db = this.getReadableDatabase();

        // SQL statement
        String get_scores =
                "SELECT "
                        + KEY_ID + ", "
                        + KEY_ATTEMPT_ID + ", "
                        + KEY_USER_ID + ", "
                        + KEY_QUIZ_ID + ", "
                        + "MAX(" + KEY_GRADE + "), "
                        + KEY_DATETIME + " "
                        + "FROM " + TABLE_SCORES + " WHERE "
                        + KEY_QUIZ_ID + " = " + _quizid + " AND "
                        + KEY_USER_ID + " = " + _userid;

        // Build object
        Cursor cursor = db.rawQuery(get_scores, null);
        if (cursor != null) {
            cursor.moveToFirst();

            Score score = new Score(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getDouble(4),
                    new Date(cursor.getInt(5)));

            cursor.close();

            return score;
        }

        return null;
    }

    /**
     * Gets the total number of attempts of a user for a quiz.
     *
     * @param _quizid ID of the quiz
     * @param _userid ID of the user
     * @return Total number of attempts
     */
    public int getAttemptsCount(int _quizid, int _userid) {
        Log.i(TAG, "Get attempts count for quiz " + _quizid + " and user " + _userid);

        SQLiteDatabase db = this.getReadableDatabase();

        // SQL statement
        String get_count = "SELECT * FROM " + TABLE_SCORES
                + " WHERE "
                + KEY_QUIZ_ID + " = " + _quizid
                + " AND "
                + KEY_USER_ID + " = " + _userid;

        Cursor cursor = db.rawQuery(get_count, null);
        int count = cursor.getCount();

        cursor.close();

        return count;
    }

}
