package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.List;

public class Four extends ElementDeJeu implements Interagissable {
    private Stock<Ingredient> stock;
    private float tempsCuisson;
    private final float TEMPS_CUIT = 4.0f;
    private final float TEMPS_BRULE = 8.0f;

    public Four(float x, float y, float largeur, float hauteur) {
        super(x, y, largeur, hauteur);
        this.stock = new Stock<>();
        this.tempsCuisson = 0f;
    }

    public void updateCuisson(float delta, List<Recette> recettes) {
        if (!stock.estVide()) {
            tempsCuisson += delta;
            Ingredient ing = stock.getElement();

            if (tempsCuisson >= TEMPS_BRULE) {
                ing.setEtat("BRULE");
            } else if (tempsCuisson >= TEMPS_CUIT && ing.getEtat().equals("BRUT")) {
                boolean recetteTrouvee = false;
                for (Recette r : recettes) {
                    if (ing.getNom().equals(r.getResultatCru())) {
                        ing.setNom(r.getResultatCuit());
                        ing.setEtat("CUIT");
                        recetteTrouvee = true;
                        break;
                    }
                }
                if (!recetteTrouvee) {
                    ing.setEtat("BRULE");
                }
            }
        }
    }

    @Override
    public void interagir(Boulanger boulanger, List<Recette> recettes) {
        if (!boulanger.getStock().estVide() && stock.estVide()) {
            stock.setElement(boulanger.getStock().retirer());
            tempsCuisson = 0f;
        } else if (boulanger.getStock().estVide() && !stock.estVide()) {
            boulanger.getStock().setElement(stock.retirer());
            tempsCuisson = 0f;
        }
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}

    public Stock<Ingredient> getStock() { return stock; }
    public float getProgressionCuisson() { return Math.min(1.0f, tempsCuisson / TEMPS_BRULE); }
}
