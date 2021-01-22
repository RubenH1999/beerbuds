package be.thomasmore.drinkingbuds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BierLijst extends AppCompatActivity  {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference beerRef = db.collection("beers");
    private BeerAdapter adapter;
    private static final String TAG = "Bierlijst";
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bier_lijst);

        setUpRecyclerView();

        adapter.setOnItemClickListener(new BeerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String formattedDate = df.format(c);
                String name = documentSnapshot.getString("name");
                double abv = documentSnapshot.getDouble("abv");
                DocumentReference documentReference = db.collection("users").document(currentUser.getUid()).collection("beers").document();
                Map<String, Object> date = new HashMap<>();
                date.put("date", formattedDate);
                date.put("name", name);
                date.put("abv", abv);
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
                Toast.makeText(BierLijst.this, name + " added " , Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void setUpRecyclerView(){
        Query query = beerRef.orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Beer> options = new FirestoreRecyclerOptions.Builder<Beer>()
                .setQuery(query,Beer.class)
                .build();
        adapter = new BeerAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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
        adapter.stopListening();
    }

    public void account(View view){


        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    public void beerlist(MenuItem menuItem){


        startActivity(new Intent(getApplicationContext(),BierLijst.class));
        finish();
    }

    public void addcustom(View view){


        startActivity(new Intent(getApplicationContext(),NewBeer.class));
        finish();
    }
}
