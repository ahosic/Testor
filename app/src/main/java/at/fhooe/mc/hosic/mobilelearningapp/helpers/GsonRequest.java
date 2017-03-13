package at.fhooe.mc.hosic.mobilelearningapp.helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Implements a custom request type for requesting JSON data and converting them to a Java object.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> bodyParams;
    private final Response.Listener<T> listener;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param _method        Method type of the request
     * @param _url           URL of the request to make
     * @param _clazz         Relevant class object, for Gson's reflection
     * @param _bodyParams    Map of request parameters
     * @param _listener      Listener for response
     * @param _errorListener Listener for errors
     */
    public GsonRequest(int _method, String _url, Class<T> _clazz, Map<String, String> _bodyParams,
                       Response.Listener<T> _listener, Response.ErrorListener _errorListener) {
        super(_method, _url, _errorListener);
        this.clazz = _clazz;
        this.bodyParams = _bodyParams;
        this.listener = _listener;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return bodyParams != null ? bodyParams : super.getParams();
    }


    /**
     * When the response has been parsed, it gets delivered to the listener.
     *
     * @param response The response as a Java object
     */
    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    /**
     * Parses a JSON network response and creates a new Java object from it.
     *
     * @param response Network response
     * @return A response object containing the created Java object.
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
}