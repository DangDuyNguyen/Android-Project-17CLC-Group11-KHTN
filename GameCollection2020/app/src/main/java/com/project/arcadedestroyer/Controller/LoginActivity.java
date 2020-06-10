package com.project.arcadedestroyer.Controller;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.arcadedestroyer.R;

public class LoginActivity extends Activity {

    private static final String TAG = "MyActivity";
    final int LOGIN_REQUEST = 98;
    final int SIGNUP_REQUEST = 99;
    Button signin, closebtn;
    TextView link_signup;
    EditText usernameEdit, passwordEdit;
    boolean isLoginAfterSignup = false;
    boolean isAuthentication = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setComponent();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, SIGNUP_REQUEST);
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(LOGIN_REQUEST, null);
                finish();
            }
        });
    }

    private void setComponent(){
        signin = (Button) findViewById(R.id.btn_login);
        link_signup = (TextView) findViewById(R.id.link_signup);
        closebtn = (Button) findViewById(R.id.login_close_button);
        usernameEdit = (EditText) findViewById(R.id.login_usernameEdit);
        passwordEdit = (EditText) findViewById(R.id.login_passwordEdit);
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        signin.setEnabled(false);

        final LinearLayout progressBar = (LinearLayout) findViewById(R.id.login_progressView);
        progressBar.setVisibility(View.VISIBLE);

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        // implement check authentication
        db.collection("user")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0)
                                isAuthentication = true;
                            else
                                isAuthentication = false;
                        }
                        else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if (isAuthentication)
                            onLoginSuccess();
                        else
                            onLoginFailed();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SIGNUP_REQUEST) {
            if (data == null){

            }
            else if (data.getBooleanExtra("exit", false))
                finish();
            else{
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");

                usernameEdit.setText(username);
                passwordEdit.setText(password);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        signin.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra("username", usernameEdit.getText().toString());
        setResult(LOGIN_REQUEST, intent);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed, please try again", Toast.LENGTH_LONG).show();

        signin.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            usernameEdit.setError("required a valid username");
            valid = false;
        } else {
            usernameEdit.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 30) {
            passwordEdit.setError("required between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordEdit.setError(null);
        }

        return valid;
    }

}