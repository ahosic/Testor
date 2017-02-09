package at.fhooe.mc.hosic.mobilelearningapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.models.AuthenticationModel;
import at.fhooe.mc.hosic.mobilelearningapp.models.Token;

/**
 * Manages the Login procedure.
 *
 * @author Almin Hosic
 * @version 1.0
 */

public class LoginActivity extends AppCompatActivity implements Observer {

    private static final String TAG = "LoginActivity";

    private EditText mUsername;
    private EditText mPassword;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        mUsername = (EditText) findViewById(R.id.input_username);
        mPassword = (EditText) findViewById(R.id.input_password);

        // Observe AuthenticationModel
        AuthenticationModel.getInstance().addObserver(this);
    }

    public void signIn(View _view) {
        Log.i(TAG, "signIn");

        mProgressDialog = new ProgressDialog(LoginActivity.this, R.style.AuthenticationDialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage(getString(R.string.authentication_dialog_text));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        // Authenticate at server
        AuthenticationModel.getInstance().authenticate(mUsername.getText().toString().trim(), mPassword.getText().toString());
    }

    /**
     * Gets invoked on changes of an observable
     *
     * @param observable The changed observable
     * @param o          Possible arguments passed by the changed observable
     */
    @Override
    public void update(Observable observable, Object o) {
        if (o instanceof ModelChangedMessage) {
            ModelChangedMessage msg = (ModelChangedMessage) o;

            switch (msg.getType()) {
                case AUTHENTICATION_OK:
                    Log.i(TAG, "Authentication OK");

                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.hide();
                    }

                    Token token = (Token) msg.getArgs();
                    Toast.makeText(getApplicationContext(), "" + token.getToken(), Toast.LENGTH_SHORT).show();

                    break;
                case AUTHENTICATION_FAILED:
                    Log.i(TAG, "Authentication failed");

                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.hide();
                    }

                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    }
}
