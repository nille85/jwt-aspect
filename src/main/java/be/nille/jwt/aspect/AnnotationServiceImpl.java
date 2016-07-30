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
public class AnnotationServiceImpl implements AnnotationService {

    @Override
    public <T extends Annotation> T getAnnotationByType(Annotation[] annotations, Class<T> clazz) {
        T result = null;
        for (final Annotation annotation : annotations) {
            if (clazz.isAssignableFrom(annotation.getClass())) {

                result = (T) annotation;
                break;
            }
        }
        return result;
    }

}
