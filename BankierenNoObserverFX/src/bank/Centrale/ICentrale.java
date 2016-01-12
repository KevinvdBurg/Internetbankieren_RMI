/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.Centrale;

import bank.bankieren.Money;
import bank.server.RemotePublisher;

/**
 *
 * @author Kevin van der Burg <kevin.vanderburg@student.fontys.nl> 
 */
public interface ICentrale extends RemotePublisher{
    
    /**
     * 
     * Maakt een specifiek bedrag over van het ene rekeingsnummer naar de andere.
     * 
     * 
     * @param to Het rekenings nummer waar het bedrag heen moet.
     * @param from het rekenings nummer van welk het bedrag komt
     * @param amount het bedrag dat wordt overgemaakt tussen de rekeningnummers
     * @return returned true als het geld goed is overgemaakt en false als dit niet is gelukt.
     */
    public boolean transferMoney(int to, int from, Money amount);
    
    /**
     * 
     * Reserveerd de overlegende vrije account nummer.
     * 
     * @return een vrije account nummer.
     */
    public int reserveAccountNumber();
}
