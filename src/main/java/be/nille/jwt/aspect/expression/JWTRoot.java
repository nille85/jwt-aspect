/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect.expression;

import be.nille.jwt.components.Claim;
import be.nille.jwt.components.JWT;

/**
 * @author nholvoet
 */
public class JWTRoot {

    private final JWT jwt;

    public JWTRoot(final JWT jwt) {
        this.jwt = jwt;
    }

    public boolean hasClaim(final String name, final String value) {
        Claim claim = new Claim(name, value);
        return jwt.hasClaim(claim);
    }

}
