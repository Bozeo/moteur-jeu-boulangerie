package com.boulangerie.moteur.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Boulanger extends ElementDeJeu {
    private Stock<Ingredient> stock;
    private float vitesse = 220f;

    public Boulanger(float x, float y, float largeur, float hauteur) {
        super(x, y, largeur, hauteur);
        this.stock = new Stock<>();
    }

    @Override
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.Z) || Gdx.input.isKeyPressed(Input.Keys.UP)) y += vitesse * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= vitesse * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.Q) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= vitesse * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += vitesse * delta;

        // Nouvelles limites plus compactes et décalées à gauche
        if (x < 10) x = 10;
        if (x > 520) x = 520;
        if (y < 45) y = 45;
        if (y > 225) y = 225;
    }

    @Override
    public void render(SpriteBatch batch) {}

    public Stock<Ingredient> getStock() { return stock; }
}
