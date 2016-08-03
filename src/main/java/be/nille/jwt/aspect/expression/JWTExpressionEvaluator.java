/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect.expression;


import be.nille.jwt.components.exception.UnexistingJWTClaimException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.SpelParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author nholvoet
 */
@Slf4j
public class JWTExpressionEvaluator {

    private final ExpressionParser parser;

    public JWTExpressionEvaluator() {
        parser = new SpelExpressionParser();
    }

    public boolean evaluate(final PayloadRoot jwtRoot, final String expression) {
        EvaluationContext context = new StandardEvaluationContext(jwtRoot);
        try{
            Expression exp = parser.parseExpression(expression);
            return (boolean) exp.getValue(context);
        }catch(SpelEvaluationException | SpelParseException ex){
            String message = "Encountered an invalid expression: " + expression;
            log.error(message);
            throw new InvalidJWTExpressionException(message, ex);
        }catch(UnexistingJWTClaimException ex){
            return false;
        }
    }

    

}
