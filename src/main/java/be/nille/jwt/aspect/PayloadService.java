/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import be.nille.jwt.components.Payload;



/**
 * @author nholvoet
 */
public interface PayloadService {

    public Payload getPayload();
    
    public void verify();

}
