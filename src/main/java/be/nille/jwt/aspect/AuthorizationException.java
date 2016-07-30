/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

/**
 * @author nholvoet
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(final String message) {
        super(message);
    }
}
