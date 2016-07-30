/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import be.nille.jwt.components.ExpiredJWTException;
import be.nille.jwt.components.InvalidJWTException;
import be.nille.jwt.components.JWT;
import be.nille.jwt.components.JWTSigner;
import be.nille.jwt.components.Payload;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

/**
 * @author nholvoet
 */
public class JWTAspectTest {
    
    private AspectJProxyFactory factory;
   
    
    @Before
    public void setup(){
        AspectTarget target = new AspectTarget();
        this.factory = new AspectJProxyFactory(target);      
    }
    
    private AspectTarget createProxy(final JWTService jwtService){
        JWTAspect aspect = new JWTAspect(jwtService);
        factory.addAspect(aspect);
        return factory.getProxy();
    }
    
    @Test
    public void authorizedTrueWithPlaceholder(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Nille")
                .withClaim("sub", "Token")
                .build();
        JWTSigner signer = new JWTSigner("mysecret");
        JWT jwt = signer.sign(payload);
        
        
        JWTService jwtService = Mockito.mock(JWTService.class);
        when(jwtService.getJWT()).thenReturn(jwt);
        AspectTarget target = createProxy(jwtService);
        assertEquals("value",target.getStringWithPlaceholder("Nille"));
    }
    
    @Test
    public void authorizedTrue(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Nille")
                .withClaim("sub", "Token")
                .build();
        JWTSigner signer = new JWTSigner("mysecret");
        JWT jwt = signer.sign(payload);
        
        
        JWTService jwtService = Mockito.mock(JWTService.class);
        when(jwtService.getJWT()).thenReturn(jwt);
        AspectTarget target = createProxy(jwtService);
        assertEquals("value",target.getString());
    }
    
    @Test(expected = AuthorizationException.class)
    public void authorizedFalse(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Other")
                .withClaim("sub", "Token")
                .build();
        JWTSigner signer = new JWTSigner("mysecret");
        JWT jwt = signer.sign(payload);
        
        JWTService jwtService = Mockito.mock(JWTService.class);
        when(jwtService.getJWT()).thenReturn(jwt);
        AspectTarget target = createProxy(jwtService);
        assertEquals("value",target.getString());
    }
    
    @Test(expected = AuthenticationException.class)
    public void unauthenticatedOrInvalid(){
        JWTService jwtService = Mockito.mock(JWTService.class);
        doThrow(new InvalidJWTException("")).when(jwtService).verify();
        AspectTarget target = createProxy(jwtService);
        target.getString();
    }
    
    @Test(expected = ExpiredException.class)
    public void expired(){
        JWTService jwtService = Mockito.mock(JWTService.class);
        doThrow(new ExpiredJWTException("")).when(jwtService).verify();
        AspectTarget target = createProxy(jwtService);
        target.getString();
    }

}
