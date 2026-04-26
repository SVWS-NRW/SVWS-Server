package de.svws_nrw.core.adt.iterator;

import de.svws_nrw.asd.adt.PairNN;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Ein Iterable, das für jede Iteration einen neuen {@link PairIterator} erzeugt.
 *
 * @param <T> Der Typ der Elemente in der Collection.
 * @author Benjamin A. Bartsch
 */
public final class PairIterable<T> implements Iterable<@NotNull PairNN<T, T>> {

	/** Die interne Liste der Elemente, die aus der Quell-Collection befüllt wird. */
	private final @NotNull List<T> elemente;

	/** Der Iterationsmodus, der bestimmt, welche Paare geliefert werden. */
	private final @NotNull PairIteratorModus modus;

	/**
	 * Erstellt ein neues Iterable über alle Paare der angegebenen Collection im gewünschten Modus.
	 *
	 * @param source   die Quell-Collection (List, Set oder jedes Iterable)
	 * @param modus    der gewünschte Iterationsmodus
	 */
	public PairIterable(final @NotNull Iterable<T> source, final @NotNull PairIteratorModus modus) {
		this.modus = modus;
		this.elemente = new ArrayList<>();
		for (final T element : source) {
			elemente.add(element);
		}
	}


	/**
	 * Erstellt ein neues Iterable über alle Paare des angegebenen Arrays im gewünschten Modus.
	 *
	 * @param source das Quell-Array
	 * @param modus  der gewünschte Iterationsmodus
	 */
	public PairIterable(final @NotNull T @NotNull [] source, final @NotNull PairIteratorModus modus) {
		this.modus = modus;
		this.elemente = Arrays.asList(source);
	}


	/**
	 * Liefert einen neuen Iterator über alle Paare.
	 *
	 * @return einen neuen Iterator über alle Paare.
	 */
	@Override
	public @NotNull Iterator<@NotNull PairNN<T, T>> iterator() {
		return new PairIterator<>(elemente, modus);
	}

}
