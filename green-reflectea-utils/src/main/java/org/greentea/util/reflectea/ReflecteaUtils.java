package org.greentea.util.reflectea;
//
import java.util.ArrayList;
import java.lang.reflect.Modifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.lang.reflect.Field;
/**
 * @author Filipe Gonzaga Miranda
 */
public class ReflecteaUtils{

	private ReflecteaUtils() {
		throw new AssertionError("Nice try... But you won't have an instance of this class");
	}
	
	public static void makeListFieldsRTAccessible(List<Field> listOfFields){
		if(listOfFields == null){
			return;
		}
		for(Field field: listOfFields){
			fromPrivateToDynamicPublic(field);
		}
	}
	
	public static Annotation checkClassAnnotatedWith(
			Class<? extends Annotation> annotation, Class<?> annotatedClazz)
			throws AnnotationNotEncouteredException {
		if (annotation == null) {
			throw new IllegalArgumentException(
					"The parameter annotation cannot be null");
		}

		if (annotatedClazz == null) {
			throw new IllegalArgumentException(
					"The parameter annotatedClazz cannot be null");
		}

		if (!annotatedClazz.isAnnotationPresent(annotation)) {
			throw new AnnotationNotEncouteredException("The annotation "
					+ annotation.getName() + " was not found in the class "
					+ annotatedClazz.getName());
		}

		return annotatedClazz.getAnnotation(annotation);
	}

	
	public static List<Field> getFieldsAnnotatedWith(
			Class<? extends Annotation> annotation, Object obj) {
		if (obj == null) {
			throw new NullPointerException("obj cannot be null!");
		}
		return getFieldsAnnotatedWith(annotation, obj.getClass());
	}
	
	public static List<Field> getFieldsAnnotatedWith(
			Class<? extends Annotation> annotation, Class<?> clazz) {
		if (annotation == null) {
			throw new NullPointerException(
					"annotation cannot be null.");
		}
		if (clazz == null) {
			throw new NullPointerException(
					"clazz cannot be null.");
		}
		Field fields[] = clazz.getDeclaredFields();
		if (fields == null) {
			return new ArrayList<Field>(0);
		}
		ArrayList<Field> listFields = new ArrayList<Field>(fields.length);
		for (Field field : fields) {
			if (field.isAnnotationPresent(annotation)) {
				listFields.add(field);
			}
		}
		return listFields;
	}

	public static Annotation checkFieldAnnotatedWith(
			Class<? extends Annotation> annotation, Field field)
			throws AnnotationNotEncouteredException {
		if (annotation == null) {
			throw new IllegalArgumentException(
					"The parameter annotation cannot be null");
		}

		if (field == null) {
			throw new IllegalArgumentException(
					"The parameter field cannot be null");
		}

		if (!field.isAnnotationPresent(annotation)) {
			throw new AnnotationNotEncouteredException("The annotation "
					+ annotation.getName() + " was not found for the field "
					+ field.getName());
		}

		return field.getAnnotation(annotation);
	}

