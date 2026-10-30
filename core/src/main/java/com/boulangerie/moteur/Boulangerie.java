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
import com.boulangerie.moteur.model.Interagissable;
import com.boulangerie.moteur.model.PlanDeTravail;
import com.boulangerie.moteur.model.Poubelle;
import com.boulangerie.moteur.model.Recette;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boulangerie extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Boulanger boulanger;
    private PlanDeTravail bacFarine, bacBeurre, bacChocolat, bacSauceTomate, bacOignon, planDeTravail;
    private Four four;
    private Poubelle poubelle;

    private List<Interagissable> stationsInteractives;
    private List<Recette> catalogueRecettes;
    private Client[] tablesClients = new Client[3];
    private String[] platsMenu = {"Baguette", "Croissant", "Pain Choco", "Pizza", "Pissaladière"};
    private int score = 0;
    private Random random;

    private boolean afficherLivre = false; // Gestion du livre de recettes

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        random = new Random();

        // 1. Catalogue complet des recettes (incluant Pizza et Pissaladière)
        catalogueRecettes = new ArrayList<>();
        catalogueRecettes.add(new Recette("Farine", "Farine", "Baguette Crue", "Baguette"));
        catalogueRecettes.add(new Recette("Farine", "Beurre", "Croissant Cru", "Croissant"));
        catalogueRecettes.add(new Recette("Croissant Cru", "Chocolat", "Pain Choco Cru", "Pain Choco"));
        catalogueRecettes.add(new Recette("Farine", "Sauce Tomate", "Pizza Crue", "Pizza"));
        catalogueRecettes.add(new Recette("Farine", "Oignon", "Pissaladiere Crue", "Pissaladière"));

        // 2. Initialisation des éléments de jeu (ajustés et compacts)
        boulanger = new Boulanger(180, 90, 28, 28);
        bacFarine = new PlanDeTravail(10, 130, 42, 42, "Farine");
        bacBeurre = new PlanDeTravail(60, 130, 42, 42, "Beurre");
        bacChocolat = new PlanDeTravail(110, 130, 42, 42, "Chocolat");
        bacSauceTomate = new PlanDeTravail(160, 130, 42, 42, "Sauce Tomate");
        bacOignon = new PlanDeTravail(210, 130, 42, 42, "Oignon");
        planDeTravail = new PlanDeTravail(270, 130, 48, 48);
        four = new Four(335, 130, 48, 48);
        poubelle = new Poubelle(400, 130, 42, 42);

        stationsInteractives = new ArrayList<>();
        stationsInteractives.add(bacFarine);
        stationsInteractives.add(bacBeurre);
        stationsInteractives.add(bacChocolat);
        stationsInteractives.add(bacSauceTomate);
        stationsInteractives.add(bacOignon);
        stationsInteractives.add(planDeTravail);
        stationsInteractives.add(four);
        stationsInteractives.add(poubelle);

        tablesClients[0] = new Client("Croissant");
        tablesClients[1] = new Client("Pizza");
        tablesClients[2] = null;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // Ouvrir ou fermer le livre de recettes avec la touche 'L'
        if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
            afficherLivre = !afficherLivre;
        }

        // Si le livre est ouvert, on met en pause le jeu
        if (!afficherLivre) {
            boulanger.update(delta);
            four.updateCuisson(delta, catalogueRecettes);

            for (int i = 0; i < 3; i++) {
                if (tablesClients[i] != null) {
                    tablesClients[i].update(delta);
                    if (!tablesClients[i].isPresent()) {
                        tablesClients[i] = null;
                    }
                } else if (random.nextFloat() < 0.004f) {
                    tablesClients[i] = new Client(platsMenu[random.nextInt(platsMenu.length)]);
                }
            }

            // Gestion de la touche ESPACE
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (boulanger.estProche(bacFarine, 45)) bacFarine.interagir(boulanger, catalogueRecettes);
                else if (boulanger.estProche(bacBeurre, 45)) bacBeurre.interagir(boulanger, catalogueRecettes);
                else if (boulanger.estProche(bacChocolat, 45)) bacChocolat.interagir(boulanger, catalogueRecettes);
                else if (boulanger.estProche(bacSauceTomate, 45)) bacSauceTomate.interagir(boulanger, catalogueRecettes);
                else if (boulanger.estProche(bacOignon, 45)) bacOignon.interagir(boulanger, catalogueRecettes);
                else if (boulanger.estProche(planDeTravail, 50)) planDeTravail.interagir(boulanger, catalogueRecettes);
                else if (boulanger.estProche(four, 50)) four.interagir(boulanger, catalogueRecettes);
                else if (boulanger.estProche(poubelle, 45)) poubelle.interagir(boulanger, catalogueRecettes);

                // Livraison aux clients
                if (boulanger.getY() >= 195 && !boulanger.getStock().estVide()) {
                    Ingredient platPorte = boulanger.getStock().getElement();
                    if (platPorte.getEtat().equals("CUIT")) {
                        int indexTable = Math.min(2, Math.max(0, (int) ((boulanger.getX() - 15) / 165)));
                        Client client = tablesClients[indexTable];
                        if (client != null && client.isPresent() && platPorte.getNom().equals(client.getCommande())) {
                            score += 200;
                            boulanger.getStock().vider();
                            tablesClients[indexTable] = null;
                        }
                    }
                }
            }
        }

        // ==================== RENDU GRAPHIQUE ====================
        Gdx.gl.glClearColor(0.08f, 0.07f, 0.10f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Zone Salle des clients
        shapeRenderer.setColor(0.18f, 0.14f, 0.22f, 1);
        shapeRenderer.rect(0, 238, 520, 130);
        for (int i = 0; i < 3; i++) {
            float tableX = 20 + i * 165;
            shapeRenderer.setColor(0.40f, 0.25f, 0.15f, 1);
            shapeRenderer.rect(tableX, 255, 115, 35);
            if (tablesClients[i] != null && tablesClients[i].isPresent()) {
                shapeRenderer.setColor(0.85f, 0.45f, 0.35f, 1);
                shapeRenderer.rect(tableX + 40, 285, 32, 32);
            }
        }

        // Comptoir de séparation
        shapeRenderer.setColor(0.12f, 0.09f, 0.06f, 1);
        shapeRenderer.rect(0, 230, 520, 8);

        // Dessin des Bacs
        shapeRenderer.setColor(0.20f, 0.45f, 0.75f, 1); shapeRenderer.rect(bacFarine.getX(), bacFarine.getY(), 42, 42);
        shapeRenderer.setColor(0.95f, 0.80f, 0.20f, 1); shapeRenderer.rect(bacBeurre.getX(), bacBeurre.getY(), 42, 42);
        shapeRenderer.setColor(0.45f, 0.25f, 0.10f, 1); shapeRenderer.rect(bacChocolat.getX(), bacChocolat.getY(), 42, 42);
        shapeRenderer.setColor(0.85f, 0.20f, 0.20f, 1); shapeRenderer.rect(bacSauceTomate.getX(), bacSauceTomate.getY(), 42, 42);
        shapeRenderer.setColor(0.80f, 0.70f, 0.85f, 1); shapeRenderer.rect(bacOignon.getX(), bacOignon.getY(), 42, 42);

        // Plan de travail + ingrédient posé
        shapeRenderer.setColor(0.55f, 0.35f, 0.20f, 1);
        shapeRenderer.rect(planDeTravail.getX(), planDeTravail.getY(), 48, 48);
        if (!planDeTravail.getStock().estVide()) {
            shapeRenderer.setColor(planDeTravail.getStock().getElement().getCouleur());
            shapeRenderer.rect(planDeTravail.getX() + 12, planDeTravail.getY() + 12, 24, 24);
        }

        // Four + ingrédient + barre de cuisson
        shapeRenderer.setColor(0.42f, 0.45f, 0.48f, 1);
        shapeRenderer.rect(four.getX(), four.getY(), 48, 48);
        if (!four.getStock().estVide()) {
            Ingredient ingFour = four.getStock().getElement();
            shapeRenderer.setColor(ingFour.getCouleur());
            shapeRenderer.rect(four.getX() + 12, four.getY() + 12, 24, 24);

            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(four.getX() - 4, four.getY() + 54, 56, 8);
            if (ingFour.getEtat().equals("BRULE")) shapeRenderer.setColor(Color.BLACK);
            else if (ingFour.getEtat().equals("CUIT")) shapeRenderer.setColor(Color.GREEN);
            else shapeRenderer.setColor(Color.YELLOW);
            shapeRenderer.rect(four.getX() - 4, four.getY() + 54, 56 * four.getProgressionCuisson(), 8);
        }

        // Poubelle
        shapeRenderer.setColor(0.65f, 0.20f, 0.20f, 1);
        shapeRenderer.rect(poubelle.getX(), poubelle.getY(), 42, 42);

        // Boulanger
        shapeRenderer.setColor(0.95f, 0.95f, 0.98f, 1);
        shapeRenderer.rect(boulanger.getX(), boulanger.getY(), boulanger.getLargeur(), boulanger.getHauteur());
        if (!boulanger.getStock().estVide()) {
            shapeRenderer.setColor(boulanger.getStock().getElement().getCouleur());
            shapeRenderer.rect(boulanger.getX() + 4, boulanger.getY() + 32, 20, 20);
        }

        shapeRenderer.end();

        // ==================== TEXTES & INTERFACE ====================
        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, "SCORE : " + score, 15, 360);
        font.draw(batch, "[L] Livre de Recettes", 360, 360);

        for (int i = 0; i < 3; i++) {
            float tableX = 20 + i * 165;
            if (tablesClients[i] != null && tablesClients[i].isPresent()) {
                font.setColor(Color.YELLOW);
                font.draw(batch, "Veut : " + tablesClients[i].getCommande(), tableX, 335);

                int secondes = (int) Math.ceil(tablesClients[i].getPatience());
                font.setColor(secondes > 15 ? Color.GREEN : Color.RED);
                font.draw(batch, "Patience : " + secondes + "s", tableX + 5, 275);
            } else {
                font.setColor(Color.GRAY);
                font.draw(batch, "Table libre", tableX + 20, 275);
            }
        }

        // Étiquettes des stations
        font.setColor(Color.CYAN);
        font.draw(batch, "Farine", bacFarine.getX() + 2, bacFarine.getY() + 56);
        font.draw(batch, "Beurre", bacBeurre.getX() + 2, bacBeurre.getY() + 56);
        font.draw(batch, "Choco", bacChocolat.getX() + 5, bacChocolat.getY() + 56);
        font.draw(batch, "Tomate", bacSauceTomate.getX(), bacSauceTomate.getY() + 56);
        font.draw(batch, "Oignon", bacOignon.getX() + 2, bacOignon.getY() + 56);
        font.draw(batch, "Travail", planDeTravail.getX() + 4, planDeTravail.getY() + 62);
        font.draw(batch, "Four", four.getX() + 12, four.getY() + 62);
        font.draw(batch, "Poubelle", poubelle.getX() - 2, poubelle.getY() + 56);

        if (!planDeTravail.getStock().estVide()) {
            font.setColor(Color.WHITE);
            font.draw(batch, planDeTravail.getStock().getElement().getNom(), planDeTravail.getX() - 5, planDeTravail.getY() + 62);
        }

        if (!boulanger.getStock().estVide()) {
            Ingredient port = boulanger.getStock().getElement();
            font.setColor(Color.YELLOW);
            font.draw(batch, port.getNom() + " (" + port.getEtat() + ")", boulanger.getX() - 15, boulanger.getY() + 60);
        }

        // Affichage du Livre de Recettes si ouvert
        if (afficherLivre) {
            batch.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.1f, 0.1f, 0.15f, 0.95f);
            shapeRenderer.rect(60, 50, 400, 260);
            shapeRenderer.end();

            batch.begin();
            font.setColor(Color.GOLD);
            font.draw(batch, "=== LIVRE DE RECETTES ===", 130, 290);
            font.setColor(Color.WHITE);
            font.draw(batch, "- Baguette : Farine + Farine -> Four", 80, 250);
            font.draw(batch, "- Croissant : Farine + Beurre -> Four", 80, 220);
            font.draw(batch, "- Pain Choco : Croissant Cru + Chocolat -> Four", 80, 190);
            font.draw(batch, "- Pizza : Farine + Sauce Tomate -> Four", 80, 160);
            font.draw(batch, "- Pissaladiere : Farine + Oignon -> Four", 80, 130);
            font.setColor(Color.YELLOW);
            font.draw(batch, "Appuyez sur [L] pour fermer le livre", 120, 75);
        } else {
            font.setColor(Color.LIGHT_GRAY);
            font.draw(batch, "ZQSD / Fleches : Bouger | ESPACE : Interagir | [L] Livre", 15, 16);
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
