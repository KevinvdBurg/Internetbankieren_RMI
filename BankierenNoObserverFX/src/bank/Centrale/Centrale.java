/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.Centrale;

import bank.bankieren.Bank;
import bank.bankieren.IBank;
import bank.bankieren.IRekeningTbvBank;
import bank.bankieren.Money;
import bank.bankieren.Rekening;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import bank.server.BalieServer;
import bank.server.BasicPublisher;
import bank.server.RemotePropertyListener;
import java.beans.PropertyChangeEvent;
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

    
    private List<IBank> banken;
    private int accountNumber;
    private BasicPublisher bp;
    
    public Centrale() throws RemoteException
    {
        this.accountNumber = 100000000;
        this.banken = new ArrayList<>();
        startCentrale();
        this.bp = new BasicPublisher(new String[]{"overgemaakt"});
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
        try
        {
            IRekeningTbvBank rekFrom = null;
            IRekeningTbvBank rekTo =  null;
            for (IBank bank : banken)
            {
                IRekeningTbvBank rt = (IRekeningTbvBank)bank.getRekening(to);
                if (rt != null)
                {
                    rekTo = rt;
                }

                IRekeningTbvBank rf = (IRekeningTbvBank)bank.getRekening(from);
                if (rt != null)
                {
                    rekFrom = rf;
                }
            }
        
        
            Money negative = Money.difference(new Money(0, amount.getCurrency()), amount);
        
            boolean mf = rekFrom.muteer(negative);
            boolean mt = rekTo.muteer(amount);
            
            bp.inform(this, "overgemaakt", null, amount);
            return true;
            
        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
        
        return false;
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
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException
    {
        bp.removeListener(listener, property);
    }

    @Override
    public IBank addBank(IBank bank)
    {
        try
        {
            banken.add(bank);
            bank.addListener(this, "overgemaakt");
            return bank;
        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
        
        return null;
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException {
        new Centrale();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        System.out.println("Centrale informed");
        bp.inform(this, "overgemaakt", null, null);
    }

}


