package at.fhooe.mc.hosic.mobilelearningapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Observable;
import java.util.Observer;

import at.fhooe.mc.hosic.mobilelearningapp.helpers.ModelChangedMessage;
import at.fhooe.mc.hosic.mobilelearningapp.models.AuthenticationModel;

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

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Calls Authentication methods for signing in the user.
     *
     * @param _view View that invoked the method
     */
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

                    // Go to MainActivity
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case AUTHENTICATION_FAILED:
                    Log.i(TAG, "Authentication failed");

                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.hide();
                    }

                    // Show info
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setMessage(R.string.login_failed_message).setTitle(R.string.login_failed_title);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button btnPositive = alertDialog.getButton(Dialog.BUTTON_POSITIVE);

                            btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                            btnPositive.setTextColor(ContextCompat.getColor(TestorApplication.getContext(), R.color.colorPrimary));
                        }
                    });

                    alertDialog.setCancelable(false);
                    alertDialog.show();

                    break;
            }
        }
    }
}
