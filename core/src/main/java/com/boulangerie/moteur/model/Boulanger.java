package com.boulangerie.moteur.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Boulanger extends ElementDeJeu {
    private float vitesse = 200f;
    private Ingredient ingredientPorte; // L'ingrédient tenu par le boulanger

    public Boulanger(float x, float y, float largeur, float hauteur) {
        super(x, y, largeur, hauteur);
        this.ingredientPorte = null;
    }

    @Override
    public void update(float delta) {
        // Déplacements fluides (ZQSD ou Flèches)
        if (Gdx.input.isKeyPressed(Input.Keys.Z) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += vitesse * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= vitesse * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= vitesse * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += vitesse * delta;
        }

        // Touche Espace pour interagir (exemple)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            marquerInteraction();
        }
    }

    public void marquerInteraction() {
        if (ingredientPorte == null) {
            System.out.println("Le boulanger essaie d'interagir (mains vides).");
        } else {
            System.out.println("Le boulanger pose : " + ingredientPorte.getNom());
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        // Rendu visuel du boulanger
    }

    public Ingredient getIngredientPorte() {
        return ingredientPorte;
    }

    public void setIngredientPorte(Ingredient ingredientPorte) {
        this.ingredientPorte = ingredientPorte;
    }
}
