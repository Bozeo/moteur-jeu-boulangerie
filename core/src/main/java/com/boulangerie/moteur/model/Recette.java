package com.boulangerie.moteur.model;

public class Recette {
    private String ingredient1;
    private String ingredient2;
    private String resultatCru;
    private String resultatCuit;

    public Recette(String ingredient1, String ingredient2, String resultatCru, String resultatCuit) {
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.resultatCru = resultatCru;
        this.resultatCuit = resultatCuit;
    }

    // Vérifie si les deux ingrédients (peu importe l'ordre) correspondent à cette recette
    public boolean correspondAuMelange(String nom1, String nom2) {
        return (nom1.equals(ingredient1) && nom2.equals(ingredient2)) ||
            (nom1.equals(ingredient2) && nom2.equals(ingredient1));
    }

    public String getResultatCru() { return resultatCru; }
    public String getResultatCuit() { return resultatCuit; }
}
