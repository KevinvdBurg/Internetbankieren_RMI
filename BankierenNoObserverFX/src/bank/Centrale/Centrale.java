/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.Centrale;

import bank.bankieren.Money;
import bank.server.RemotePropertyListener;
import java.rmi.RemoteException;

/**
 *
 * @author Kevin van der Burg <kevin.vanderburg@student.fontys.nl> 
 */
public class Centrale implements ICentrale{

    @Override
    public boolean transferMoney(int to, int from, Money amount)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int reserveAccountNumber()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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




}


