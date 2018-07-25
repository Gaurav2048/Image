package com.image.imagenext.imagenextbig;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;

public class RegisterActivity extends AppCompatActivity {
EditText email;
EditText password;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button = (Button) findViewById(R.id.button);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText().toString())&& TextUtils.isEmpty(password.getText().toString())){

                 }else {
                    LoginUser();
                }
            }
        });
}
    private void LoginUser() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent intent = new Intent(getApplicationContext(), NextActivity.class);
                startActivity(intent);
                finish();

            }

        });

    }
}