package bank.internettoegang;

import bank.Centrale.Centrale;
import bank.Centrale.ICentrale;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import bank.bankieren.IBank;
import bank.bankieren.IRekening;
import bank.bankieren.Money;
import bank.server.BasicPublisher;
import bank.server.RemotePropertyListener;

import fontys.util.InvalidSessionException;
import fontys.util.NumberDoesntExistException;
import java.beans.PropertyChangeEvent;

public class Bankiersessie extends UnicastRemoteObject implements IBankiersessie {

	private static final long serialVersionUID = 1L;
	private long laatsteAanroep;
	private int reknr;
	private IBank bank;
        private boolean isActive;
        private BasicPublisher bp;
        private Balie balie;

	public Bankiersessie(int reknr, IBank bank, Balie balie) throws RemoteException {
		laatsteAanroep = System.currentTimeMillis();
		this.reknr = reknr;
		this.bank = bank;
                this.isActive = true;
                this.balie = balie;
                balie.addListener(this, "overgemaakt");
                this.bp = new BasicPublisher(new String[]{"overgemaakt"});
                bank.addListener(this, "overgemaakt");
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
		
                ICentrale centrale = balie.getCentrale();
                boolean suc = centrale.transferMoney(reknr, bestemming, bedrag);
		//boolean suc = bank.maakOver(reknr, bestemming, bedrag);
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
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        if(this.isGeldig())
            bp.inform(this, "overgemaakt", null, null);
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
