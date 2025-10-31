package ma.projet.tp10;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ma.projet.tp10.adapter.CompteAdapter;
import ma.projet.tp10.entities.Compte;
import ma.projet.tp10.repository.CompteRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CompteAdapter.OnDeleteClickListener, CompteAdapter.OnUpdateClickListener {
    private RecyclerView recyclerView;
    private CompteAdapter adapter;
    private RadioGroup formatGroup;
    private FloatingActionButton addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupFormatSelection();
        setupAddButton();

        loadData("JSON");
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        formatGroup = findViewById(R.id.formatGroup);
        addbtn = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CompteAdapter(this, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupFormatSelection() {
        formatGroup.setOnCheckedChangeListener((group, checkedId) -> {
        String format = checkedId == R.id.radioJson ? "JSON" : "XML";
        loadData(format);
    });
    }

    private void setupAddButton() {
        addbtn.setOnClickListener(v -> showAddCompteDialog());
    }

    private void showAddCompteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);

        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);

        builder.setView(dialogView)
            .setTitle("Ajouter un compte")
            .setPositiveButton("Ajouter", (dialog, which) -> {
        String solde = etSolde.getText().toString();
        String type = typeGroup.getCheckedRadioButtonId() == R.id.radioCourant
        ? "COURANT" : "EPARGNE";

        String formattedDate = getCurrentDateFormatted();
        Compte compte = new Compte(null, Double.parseDouble(solde), type, formattedDate);
        addCompte(compte);
    })
        .setNegativeButton("Annuler", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getCurrentDateFormatted() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(calendar.getTime());
    }

    private void addCompte(Compte compte) {
        CompteRepository compteRepository = new CompteRepository("JSON");
        compteRepository.addCompte(compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    showToast("Compte ajouté");
                    loadData("JSON");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                showToast("Erreur lors de l'ajout");
            }
        });
    }

    private void loadData(String format) {
        CompteRepository compteRepository = new CompteRepository(format);
        compteRepository.getAllCompte(new Callback<List<Compte>>() {
            @Override
            public void onResponse(Call<List<Compte>> call, Response<List<Compte>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Compte> comptes = response.body();
                    runOnUiThread(() -> adapter.updateData(comptes));
                }
            }

            @Override
            public void onFailure(Call<List<Compte>> call, Throwable t) {
                showToast("Erreur: " + t.getMessage());
            }
        });
    }

    @Override
    public void onUpdateClick(Compte compte) {
        showUpdateCompteDialog(compte);
    }

    private void showUpdateCompteDialog(Compte compte) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_compte, null);

        EditText etSolde = dialogView.findViewById(R.id.etSolde);
        RadioGroup typeGroup = dialogView.findViewById(R.id.typeGroup);
        etSolde.setText(String.valueOf(compte.getSolde()));
        if (compte.getType().equalsIgnoreCase("COURANT")) {
            typeGroup.check(R.id.radioCourant);
        } else if (compte.getType().equalsIgnoreCase("EPARGNE")) {
            typeGroup.check(R.id.radioEpargne);
        }

        builder.setView(dialogView)
            .setTitle("Modifier un compte")
            .setPositiveButton("Modifier", (dialog, which) -> {
        String solde = etSolde.getText().toString();
        String type = typeGroup.getCheckedRadioButtonId() == R.id.radioCourant
        ? "COURANT" : "EPARGNE";
        compte.setSolde(Double.parseDouble(solde));
        compte.setType(type);
        updateCompte(compte);
    })
        .setNegativeButton("Annuler", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateCompte(Compte compte) {
        CompteRepository compteRepository = new CompteRepository("JSON");
        compteRepository.updateCompte(compte.getId(), compte, new Callback<Compte>() {
            @Override
            public void onResponse(Call<Compte> call, Response<Compte> response) {
                if (response.isSuccessful()) {
                    showToast("Compte modifié");
                    loadData("JSON");
                }
            }

            @Override
            public void onFailure(Call<Compte> call, Throwable t) {
                showToast("Erreur lors de la modification");
            }
        });
    }

    @Override
    public void onDeleteClick(Compte compte) {
        showDeleteConfirmationDialog(compte);
    }

    private void showDeleteConfirmationDialog(Compte compte) {
        new AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Voulez-vous vraiment supprimer ce compte ?")
            .setPositiveButton("Oui", (dialog, which) -> deleteCompte(compte))
        .setNegativeButton("Non", null)
        .show();
    }

    private void deleteCompte(Compte compte) {
        CompteRepository compteRepository = new CompteRepository("JSON");
        compteRepository.deleteCompte(compte.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showToast("Compte supprimé");
                    loadData("JSON");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showToast("Erreur lors de la suppression");
            }
        });
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show());
    }
}