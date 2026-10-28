package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Four extends ElementDeJeu implements Interagissable {
    private Ingredient ingredientInterne;
    private float tempsCuisson;
    private final float TEMPS_CUISSON_MAX = 5.0f; // 5 secondes pour cuire
    private final float TEMPS_BRULURE_MAX = 10.0f; // 10 secondes -> l'ingrédient brûle

    public Four(float x, float y, float largeur, float hauteur) {
        super(x, y, largeur, hauteur);
        this.ingredientInterne = null;
        this.tempsCuisson = 0f;
    }

    @Override
    public void update(float delta) {
        // Si un ingrédient est dans le four, la cuisson progresse
        if (ingredientInterne != null) {
            tempsCuisson += delta;

            if (tempsCuisson >= TEMPS_BRULURE_MAX) {
                ingredientInterne.setEtat(Ingredient.EtatIngredient.BRULE);
            } else if (tempsCuisson >= TEMPS_CUISSON_MAX) {
                ingredientInterne.setEtat(Ingredient.EtatIngredient.CUIT);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Géré ultérieurement avec les textures graphiques
    }

    public void interagir() {
        if (ingredientInterne == null) {
            System.out.println("Four : Le four est vide.");
        } else {
            System.out.println("Four : Contient " + ingredientInterne.getNom() + " [État : " + ingredientInterne.getEtat() + " - Temps : " + (int)tempsCuisson + "s]");
        }
    }

    public boolean estVide() {
        return ingredientInterne == null;
    }

    public float getProgressionCuisson() {
        return Math.min(1.0f, tempsCuisson / TEMPS_BRULURE_MAX);
    }

    public Ingredient getIngredientInterne() {
        return ingredientInterne;
    }

    public void setIngredientInterne(Ingredient ingredient) {
        this.ingredientInterne = ingredient;
        this.tempsCuisson = 0f; // Réinitialisation du chrono à la pose
    }

    public Ingredient retirerIngredient() {
        Ingredient temp = this.ingredientInterne;
        this.ingredientInterne = null;
        this.tempsCuisson = 0f;
        return temp;
    }
}
