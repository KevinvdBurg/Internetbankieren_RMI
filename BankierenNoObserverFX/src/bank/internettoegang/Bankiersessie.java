package bank.internettoegang;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.server.BasicPublisher;
import bank.server.RemotePropertyListener;
import bank.server.RemotePublisher;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.beans.PropertyChangeEvent;

public class Bankiersessie extends UnicastRemoteObject implements IBankiersessie {

        private BasicPublisher bp;
	private static final long serialVersionUID = 1L;
	private long laatsteAanroep;
	private int reknr;
	private IBank bank;
        private boolean isActive;

	public Bankiersessie(int reknr, IBank bank) throws RemoteException {
		laatsteAanroep = System.currentTimeMillis();
		this.reknr = reknr;
		this.bank = bank;
                this.isActive = true;
                this.bp = new BasicPublisher(new String[]{"bedrag"});
		
	}

	public boolean isGeldig() {
		return System.currentTimeMillis() - laatsteAanroep < GELDIGHEIDSDUUR && isActive;
	}

	@Override
	public boolean maakOver(int bestemming, Money bedrag)
			throws NumberDoesntExistException, InvalidSessionException,
			RemoteException {
		  
		updateLaatsteAanroep();
		
		if (reknr == bestemming)
			throw new RuntimeException(
					"source and destination must be different");
		if (!bedrag.isPositive())
			throw new RuntimeException("amount must be positive");
		
                
		boolean suc = bank.maakOver(reknr, bestemming, bedrag);
                bp.inform(this, "bedrag", null, bedrag);
                return suc;
	}

	private void updateLaatsteAanroep() throws InvalidSessionException {
		if (!isGeldig()) {
			throw new InvalidSessionException("session has been expired");
		}
		
		laatsteAanroep = System.currentTimeMillis();
	}

	@Override
	public IRekening getRekening() throws InvalidSessionException,
			RemoteException {

		updateLaatsteAanroep();
		
		return bank.getRekening(reknr);
	}

	@Override
	public void logUit() throws RemoteException {
            this.isActive = false;
		UnicastRemoteObject.unexportObject(this, true);
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

}
