package be.thomasmore.drinkingbuds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getName();
    EditText Username, Email, Password,Weight;

    Button Register;
    FirebaseAuth Auth;
    FirebaseFirestore Store;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        Weight = findViewById(R.id.weight);
        Password = findViewById(R.id.password);
        Register = findViewById(R.id.register);

        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();

        if(Auth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        Register.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){

            final String email = Email.getText().toString().trim();
            String g = Weight.getText().toString().trim();
            final int weight = Integer.parseInt(Weight.getText().toString().trim());
            String password = Password.getText().toString().trim();
            final String username = Username.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                Email.setError("Email is required");
                return;
            }
            if(TextUtils.isEmpty(password)){
                Password.setError("Password is required");
                return;
            }

            if(TextUtils.isEmpty(g)){
                Email.setError("weight is required");
                return;
            }

            if(password.length() < 6){
                Password.setError("Password must contain 6 or more characters");
                return;
            }

            Auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                        userID = Auth.getCurrentUser().getUid();
                        DocumentReference documentReference = Store.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("email", email);
                        user.put("weight", weight);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG , "User registered");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.v(TAG, "User registration failed");
                            }
                        });

                        startActivity(new Intent(getApplicationContext(),BierLijst.class));
                    }else{
                        Toast.makeText(Register.this, "Error! "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    });

}
    public void loginPage(View view){


        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }

}
