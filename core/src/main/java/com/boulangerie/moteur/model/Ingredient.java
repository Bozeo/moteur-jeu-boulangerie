package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Ingredient extends ElementDeJeu {
    private String nom;
    private String etat; // "BRUT", "CUIT", "BRULE"

    public Ingredient(float x, float y, float largeur, float hauteur, String nom) {
        super(x, y, largeur, hauteur);
        this.nom = nom;
        this.etat = "BRUT";
    }

    public Color getCouleur() {
        if (etat.equals("BRULE")) return Color.BLACK;
        if (etat.equals("CUIT")) return new Color(1.0f, 0.55f, 0.0f, 1);

        switch (nom) {
            case "Farine": return new Color(0.95f, 0.95f, 0.90f, 1);
            case "Beurre": return new Color(0.95f, 0.85f, 0.25f, 1);
            case "Chocolat": return new Color(0.35f, 0.20f, 0.10f, 1);
            case "Sauce Tomate": return new Color(0.85f, 0.20f, 0.20f, 1);
            case "Oignon": return new Color(0.80f, 0.70f, 0.85f, 1);
            case "Baguette Crue": return new Color(0.90f, 0.85f, 0.70f, 1);
            case "Croissant Cru": return new Color(0.85f, 0.70f, 0.45f, 1);
            case "Pain Choco Cru": return new Color(0.55f, 0.35f, 0.20f, 1);
            case "Pizza Crue": return new Color(0.90f, 0.50f, 0.40f, 1);
            case "Pissaladiere Crue": return new Color(0.85f, 0.80f, 0.65f, 1);
            default: return Color.WHITE;
        }
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEtat() { return etat; }
    public void setEtat(String etat) { this.etat = etat; }
}
