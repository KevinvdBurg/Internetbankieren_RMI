/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.Centrale;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.Money;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import bank.server.BalieServer;
import bank.server.RemotePropertyListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin van der Burg <kevin.vanderburg@student.fontys.nl> 
 */
public class Centrale extends UnicastRemoteObject implements ICentrale{

    
    private List<Bank> banken;
    private int accountNumber;
    
    public Centrale() throws RemoteException
    {
        this.accountNumber = 100000000;
        this.banken = new ArrayList<>();
        startCentrale();
    }
    

    public boolean startCentrale() 
    {
            
            FileOutputStream out = null;
            try {
                int port = 1098;
                java.rmi.registry.LocateRegistry.createRegistry(port);
                ICentrale centrale = this;
                Naming.rebind("rmi://localhost:" + port + "/centrale", centrale);
               
                return true;

            } catch (IOException ex) {
                Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            return false;
    }

    @Override
    public boolean transferMoney(int to, int from, Money amount)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int reserveAccountNumber()
    {
        this.accountNumber++;
        return this.accountNumber - 1;
    }


    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addBank(IBank bank)
    {
        try
        {
            banken.add((Bank) bank);
            return true;
        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
        
        return false;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException {
        new Centrale();
    }

}


