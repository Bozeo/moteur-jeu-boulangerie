package com.boulangerie.moteur;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.boulangerie.moteur.model.Boulanger;
import com.boulangerie.moteur.model.Client;
import com.boulangerie.moteur.model.Four;
import com.boulangerie.moteur.model.Ingredient;
import com.boulangerie.moteur.model.PlanDeTravail;
import com.boulangerie.moteur.model.Recette;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boulangerie extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Boulanger boulanger;

    // Stations de la cuisine
    private PlanDeTravail bacFarine;
    private PlanDeTravail bacBeurre;
    private PlanDeTravail bacChocolat;
    private PlanDeTravail planDeTravail;
    private Four four;

    // Gestion des recettes et des clients
    private List<Recette> catalogueRecettes;
    private Client[] tablesClients = new Client[3];
    private String[] typesPlatsDisponibles = {"Baguette", "Croissant", "Pain Choco"};
    private int score = 0;
    private Random random;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        random = new Random();

        // Initialisation des recettes du jeu
        catalogueRecettes = new ArrayList<>();
        catalogueRecettes.add(new Recette("Croissant", "Farine", "Beurre", "Croissant Cru"));
        catalogueRecettes.add(new Recette("Pain Choco", "Farine", "Chocolat", "Pain Choco Cru"));

        // Position du boulanger
        boulanger = new Boulanger(300, 140, 35, 35);

        // Implantation des stations en bas
        bacFarine = new PlanDeTravail(50, 200, 55, 55);
        bacBeurre = new PlanDeTravail(150, 200, 55, 55);
        bacChocolat = new PlanDeTravail(250, 200, 55, 55);
        planDeTravail = new PlanDeTravail(380, 200, 65, 65);
        four = new Four(510, 200, 65, 65);

        // Initialisation de la salle à manger (haut)
        tablesClients[0] = new Client("Croissant");
        tablesClients[1] = new Client("Baguette");
        tablesClients[2] = null;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 1. Logique
        boulanger.update(delta);
        four.update(delta);

        // Gestion de l'apparition et patience des clients
        for (int i = 0; i < 3; i++) {
            if (tablesClients[i] != null) {
                tablesClients[i].update(delta);
                if (tablesClients[i].getPatience() <= 0) {
                    tablesClients[i] = null; // Le client s'en va mécontent
                }
            } else {
                if (random.nextFloat() < 0.004f) {
                    String commandeAleatoire = typesPlatsDisponibles[random.nextInt(typesPlatsDisponibles.length)];
                    tablesClients[i] = new Client(commandeAleatoire);
                }
            }
        }

        // Limites de déplacement du boulanger
        if (boulanger.getX() < 30) boulanger.setX(30);
        if (boulanger.getX() > 650) boulanger.setX(650);
        if (boulanger.getY() < 25) boulanger.setY(25);
        if (boulanger.getY() > 350) boulanger.setY(350);

        // 2. Interactions clavier (ESPACE)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            // Distributeur de Farine
            if (Math.abs(boulanger.getX() - bacFarine.getX()) < 50 && Math.abs(boulanger.getY() - bacFarine.getY()) < 50) {
                if (boulanger.getIngredientPorte() == null) {
                    boulanger.setIngredientPorte(new Ingredient(0, 0, 25, 25, "Farine"));
                }
            }
            // Distributeur de Beurre
            if (Math.abs(boulanger.getX() - bacBeurre.getX()) < 50 && Math.abs(boulanger.getY() - bacBeurre.getY()) < 50) {
                if (boulanger.getIngredientPorte() == null) {
                    boulanger.setIngredientPorte(new Ingredient(0, 0, 25, 25, "Beurre"));
                }
            }
            // Distributeur de Chocolat
            if (Math.abs(boulanger.getX() - bacChocolat.getX()) < 50 && Math.abs(boulanger.getY() - bacChocolat.getY()) < 50) {
                if (boulanger.getIngredientPorte() == null) {
                    boulanger.setIngredientPorte(new Ingredient(0, 0, 25, 25, "Chocolat"));
                }
            }

            // Plan de travail (Assemblage des recettes ou pose/récupération)
            if (Math.abs(boulanger.getX() - planDeTravail.getX()) < 55 && Math.abs(boulanger.getY() - planDeTravail.getY()) < 55) {
                if (boulanger.getIngredientPorte() == null && !planDeTravail.estVide()) {
                    boulanger.setIngredientPorte(planDeTravail.getIngredientPose());
                    planDeTravail.setIngredientPose(null);
                } else if (boulanger.getIngredientPorte() != null && planDeTravail.estVide()) {
                    planDeTravail.setIngredientPose(boulanger.getIngredientPorte());
                    boulanger.setIngredientPorte(null);
                } else if (boulanger.getIngredientPorte() != null && !planDeTravail.estVide()) {
                    // Logique de Recette : Combine l'ingrédient en main et celui sur la table
                    String itemTable = planDeTravail.getIngredientPose().getNom();
                    String itemMain = boulanger.getIngredientPorte().getNom();

                    if ((itemTable.equals("Farine") && (itemMain.equals("Beurre") || itemMain.equals("Chocolat"))) ||
                        (itemMain.equals("Farine") && (itemTable.equals("Beurre") || itemTable.equals("Chocolat")))) {

                        String resultat = itemTable.equals("Beurre") || itemMain.equals("Beurre") ? "Croissant Cru" : "Pain Choco Cru";
                        planDeTravail.setIngredientPose(new Ingredient(0, 0, 25, 25, resultat));
                        boulanger.setIngredientPorte(null);
                        System.out.println("-> Recette assemblée sur le plan de travail : " + resultat);
                    } else if (itemTable.equals("Farine") && itemMain.equals("Farine")) {
                        planDeTravail.setIngredientPose(new Ingredient(0, 0, 25, 25, "Baguette Crue"));
                        boulanger.setIngredientPorte(null);
                    }
                }
            }

            // Four Professionnel
            if (Math.abs(boulanger.getX() - four.getX()) < 55 && Math.abs(boulanger.getY() - four.getY()) < 55) {
                if (boulanger.getIngredientPorte() != null && four.estVide()) {
                    four.setIngredientInterne(boulanger.getIngredientPorte());
                    boulanger.setIngredientPorte(null);
                } else if (boulanger.getIngredientPorte() == null && !four.estVide()) {
                    boulanger.setIngredientPorte(four.retirerIngredient());
                }
            }

            // Livraison aux tables en haut
            if (boulanger.getY() >= 280 && boulanger.getIngredientPorte() != null) {
                Ingredient held = boulanger.getIngredientPorte();
                if (held.getEtat() == Ingredient.EtatIngredient.CUIT) {
                    int tableIndex = (int)((boulanger.getX() - 30) / 220);
                    if (tableIndex < 0) tableIndex = 0;
                    if (tableIndex > 2) tableIndex = 2;

                    if (tablesClients[tableIndex] != null) {
                        score += 200;
                        boulanger.setIngredientPorte(null);
                        tablesClients[tableIndex] = null; // Client servi !
                        System.out.println("-> Commande livrée avec succès ! Score : " + score);
                    }
                }
            }
        }

        // 3. Rendu
        Gdx.gl.glClearColor(0.06f, 0.05f, 0.08f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // --- SALLE À MANGER (HAUT) ---
        shapeRenderer.setColor(0.18f, 0.14f, 0.22f, 1);
        shapeRenderer.rect(0, 310, 720, 170);

        for (int i = 0; i < 3; i++) {
            float tableX = 50 + i * 220;
            shapeRenderer.setColor(0.40f, 0.25f, 0.15f, 1);
            shapeRenderer.rect(tableX, 340, 140, 45);

            if (tablesClients[i] != null) {
                shapeRenderer.setColor(0.85f, 0.45f, 0.35f, 1);
                shapeRenderer.rect(tableX + 50, 375, 40, 40);
            }
        }

        // Séparation
        shapeRenderer.setColor(0.12f, 0.09f, 0.06f, 1);
        shapeRenderer.rect(0, 300, 720, 10);

        // --- CUISINE (BAS) ---
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 10; col++) {
                if ((row + col) % 2 == 0) {
                    shapeRenderer.setColor(0.78f, 0.75f, 0.70f, 1);
                } else {
                    shapeRenderer.setColor(0.68f, 0.65f, 0.60f, 1);
                }
                shapeRenderer.rect(20 + col * 70, 20 + row * 60, 70, 60);
            }
        }

        // ==========================================
        // STATIONS & INGRÉDIENTS AVEC TEXTURE PROpre
        // ==========================================

        // Bac Farine
        shapeRenderer.setColor(0.20f, 0.45f, 0.75f, 1);
        shapeRenderer.rect(bacFarine.getX(), bacFarine.getY(), 55, 55);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(bacFarine.getX() + 12, bacFarine.getY() + 12, 31, 31);

        // Bac Beurre
        shapeRenderer.setColor(0.95f, 0.80f, 0.20f, 1);
        shapeRenderer.rect(bacBeurre.getX(), bacBeurre.getY(), 55, 55);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(bacBeurre.getX() + 12, bacBeurre.getY() + 12, 31, 31);

        // Bac Chocolat
        shapeRenderer.setColor(0.45f, 0.25f, 0.10f, 1);
        shapeRenderer.rect(bacChocolat.getX(), bacChocolat.getY(), 55, 55);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(bacChocolat.getX() + 12, bacChocolat.getY() + 12, 31, 31);

        // Plan de Travail
        shapeRenderer.setColor(0.50f, 0.30f, 0.15f, 1);
        shapeRenderer.rect(planDeTravail.getX(), planDeTravail.getY(), 65, 65);
        if (!planDeTravail.estVide()) {
            shapeRenderer.setColor(planDeTravail.getIngredientPose().getCouleur());
            shapeRenderer.rect(planDeTravail.getX() + 17, planDeTravail.getY() + 17, 31, 31);
        }

        // Four Pro
        shapeRenderer.setColor(0.42f, 0.45f, 0.48f, 1);
        shapeRenderer.rect(four.getX(), four.getY(), 65, 65);
        if (!four.estVide()) {
            shapeRenderer.setColor(four.getIngredientInterne().getCouleur());
            shapeRenderer.rect(four.getX() + 17, four.getY() + 17, 31, 31);
        }

        // ==========================================
        // BOULANGER & INGRÉDIENT PORTÉ
        // ==========================================
        shapeRenderer.setColor(0.95f, 0.95f, 0.98f, 1);
        shapeRenderer.rect(boulanger.getX(), boulanger.getY(), 35, 35);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(boulanger.getX() + 4, boulanger.getY() + 30, 27, 8);

        if (boulanger.getIngredientPorte() != null) {
            shapeRenderer.setColor(boulanger.getIngredientPorte().getCouleur());
            shapeRenderer.rect(boulanger.getX() + 5, boulanger.getY() + 42, 25, 25);
        }

        // Jauge de cuisson
        if (!four.estVide()) {
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(four.getX() - 5, four.getY() + 75, 75, 10);
            float prog = four.getProgressionCuisson();
            shapeRenderer.setColor(four.getIngredientInterne().getEtat() == Ingredient.EtatIngredient.CUIT ? Color.GREEN : Color.YELLOW);
            shapeRenderer.rect(four.getX() - 5, four.getY() + 75, 75 * prog, 10);
        }

        shapeRenderer.end();

        // ==========================================
        // HUD & COMMANDES CLIENTS
        // ==========================================
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "SCORE : " + score, 30, 455);
        font.draw(batch, "SALLE A MANGER", 310, 455);

        for (int i = 0; i < 3; i++) {
            float tableX = 50 + i * 220;
            if (tablesClients[i] != null) {
                font.setColor(Color.BLACK);
                font.draw(batch, "Veut: " + tablesClients[i].getcommande(), tableX + 25, 415);

                float patience = tablesClients[i].getPatience();
                font.setColor(patience > 30 ? Color.GREEN : Color.RED);
                font.draw(batch, (int)patience + "%", tableX + 45, 395);
            } else {
                font.setColor(Color.LIGHT_GRAY);
                font.draw(batch, "Table vide", tableX + 35, 410);
            }
        }

        font.setColor(Color.WHITE);
        font.draw(batch, "ZQSD : Bouger | ESPACE : Prendre, Combiner & Cuire", 30, 25);

        font.draw(batch, "Farine", bacFarine.getX() + 8, bacFarine.getY() - 8);
        font.draw(batch, "Beurre", bacBeurre.getX() + 8, bacBeurre.getY() - 8);
        font.draw(batch, "Choco", bacChocolat.getX() + 10, bacChocolat.getY() - 8);
        font.draw(batch, "Plan", planDeTravail.getX() + 18, planDeTravail.getY() - 8);
        font.draw(batch, "Four", four.getX() + 20, four.getY() - 8);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
