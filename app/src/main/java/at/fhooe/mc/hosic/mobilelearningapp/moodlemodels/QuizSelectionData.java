package at.fhooe.mc.hosic.mobilelearningapp.moodlemodels;

/**
 * Holds information about quiz data that should be saved on the Moodle server.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class QuizSelectionData {

    private int mAttemptID;
    private int mQuestionNumber;
    private int mAnswerNumber;
    private int mSequenceCheck;
    private int mCurrentPage;
    private int mNextPage;
    private int mSlot;

    public QuizSelectionData(int _attemptID, int _questionNumber, int _answerNumber, int _sequenceCheck, int _currentPage, int _nextPage, int _slot) {
        mAttemptID = _attemptID;
        mQuestionNumber = _questionNumber;
        mAnswerNumber = _answerNumber;
        mSequenceCheck = _sequenceCheck;
        mCurrentPage = _currentPage;
        mNextPage = _nextPage;
        mSlot = _slot;
    }

    public int getAttemptID() {
        return mAttemptID;
    }

    public void setAttemptID(int _attemptID) {
        mAttemptID = _attemptID;
    }

    public int getQuestionNumber() {
        return mQuestionNumber;
    }

    public void setQuestionNumber(int _questionNumber) {
        mQuestionNumber = _questionNumber;
    }

    public int getAnswerNumber() {
        return mAnswerNumber;
    }

    public void setAnswerNumber(int _answerNumber) {
        mAnswerNumber = _answerNumber;
    }

    public int getSequenceCheck() {
        return mSequenceCheck;
    }

    public void setSequenceCheck(int _sequenceCheck) {
        mSequenceCheck = _sequenceCheck;
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(int _currentPage) {
        mCurrentPage = _currentPage;
    }

    public int getNextPage() {
        return mNextPage;
    }

    public void setNextPage(int _nextPage) {
        mNextPage = _nextPage;
    }

    public int getSlot() {
        return mSlot;
    }

    public void setSlot(int _slot) {
        mSlot = _slot;
    }
}
