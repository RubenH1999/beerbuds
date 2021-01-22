package be.thomasmore.drinkingbuds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference beerDateRef = db.collection("users").document(currentUser.getUid()).collection("beers");
    private DocumentReference weightRef = db.collection("users").document(currentUser.getUid());
    private BeerDateAdapter adapter;
    TextView username, email;
    FirebaseAuth Auth;
    FirebaseFirestore Store;
    String uID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);

        Auth = FirebaseAuth.getInstance();
        Store = FirebaseFirestore.getInstance();

        uID = Auth.getCurrentUser().getUid();

        DocumentReference documentReference = Store.collection("users").document(uID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                username.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
            }
        });

        setUpRecyclerView();
    }

    private void setUpRecyclerView(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);



        Query query = beerDateRef.whereEqualTo("date",formattedDate);
        FirestoreRecyclerOptions<Beer> options = new FirestoreRecyclerOptions.Builder<Beer>()
                .setQuery(query, Beer.class)
                .build();
        adapter = new BeerDateAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }





    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapter.startListening();
    }


    public void logout(View view){

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }


    public void beerlist(View view){


        startActivity(new Intent(getApplicationContext(),BierLijst.class));
        finish();
    }
    public void account(View view){


        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }



}
