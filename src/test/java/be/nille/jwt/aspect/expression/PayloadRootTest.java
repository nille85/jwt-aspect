/*
 * The MIT License
 *
 * Copyright 2016 Niels Holvoet.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package be.nille.jwt.aspect.expression;

import be.nille.jwt.components.model.Payload;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Niels Holvoet
 */
public class PayloadRootTest {
    
    @Test
    public void hasClaimRegexTrue(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Nille")
                .withClaim("scope", "read,write,execute")
                .build();
        PayloadRoot root = new PayloadRoot(payload);
        assertTrue(root.hasClaim("scope", ".*write.*"));
    }
    
    @Test
    public void hasClaimRegexFalse(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Nille")
                .withClaim("scope", "read,write,execute")
                .build();
        PayloadRoot root = new PayloadRoot(payload);
        assertFalse(root.hasClaim("scope", ".*writer.*"));
    }
    
    @Test
    public void hasClaimRegexWithoutRegexValue(){
        Payload payload = Payload.builder()
                .withClaim("iss", "Nille")
                .withClaim("scope", "read")
                .build();
        PayloadRoot root = new PayloadRoot(payload);
        assertTrue(root.hasClaim("scope", "read"));
    }
    
}
