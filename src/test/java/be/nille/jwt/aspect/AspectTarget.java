/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import be.nille.jwt.aspect.annotation.ClaimValue;
import be.nille.jwt.aspect.annotation.Authorize;

/**
 * @author nholvoet
 */
public class AspectTarget {

    @Authorize("hasClaim('iss','Nille')")
    public String getString(){
        return "value";
    }
    
    @Authorize("hasClaim('iss','#issuer')")
    public String getStringWithPlaceholder(@ClaimValue(value = "issuer") String issuer){
        return "value";
    }
    
    @Authorize("hasClaim('scope','.*write.*')")
    public String getStringWithRegex(){
        return "value";
    }
    
    @Authorize("hasClaim('scope','.*#scope.*')")
    public String getStringWithPlaceholderAndRegex(@ClaimValue(value = "scope") String scope){
        return "value";
    }
    
    
    
    
    
   
}
