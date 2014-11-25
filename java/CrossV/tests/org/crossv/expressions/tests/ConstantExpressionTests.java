package org.crossv.expressions.tests;

import static org.crossv.expressions.Expressions.constant;
import static org.crossv.expressions.Expressions.context;
import static org.crossv.expressions.Expressions.instance;
import static org.crossv.primitives.Iterables.repeat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.crossv.expressions.Expression;
import org.crossv.expressions.IllegalOperandException;
import org.junit.Test;

public class ConstantExpressionTests {

	@Test
	public void createConstantWithNullValueExpression_callingToString_getsJavaLikeExpression() {
		Expression e = constant(null);
		assertThat(e.toString(), is("null"));
	}

	@Test
	public void createConstantWithStringValueExpression_callingToString_getsJavaLikeExpression() {
		Expression e = constant("abc");
		assertThat(e.toString(), is("\"abc\""));
	}

	@Test
	public void createConstantWithNegativeByteValueExpression_callingToString_getsJavaLikeExpression() {
		Expression e = constant((byte) -1);
		assertThat(e.toString(), is("(-1)"));
	}

	@Test
	public void createConstantWithPositiveFloatValueExpression_callingToString_getsJavaLikeExpression() {
		Expression e = constant((float) 1);
		assertThat(e.toString(), is("1.0"));
	}
	
	@Test
	public void createConstantExpressionWithIterableOfFloats_callingToString_getsJavaLikeExpression() {
		Expression e = constant(repeat((float) 1, 3));
		assertThat(e.toString(), is("new java.lang.Float[] { 1.0, 1.0, 1.0 }"));
	}

	@Test(expected = IllegalOperandException.class)
	public void createConstantExpression_WithContextValue_IllegalOperandExceptionIsThrown() {
		constant(context());
	}

	@Test(expected = IllegalOperandException.class)
	public void createConstantExpression_WithInstanceExpressionAsValue_IllegalOperandExceptionIsThrown() {
		constant(instance());
	}

	@Test
	public void evaluateConstantExpression_WithObjectValue_ReturnsTheObject()
			throws Exception {
		Object value = new Object();
		Expression e = constant(value);
		assertThat(e.evaluate(), is(value));
	}
}
