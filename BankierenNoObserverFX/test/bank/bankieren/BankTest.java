/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.bankieren;

import java.rmi.RemoteException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Martin Drost
 */
public class BankTest {
    
    public BankTest() {
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
     * Test of openRekening method, of class Bank.
     */
    @org.junit.Test
    public void testOpenRekening() throws RemoteException {
        String name;
        String city;
        int expResult;
        int result;
        
        Bank instance = new Bank("Rabobank");
        
        
        //Open correct account
        System.out.println("openRekening");
        
        name = "John";
        city = "Meerdijk";
        
        expResult = 100000000;
        result = instance.openRekening(name, city);
        assertEquals(expResult, result);
        
        
        //Open an account wihout name
        System.out.println("openRekeningEmptyName");
        
        expResult = -1;
        result = instance.openRekening("", city);
        assertEquals(expResult, result);
        
        
        //Open an account wihout city
        System.out.println("openRekeningEmptyCity");
        
        expResult = -1;
        result = instance.openRekening(name, "");
        assertEquals(expResult, result);
        
        
        //Open an account wihout city or name
        System.out.println("openRekeningEmptyNameAndCity");
        
        expResult = -1;
        result = instance.openRekening("", "");
        assertEquals(expResult, result);
    }

    /**
     * Test of getRekening method, of class Bank.
     */
    @org.junit.Test
    public void testGetRekening() throws RemoteException {
        Bank instance = new Bank("Rabobank");
        instance.openRekening("John", "Meerdijk");
        int nr;
        int expResult;
        IRekening result;
       
        
        //Test get existing account
        System.out.println("getRekening");
        nr = 100000000;
        expResult = 100000000;
        result = instance.getRekening(nr);
        assertEquals(expResult, result.getNr());
        
        
        //Test get none existing account
        System.out.println("getRekeningNoneExisting");
        nr = 100000001;
        result = instance.getRekening(nr);
        assertEquals(null, result);
        
    }

    /**
     * Test of maakOver method, of class Bank.
     */
    @org.junit.Test
    public void testMaakOver() throws Exception {
        Bank instance = new Bank("Rabobank");
        instance.openRekening("John", "Meerdijk");
        instance.openRekening("Larry", "Meerdijk");
        int source;
        int destination;
        Money money;
        boolean expResult;
        boolean result;
        
        
        //Test send money no funds
        System.out.println("maakOverNoFunds");
        source = 100000000;
        destination = 100000001;
        money = new Money(10001, Money.EURO);
        expResult = false;
        result = instance.maakOver(source, destination, money);
        assertEquals(expResult, result);
        
        
        //Test send money same as source
        try
        {
            System.out.println("maakOverSameAsSource");
            source = 100000000;
            destination = 100000000;
            money = new Money(1, Money.EURO);
            expResult = false;
            result = instance.maakOver(source, destination, money);
            fail("Can't send to same source");
        }catch(Exception e){}
        
        
        //Test send money 0 euros
        try
        {
            System.out.println("maakOverSendNothing");
            source = 100000000;
            destination = 100000001;
            money = new Money(0, Money.EURO);
            expResult = false;
            result = instance.maakOver(source, destination, money);
            fail("Can't send 0 amount");
        }catch(Exception e){}
        
        
        //Test send money
        System.out.println("maakOver");
        source = 100000000;
        destination = 100000001;
        money = new Money(10000, Money.EURO);
        expResult = true;
        result = instance.maakOver(source, destination, money);
        assertEquals(expResult, result);
    }

    /**
     * Test of getName method, of class Bank.
     */
    @org.junit.Test
    public void testGetName() throws RemoteException {
        Bank instance = new Bank("Rabobank");
        String expResult;
        String result;
        
        
        //Get the name of the bank
        System.out.println("getName");
        expResult = "Rabobank";
        result = instance.getName();
        assertEquals(expResult, result);
    }
    
}
