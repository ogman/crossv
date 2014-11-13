package org.crossv.expressions.tests;

import static org.crossv.expressions.Expression.constant;
import static org.crossv.expressions.Expression.instance;
import static org.crossv.expressions.Expression.memberAccess;
import static org.crossv.expressions.Expression.call;
import static org.crossv.expressions.Expression.add;
import static org.crossv.expressions.Expression.validIf;
import static org.crossv.tests.helpers.Matchers.assignableTo;
import static org.crossv.tests.helpers.Matchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.crossv.expressions.EvaluatorDescriptor;
import org.crossv.expressions.Expression;
import org.crossv.expressions.IllegalOperandException;
import org.crossv.tests.subjects.Monkey;
import org.junit.Test;

public class ValidIfExpressionTests {

	@Test(expected = IllegalArgumentException.class)
	public void createValidIfExpression_NullScope_ThrowsIllegalArgumentException() {
		Expression scope = null;
		Expression test = constant(true);
		String ifFalseMessage = "message";
		validIf(scope, test, ifFalseMessage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createValidIfExpression_NullTest_ThrowsIllegalArgumentException() {
		Expression scope = memberAccess(new Monkey(), "nickname");
		Expression test = null;
		String ifFalseMessage = "error";
		validIf(scope, test, ifFalseMessage);
	}

	@Test(expected = IllegalOperandException.class)
	public void createValidIfExpression_ScopeOtherThanMemberAccessCallOrStringConstant_ThrowsIllegalOperandException() {
		Expression scope = add(1, 2);
		Expression test = constant(true);
		String ifFalseMessage = "error";
		validIf(scope, test, ifFalseMessage);
	}

	@Test
	public void createValidIfExpression_AnyOperands_ReturnClassIsEvaluatorDescriptor() {
		Class<?> expectedClass = EvaluatorDescriptor.class;
		Expression scope = memberAccess(new Monkey(), "nickname");
		Expression test = constant(true);
		String ifFalseMessage = "error";
		Expression e = validIf(scope, test, ifFalseMessage);
		assertThat(e.getResultClass(), is(assignableTo(expectedClass)));
	}

	@Test
	public void createValidIfExpressionForMemberAccess_callingToString_getsJavaLikeExpression() {
		Expression scope = memberAccess(instance(), "nickname");
		Expression test = constant(true);
		String ifFalseMessage = "error";
		Expression e = validIf(scope, test, ifFalseMessage);
		assertThat(e.toString(), is("obj.nickname validif true else \"error\""));
	}

	@Test
	public void createValidIfExpressionForCall_callingToString_getsJavaLikeExpression()
			throws Exception {
		Expression scope = call(instance(), "nickname");
		Expression test = constant(true);
		String ifFalseMessage = "error";
		Expression e = validIf(scope, test, ifFalseMessage);
		assertThat(e.toString(),
				is("obj.nickname() validif true else \"error\""));
	}

	@Test
	public void evaluateValidIfExpressionForMockeyInstance_ForInstanceNickname_ReturnsDescriptorWithScopeTextNickname()
			throws Exception {
		Expression scope = memberAccess(instance(), "nickname");
		Expression test = constant(true);
		String ifFalseMessage = "error";
		Expression e = validIf(scope, test, ifFalseMessage);
		EvaluatorDescriptor descriptor;
		descriptor = (EvaluatorDescriptor) e.evaluate(new Monkey());
		assertThat(descriptor.getScopeDescription(), is(equalTo("nickname")));
	}

	@Test
	public void evaluateValidIfExpressionForMockeyInstance_CallInstanceGetRelativesAsList_ReturnsDescriptorWithScopeTextGetRelativesAsList()
			throws Exception {
		Expression scope = call(instance(), "getRelativesAsList");
		Expression test = constant(true);
		String ifFalseMessage = "error";
		Expression e = validIf(scope, test, ifFalseMessage);
		EvaluatorDescriptor descriptor;
		descriptor = (EvaluatorDescriptor) e.evaluate(new Monkey());
		assertThat(descriptor.getScopeDescription(),
				is(equalTo("getRelativesAsList")));
	}

	public void evaluateValidIfExpressionForMockeyInstance_WithErrorAsIfFalseMessage_ReturnsDescriptorWithIfFalseMessageError()
			throws Exception {
		Expression scope = memberAccess(instance(), "nickname");
		Expression test = constant(true);
		String ifFalseMessage = "error";
		Expression e = validIf(scope, test, ifFalseMessage);
		EvaluatorDescriptor descriptor;
		descriptor = (EvaluatorDescriptor) e.evaluate(new Monkey());
		assertThat(descriptor.getIfFalseMessage(), is(equalTo("error")));
	}

	@Test
	public void evaluateValidIfExpression_WithConstantBooleanAsTest_ReturnsDescriptorWithTestSetToBooleanConstant()
			throws Exception {
		Expression scope = memberAccess(instance(), "nickname");
		Expression test = constant(true);
		String ifFalseMessage = "error";
		Expression e = validIf(scope, test, ifFalseMessage);
		EvaluatorDescriptor descriptor;
		descriptor = (EvaluatorDescriptor) e.evaluate(new Monkey());
		assertThat(descriptor.getTest(), is(equalTo(test)));
	}
}