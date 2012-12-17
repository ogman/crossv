package org.crossv.evaluators.getters;

import static org.crossv.primitives.Iterables.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;

import org.crossv.BasicEvaluator;
import org.crossv.Evaluation;
import org.crossv.primitives.ArgumentNullException;
import org.crossv.primitives.Strings;

public abstract class GetterEvaluator<E, EGetter> extends BasicEvaluator<E> {
	private final String getterName;
	private final Class<EGetter> getterClass;

	public GetterEvaluator(Class<E> objClass, Class<EGetter> getterClass,
			String getterName) {
		super(objClass);
		if (getterClass == null)
			throw new ArgumentNullException("getterClass");
		if (Strings.isNullOrWhitespace(getterName))
			throw new ArgumentNullException("getterName");
		this.getterClass = getterClass;
		this.getterName = getterName;
	}

	protected EGetter getValue(E obj) throws ReflectiveOperationException {
		Object result;
		Class<E> instanceClass;
		String message;
		String prefix;
		String[] names;
		Method method;
		Field field;
		boolean isMethod;

		isMethod = false;
		instanceClass = getInstanceClass();
		names = new String[2];
		prefix = "get";
		if (getterClass.equals(Boolean.class))
			prefix = "is";
		names[0] = prefix + getterName;
		names[1] = getterName;
		for (int i = 0; i < names.length; i++) {
			try {
				method = instanceClass.getMethod(names[i]);
				isMethod = true;
				return invoke(obj, method);

			} catch (NoSuchMethodException e) {
			}
		}

		try {
			field = instanceClass.getField(getterName);
			result = field.get(getInstanceClass());
			return getterClass.cast(result);
		} catch (NoSuchFieldException e) {
		}

		if (isMethod) {
			message = "There is no public, parameterless method that "
					+ "maches get\"{0}\" or \"{0}\".";
			message = MessageFormat.format(message, getterName);
			throw new MalformedMemberException(message);
		}

		message = "There is no public method or field that "
				+ "maches \"get{0}\" or \"{0}\".";
		message = MessageFormat.format(message, getterName);
		throw new NoSuchMemberException(message);

	}

	private EGetter invoke(E obj, Method method)
			throws ReflectiveOperationException {
		Object result;
		if (obj == null)
			return null;
		result = method.invoke(obj);
		return getterClass.cast(result);
	}

	protected String getGetterName() {
		return getterName;
	}

	@Override
	public final Iterable<Evaluation> evaluateInstance(E obj) throws Exception {
		EGetter value;
		value = getValue(obj);
		return emptyIfNull(evaluateGetter(obj, value));
	}

	protected abstract Iterable<Evaluation> evaluateGetter(E scopeInstance,
			EGetter getterValue) throws Exception;
}
