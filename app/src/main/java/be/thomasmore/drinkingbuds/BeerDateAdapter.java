package be.thomasmore.drinkingbuds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BeerDateAdapter extends FirestoreRecyclerAdapter<Beer, BeerDateAdapter.BeerDateHolder> {


    public BeerDateAdapter(@NonNull FirestoreRecyclerOptions<Beer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BeerDateHolder holder, int position, @NonNull Beer model) {
        holder.textViewNaam.setText(model.getName());
        holder.textViewAbv.setText(String.valueOf(model.getAbv()));
    }

    @NonNull
    @Override
    public BeerDateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_today, parent, false);
        return new BeerDateHolder(v);
    }

    class BeerDateHolder extends RecyclerView.ViewHolder{

        TextView textViewNaam;
        TextView textViewAbv;


        public BeerDateHolder(@NonNull View itemView) {
            super(itemView);
            textViewNaam = itemView.findViewById(R.id.text_view_bydate_name);
            textViewAbv = itemView.findViewById(R.id.text_view_bydate_abv);
        }
    }
}
