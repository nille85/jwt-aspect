The JWTAspect enables you to put authorization checks on methods by using annotations. The authorization checks are based on JWT claims.

##PayloadService

The JWTAspect has one dependency namely a PayloadService. The PayloadService interface is something that needs to be implemented by the consumer.
This interface contains one method:

```java
public interface PayloadService {

    public Payload verify();

}
```

An example of an implementation that extracts a JWT from a request header:

```java
public class PayloadRequestService implements PayloadService {
    
    private final JWTVerifier verifier;
    
    public PayloadRequestService(final JWTVerifier verifier){
        this.verifier = verifier;
    }

    @Override
    public Payload verify() {
        ServletRequestAttributes t = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = t.getRequest();
     
        final String jwtValue = request.getHeader("X-AUTH");
        JWT jwt = new JWT(jwtValue);
        Payload payload =verifier.verify(jwt);
       
        return payload;
    }

}
```

##@Authorize

The Aspect looks for methods annotated with the @Authorize annotation. The following are some examples how it can be used:

```java
@Authorize("hasClaim('iss','Nille')")
public void checkIssuer(){
}
```

```java
@Authorize("hasClaim('iss','#issuer')")
public void checkIssuerWithPlaceholder(@ClaimValue(value = "issuer") String issuer){
}
```

```java
@Authorize("hasClaim('scope','.*write.*')")
public void checkScopeWithRegex(){
}
```

```java
@Authorize("hasClaim('scope','.*#scope.*')")
public void checkScopeWithRegexAndPlaceholder(@ClaimValue(value = "scope") String scope){
}
```
