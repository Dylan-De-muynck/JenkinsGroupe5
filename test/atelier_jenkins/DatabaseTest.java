
package atelier_jenkins;

import main.java.com.atelier_jenkins.Calculs;
import main.java.com.atelier_jenkins.modele.Product;
import main.java.com.atelier_jenkins.repository.ProductRepository;


import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
/**
 *
 */

/**
 * @author test
 *
 */

@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes={main.java.com.atelier_jenkins.repository.ProductRepository.class})
public class DatabaseTest {

	@LocalServerPort
	int randomServerPort;
	
    @Mock
	private ProductRepository test1;
	
	private Product productTest = new Product();
	
	private List<Product> getProductL = new ArrayList<Product>();
	
	
	/**
	 * Initialise les valeurs avant chaque test
	 */
	@BeforeEach // D�clencher cette m�thode avant l'ex�cution
	public void setUp() throws Exception
	{
		
		productTest.setId(1);
		productTest.setName("product1");
		productTest.setPrice((float)487);
		//productTest.setUpdatedPrice(null);
		
		getProductL.add(productTest);
	}
	

	/**
	 * Test method for {@link Calculs#multiplier()}.
	 */
	@Test
	public void testSelectproduct()
	{
		
		when(test1.getProductList()).thenReturn(getProductL);
		
		List<Product> productsT = test1.getProductList();
		
		assertEquals("product18", productsT.get(0).getName());

	}

	@org.junit.Test
	public void testProductListValues() throws URISyntaxException
	{
		//Permet de générer des requêtes HTTP
		RestTemplate restTemplate = new RestTemplate();

		//URL de notre endpoint
		final String baseUrl = "http://localhost:" + randomServerPort + "/products";
		URI uri = new URI(baseUrl);

		//Fait un appel au endpoint grâce à l'instance de l'objet RestTemplate
		ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

		//Test le code status de la requête
		Assert.assertEquals(200, result.getStatusCodeValue());

		//Test le retour de la requête
		Assert.assertEquals(true, result.getBody().contains("ProductList"));

	}

}
