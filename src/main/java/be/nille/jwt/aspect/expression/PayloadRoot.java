/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect.expression;

import be.nille.jwt.components.Claim;
import be.nille.jwt.components.Payload;



/**
 * @author nholvoet
 */
public class PayloadRoot {

    private final Payload payload;

    public PayloadRoot(final Payload payload) {
        this.payload = payload;
    }

    public boolean hasClaim(final String name, final String value) {
     
        boolean claimExists = payload.hasClaim(name);
        if(claimExists){
            Claim retrievedClaim = payload.getClaim(name);
            return value.equals(retrievedClaim.getValue());
        }
        return false;
     
    }

}
