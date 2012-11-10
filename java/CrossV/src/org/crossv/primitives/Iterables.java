package org.crossv.primitives;

import java.util.*;

public class Iterables {

	public static <E> List<E> toList(Iterable<E> iterable) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");

		List<E> list = new ArrayList<E>();
		for (E item : iterable)
			list.add(item);
		return list;
	}

	public static <E> List<E> toList(E[] iterable) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");

		List<E> list = new ArrayList<E>();
		for (E item : iterable)
			list.add(item);
		return list;
	}

	public static <E> List<E> asList(IterableOnly<E> iterable) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");

		if (iterable.innerIterable() instanceof List)
			return (List<E>) iterable.innerIterable();
		return toList((Iterable<E>) iterable);
	}

	public static <E> List<E> asList(Iterable<E> iterable) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");

		if (iterable instanceof List)
			return (List<E>) iterable;
		return null;
	}

	public static <E extends Comparable<? super E>> List<E> createSortedIterable(
			Iterable<E> iterable) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");

		List<E> list = toList(iterable);
		Collections.sort(list);
		return list;
	}

	public static <E> Iterable<E> createSortedIterable(Iterable<E> iterable,
			Comparator<? super E> comparator) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");
		if (comparator == null)
			throw new ArgumentNullException("comparator");

		List<E> list = toList(iterable);
		Collections.sort(list, comparator);
		return list;
	}

	public static <E> boolean any(Iterable<E> iterable, Predicate<E> predicate) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");
		if (predicate == null)
			throw new ArgumentNullException("predicate");

		Iterator<E> iterator = iterable.iterator();
		while (iterator.hasNext())
			if (predicate.eval(iterator.next()))
				return true;
		return false;
	}

	public static <E> boolean any(Iterable<E> iterable) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");

		Iterator<E> iterator = iterable.iterator();
		if (iterator.hasNext())
			return true;
		return false;
	}

	private static <E> int countInternal(Iterable<E> iterable,
			Predicate<E> predicate) {

		Iterator<E> iterator = iterable.iterator();
		int count = 0;
		while (iterator.hasNext())
			if (predicate == null || predicate.eval(iterator.next()))
				count++;
		return count;
	}

	public static <E> int count(Iterable<E> iterable, Predicate<E> predicate) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");
		if (predicate == null)
			throw new ArgumentNullException("predicate");

		return countInternal(iterable, predicate);
	}

	public static <E> int count(Iterable<E> iterable) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");

		if (iterable instanceof List)
			return ((List<E>) iterable).size();

		return countInternal(iterable, null);
	}

	public static <E> E first(Iterable<E> iterable, Predicate<E> predicate) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");
		if (predicate == null)
			throw new ArgumentNullException("predicate");

		Iterator<E> iterator = iterable.iterator();
		while (iterator.hasNext()) {
			E next = iterator.next();
			if (predicate.eval(next))
				return next;
		}
		return null;
	}

	public static <E, ER> Iterable<ER> select(Iterable<E> iterable,
			Function<E, ER> converter) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");
		if (converter == null)
			throw new ArgumentNullException("converter");

		Iterator<E> iterator = iterable.iterator();
		List<ER> selection = new ArrayList<ER>();
		while (iterator.hasNext())
			selection.add(converter.eval(iterator.next()));

		return new IterableOnly<ER>(selection);
	}

	public static <E, ER> Iterable<ER> select(E[] iterable,
			Function<E, ER> converter) {
		if (iterable == null)
			throw new ArgumentNullException("iterable");
		if (converter == null)
			throw new ArgumentNullException("converter");

		List<ER> selection = new ArrayList<ER>();
		for (int i = 0; i < iterable.length; i++) {
			selection.add(converter.eval(iterable[i]));
		}
		return new IterableOnly<ER>(selection);
	}

	public static <E> void addAllToList(List<E> list, Iterable<E> iterable) {
		for (E item : iterable)
			list.add(item);
	}

	public static <E> Iterable<E> toIterable(E... es) {
		List<E> result = new ArrayList<E>();
		for (E e : es)
			result.add(e);

		return new IterableOnly<E>(result);
	}

	public static <E> Iterable<E> empty() {
		return new IterableOnly<E>();
	}
	
	public static <E> boolean containsAll(Iterable<E> iterable, Iterable<E> objs) {
		for(E e : objs)
			for(E e1 : iterable)
				if((e == null && e1 != null) || (e != null && !e.equals(e1)))
					return false;
		return true;
	}
}