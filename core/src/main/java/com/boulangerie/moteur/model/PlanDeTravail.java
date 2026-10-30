package com.boulangerie.moteur.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.List;

public class PlanDeTravail extends ElementDeJeu implements Interagissable {
    private Stock<Ingredient> stock;
    private String ingredientFourni; // null si c'est un plan de travail normal, sinon "Farine", "Beurre", etc.

    // Constructeur pour le plan de travail de mélange
    public PlanDeTravail(float x, float y, float largeur, float hauteur) {
        super(x, y, largeur, hauteur);
        this.stock = new Stock<>();
        this.ingredientFourni = null;
    }

    // Constructeur pour un bac distributeur d'ingrédient
    public PlanDeTravail(float x, float y, float largeur, float hauteur, String ingredientFourni) {
        super(x, y, largeur, hauteur);
        this.stock = new Stock<>();
        this.ingredientFourni = ingredientFourni;
    }

    @Override
    public void interagir(Boulanger boulanger, List<Recette> recettes) {
        // Cas 1 : C'est un bac distributeur (Farine, Beurre, Chocolat)
        if (ingredientFourni != null) {
            if (boulanger.getStock().estVide()) {
                boulanger.getStock().setElement(new Ingredient(0, 0, 25, 25, ingredientFourni));
            }
            return;
        }

        // Cas 2 : C'est le plan de travail de préparation
        Stock<Ingredient> mainBoulanger = boulanger.getStock();

        // Poser un objet sur la table vide
        if (!mainBoulanger.estVide() && stock.estVide()) {
            stock.setElement(mainBoulanger.retirer());
        }
        // Récupérer l'objet posé sur la table
        else if (mainBoulanger.estVide() && !stock.estVide()) {
            mainBoulanger.setElement(stock.retirer());
        }
        // Mélanger l'objet en main avec l'objet sur la table via le système de Recettes
        else if (!mainBoulanger.estVide() && !stock.estVide()) {
            Ingredient ingTable = stock.getElement();
            Ingredient ingMain = mainBoulanger.getElement();

            // On ne mélange que des éléments non cuits et non brûlés
            if (ingTable.getEtat().equals("BRUT") && ingMain.getEtat().equals("BRUT")) {
                for (Recette r : recettes) {
                    if (r.correspondAuMelange(ingTable.getNom(), ingMain.getNom())) {
                        stock.setElement(new Ingredient(0, 0, 25, 25, r.getResultatCru()));
                        mainBoulanger.vider();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void update(float delta) {}

    @Override
    public void render(SpriteBatch batch) {}

    public Stock<Ingredient> getStock() { return stock; }
}
