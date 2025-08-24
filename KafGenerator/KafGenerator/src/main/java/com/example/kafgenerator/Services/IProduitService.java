package com.example.kafgenerator.Services;

import com.example.kafgenerator.Entities.Produit;

import java.util.List;

public interface IProduitService {
    Produit add(Produit produit);

    void delete(Long id);

    List<Produit> getAll();
}
