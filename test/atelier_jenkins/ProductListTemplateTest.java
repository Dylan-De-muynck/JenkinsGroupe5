package atelier_jenkins;

import main.java.com.atelier_jenkins.modele.Product;
import main.java.com.atelier_jenkins.service.ProductService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * @author test
 *
 */

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
    private WebApplicationContext webApplicationContext;


    /**
     * Test {La liste des produits dans le back-end et dans le front-end}
     */
    @Ignore
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

        //Je récupére tous les produits provenant de la base de données
        List<Product> productList = productService.getProductList();

        //Je récupére toutes les balises html de notre front affichant les prix des produits
        String stringResponse = result.getResponse().getContentAsString();

        boolean properlyBondedFront = getPresenceofOurPrice(stringResponse, productList);

        Assert.assertEquals(true, properlyBondedFront);
        Assert.assertEquals(200, result.getResponse().getStatus());


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
