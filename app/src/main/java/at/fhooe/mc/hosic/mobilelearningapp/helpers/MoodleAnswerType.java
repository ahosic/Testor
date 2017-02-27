package at.fhooe.mc.hosic.mobilelearningapp.helpers;

import at.fhooe.mc.hosic.mobilelearningapp.moodlemodels.QuestionDTO;

/**
 * Defines a common interface for Moodle answer types.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public interface MoodleAnswerType {

    /**
     * Sets the possible answers for displaying in the view.
     *
     * @param _question  Question data with answer possibilities
     * @param _attemptID ID of the quiz attempt
     */
    void setAnswers(QuestionDTO _question, int _attemptID);

    /**
     * Gets the answer that has been selected by the user.
     *
     * @return A String array containing a key and the value used for transmitting to the Moodle server.
     */
    String[] getSelectedAnswer();

    /**
     * Checks, whether the user has selected an answer, or not.
     *
     * @return True, if the user has selected an answer, false if not.
     */
    boolean isAnswerSelected();
}
