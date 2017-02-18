package at.fhooe.mc.hosic.mobilelearningapp.helpers;

/**
 * Defines an enumeration of all types of messages.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public enum MessageType {
    AUTHENTICATION_OK, AUTHENTICATION_FAILED, GET_QUIZZES_SUCCESS, GET_QUIZZES_FAILED,
    QUIZ_ATTEMPT_STARTED, QUIZ_ATTEMPT_FAILED, QUIZ_ATTEMPT_FINISHED_SUCCESS, QUIZ_ATTEMPT_FINISHED_FAILED,
    ATTEMPT_DATA_RECEIVED, ATTEMPT_DATA_FAILED, ATTEMPT_SAVE_SUCCESS, ATTEMPT_SAVE_FAILED
}
