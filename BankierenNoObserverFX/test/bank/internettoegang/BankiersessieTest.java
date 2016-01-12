/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.internettoegang;

import bank.bankieren.Bank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Martin Drost <info@martindrost.nl>
 */
public class BankiersessieTest {
    
    public BankiersessieTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isGeldig method, of class Bankiersessie.
     */
    @Test
    public void testIsGeldig() throws InterruptedException, RemoteException {
        Bankiersessie instance;
        Bank bank = new Bank("Icebank");
        boolean expResult;
        boolean result;

        
        System.out.println("isGeldig correct session");    
        try {
            instance = new Bankiersessie(100, bank);
            expResult = true;
            result = instance.isGeldig();
            assertEquals(expResult, result);
        } catch (RemoteException ex) {
            fail("Can't instantiate session.");
        }
              
        
        System.out.println("isGeldig logged out");    
        try {
            instance = new Bankiersessie(100, bank);
            expResult = false;
            instance.logUit();
            result = instance.isGeldig();
            assertEquals(expResult, result);
        } catch (RemoteException ex) {
            fail("User has a session while he/she isn't logged in.");
        }
          
        
        System.out.println("isGeldig session expired");    
        try {
            instance = new Bankiersessie(100, bank);
            expResult = false;
            sleep(IBankiersessie.GELDIGHEIDSDUUR);
            result = instance.isGeldig();
            assertEquals(expResult, result);
        } catch (RemoteException ex) {
            fail("User has session after timeout time.");
        }
    }

    /**
     * Test of maakOver method, of class Bankiersessie.
     */
    @Test
    public void testMaakOver() throws RemoteException, NumberDoesntExistException, InvalidSessionException {
        Bank bank = new Bank("Icebank");
        bank.openRekening("Harry", "Tilly");
        bank.openRekening("Kara", "Tilly");
        int bestemming;
        Money bedrag;
        Bankiersessie instance;
        boolean expResult;
        boolean result;
        
        
        System.out.println("maakOver same account number.");
        try
        {
            instance = new Bankiersessie(100000000, bank);
            bestemming = 100000000;
            bedrag = new Money(1, Money.EURO);
            expResult = false;
            result = instance.maakOver(bestemming, bedrag);
            fail("Can't send to same account number as session");
        }
        catch(RuntimeException e)
        {
            
        }
        
        System.out.println("maakOver too low amount.");
        try
        {
            instance = new Bankiersessie(100000000, bank);
            bestemming = 100000001;
            bedrag = new Money(-1, Money.EURO);
            expResult = false;
            result = instance.maakOver(bestemming, bedrag);
            fail("Can't send negative amount");
        }
        catch(RuntimeException e)
        {
            
        }
        
        System.out.println("maakOver unknown account.");
        try
        {
            instance = new Bankiersessie(100000000, bank);
            bestemming = 101;
            bedrag = new Money(1, Money.EURO);
            expResult = false;
            result = instance.maakOver(bestemming, bedrag);
            fail("Can send to unknown account");
        }
        catch(Exception e)
        {
            assertTrue(e.getClass() == NumberDoesntExistException.class);
        }
        
        
        System.out.println("maakOver no session.");
        try
        {
            instance = new Bankiersessie(100000000, bank);
            instance.logUit();
            bestemming = 100000001;
            bedrag = new Money(1, Money.EURO);
            expResult = false;
            result = instance.maakOver(bestemming, bedrag);
            fail("Can send without session");
        }
        catch(Exception e)
        {
            assertTrue(e.getClass() == InvalidSessionException.class);
        }
        
    }

    /**
     * Test of getRekening method, of class Bankiersessie.
     */
    @Test
    public void testGetRekening() throws Exception {
        Bank bank = new Bank("Icebank");
        bank.openRekening("Harry", "Tilly");
        Bankiersessie instance;
        IRekening expResult;
        IRekening result;
        
        
        System.out.println("getRekening correct session.");
        instance = new Bankiersessie(100000000, bank);
        expResult = instance.getRekening();
        result = instance.getRekening();
        assertEquals(expResult.getEigenaar(), result.getEigenaar());
        
        
        System.out.println("getRekening no session.");
        try
        {
            instance = new Bankiersessie(100000000, bank);
            instance.logUit();
            expResult = null;
            result = instance.getRekening();
            fail("can't get account when no session");
        }
        catch(Exception e)
        {
            assertTrue(e.getClass() == InvalidSessionException.class);
        }
        
        
        System.out.println("getRekening timeout session.");
        try
        {
            instance = new Bankiersessie(100000000, bank);
            sleep(IBankiersessie.GELDIGHEIDSDUUR);
            expResult = null;
            result = instance.getRekening();
            fail("can't get account when no session");
        }
        catch(Exception e)
        {
            assertTrue(e.getClass() == InvalidSessionException.class);
        }
    }

    /**
     * Test of logUit method, of class Bankiersessie.
     */
    @Test
    public void testLogUit() throws RemoteException {
        Bank bank = new Bank("Icebank");
        
        System.out.println("logUit");
        Bankiersessie instance = new Bankiersessie(100000000, bank);
        instance.logUit();
    }
    
}
