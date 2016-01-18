package bank.internettoegang;

import bank.Centrale.ICentrale;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import bank.bankieren.*;
import bank.server.BasicPublisher;
import bank.server.RemotePropertyListener;
import bank.server.RemotePublisher;
import java.beans.PropertyChangeEvent;

public class Balie extends UnicastRemoteObject implements IBalie, RemotePropertyListener, RemotePublisher{

        
	private static final long serialVersionUID = -4194975069137290780L;
	private IBank bank;
        private ICentrale centrale;
        private BasicPublisher bp;
	private HashMap<String, ILoginAccount> loginaccounts;
	//private Collection<IBankiersessie> sessions;
	private java.util.Random random;

	public Balie(IBank bank) throws RemoteException {
                this.centrale = connectToServer();
		this.bank = this.centrale.addBank(bank);
                this.bp = new BasicPublisher(new String[]{"overgemaakt"});
		loginaccounts = new HashMap<String, ILoginAccount>();
		//sessions = new HashSet<IBankiersessie>();
		random = new Random();
                
	} 
        
        protected ICentrale connectToServer() {
            try {

                ICentrale centrale = (ICentrale) Naming.lookup("rmi://127.0.0.1:1098/centrale");
                int number = centrale.reserveAccountNumber();
                centrale.addListener(this, "overgemaakt");
                return centrale;

            } catch (Exception exc) {
                exc.printStackTrace();
                return null;
            }
    }

	public String openRekening(String naam, String plaats, String wachtwoord) throws RemoteException {
		if (naam.equals(""))
			return null;
		if (plaats.equals(""))
			return null;

		if (wachtwoord.length() < 4 || wachtwoord.length() > 8)
			return null;

		int nr = bank.openRekening(centrale.reserveAccountNumber(), naam, plaats);
		if (nr == -1)
			return null;

		String accountname = generateId(8);
		while (loginaccounts.containsKey(accountname))
			accountname = generateId(8);
		loginaccounts.put(accountname, new LoginAccount(accountname,
				wachtwoord, nr));

		return accountname;
	}

	public IBankiersessie logIn(String accountnaam, String wachtwoord)
			throws RemoteException {
		ILoginAccount loginaccount = loginaccounts.get(accountnaam);
		if (loginaccount == null)
			return null;
		if (loginaccount.checkWachtwoord(wachtwoord)) {
			IBankiersessie sessie = new Bankiersessie(loginaccount
					.getReknr(), bank, this);
			this.centrale.addListener(sessie, "overgemaakt");
		 	return sessie;
		}
		else return null;
	}

	private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

	private String generateId(int x) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < x; i++) {
			int rand = random.nextInt(36);
			s.append(CHARS.charAt(rand));
		}
		return s.toString();
	}

        public ICentrale getCentrale()
        {
            return centrale;
        }
        
        
    @Override
    public void propertyChange(PropertyChangeEvent evt) throws RemoteException {
        bp.inform(this, "overgemaakt", null, null);
    }

    @Override
    public void addListener(RemotePropertyListener listener, String property) throws RemoteException
    {
        bp.addListener(listener, property);
    }

    @Override
    public void removeListener(RemotePropertyListener listener, String property) throws RemoteException
    {
        bp.addListener(listener, property);
    }


}
