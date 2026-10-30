package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class ElementDeJeu {
    protected float x, y;
    protected float largeur, hauteur;

    public ElementDeJeu(float x, float y, float largeur, float hauteur) {
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

    public abstract void update(float delta);
    public abstract void render(SpriteBatch batch);

    public boolean estProche(ElementDeJeu autre, float distanceMax) {
        return Math.abs(this.x - autre.x) < distanceMax && Math.abs(this.y - autre.y) < distanceMax;
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    public float getLargeur() { return largeur; }
    public float getHauteur() { return hauteur; }
}
