package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

import com.google.gson.annotations.SerializedName;

/**
 * Used for retrieving an array of quizzes from the Moodle server.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizzesDTO {
    @SerializedName("quizzes")
    private QuizDTO[] mQuizzes;

    public QuizzesDTO(QuizDTO[] _quizzes) {
        mQuizzes = _quizzes;
    }

    public QuizDTO[] getQuizzes() {
        return mQuizzes;
    }

    public void setQuizzes(QuizDTO[] _quizzes) {
        mQuizzes = _quizzes;
    }
}
