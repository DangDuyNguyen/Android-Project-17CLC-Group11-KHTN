package com.ndduy.gamecollection2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends Activity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MyActivity";
    final int LOGIN_REQUEST = 98;
    final int SIGNUP_REQUEST = 99;
    Button signup, closebtn;
    TextView link_login;
    EditText usernameEdit, passwordEdit, confirmEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        setComponent();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(SIGNUP_REQUEST, null);
                finish();
            }
        });

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("exit", true);
                setResult(SIGNUP_REQUEST, intent);
                finish();
            }
        });
    }

    private void setComponent(){
        signup = (Button) findViewById(R.id.btn_signup);
        link_login = (TextView) findViewById(R.id.link_login);
        closebtn = (Button) findViewById(R.id.signup_close_button);
        usernameEdit = (EditText) findViewById(R.id.usernameEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        confirmEdit = (EditText) findViewById(R.id.confirmEdit);
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signup.setEnabled(false);

        final LinearLayout progressBar = (LinearLayout) findViewById(R.id.progressView);
        progressBar.setVisibility(View.VISIBLE);

        String username = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String confirm = confirmEdit.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        //onSignupFailed();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 5000);
    }

    public void writeUsertoFirebase(){
        Map<String, Object> user = new HashMap<>();
        user.put("username", usernameEdit.getText().toString());
        user.put("password", passwordEdit.getText().toString());
        user.put("name", "My name is...");
        user.put("coin", "100");
        user.put("hungriness", 100);
        user.put("flattering", 100);
        user.put("sleepiness", 100);
        user.put("mood", 100);
        ArrayList<String> char_list = new ArrayList<>();
        ArrayList<String> bg_list = new ArrayList<>();
        user.put("character", char_list);
        user.put("background", bg_list);

        db.collection("user")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void onSignupSuccess() {
        writeUsertoFirebase();
        Toast.makeText(this, "Sign up successfully", Toast.LENGTH_LONG).show();
        signup.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra("username", usernameEdit.getText().toString());
        intent.putExtra("password", passwordEdit.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed, please try again", Toast.LENGTH_LONG).show();

        signup.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = usernameEdit.getText().toString();
        String confirm = confirmEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            usernameEdit.setError("required at least 3 characters");
            valid = false;
        } else {
            usernameEdit.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 30) {
            passwordEdit.setError("required between 4 - 30 alphanumeric characters");
            valid = false;
        } else {
            passwordEdit.setError(null);
        }

        if (!password.equals(confirm)){
            confirmEdit.setError("your confirmation mismatch");
            valid = false;
        }
        else{
            confirmEdit.setError(null);
        }
        return valid;
    }

}