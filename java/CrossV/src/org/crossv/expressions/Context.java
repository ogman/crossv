package org.crossv.expressions;

import static org.crossv.expressions.ExpressionClass.CObject;

public final class Context extends Expression {

	private final Class<?> clazz;

	public Context() {
		this.clazz = CObject;
	}

	public Context(Class<?> clazz) {
		this.clazz = clazz;
	}

	@Override
	public Class<?> getResultClass() {
		return clazz;
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitContext(this);
	}
}
