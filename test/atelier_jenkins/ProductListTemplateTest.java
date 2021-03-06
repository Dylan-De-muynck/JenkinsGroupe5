package atelier_jenkins;

import main.java.com.atelier_jenkins.modele.Product;
import main.java.com.atelier_jenkins.service.ProductService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private ProductService productService;
    
    @Autowired
    private ProductListController controller; //Ajout
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private List<Product> productTestList = new ArrayList<Product>();
    
    private Product productTest = new Product();
    
    
    static Stream<Arguments> chargerLesPrix() throws Throwable 
    {
    	Customer costumerTest = new Customer();
        
        Contract contractTest = new Contract();
        
        costumerTest.setUserId(0);
        costumerTest.setUsername("customerTest");
        costumerTest.setPassword("test");
        
        contractTest.setId(0);
        contractTest.setType("contractTest");
        
        float[] listPrice = {180f, 397f, 254f, 312f, 222f};
    	
        float[] listPriceResult = {252f, 496.25f, 750f, 750f, 750f};
        
        return Stream.of(
        		Arguments.of(listPrice[0], listPriceResult[0], costumerTest, contractTest, 20), 
        		Arguments.of(listPrice[1], listPriceResult[1], costumerTest, contractTest, 5), 
        		Arguments.of(listPrice[2], listPriceResult[2], costumerTest, contractTest, 10),
        		Arguments.of(listPrice[3], listPriceResult[3], costumerTest, contractTest, 14),
        		Arguments.of(listPrice[4], listPriceResult[4], costumerTest, contractTest, 5)
        );
    }
    
    /**
     * Test {La liste des produits dans le back-end et dans le front-end}
     */
    @Ignore
    public void testProductListValues() throws URISyntaxException
    {
        //Permet de g??n??rer des requ??tes HTTP
        RestTemplate restTemplate = new RestTemplate();

        //URL de notre endpoint
        final String baseUrl = "http://localhost:" + randomServerPort + "/products";
        URI uri = new URI(baseUrl);

        //Fait un appel au endpoint gr??ce ?? l'instance de l'objet RestTemplate
        ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        //Test le code status de la requ??te
        Assert.assertEquals(200, result.getStatusCodeValue());

        //Test le retour de la requ??te
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
                //.andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertEquals(200, result.getResponse().getStatus());          
        
    }
    
    /**
     * Test {Le calcul des marges}
     */
    @Test
    @ParameterizedTest(name="num??ro {index}: prix sans marge={0} prix attendu={1}")
	@MethodSource("chargerLesPrix")
    public void testPriceCalculWithMargin(float testPrice, float expectedResult, Customer customerTest, Contract contractTest, Integer margin) {
        
        productTest.setPrice(testPrice);
        productTestList.add(productTest);
        
        contractTest.setMargin(margin);
        
        customerTest.setContract(contractTest);
        
        Integer remise = customerTest.getContract().getMargin();
        
        List<Product> testProductsWithMargin = productService.getProductsWithMargin(productTestList, remise);

        float testpriceMargin = testProductsWithMargin.get(0).getUpdatedPrice();
        
        assertEquals(testpriceMargin, expectedResult, " test en echec pour " + testpriceMargin + " != " + expectedResult);


    }

    public Boolean getPresenceofOurPrice(String str, List<Product> productList){
        List<String> priceList = new ArrayList<String>();
        Pattern p = Pattern.compile("<td>(\\S+)</td>");
        Matcher m = p.matcher(str);
        while(m.find()) {
            String tag = m.group(1);
            priceList.add(tag);
        }

        int i = 0;
        for(Product product : productList){
            for(String price : priceList){
                if(product.getPrice().toString().equals(price)){
                    i++;
                }
            }
        }

        if ( productList.toArray().length == i) {
            return true;
        }else{
            return false;
        }

    }
}
