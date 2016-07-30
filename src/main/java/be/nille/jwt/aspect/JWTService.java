/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import be.nille.jwt.components.JWT;

/**
 * @author nholvoet
 */
public interface JWTService {

    public JWT getJWT();

    public void verify();

}
