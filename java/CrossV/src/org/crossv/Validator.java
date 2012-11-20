package org.crossv;

import java.util.ArrayList;
import java.util.List;

import org.crossv.primitives.Iterables;
import org.crossv.strategies.ValidationStrategy;

public class Validator {

	private EvaluatorRegistry registry;
	private ValidationStrategy strategy;

	public Validator(Evaluator evaluator) {
		this(evaluator, (Evaluator[]) null);
	}

	public Validator(Evaluator evaluator1, Evaluator... evaluators) {
		this(new EvaluatorRegistry(evaluator1, evaluators));
	}

	public Validator(EvaluatorRegistry registry) {
		this.registry = registry;
		this.strategy = ValidationStrategy.DEFAULT;
	}

	public ValidationStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(ValidationStrategy strategy) {
		this.strategy = strategy != null ? strategy
				: ValidationStrategy.DEFAULT;
	}

	public <E> Validation validate(Class<E> objClass, E obj) {
		return validate(objClass, obj, NoContext.instance);
	}

	public <E> Validation validate(Class<E> objClass, E obj, Object context) {
		List<Evaluation> allResults = new ArrayList<Evaluation>();
		Iterable<Evaluator> evaluators;

		context = context != null ? context : NoContext.instance;
		Class<?> contextClass = context.getClass();
		evaluators = registry.get(objClass, contextClass);
		evaluators = strategy.apply(evaluators);

		for (Evaluator evaluator : evaluators) {
			Iterable<Evaluation> results = evaluator.evaluate(obj, context);
			Iterables.addAllToList(allResults, results);
		}
		return new Validation(allResults);
	}
}
