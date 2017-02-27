package at.fhooe.mc.hosic.mobilelearningapp.models;

import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuizDTO;

/**
 * Holds the highscore for a quiz.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizHighscore {
    private QuizDTO mQuiz;
    private Score mHighscore;
    private int mAttemptCount;

    public QuizHighscore(QuizDTO _quiz, Score _highscore, int _attemptCount) {
        mQuiz = _quiz;
        mHighscore = _highscore;
        mAttemptCount = _attemptCount;
    }

    public QuizDTO getQuiz() {
        return mQuiz;
    }

    public void setQuiz(QuizDTO _quiz) {
        mQuiz = _quiz;
    }

    public Score getHighscore() {
        return mHighscore;
    }

    public void setHighscore(Score _highscore) {
        mHighscore = _highscore;
    }

    public int getAttemptCount() {
        return mAttemptCount;
    }

    public void setAttemptCount(int _attemptCount) {
        mAttemptCount = _attemptCount;
    }
}
