
package atelier_jenkins;

import main.java.com.atelier_jenkins.Calculs;
import main.java.com.atelier_jenkins.modele.Product;
import main.java.com.atelier_jenkins.repository.ProductRepository;


import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
/**
 *
 */

/**
 * @author test
 *
 */

@ExtendWith(MockitoExtension.class)
public class DatabaseTest {
	
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

}
