package main.java.com.atelier_jenkins.service;

import main.java.com.atelier_jenkins.modele.Product;
import main.java.com.atelier_jenkins.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> getProductList(Integer remise){
		
		return getProductsWithMargin(productRepository.getProductList(), remise);
	}
	
	public List<Product> getProductsWithMargin(List<Product> products, Integer remise){
		
		
		for(Product p : products){
			//Ajout de la marge
			Float pdvApresRemise = Float.valueOf(p.getPrice() + (p.getPrice() * remise) / 100);
			//Ajout de la TVA
			Float pdvApresRemiseEtTva = Float.valueOf(pdvApresRemise + (p.getPrice() * 20) / 100);
			p.setUpdatedPrice(pdvApresRemiseEtTva);
		}
	
	return products;
}
	
}
