package ma.projet.tp10.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ma.projet.tp10.R;
import ma.projet.tp10.entities.Compte;

import java.util.ArrayList;
import java.util.List;

public class CompteAdapter extends RecyclerView.Adapter<CompteAdapter.CompteViewHolder> {
    public interface OnDeleteClickListener {
        void onDeleteClick(Compte compte);
    }
    public interface OnUpdateClickListener {
        void onUpdateClick(Compte compte);
    }

    private List<Compte> comptes;
    private OnDeleteClickListener onDeleteClickListener;
    private OnUpdateClickListener onUpdateClickListener;

    public CompteAdapter(OnDeleteClickListener onDeleteClickListener, OnUpdateClickListener onUpdateClickListener) {
        this.comptes = new ArrayList<>();
        this.onDeleteClickListener = onDeleteClickListener;
        this.onUpdateClickListener = onUpdateClickListener;
    }

    @NonNull
    @Override
    public CompteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_compte, parent, false);
        return new CompteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompteViewHolder holder, int position) {
        Compte compte = comptes.get(position);
        holder.bind(compte);
    }

    @Override
    public int getItemCount() {
        return comptes.size();
    }

    public void updateData(List<Compte> newComptes) {
        this.comptes.clear();
        this.comptes.addAll(newComptes);
        notifyDataSetChanged();
    }

    class CompteViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvSolde, tvType, tvDate;
        private View btnDelete, btnUpdate;

        public CompteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvSolde = itemView.findViewById(R.id.tvSolde);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUpdate = itemView.findViewById(R.id.btnEdit);
        }

        public void bind(Compte compte) {
            tvId.setText("ID: " + compte.getId());
            tvSolde.setText(String.format("Solde: %.2f", compte.getSolde()));
            tvType.setText("Type: " + compte.getType());
            tvDate.setText("Date: " + compte.getDateCreation());

            btnDelete.setOnClickListener(v -> {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDeleteClick(compte);
                }
            });
            btnUpdate.setOnClickListener(v -> {
                if (onUpdateClickListener != null) {
                    onUpdateClickListener.onUpdateClick(compte);
                }
            });
        }
    }
}
