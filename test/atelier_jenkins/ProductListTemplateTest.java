package atelier_jenkins;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
/**
 *
 */

/**
 * @author test
 *
 */

@RunWith(SpringRunner.class)
@SpringBootApplication
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes={main.java.com.atelier_jenkins.repository.ProductRepository.class})
public class ProductListTemplateTest {

    @LocalServerPort
    int randomServerPort;




    /**
     * Initialise les valeurs avant chaque test
     */
    @BeforeEach
    // Declencher cette methode avant l'execution
    void setUp() throws Exception
    {

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


}
