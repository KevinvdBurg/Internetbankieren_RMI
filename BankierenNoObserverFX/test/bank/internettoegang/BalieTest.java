/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kevin van der Burg <kevin.vanderburg@student.fontys.nl>
 */
public class BalieTest
{
    
    public BalieTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }

    /**
     * Test of openRekening method, of class Balie.
     */
    @Test
    public void testOpenRekening() throws RemoteException
    {
        String naam;
        String plaats;
        String wachtwoord;
        Bank bank = new Bank("bank");
        Balie instance = new Balie(bank);
        
        try
        {
            naam = "";
            plaats = "";
            wachtwoord = "";
            assertEquals("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is kleiner dan 4", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is kleiner dan 4 - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        try
        {
            naam = "Piet";
            plaats = "";
            wachtwoord = "";
            assertEquals("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is kleiner dan 4", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Plaats mag niet leeg zijn, Wachtwoord is kleiner dan 4 - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        try
        {
            naam = "Piet";
            plaats = "Tilburg";
            wachtwoord = "";
            assertEquals("Wachtwoord is kleiner dan 4 - Moet een IllegalArgumentException opgooien", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Wachtwoord is kleiner dan 4 - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        try
        {
            naam = "";
            plaats = "Tilburg";
            wachtwoord = "";
            assertEquals("Naam mag niet leeg zijn, Wachtwoord is kleiner dan 4 ", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam mag niet leeg zijn, Wachtwoord is kleiner dan 4 - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        try
        {
            naam = "";
            plaats = "Tilburg";
            wachtwoord = "1234";
            assertEquals("Naam mag niet leeg zijn, Wachtwoord is kleiner dan 4",null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam mag niet leeg zijn, Wachtwoord is kleiner dan 4 - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        try
        {
            naam = "";
            plaats = "";
            wachtwoord = "1234";
            assertEquals("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is 4 lang", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is 4 lang - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
         try
        {
            naam = "";
            plaats = "";
            wachtwoord = "12345678";
            assertEquals("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is 8 lang", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is 8 lang - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
         
        try
        {
            naam = "";
            plaats = "";
            wachtwoord = "123456789";
            assertEquals("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is langer dan 8", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam mag niet leeg zijn, Plaats mag niet leeg zijn, Wachtwoord is langer dan 8 - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        
        
        try
        {
            naam = "Kevin";
            plaats = "Tilburg";
            wachtwoord = "123";
            assertEquals("Naam is ingevuld, Plaats is ingevuld, Wachtwoord is kleiner dan 4", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam is ingevuld, Plaats is ingevuld, Wachtwoord is kleiner dan 4 - Moet een IllegalArgumentException opgooien");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        try
        {
            naam = "Kevin";
            plaats = "Tilburg";
            wachtwoord = "123456789";
            assertEquals("Naam is ingevuld, Plaats is ingevuld, Wachtwoord is groter dan 8", null, instance.openRekening(naam, plaats, wachtwoord));
            //fail("Naam is ingevuld, Plaats is ingevuld, Wachtwoord is groter dan 8 - Moet een IllegalArgumentException opgooien");

        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
        
        //---- Goed
        try
        {
            naam = "Kevin";
            plaats = "Tilburg";
            wachtwoord = "1234";
            String account = instance.openRekening(naam, plaats, wachtwoord);
            
            assertFalse("Account Naam lengte is niet groot genoeg", account.length() < 8);
            assertFalse("Account Naam lengte is te groot", account.length() > 8);
            assertTrue("Account Naam lengte = 8", account.length() == 8);
            
        } catch (Exception e)
        {
            fail("Naam is ingevuld, Plaats is ingevuld, Wachtwoord is 4 of groter en kleiner dan 8");
        }
        
    }

    /**
     * Test of logIn method, of class Balie.
     */
    @Test
    public void testLogIn() throws Exception
    {
        String naam = "Piet";
        String plaats = "Breda";
        String wachtwoord = "12345";
        Bank bank = new Bank("bank");
        Balie instance = new Balie(bank);
        String accountName = instance.openRekening(naam, plaats, wachtwoord);

        
        try
        {
            instance.logIn(accountName, wachtwoord);
        } catch (Exception e)
        {
            fail("Accountnaam en wachtwoord moeten Matchen");
        }
        
        try
        {
            IBankiersessie result = instance.logIn(accountName + "nope", wachtwoord);
            IBankiersessie result2 = instance.logIn(accountName, wachtwoord + "nope");
            IBankiersessie result3 = instance.logIn(accountName + "nope", wachtwoord + "nope");
            assertEquals(null, result);
            assertEquals(null, result2);
            assertEquals(null, result3);
            //fail("Accountnaam en wachtwoord moeten niet machten");
        } catch (Exception e)
        {
            assertTrue(e.getClass() == IllegalAccessException.class);
        }
    }
    
}
