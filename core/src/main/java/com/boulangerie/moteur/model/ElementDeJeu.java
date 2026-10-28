package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ElementDeJeu {
    protected float x;
    protected float y;
    protected float largeur;
    protected float hauteur;

    public ElementDeJeu(float x, float y, float largeur, float hauteur) {
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch);

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
}
