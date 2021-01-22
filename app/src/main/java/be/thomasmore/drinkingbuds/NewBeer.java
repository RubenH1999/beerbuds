package be.thomasmore.drinkingbuds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewBeer extends AppCompatActivity {
    private static final String TAG = "custom beer";
    Button Add, Cancel;
    EditText Name, Abv;
    FirebaseFirestore Store;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beer);

        Name = findViewById(R.id.beer_name);
        Abv = findViewById(R.id.beer_abv);
        Add = findViewById(R.id.add_beer);
        Cancel = findViewById(R.id.cancel);


        Store = FirebaseFirestore.getInstance();

        Add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c);
                String beerName = Name.getText().toString().trim();

                double beerAbv = Integer.parseInt(Abv.getText().toString().trim());

                DocumentReference documentReference = Store.collection("users").document(currentUser.getUid()).collection("beers").document();
                Map<String, Object> date = new HashMap<>();
                date.put("date", formattedDate);
                date.put("name", beerName);
                date.put("abv", beerAbv);
                documentReference.set(date).addOnSuccessListener(new OnSuccessListener<Void>() {




                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG , "Beer added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.v(TAG, "adding beer failed");
                    }
                });

                startActivity(new Intent(getApplicationContext(),BierLijst.class));

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){



                startActivity(new Intent(getApplicationContext(),BierLijst.class));

            }
        });

    }
}
