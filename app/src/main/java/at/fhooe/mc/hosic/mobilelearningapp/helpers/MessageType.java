package at.fhooe.mc.hosic.mobilelearningapp.helpers;

/**
 * Defines an enumeration of all types of messages.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public enum MessageType {
    AUTHENTICATION_OK,
    AUTHENTICATION_FAILED,

    LOAD_QUIZZES_SUCCESS,
    LOAD_QUIZZES_FAILED,

    ATTEMPT_START_SUCCESS,
    ATTEMPT_START_FAILED,

    ATTEMPT_FINISH_SUCCESS,
    ATTEMPT_FINISH_FAILED,

    ATTEMPT_DATA_SUCCESS,
    ATTEMPT_DATA_FAILED,

    ATTEMPT_SAVE_SUCCESS,
    ATTEMPT_SAVE_FAILED,

    ATTEMPT_REVIEW_SUCCESS,
    ATTEMPT_REVIEW_FAILED
}
