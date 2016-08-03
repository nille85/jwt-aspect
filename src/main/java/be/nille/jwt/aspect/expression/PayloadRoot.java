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

    public boolean hasClaim(final String name, final String value) {

        boolean claimExists = payload.hasClaim(name);
        if (claimExists) {
            Claim retrievedClaim = payload.getClaim(name);
            log.debug("VALUE:" + value);
            log.debug("RETRIEVED:" + retrievedClaim.getValue());
            boolean equals = value.equals(retrievedClaim.getValue());
            log.debug("EQUALS:" + equals);
            return equals;
        }
        return false;

    }

}
