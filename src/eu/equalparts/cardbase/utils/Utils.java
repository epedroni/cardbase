package eu.equalparts.cardbase.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class Utils {
	public static boolean hasAnnotation(Field fieldToAnalyse, Class<? extends Annotation> annotation) {
		for (Annotation a : fieldToAnalyse.getAnnotations()) {
			if (a.annotationType().equals(annotation)) {
				return true;
			}
		}
		return false;
	}
}
