
package test.atelier_jenkins;

import main.java.com.atelier_jenkins.Calculs;



import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;


import static org.junit.jupiter.api.Assertions.*;

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
	@BeforeEach // D�clencher cette m�thode avant l'ex�cution
	void setUp() throws Exception
	{
		c1 = new Calculs(1,2);
		c2 = new Calculs(10,20);
		c3 = new Calculs(100,200);
	}



	/**
	 * Test method for {@link Calculs#multiplier()}.
	 */
	@org.junit.jupiter.api.Test
	void testMultiplier()
	{
		if(c1.multiplier() != 8)
		{
			fail("Methode multiplier non conforme 2*1=2 ne fonctionne pas.");
		}

		assertEquals(c1.multiplier(), 2);

	}

	/**
	 * Test method for {@link Calculs#additionner()}.
	 */
	@org.junit.jupiter.api.Test
	void testAdditionner() {
		if(c1.additionner() != 3)
		{
			fail("Methode additionner non conforme 2+1=3 ne fonctionne pas.");
		}

		assertEquals(c1.additionner(), 3);
	}

	/**
	 * Test method for {@link Calculs#diviser()}.
	 */
	@org.junit.jupiter.api.Test
	void testDiviser() {
		if(c1.diviser() != 0)
		{
			fail("Methode diviser non conforme 1/2=0 ne fonctionne pas.");
		}

		assertEquals(c1.diviser(), 0);
	}

	/**
	 * Test method for {@link Calculs#soustraire()}.
	 */
	@Test
	void testSoustraire() {
		if(c1.soustraire() != -1)
		{
			fail("Methode soustraire non conforme 2-1=1 ne fonctionne pas.");
		}

		assertEquals(c1.soustraire(), -1);
	}

}
