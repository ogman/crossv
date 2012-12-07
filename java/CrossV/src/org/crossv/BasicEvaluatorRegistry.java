package org.crossv;

import static org.crossv.primitives.Iterables.addAllToList;
import static org.crossv.primitives.Iterables.emptyIfNull;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.crossv.primitives.ArgumentException;

/**
 * An registry of evaluators that will be used by the {@link Validator}.
 * 
 * @author yochanan.miykael
 */
public final class BasicEvaluatorRegistry implements EvaluatorProvider {
	private List<Evaluator> allEvaluators;
	private Dictionary<Class<?>, List<Evaluator>> noContextEvaluatorsByEvaluatedClass;
	private Dictionary<Class<?>, Dictionary<Class<?>, List<Evaluator>>> contextTable;

	/**
	 * Creates an instance of an {@link BasicEvaluatorRegistry}.
	 */
	public BasicEvaluatorRegistry() {
		this(null);
	}

	/**
	 * Creates an instance of an {@link BasicEvaluatorRegistry}.
	 * 
	 * @param evaluator
	 *            that will be added to the registry.
	 */
	public BasicEvaluatorRegistry(Evaluator evaluator) {
		this(evaluator, (Evaluator[]) null);
	}

	/**
	 * Creates an instance of an {@link BasicEvaluatorRegistry}.
	 * 
	 * @param evaluators
	 *            that will be added to the registry.
	 */
	public BasicEvaluatorRegistry(Evaluator evaluator1, Evaluator... evaluators) {
		allEvaluators = new ArrayList<Evaluator>();
		contextTable = new Hashtable<Class<?>, Dictionary<Class<?>, List<Evaluator>>>();
		noContextEvaluatorsByEvaluatedClass = new Hashtable<Class<?>, List<Evaluator>>();

		if (evaluator1 != null)
			register(evaluator1);

		if (evaluators == null)
			return;
		for (Evaluator evaluator : evaluators)
			register(evaluator);
	}

	/**
	 * Registers an instance of {@link Evaluator}.
	 * 
	 * @param evaluator
	 *            that will be registered.
	 */
	public void register(Evaluator evaluator) {
		Class<?> contextClass;
		Class<?> instanceClass;
		List<Evaluator> evals;
		Dictionary<Class<?>, List<Evaluator>> entry;

		contextClass = evaluator.getContextClass();
		contextClass = contextClass != null ? contextClass : NoContext.class;
		if (contextClass.equals(Object.class))
			throw new ArgumentException("evaluator",
					"An evaluator cannot have Object class as a context. "
							+ "You can provide the NoContext class or simply "
							+ "'null' instead.");

		instanceClass = evaluator.getInstanceClass();
		if (!contextClass.equals(NoContext.class)) {

			entry = contextTable.get(contextClass);
			entry = entry != null ? entry
					: new Hashtable<Class<?>, List<Evaluator>>();

			contextTable.put(contextClass, entry);

			evals = entry.get(instanceClass);
			evals = evals != null ? evals : new ArrayList<Evaluator>();

			entry.put(instanceClass, evals);

			if (!evals.contains(evaluator))
				evals.add(evaluator);
		}

		if (contextClass.equals(NoContext.class)) {

			evals = noContextEvaluatorsByEvaluatedClass.get(instanceClass);
			evals = evals != null ? evals : new ArrayList<Evaluator>();
			noContextEvaluatorsByEvaluatedClass.put(instanceClass, evals);

			if (!evals.contains(evaluator))
				evals.add(evaluator);
		}

		allEvaluators.add(evaluator);
	}

	/**
	 * Registers an instance of {@link ContextEvaluator}&lt;E, EContext&gt;.
	 * 
	 * @param evaluator
	 *            that will be registered.
	 */
	public <E, EContext> void register(ContextEvaluator<E, EContext> evaluator) {
		register((Evaluator) evaluator);
	}

	/**
	 * Registers an instance of {@link BasicEvaluator}&lt;E&gt;.
	 * 
	 * @param evaluator
	 *            that will be registered.
	 */
	public <E> void register(BasicEvaluator<E> evaluator) {
		register((Evaluator) evaluator);
	}

	/**
	 * Evaluates if the provided evaluator is present in the registry.
	 * 
	 * @param evaluator
	 *            that will be searched.
	 * @return {@code true} is the provided evaluator is present in the
	 *         registry.{@code false} otherwise.
	 */
	public boolean contains(Evaluator evaluator) {
		return allEvaluators.contains(evaluator);
	}

	/**
	 * Gets all the registered evaluators for the provided object and context
	 * {@link Class}es.
	 * 
	 * @param objClass
	 *            is the {@link Class} of the object that will be evaluated.
	 * @param contextClass
	 *            is the {@link Class} of the context on which the object will
	 *            be evaluated.
	 * @return a sequence of evaluators for the provided object and context
	 *         {@link Class}es.
	 */
	@Override
	public <E, EContext> Iterable<Evaluator> get(Class<E> objClass,
			Class<EContext> contextClass) {
		List<Evaluator> result;
		Class<?> actualContextClass;
		Dictionary<Class<?>, List<Evaluator>> entry;
		List<Evaluator> all;

		actualContextClass = contextClass != null ? contextClass
				: NoContext.class;
		result = new ArrayList<Evaluator>();

		if (!actualContextClass.equals(NoContext.class))
			do {
				entry = contextTable.get(actualContextClass);
				all = entry != null ? entry.get(objClass) : null;

				addAllToList(result, all);

				actualContextClass = actualContextClass.getSuperclass();
			} while (actualContextClass != null);
		else {
			result = noContextEvaluatorsByEvaluatedClass.get(objClass);
		}

		return emptyIfNull(result);
	}
}