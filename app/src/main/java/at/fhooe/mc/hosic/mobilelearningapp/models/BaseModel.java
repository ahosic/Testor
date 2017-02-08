package at.fhooe.mc.hosic.mobilelearningapp.models;

import java.util.Observable;

/**
 * Represents the base model of all models used in the app.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class BaseModel extends Observable {
    protected final String BASE_URL = "https://fh-mlearning.moodlecloud.com/webservice/rest/";
    protected final String SERVICE_NAME = "create_category";
}
