package com.example.kafgenerator.Repositories;

import com.example.kafgenerator.Entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
