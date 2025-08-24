package com.example.kafgenerator.Controller;

import com.example.kafgenerator.Entities.Produit;
import com.example.kafgenerator.Services.IProduitService;
import com.example.kafgenerator.Services.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produit")

public class ProduitRestController {
    @Autowired
    private IProduitService produitService;

    @PostMapping("add")
    public Produit add(@RequestBody Produit produit) {
        return produitService.add(produit);
    }
@DeleteMapping("delete/{id}")
    public void delete(@PathVariable Long id) {
        produitService.delete(id);
}

@GetMapping("/get/all")
    public List<Produit> getAll() {
        return produitService.getAll();
}



}
