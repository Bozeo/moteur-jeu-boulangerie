package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Ingredient extends ElementDeJeu {
    private String nom;
    private EtatIngredient etat;

    public enum EtatIngredient {
        BRUT, PREPARE, CUIT, BRULE
    }

    public Ingredient(float x, float y, float largeur, float hauteur, String nom) {
        super(x, y, largeur, hauteur);
        this.nom = nom;
        this.etat = EtatIngredient.BRUT;
    }

    // Donne une couleur propre à chaque ingrédient et à son état
    public Color getCouleur() {
        if (etat == EtatIngredient.CUIT) {
            return new Color(1.0f, 0.6f, 0.1f, 1); // Doré / Cuit
        } else if (etat == EtatIngredient.BRULE) {
            return Color.BLACK; // Brûlé
        }

        // Selon le type d'ingrédient brut ou préparé
        switch (nom) {
            case "Farine":
                return new Color(0.95f, 0.95f, 0.90f, 1); // Blanc cassé
            case "Beurre":
                return new Color(0.95f, 0.85f, 0.30f, 1); // Jaune beurre
            case "Chocolat":
                return new Color(0.35f, 0.20f, 0.10f, 1); // Marron chocolat
            case "Croissant Cru":
                return new Color(0.85f, 0.70f, 0.50f, 1); // Beige croissant
            case "Pain Choco Cru":
                return new Color(0.50f, 0.30f, 0.20f, 1); // Marron clair
            default:
                return Color.WHITE;
        }
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public EtatIngredient getEtat() {
        return etat;
    }

    public void setEtat(EtatIngredient etat) {
        this.etat = etat;
    }
}
