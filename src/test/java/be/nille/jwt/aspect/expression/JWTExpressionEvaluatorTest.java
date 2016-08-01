package be.nille.jwt.aspect.expression;



import be.nille.jwt.components.Payload;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author nholvoet
 */
@Slf4j
public class JWTExpressionEvaluatorTest {
    
    private JWTExpressionEvaluator evaluator;
    private PayloadRoot payloadRoot;
    
    @Before
    public void setup(){
        evaluator = new JWTExpressionEvaluator();
        Payload payload = Payload.builder()
                .withClaim("iss", "Nille")
                .withClaim("sub", "Token")
                .build();
        payloadRoot = new PayloadRoot(payload);
     
    }

    @Test
    public void evaluateTrue() {
        
        String expression = "hasClaim('iss','Nille')";
        boolean evaluation = evaluator.evaluate(payloadRoot, expression);
        assertTrue(evaluation);
    }
    
    @Test
    public void evaluateFalse() {
        
        String expression = "hasClaim('iss','NotNille')";
        boolean evaluation = evaluator.evaluate(payloadRoot, expression);
        assertFalse(evaluation);
    }
    
    @Test
    public void evaluateFalseBecauseClaimDoesNotExist() {
        
        String expression = "hasClaim('unknow','somevalue')";
        boolean evaluation = evaluator.evaluate(payloadRoot, expression);
        assertFalse(evaluation);
    }
    
    @Test(expected = InvalidJWTExpressionException.class)
    public void evaluateNonsense() {
        
        String expression = "klsdfkl";
        boolean evaluation = evaluator.evaluate(payloadRoot, expression);
        assertFalse(evaluation);
    }

}
