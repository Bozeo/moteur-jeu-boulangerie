package com.boulangerie.moteur.model;

import java.util.List;

public class Recette {
    private String nomRecette;
    private String ingredientPrincipal;
    private String garniture;
    private String resultat;

    public Recette(String nomRecette, String ingredientPrincipal, String garniture, String resultat) {
        this.nomRecette = nomRecette;
        this.ingredientPrincipal = ingredientPrincipal;
        this.garniture = garniture;
        this.resultat = resultat;
    }

    public boolean correspond(String base, String ajoute) {
        return (this.ingredientPrincipal.equals(base) && this.garniture.equals(ajoute)) ||
            (this.ingredientPrincipal.equals(ajoute) && this.garniture.equals(base));
    }

    public String getNomRecette() {
        return nomRecette;
    }

    public String getResultat() {
        return resultat;
    }
}
