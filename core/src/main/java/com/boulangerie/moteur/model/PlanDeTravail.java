package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlanDeTravail extends ElementDeJeu implements Interagissable {
    private Ingredient ingredientPose; // L'ingrédient actuellement sur la table

    public PlanDeTravail(float x, float y, float largeur, float hauteur) {
        super(x, y, largeur, hauteur);
        this.ingredientPose = null; // Au départ, le plan de travail est vide
    }

    public void interagir() {
        if (ingredientPose == null) {
            System.out.println("Action : Le plan de travail est vide.");
        } else {
            System.out.println("Action : Récupération de l'ingrédient -> " + ingredientPose.getNom());
            // Ici, tu pourras transférer l'ingrédient dans les mains du boulanger
        }
    }

    @Override
    public void update(float delta) {
        // Mise à jour de la station de travail
    }

    @Override
    public void render(SpriteBatch batch) {
        // Affichage visuel de la table et de l'ingrédient s'il y en a un
    }

    public boolean estVide() {
        return ingredientPose == null;
    }

    public Ingredient getIngredientPose() {
        return ingredientPose;
    }

    public void setIngredientPose(Ingredient ingredientPose) {
        this.ingredientPose = ingredientPose;
    }
}
