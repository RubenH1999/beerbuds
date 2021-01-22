package be.thomasmore.drinkingbuds;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BeerAdapter extends FirestoreRecyclerAdapter<Beer, BeerAdapter.BeerHolder> {

    private OnItemClickListener listener;
    public BeerAdapter(@NonNull FirestoreRecyclerOptions<Beer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull BeerHolder holder, int position, @NonNull Beer model) {
        holder.textViewNaam.setText(model.getName());
        holder.textViewAbv.setText(String.valueOf(model.getAbv()));
    }

    @NonNull
    @Override
    public BeerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.beer_item,parent,false);
        return new BeerHolder(v);
    }

    class BeerHolder extends RecyclerView.ViewHolder{

        TextView textViewNaam;
        TextView textViewAbv;

        public BeerHolder(@NonNull View itemView) {
            super(itemView);
            textViewNaam = itemView.findViewById(R.id.text_view_naam);
            textViewAbv = itemView.findViewById(R.id.text_view_abv);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                 int position = getAdapterPosition();
                 if(position != RecyclerView.NO_POSITION && listener != null){
                     listener.onItemClick(getSnapshots().getSnapshot(position), position);
                 }

                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
