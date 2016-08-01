/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import be.nille.jwt.components.ExpiredJWTException;
import be.nille.jwt.components.InvalidJWTException;
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
    
    private AspectTarget createProxy(final PayloadService jwtService){
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
      
        
        
        PayloadService service = Mockito.mock(PayloadService.class);
        when(service.getPayload()).thenReturn(payload);
        AspectTarget target = createProxy(service);
        assertEquals("value",target.getStringWithPlaceholder("Nille"));
    }
    
    @Test
    public void authorizedTrue(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Nille")
                .withClaim("sub", "Token")
                .build();
        
        
        
        PayloadService service = Mockito.mock(PayloadService.class);
        when(service.getPayload()).thenReturn(payload);
        AspectTarget target = createProxy(service);
        assertEquals("value",target.getString());
    }
    
    @Test(expected = AuthorizationException.class)
    public void authorizedFalse(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Other")
                .withClaim("sub", "Token")
                .build();
       PayloadService service = Mockito.mock(PayloadService.class);
        when(service.getPayload()).thenReturn(payload);
        AspectTarget target = createProxy(service);
       
        assertEquals("value",target.getString());
    }
    
    @Test(expected = AuthenticationException.class)
    public void unauthenticatedOrInvalid(){
        PayloadService service = Mockito.mock(PayloadService.class);
        doThrow(new InvalidJWTException("")).when(service).verify();
        AspectTarget target = createProxy(service);
        target.getString();
    }
    
    @Test(expected = ExpiredException.class)
    public void expired(){
        PayloadService service = Mockito.mock(PayloadService.class);
        doThrow(new ExpiredJWTException("")).when(service).verify();
        AspectTarget target = createProxy(service);
        target.getString();
    }

}
