/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect.expression;

/**
 * @author nholvoet
 */
public class InvalidJWTExpressionException extends RuntimeException {

    public InvalidJWTExpressionException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
