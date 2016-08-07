/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import be.nille.jwt.aspect.annotation.ClaimValue;
import be.nille.jwt.aspect.annotation.Authorize;
import be.nille.jwt.aspect.expression.JWTExpressionEvaluator;
import be.nille.jwt.aspect.expression.PayloadRoot;
import be.nille.jwt.components.exception.ExpiredJWTException;
import be.nille.jwt.components.exception.InvalidJWTException;
import be.nille.jwt.components.model.Payload;
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

    private final PayloadService payloadService;

    public JWTAspect(final PayloadService payloadService) {
        this.payloadService = payloadService;
    }

    @Around(value = "@annotation(authorize)")
    public Object methodsAnnotatedWithAuthorized(final ProceedingJoinPoint joinPoint, Authorize authorize) throws Throwable {
        log.debug("in authorize pointcut");
        Payload payload = verify();
        String expression = authorize.value();
        expression = replacePlaceholdersInExpression(joinPoint, expression);
        evaluate(expression, payload);
        return joinPoint.proceed();
    }

    private Payload verify() {
        try {
            Payload payload = payloadService.verify();
            return payload;
        } catch (InvalidJWTException ex) {
            log.debug(ex.getMessage());
            throw new AuthenticationException("Not authenticated to enter this method");
        } catch (ExpiredJWTException ex) {
            log.debug(ex.getMessage());
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
                expression = expression.replace("#"+claimValue, argument );
                log.debug(claimValue);
                break;
            }
        }
        return expression;
    }
    
  

    private void evaluate(final String expression, final Payload payload) {
        log.debug("Expression:" + expression);
        PayloadRoot jwtRoot = new PayloadRoot(payload);
        JWTExpressionEvaluator evaluator = new JWTExpressionEvaluator();
        boolean isAuthorized = evaluator.evaluate(jwtRoot, expression);
        log.debug("Authorized:" + isAuthorized);
        if (!isAuthorized) {
            throw new AuthorizationException("Not authorized to enter this method");
        }
    }

}
