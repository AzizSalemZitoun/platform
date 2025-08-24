package com.example.kafgenerator.Services;

import com.example.kafgenerator.Entities.Produit;
import com.example.kafgenerator.Repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitService implements IProduitService {
    @Autowired
    private ProduitRepository produitRepository;

    @Override
    public Produit add(Produit produit){
        System.out.println(produit.getConditions());
       return produitRepository.save(produit);
 }
@Override
public void delete(Long id){
        produitRepository.deleteById(id);
}

@Override
public List<Produit> getAll(){
        return produitRepository.findAll();

}





}
