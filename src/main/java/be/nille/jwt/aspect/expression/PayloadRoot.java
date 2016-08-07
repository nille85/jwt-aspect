/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect.expression;

import be.nille.jwt.components.model.Claim;
import be.nille.jwt.components.model.Payload;
import lombok.extern.slf4j.Slf4j;

/**
 * @author nholvoet
 */
@Slf4j
public class PayloadRoot {

    private final Payload payload;

    public PayloadRoot(final Payload payload) {
        this.payload = payload;
    }

    
    public boolean hasClaim(final String name, final String regexValue){
        boolean claimExists = payload.hasClaim(name);
        if (claimExists) {
            Claim retrievedClaim = payload.getClaim(name);
            Object retrievedObjectValue = retrievedClaim.getValue();
            if(retrievedObjectValue instanceof String){
                String retrievedValue = (String) retrievedObjectValue;
                return retrievedValue.matches(regexValue);
            }     
        }
        return false;
    }

}
