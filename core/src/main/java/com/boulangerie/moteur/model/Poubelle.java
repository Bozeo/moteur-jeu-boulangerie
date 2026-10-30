package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.List;

public class Poubelle extends ElementDeJeu implements Interagissable {
    public Poubelle(float x, float y, float largeur, float hauteur) {
        super(x, y, largeur, hauteur);
    }

    @Override
    public void interagir(Boulanger boulanger, List<Recette> recettes) {
        boulanger.getStock().vider();
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}
}
