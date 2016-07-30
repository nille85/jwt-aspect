/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import be.nille.jwt.aspect.annotation.ClaimValue;
import be.nille.jwt.aspect.annotation.Authorize;
import be.nille.jwt.components.ExpiredJWTException;
import be.nille.jwt.components.InvalidJWTException;
import be.nille.jwt.components.JWT;
import be.nille.jwt.aspect.expression.JWTExpressionEvaluator;
import be.nille.jwt.aspect.expression.JWTRoot;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author nholvoet
 */
@Slf4j
@Aspect
public class JWTAspect {

    private final JWTService jwtService;

    public JWTAspect(final JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Around(value = "@annotation(authorize)")
    public Object methodsAnnotatedWithAuthorized(final ProceedingJoinPoint joinPoint, Authorize authorize) throws Throwable {
        log.debug("in authorize pointcut");
        
        verify();
        String expression = authorize.expression();
        expression = replacePlaceholdersInExpression(joinPoint, expression);
        evaluate(expression);
        return joinPoint.proceed();
    }

    private void verify() {
        
        
        try {
            jwtService.verify();
        } catch (InvalidJWTException ex) {
            throw new AuthenticationException("Not authentication to enter this method");
        } catch (ExpiredJWTException ex) {
            throw new ExpiredException("JWT is expired");
        }
    }
    
    
    private String replacePlaceholdersInExpression(final ProceedingJoinPoint joinPoint, String expression){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            final Annotation[] annotations = parameterAnnotations[i];
            AnnotationService annotationService = new AnnotationServiceImpl();
            final ClaimValue claimAnnotation = annotationService.getAnnotationByType(annotations, ClaimValue.class);

            if (claimAnnotation != null) {
                String claimValue = claimAnnotation.value();
                String argument = (String) args[i];
                expression = expression.replace("#"+claimValue, "'" + argument + "'");
                log.debug(claimValue);
                break;
            }
        }
        return expression;
    }
    
  

    private void evaluate(final String expression) {
        log.debug(expression);
        JWT jwt = jwtService.getJWT();
        JWTRoot jwtRoot = new JWTRoot(jwt);
        JWTExpressionEvaluator evaluator = new JWTExpressionEvaluator();
        boolean isAuthorized = evaluator.evaluate(jwtRoot, expression);
        if (!isAuthorized) {
            throw new AuthorizationException("Not authorized to enter this method");
        }
    }

}
