package com.boulangerie.moteur.model;

public class Client {
    private String commande;
    private float patience; // de 100 à 0
    private boolean present;

    public Client(String commande) {
        this.commande = commande;
        this.patience = 100f;
        this.present = true;
    }

    public void update(float delta) {
        if (present) {
            patience -= delta * 2f; // Baisse progressive de la patience
            if (patience <= 0) {
                patience = 0;
            }
        }
    }

    public String getcommande() {
        return commande;
    }

    public float getPatience() {
        return patience;
    }

    public boolean isPresent() {
        return present && patience > 0;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }
}
