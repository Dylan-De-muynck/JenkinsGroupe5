package atelier_jenkins;

import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import main.java.com.atelier_jenkins.Calculs;
import main.java.com.atelier_jenkins.controller.ProductListController;
import main.java.com.atelier_jenkins.modele.Contract;
import main.java.com.atelier_jenkins.modele.Customer;
import main.java.com.atelier_jenkins.modele.Product;
import main.java.com.atelier_jenkins.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * @author test
 *
 */

@RunWith(Parameterized.class)
@ExtendWith(SpringExtension.class)
//@SpringBootApplication
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT,
         classes={ main.java.com.atelier_jenkins.AtelierJenkinsApplication.class })
public class ProductListTemplateTest {

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private WebApplicationContext context; //Ajout

    @Autowired
    private TestRestTemplate template; //Ajout

    @Autowired
    private ProductService productService; //Ajout
    
    @Autowired
    private ProductListController controller; //Ajout
    
    @Autowired
    private WebApplicationContext webApplicationContext;


   
    
    private List<Product> productTestList = new ArrayList<Product>();
    
   
    Product productTest = new Product();
    
	 /*
	int[] listId = {0, 1, 2, 3, 4};
   
   String[] listName = {"product1", "product2", "product3", "product4", "product5"};*/
    
    @BeforeEach // D�clencher cette m�thode avant l'ex�cution
	void setUp() throws Exception
	{
    	
	}
    
    static Stream<Arguments> chargerLesPrix() throws Throwable 
    {
        
        float[] listPrice = {487, 180, 254, 312, 222};
    	
        return Stream.of(
        		Arguments.of(listPrice[0], 260), 
        		Arguments.of(listPrice[1], 560), 
        		Arguments.of(listPrice[2], 750),
        		Arguments.of(listPrice[3], 750),
        		Arguments.of(listPrice[4], 750)
        );
    }
    
    /**
     * Test {La liste des produits dans le back-end et dans le front-end}
     */
    @Test
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

    /**
     * Test {}
     */
    @Test
    @WithUserDetails("customer1")
    public void testProductListValuesWithAuthRequest_shouldSucceedWith200AndReturnTemplate() throws Exception {

    	MockMvc mockMvc = webAppContextSetup(webApplicationContext).build();

        MvcResult result = mockMvc.perform(get("/products")
                .with(SecurityMockMvcRequestPostProcessors
                        .user("customer1")
                        .password("password123")
                        .roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertEquals(200, result.getResponse().getStatus());  
        
    }
    
    /**
     * Test {Le calcul des marges}
     */
    @Test
    @WithUserDetails("customer1")
    @ParameterizedTest(name="Multiplication numéro {index}: nombre1={1}")
	@MethodSource("chargerLesPrix")
    public void testPriceCalculWithMargin(float testPrice, float expectedResult) throws Exception {
        
        productTest.setPrice(testPrice);
        productTestList.add(productTest);
        
        controller.getConnectedCustomer().getContract().setMargin(5);
        
        Integer remise = controller.getConnectedCustomer().getContract().getMargin();
        
        List<Product> testProductsWithMargin = productService.getProductsWithMargin(productTestList, remise);

        float testpriceMargin = testProductsWithMargin.get(0).getUpdatedPrice();
        
        assertEquals(testpriceMargin, expectedResult, " test en echec pour " + testpriceMargin + " != " + expectedResult);
        
    }
}
