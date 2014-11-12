package org.crossv.expressions;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.crossv.primitives.ClassDescriptor;

public class RuntimeMember extends AccessibleObject {
	private String name;
	private Expression instance;

	public RuntimeMember(Expression instance, String name) {
		this.instance = instance;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Expression getInstance() {
		return instance;
	}

	public Object invoke(ExpressionEvaluator instanceEvaluator)
			throws InvocationTargetException, IllegalAccessException,
			IllegalArgumentException {
		try {
			instanceEvaluator.evaluate(instance);
			Object obj = instanceEvaluator.getValue();
			ClassDescriptor desctiptor;
			desctiptor = new ClassDescriptor(obj.getClass());
			AccessibleObject member = desctiptor.findMember(name);
			if (member instanceof Method) {
				Method method = (Method) member;
				return method.invoke(obj);
			} else {
				Field field = (Field) member;
				return field.get(obj);
			}
		} catch (EvaluationException e) {
			throw new InvocationTargetException(e, "");
		}
	}
}
