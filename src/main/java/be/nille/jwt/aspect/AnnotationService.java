/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nille.jwt.aspect;

import java.lang.annotation.Annotation;

/**
 * @author nholvoet
 */
public interface AnnotationService {
    
    <T extends Annotation> T getAnnotationByType(final Annotation[] annotations, final Class<T> clazz);

}
