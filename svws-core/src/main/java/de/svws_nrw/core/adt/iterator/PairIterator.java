package de.svws_nrw.core.adt.iterator;

import de.svws_nrw.asd.adt.PairNN;
import jakarta.validation.constraints.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Ein Iterator, der über alle Paare einer Liste iteriert.
 *
 * @param <T> Der Typ der Elemente in der Collection.
 * @author Benjamin A. Bartsch
 */
public final class PairIterator<T> implements Iterator<@NotNull PairNN<T, T>> {

	/** Die interne Liste der Elemente. */
	private final @NotNull List<T> elemente;

	/** Der Iterationsmodus, der bestimmt, welche Paare geliefert werden. */
	private final @NotNull PairIteratorModus modus;

	/** Der aktuelle Index des ersten Elements des nächsten Paares. */
	private int i;

	/** Der aktuelle Index des zweiten Elements des nächsten Paares. */
	private int j;

	/** Das nächste Paar, das von {@link #next()} geliefert wird, oder {@code null}. */
	private PairNN<T, T> nextElement;

	/**
	 * Erstellt einen neuen Iterator über alle Paare der angegebenen Liste im gewünschten Modus.
	 *
	 * @param elemente   die interne Liste der Elemente
	 * @param modus      der gewünschte Iterationsmodus
	 */
	PairIterator(final @NotNull List<T> elemente, final @NotNull PairIteratorModus modus) {
		this.elemente = elemente;
		this.modus = modus;
		this.i = 0;
		this.j = 0;
		this.nextElement = null;

		if (modus == PairIteratorModus.LOWER_ONLY) {
			j = 1;
		}

		calculateNextElement();
	}

	/**
	 * Diese Operation wird nicht unterstützt.
	 *
	 * @throws UnsupportedOperationException immer
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Liefert {@code true}, falls der Iterator mindestens ein weiteres Paar bereithält.
	 *
	 * @return {@code true}, falls der Iterator mindestens ein weiteres Paar bereithält.
	 */
	@Override
	public boolean hasNext() {
		return nextElement != null;
	}

	/**
	 * Liefert das nächste Paar und rückt den Iterator einen Schritt vor.
	 *
	 * @return das nächste Paar und rückt den Iterator einen Schritt vor.
	 */
	@Override
	public @NotNull PairNN<T, T> next() {
		if (nextElement == null) {
			throw new NoSuchElementException();
		}
		final PairNN<T, T> result = nextElement;
		calculateNextElement();
		return result;
	}

	/**
	 * Berechnet das nächste Paar und speichert es in {@link #nextElement}.
	 * Wenn kein weiteres Paar existiert, wird {@link #nextElement} auf {@code null} gesetzt.
	 */
	private void calculateNextElement() {
		nextElement = null;

		while (i < elemente.size()) {
			if (j >= elemente.size()) {
				i++;
				j = (modus == PairIteratorModus.LOWER_ONLY) ? (i + 1) : 0;
				continue;
			}

			final boolean skip =
					((modus == PairIteratorModus.NO_EQUAL) && (i == j))
				 || ((modus == PairIteratorModus.LOWER_ONLY) && (i >= j));

			if (!skip) {
				nextElement = new PairNN<>(elemente.get(i), elemente.get(j));
				j++;
				return;
			}

			j++;
		}
	}

}
