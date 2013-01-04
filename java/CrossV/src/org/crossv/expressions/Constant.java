package org.crossv.expressions;

public class Constant extends Expression {
	private final Object value;

	public Constant(Object value) {
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value != null ? value.toString() : "null";
	}

	@Override
	public Class<?> getResultClass() {
		return value != null ? value.getClass() : Object.class;
	}
}
