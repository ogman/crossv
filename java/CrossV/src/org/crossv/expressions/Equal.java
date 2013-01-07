package org.crossv.expressions;

public class Equal extends BooleanExpression {
	public Equal(Expression left, Expression right) {
		super(left, right);
		checkOperandClass(left, right.getResultClass());
	}

	@Override
	protected String getOperatorString() {
		return "==";
	}

}
