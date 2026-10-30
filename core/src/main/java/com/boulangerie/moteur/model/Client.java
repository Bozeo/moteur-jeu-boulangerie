package com.boulangerie.moteur.model;

public class Client {
    private String commande;
    private float patience; // Temps restant en secondes
    private boolean present;

    public Client(String commande) {
        this.commande = commande;
        this.patience = 60.0f; // 60 secondes d'attente
        this.present = true;
    }

    public void update(float delta) {
        if (present) {
            patience -= delta; // Diminue de 1 seconde par seconde réelle
            if (patience <= 0) {
                patience = 0;
                present = false;
            }
        }
    }

    public String getCommande() { return commande; }
    public float getPatience() { return patience; }
    public boolean isPresent() { return present; }
}
