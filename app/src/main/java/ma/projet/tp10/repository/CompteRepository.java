package ma.projet.tp10.repository;

import ma.projet.tp10.api.CompteService;
import ma.projet.tp10.entities.Compte;
import ma.projet.tp10.entities.CompteList;
import ma.projet.tp10.config.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompteRepository {
    private CompteService compteService;
    private String format;

    // Constructeur pour initialiser le service avec le type de convertisseur
    public CompteRepository(String converterType) {
        compteService = RetrofitClient.getClient(converterType).create(CompteService.class);
        this.format = converterType;
    }

    // Méthode pour récupérer tous les comptes
    public void getAllCompte(Callback<List<Compte>> callback) {
        if ("JSON".equals(format)) {
            Call<List<Compte>> call = compteService.getAllCompteJson();
            call.enqueue(callback);
        } else {
            Call<CompteList> call = compteService.getAllCompteXml();
            call.enqueue(new Callback<CompteList>() {
                @Override
                public void onResponse(Call<CompteList> call, Response<CompteList> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Convertir CompteList en List<Compte>
                        List<Compte> comptes = response.body().getComptes();
                        callback.onResponse(null, Response.success(comptes));
                    }
                }

                @Override
                public void onFailure(Call<CompteList> call, Throwable t) {
                    // Gérer les erreurs ici si nécessaire
                }
            });
        }
    }

    // Méthode pour récupérer un compte par son ID
    public void getCompteById(Long id, Callback<Compte> callback) {
        Call<Compte> call = compteService.getCompteById(id);
        call.enqueue(callback);
    }

    // Méthode pour ajouter un compte
    public void addCompte(Compte compte, Callback<Compte> callback) {
        Call<Compte> call = compteService.addCompte(compte);
        call.enqueue(callback);
    }

    // Méthode pour mettre à jour un compte
    public void updateCompte(Long id, Compte compte, Callback<Compte> callback) {
        Call<Compte> call = compteService.updateCompte(id, compte);
        call.enqueue(callback);
    }

    // Méthode pour supprimer un compte
    public void deleteCompte(Long id, Callback<Void> callback) {
        Call<Void> call = compteService.deleteCompte(id);
        call.enqueue(callback);
    }
}
