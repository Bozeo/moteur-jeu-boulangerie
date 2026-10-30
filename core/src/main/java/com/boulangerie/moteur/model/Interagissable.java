package com.boulangerie.moteur.model;

import java.util.List;

public interface Interagissable {
    void interagir(Boulanger boulanger, List<Recette> recettes);
}
