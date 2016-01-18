package bank.bankieren;

import bank.server.BasicPublisher;
import bank.server.RemotePropertyListener;
import fontys.util.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bank extends UnicastRemoteObject implements IBank
{

    /**
     *
     */
    private static final long serialVersionUID = -8728841131739353765L;
    private Map<Integer, IRekeningTbvBank> accounts;
    private Collection<IKlant> clients;
    private String name;
    private BasicPublisher bp;

    public Bank(String name) throws RemoteException
    {
        accounts = new HashMap<Integer, IRekeningTbvBank>();
        clients = new ArrayList<IKlant>();
        this.name = name;
        this.bp = new BasicPublisher(new String[]{"overgemaakt"});
    }

    public synchronized int openRekening(int nieuwReknr, String name, String city)
    {
            if (name.equals("") || city.equals(""))
            {
                return -1;
            }

            IKlant klant = getKlant(name, city);
            
            //Haal uit de centrale welke rekeningeNR er over is.
            IRekeningTbvBank account = new Rekening(nieuwReknr, klant, Money.EURO);
            accounts.put(nieuwReknr, account);
            nieuwReknr++;
            return nieuwReknr - 1;
        
        
    }

    private IKlant getKlant(String name, String city)
    {
        for (IKlant k : clients)
        {
            if (k.getNaam().equals(name) && k.getPlaats().equals(city))
            {
                return k;
            }
        }
        IKlant klant = new Klant(name, city);
        clients.add(klant);
        return klant;
    }

    public IRekening getRekening(int nr)
    {
        return accounts.get(nr);
    }

    public synchronized boolean maakOver(int rekeningnr, Money money) throws NumberDoesntExistException
    {
        
        IRekeningTbvBank rek_account = (IRekeningTbvBank) getRekening(rekeningnr);
        if (rek_account == null)
        {
            throw new NumberDoesntExistException("account " + rekeningnr
                    + " unknown at " + name);
        }
        boolean success = rek_account.muteer(money);
        
        bp.inform(this, "overgemaakt", null, money);
        return success;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException {
            bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException {
            bp.removeListener(listener, property);
    }

}