	public static void fromPrivateToDynamicPublic(Field field) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
		} catch (SecurityException e) {
			throw new UnsupportedOperationException(
					"It was not possible to make the field: "
							+ field.getName()
							+ " acccessible. A security exception was raised while supressing Java access controller",
					e);
		} catch (Exception e) {
			throw new UnsupportedOperationException(
					"It was not possible to make the field: " + field.getName()
							+ " acccessible.", e);
		}
	}

	public static void setPPPFieldToValue(Field field, Object fieldObjHolder,
			Object newValue) {
		if (fieldObjHolder == null && !Modifier.isStatic(field.getModifiers())) {
			throw new NullPointerException(
					"obj cannot be null in a non static field. The object is necessary to set the value");
		}
		if (field == null) {
			throw new NullPointerException("The field cannot be null");
		}
		fromPrivateToDynamicPublic(field);
		try {
			field.set(fieldObjHolder, newValue);
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException(
					"It is not possible to set a new value to the field: "
							+ field.getName(), e);
		}
	}

	public static Object getPPPFieldNoMatterIfPrivate(Field field, Object obj) {
		if (obj == null && !Modifier.isStatic(field.getModifiers())) {
			throw new NullPointerException(
					"obj cannot be null in a non static field. The object is necessary to get the value");
		}
		if (field == null) {
			throw new NullPointerException("The field cannot be null");
		}
		fromPrivateToDynamicPublic(field);
		try {
			return field.get(obj);
		} catch (IllegalAccessException e) {
			throw new UnsupportedOperationException(
					"Impossible to get the value of the field: "
							+ field.getName(), e);
		}

	}

	public static boolean isThisFieldStatic(Field field) {
		if (field == null) {
			throw new NullPointerException("The parameter field cannot be null");
		}
		return (Modifier.isStatic(field.getModifiers()));
	}

	public static <T> List<Field> getFieldsAnnotatedWith(
			Class<? extends Annotation> annotation, Class<T> scannedClass,
			Class<? super T> classUpTo) {
		Class<?> c = scannedClass;
		ArrayList<Field> list = new ArrayList<>();
		list.addAll(getFieldsAnnotatedWith(annotation, c));
		while (true) {
			c = c.getSuperclass();
			if (c == null)
				break;
			list.addAll(getFieldsAnnotatedWith(annotation, c));
			if (c.equals(classUpTo))
				break;
		}
		return list;
	}

	public static boolean classHasFieldsAnnotatedWith(
			Class<? extends Annotation> annotationClass,
			Class<?> clazzToBeChecked) {
		if (annotationClass == null) {
			throw new NullPointerException("annotationClass cannot be null.");
		}
		if (clazzToBeChecked == null) {
			throw new NullPointerException("clazzToBeChecked cannot be null.");
		}
		Field[] fields = clazzToBeChecked.getDeclaredFields();
		if (fields == null || fields.length == 0) {
			return false;
		}
		for (Field field : fields) {
			if (field.isAnnotationPresent(annotationClass)) {
				return true;
			}
		}
		return false;
	}

	public static Method getAccessorGetOrSetOfField(Field field, POJOMethods methodType) throws NoSuchMethodException {
		if (field == null) {
			throw new NullPointerException(
					"field cannot be null.");
		}
		if (methodType == null) {
			throw new NullPointerException(
					"methodType cannot be null. It must be filled with whichMethod.SETTER or whichMethod.GETTER");
		}
		String fieldName = field.getName();
		String methodName = "";
		Method methodGetterOrSetter = null;
		switch (methodType) {
		case GETTER:
			methodName = "get" + fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			try {
				methodGetterOrSetter = field.getDeclaringClass().getDeclaredMethod(methodName);
			} catch (SecurityException | NoSuchMethodException e) {
				throw new NoSuchMethodException(
						"No public accessor(getter) method for field "
								+ fieldName
								+ " was found OR IS NOT ACCESSIBLE in Class: "
								+ field.getDeclaringClass().getName()
								+ ". Without a accessor it is not possible to read instance variables. Searched method: "
								+ methodName);
			}
			return methodGetterOrSetter;
		case SETTER:
			methodName = "set" + fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
			try {
				methodGetterOrSetter = field.getDeclaringClass().getDeclaredMethod(methodName, field.getType());
			} catch (SecurityException | NoSuchMethodException e) {
				throw new NoSuchMethodException(
						"No public mutator(setter) method for field "
								+ fieldName
								+ " was found OR IS NOT ACCESSIBLE in Class: "
								+ field.getDeclaringClass().getName()
								+ ". Without a mutator it is not possible to set instance variables. Searched method: "
								+ methodName);
			}
			return methodGetterOrSetter;
			default: throw new AssertionError("Only GETTER or SETTER Supported");
		}
	}

}