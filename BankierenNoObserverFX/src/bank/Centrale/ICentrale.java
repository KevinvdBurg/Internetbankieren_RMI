/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bank.Centrale;

import bank.server.RemotePublisher;

/**
 *
 * @author Kevin van der Burg <kevin.vanderburg@student.fontys.nl> 
 */
public interface ICentrale extends RemotePublisher{
    
    public boolean maakOver();
    public int reserveAccountNumber();
}
