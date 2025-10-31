package ma.projet.tp10.api;

import ma.projet.tp10.entities.Compte;
import ma.projet.tp10.entities.CompteList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CompteService {
    // Récupérer tous les comptes en JSON
    @GET("banque/comptes")
    @Headers("Accept: application/json")
    Call<List<Compte>> getAllCompteJson();

    // Récupérer tous les comptes en XML
    @GET("banque/comptes")
    @Headers("Accept: application/xml")
    Call<CompteList> getAllCompteXml();

    // Récupérer un compte par son ID
    @GET("banque/comptes/{id}")
    Call<Compte> getCompteById(@Path("id") Long id);

    // Ajouter un nouveau compte
    @POST("banque/comptes")
    Call<Compte> addCompte(@Body Compte compte);

    // Mettre à jour un compte existant
    @PUT("banque/comptes/{id}")
    Call<Compte> updateCompte(@Path("id") Long id, @Body Compte compte);

    // Supprimer un compte
    @DELETE("banque/comptes/{id}")
    Call<Void> deleteCompte(@Path("id") Long id);
}
