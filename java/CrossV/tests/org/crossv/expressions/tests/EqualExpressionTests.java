package org.crossv.expressions.tests;

import static org.crossv.expressions.Expression.equal;
import static org.crossv.tests.helpers.Matchers.assignableTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.crossv.expressions.Expression;
import org.crossv.expressions.IllegalOperandException;
import org.crossv.tests.subjects.Mouse;
import org.crossv.tests.subjects.Rat;
import org.crossv.tests.subjects.WhiteMouse;
import org.junit.Test;

public class EqualExpressionTests {

	@Test(expected = IllegalOperandException.class)
	public void createEqualExpression_DifferentClassOperands_IllegalOperandExceptionIsThrown() {
		equal(new WhiteMouse(), new Rat());
	}
	
	@Test
	public void createEqualExpression_SuperAndSubClassOperands_IllegalOperandExceptionIsNotThrown() {
		equal(new Mouse(), new Rat());
	}

	@Test
	public void createEqualExpression_SubAndSuperClassOperands_IllegalOperandExceptionIsNotThrown() {
		equal(new Rat(), new Mouse());
	}

	@Test
	public void createEqualExpression_IntAndIntOperands_ReturnClassIsBoolean() {
		Class<?> expectedClass = Boolean.class;
		Object left = 1;
		Object right = 2;
		Expression e = equal(left, right);
		assertThat(e.getResultClass(), is(assignableTo(expectedClass)));
	}

	@Test
	public void createEqualExpression_callingToString_getsJavaLikeExpression() {
		Expression e = equal(1, 2);
		assertThat(e.toString(), is("1 == 2"));
	}
}
