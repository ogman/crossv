package org.crossv.expressions.tests;

import static org.crossv.expressions.Expression.bitwiseXor;
import static org.crossv.expressions.Expression.constant;
import static org.crossv.tests.helpers.Matchers.assignableTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.crossv.expressions.Expression;
import org.crossv.expressions.IllegalOperandException;
import org.junit.Test;

public class XorExpressionTests {

	@Test(expected = IllegalOperandException.class)
	public void createXorExpression_BooleanAndIntOperands_ThrowsIllegalOperandException() {
		bitwiseXor(constant(false), 1);
	}

	@Test(expected = IllegalOperandException.class)
	public void createXorExpression_DoubleAndBooleanOperands_ThrowsIllegalOperandException() {
		bitwiseXor((double) 1, constant(false));
	}

	@Test(expected = IllegalOperandException.class)
	public void createXorExpression_DoubleAndByteOperands_ThrowsIllegalOperandException() {
		bitwiseXor((double) 1, (byte) 1);
	}

	@Test
	public void createXorExpression_BooleanOperands_ReturnClassIsBoolean() {
		Class<?> expectedClass = Boolean.class;
		boolean left = false;
		boolean right = true;
		Expression e = bitwiseXor(left, right);
		assertThat(e.getResultClass(), is(assignableTo(expectedClass)));
	}

	@Test
	public void createXorExpression_NumericOperandsPromotedToLong_ReturnClassIsLong() {
		Class<?> expectedClass = Long.class;
		Object left = (byte) 1;
		Object right = (long) 1;
		Expression e = bitwiseXor(left, right);
		assertThat(e.getResultClass(), is(assignableTo(expectedClass)));
	}

	@Test
	public void createXorExpression_NumericOperandsPromotedToInt_ReturnClassIsInt() {
		Class<?> expectedClass = Integer.class;
		Object left = (byte) 1;
		Object right = (int) 1;
		Expression e = bitwiseXor(left, right);
		assertThat(e.getResultClass(), is(assignableTo(expectedClass)));
	}

	@Test
	public void createXorExpression_callingToString_getsJavaLikeExpression() {
		Expression e = bitwiseXor(1, 2);
		assertThat(e.toString(), is("1 ^ 2"));
	}
}
