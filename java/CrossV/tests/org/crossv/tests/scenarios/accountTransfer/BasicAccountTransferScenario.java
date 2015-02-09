//package org.crossv.tests.scenarios.accountTransfer;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertThat;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//
//import org.crossv.Evaluation;
//import org.crossv.Evaluator;
//import org.crossv.Validation;
//import org.crossv.Validator;
//import org.crossv.expressions.Expression;
//import org.crossv.expressions.Expressions;
//import org.crossv.strategies.ValidationStrategy;
//import org.junit.Test;
//
//public class BasicAccountTransferScenario {
//
//	// @formatter:off
//	Expression expressions = Expressions.parse(
//		"when obj instanceof org.crossv.tests.scenarios.users.User ["
//		+ "obj.Email validif obj.Email != null else \"Email is required.\","
//		+ "obj.Password validif obj.Password != null else \"Password is required.\","
//		+ "obj.FirstName warnif obj.FirstName == null then \"First name is missing, and is nice to have a name.\","
//		+ "obj.LastName warnif obj.LastName == null then \"Last name is missing, and is nice to have a name.\""
//		+ "]\n"
//		+ "\n"
//		+ "when obj instanceof org.crossv.tests.scenarios.users.User "
//		+ "		&& context instanceof org.crossv.tests.scenarios.users.BasicContext ["
//		+ "obj.Email validif context.validatesEmailPattern(obj.Email) else \"Email does not respect 'email@company.com' pattern.\","
//		+ "obj.Password validif context.meetsMinimalComplexity(obj.Password) else \"Password is too week.\","
//		+ "]\n"
//		+ "\n"
//		+ "when obj instanceof org.crossv.tests.scenarios.users.User "
//		+ "		&& context instanceof org.crossv.tests.scenarios.users.StoreContext ["
//		+ "obj.Email validif !context.containsEmail(obj.Email) else \"Email already registered.\","
//		+ "]\n");
//	// @formatter:on
//
//	@Test
//	public void newUserInformationWithEmailAndPasswordAndNoContextValidation()
//			throws Exception {
//
//		Iterable<Evaluator> evaluators;
//		Validator validator;
//		Validation validation;
//
//		evaluators = expressions.evaluate();
//		validator = new Validator(evaluators);
//		validator.setStrategy(ValidationStrategy.BY_CONTEXT);
//
//		Account user = new Account("email@company.com", "secret password");
//		validation = validator.validate(Account.class, user);
//
//		StringWriter actual = new StringWriter();
//		PrintWriter actualWriter = new PrintWriter(actual);
//		actualWriter.println("Validation is successfull: "
//				+ validation.isSuccessful());
//		actualWriter.println("Validation results [");
//
//		for (Evaluation evaluation : validation.getResults()) {
//			String className = evaluation.getClass().getSimpleName();
//			String message = evaluation.getMessage();
//			actualWriter.println(className + ": " + message);
//		}
//		actualWriter.println("]");
//
//		StringWriter expected = new StringWriter();
//		PrintWriter expectedWriter = new PrintWriter(expected);
//		// @formatter:off
//		expectedWriter.println("Validation is successfull: true");
//		expectedWriter.println("Validation results [");
//		expectedWriter.println("EvaluationWarning: First name is missing, and is nice to have a name.");
//		expectedWriter.println("EvaluationWarning: Last name is missing, and is nice to have a name.");
//		expectedWriter.println("]");
//		// @formatter:on
//		assertThat(actual.toString(), is(expected.toString()));
//	}
//
//	@Test
//	public void newUserInformationWithEmailAndPasswordAndBasicContextValidation()
//			throws Exception {
//
//		Iterable<Evaluator> evaluators;
//		Validator validator;
//		Validation validation;
//
//		evaluators = expressions.evaluate();
//		validator = new Validator(evaluators);
//		validator.setStrategy(ValidationStrategy.BY_CONTEXT);
//
//		Account user = new Account("email@company.com", "s3cr3t PAssword!!");
//		BasicContext context = new BasicContext();
//		validation = validator.validate(Account.class, user, context);
//
//		StringWriter actual = new StringWriter();
//		PrintWriter actualWriter = new PrintWriter(actual);
//		actualWriter.println("Validation is successfull: "
//				+ validation.isSuccessful());
//		actualWriter.println("Validation results [");
//
//		for (Evaluation evaluation : validation.getResults()) {
//			String className = evaluation.getClass().getSimpleName();
//			String message = evaluation.getMessage();
//			actualWriter.println(className + ": " + message);
//		}
//		actualWriter.println("]");
//
//		StringWriter expected = new StringWriter();
//		PrintWriter expectedWriter = new PrintWriter(expected);
//		// @formatter:off
//		expectedWriter.println("Validation is successfull: true");
//		expectedWriter.println("Validation results [");
//		expectedWriter.println("EvaluationWarning: First name is missing, and is nice to have a name.");
//		expectedWriter.println("EvaluationWarning: Last name is missing, and is nice to have a name.");
//		expectedWriter.println("]");
//		// @formatter:on
//		assertThat(actual.toString(), is(expected.toString()));
//	}
//
//	@Test
//	public void newUserInformationWithEmailAndPasswordAndStoreContextValidation()
//			throws Exception {
//
//		Iterable<Evaluator> evaluators;
//		Validator validator;
//		Validation validation;
//
//		evaluators = expressions.evaluate();
//		validator = new Validator(evaluators);
//		validator.setStrategy(ValidationStrategy.BY_CONTEXT);
//
//		Account user = new Account("already_registered@company.com",
//				"s3cr3t PAssword!!", "first name", "last name");
//		StoreContext context = new StoreContext();
//		validation = validator.validate(Account.class, user, context);
//
//		StringWriter actual = new StringWriter();
//		PrintWriter actualWriter = new PrintWriter(actual);
//		actualWriter.println("Validation is successfull: "
//				+ validation.isSuccessful());
//		actualWriter.println("Validation results [");
//
//		for (Evaluation evaluation : validation.getResults()) {
//			String className = evaluation.getClass().getSimpleName();
//			String message = evaluation.getMessage();
//			actualWriter.println(className + ": " + message);
//		}
//		actualWriter.println("]");
//
//		StringWriter expected = new StringWriter();
//		PrintWriter expectedWriter = new PrintWriter(expected);
//		// @formatter:off
//		expectedWriter.println("Validation is successfull: false");
//		expectedWriter.println("Validation results [");
//		expectedWriter.println("EvaluationFault: Email already registered.");
//		expectedWriter.println("]");
//		// @formatter:on
//		assertThat(actual.toString(), is(expected.toString()));
//	}
//}
