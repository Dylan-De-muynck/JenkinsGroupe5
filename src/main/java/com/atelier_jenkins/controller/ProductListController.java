package main.java.com.atelier_jenkins.controller;

import main.java.com.atelier_jenkins.modele.Customer;
import main.java.com.atelier_jenkins.modele.Product;
import main.java.com.atelier_jenkins.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import main.java.com.atelier_jenkins.service.ProductService;

import java.util.Arrays;
import java.util.List;

@Controller
public class ProductListController {

	@Autowired
	private ProductService productService;

	@Autowired
	private CustomerService customerService;

	// MVC : Controller
	@GetMapping(path = "/products")
	public String afficherListeProduits(@ModelAttribute("products") ProductDto product, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			Integer remise = getConnectedCustomer().getContract().getMargin();
			List<Product> products = productService.getProductList(remise);
			
			//System.out.print(products);
			model.addAttribute("ProductList", products);
			model.addAttribute("Margin", getConnectedCustomer().getContract().getMargin());
			model.addAttribute("Customer", getConnectedCustomer().getUsername());
		
		}
		// return la page html (La vue)
		return "ProductList";
	}
/*
	public List<Product> getProductsWithMargin(List<Product> products){
		
			Integer remise = getConnectedCustomer().getContract().getMargin();
			for(Product p : products){
				//Ajout de la marge
				Float pdvApresRemise = Float.valueOf(p.getPrice() + (p.getPrice() * remise) / 100);
				//Ajout de la TVA
				Float pdvApresRemiseEtTva = Float.valueOf(pdvApresRemise + (p.getPrice() * 20) / 100);
				p.setUpdatedPrice(pdvApresRemiseEtTva);
			}
		
		return products;
	}
*/
	public Customer getConnectedCustomer(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			System.out.println(currentUserName);
			Customer customer = customerService.getCustomer(currentUserName);
			return customer;
		}
		return null;
	}

}
