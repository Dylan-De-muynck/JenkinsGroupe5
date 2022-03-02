
package test.java.com.atelier_jenkins;



import static org.junit.jupiter.api.Assertions.*;

import java.awt.desktop.SystemEventListener;

import main.java.com.atelier_jenkins.Calculs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 */

/**
 * @author test
 * 
 */
class CalculsTest {

	private Calculs c1 = null;
	private Calculs c2 = null;
	private Calculs c3 = null;
	
	/**
	 * Initialise les valeurs avant chaque test 
	 */
	@BeforeEach // Déclencher cette méthode avant l'exécution
	void setUp() throws Exception 
	{
		c1 = new Calculs(1,2);
		c2 = new Calculs(10,20);
		c3 = new Calculs(100,200);
	}

	

	/**
	 * Test method for {@link Calculs#multiplier()}.
	 */
	@Test
	void testMultiplier() 
	{
		if(c1.multiplier() != 2)
		{
			fail("Methode multiplier non conforme 2*1=2 ne fonctionne pas.");
		}
		
		// Est ce que (2 == 42) ? non donc : test en Erreur
		assertEquals(c1.multiplier(), 2);
		
	}

	/**
	 * Test method for {@link Calculs#additionner()}.
	 */
	@Test
	void testAdditionner() {
		if(c1.additionner() != 3)
		{
			fail("Methode diviser non conforme 1 + 2 = 3 ne fonctionne pas.");
		}
		
		assertEquals(c1.additionner(), 3);
	}

	/**
	 * Test method for {@link Calculs#diviser()}.
	 */
	@Test
	void testDiviser() {
		if(c1.diviser() != 0)
		{
			fail("Methode diviser non conforme 1 / 2 = 0 ne fonctionne pas.");
		}
		
		assertEquals(c1.diviser(), 0);
	}

	

}
