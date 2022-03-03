package atelier_jenkins;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import main.java.com.atelier_jenkins.security.SecurityConfig;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
/**
 *
 */

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
    private WebApplicationContext context; //Ajout

    @Autowired
    private TestRestTemplate template; //Ajout


    @Autowired
    private WebApplicationContext webApplicationContext;


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
}
