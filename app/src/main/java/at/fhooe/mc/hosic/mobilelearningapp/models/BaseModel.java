package at.fhooe.mc.hosic.mobilelearningapp.models;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Observable;

/**
 * Represents the base model of all models used in the app.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class BaseModel extends Observable {
    protected final String BASE_URL = "https://fh-mlearning.moodlecloud.com/webservice/rest/";
    protected final String LOGIN_URL = "https://fh-mlearning.moodlecloud.com/login/token.php";
    protected final String SERVICE_NAME = "create_category";

    protected String encodeURLWithParams(String _url, Map<String, String> _params) {
        StringBuilder builder = new StringBuilder(_url);

        if (_params != null || !_params.isEmpty()) {
            boolean first = true;

            for (Map.Entry<String, String> entry : _params.entrySet()) {
                try {
                    String key = entry.getKey();
                    String val = URLEncoder.encode(entry.getValue(), "UTF-8");

                    if (first) {
                        builder.append("?");
                        first = false;
                    } else {
                        builder.append("&");
                    }

                    builder.append(key);
                    builder.append("=");
                    builder.append(val);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.toString();
    }
}
