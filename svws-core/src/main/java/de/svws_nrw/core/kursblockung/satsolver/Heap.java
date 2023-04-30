package de.svws_nrw.core.kursblockung.satsolver;

import java.util.Arrays;
import java.util.Random;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Ein Heap aller Variablen. Ganz oben ist die "beste" Variable. Es handelt sich um die Variable, die bei den
 * Berechnungen als nächstes ausgewählt wird.
 *
 * @author Benjamin A. Bartsch
 */
public final class Heap {

	/**
	 * Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	protected final @NotNull Random _random;

	/**
	 * Das Array beinhaltet die Daten und wird als binärer Baum interpretiert. Das linke Kind von Index i ist der Index
	 * 2*i+1 und das rechte Kind ist der Index 2*i+2.
	 */
	private @NotNull Variable @NotNull [] _data;

	/**
	 * Die wirkliche Anzahl an Elementen im Array {@link #_data}.
	 */
	private int _size;

	/**
	 * Konstruktor.
	 *
	 * @param pRandom Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 */
	Heap(final @NotNull Random pRandom) {
		_random = pRandom;
		_data = new Variable[1];
		_size = 0;
	}

	@Override
	public @NotNull String toString() {
		return "Heap: " + Arrays.toString(Arrays.copyOf(_data, _size));
	}

	/**
	 * Überprüft, ob der Heap leer ist.
	 *
	 * @return TRUE, wenn der Heap leer ist.
	 */
	boolean isEmpty() {
		return _size == 0;
	}

	/**
	 * Liefert das oberste (beste) Element des Heaps mit hoher Wahrscheinlichkeit.
	 *
	 * @return Liefert das oberste (beste) Element des Heaps mit hoher Wahrscheinlichkeit.
	 */
	@NotNull Variable top() {
		int index = 0;
		while ((_random.nextDouble() < 0.1) && (index + 1 < _size)) {
			index++;
		}
		return _data[index];
	}

	/**
	 * Liefert die Anzahl an Elementen im Heap.
	 *
	 * @return Die Anzahl an Elementen im Heap.
	 */
	int size() {
		return _size;
	}

	/**
	 * Fügt die Variable "var" dem Heap hinzu. Nach dem Einfügen kennt die Variable mit {@link Variable#index} ihre
	 * eigene Position in diesem Heap.
	 *
	 * @param pVar Die einzufügende Variable.
	 */
	void insert(final @NotNull Variable pVar) {
		// Array voll? --> Arraygröße verdoppelt.
		if (_size == _data.length) {
			_data = Arrays.copyOf(_data, 2 * _size);
		}

		// "var" im Baum nach oben traversieren, solange es besser ist als Elternteil.
		int insertI = _size; // Einfügeposition (= letzte Ebene, ganz rechts).
		while (insertI > 0) {
			final int parentI = (insertI - 1) / 2; // Elternteil-Index.
			final @NotNull
			Variable parentV = _data[parentI]; // Elternteil-Referenz.
			if (pVar.isBetterThan(parentV)) {
				_data[insertI] = parentV;
				parentV.index = insertI;
				insertI = parentI; // Weiter mit Elternteil-Index.
			} else {
				break; // Einfügeposition gefunden.
			}
		}

		// Einfügeposition setzen.
		_data[insertI] = pVar;
		pVar.index = insertI;

		// Größe des Heaps erhöhen.
		_size++;
	}

	/**
	 * Entfernt die Variable pVar vom Heap. Dabei wird zunächst mit Hilfe von {@link Variable#index} die Position
	 * bestimmt. Anschließend wird der Heap so transformiert, dass die Variable entfernt werden kann.
	 *
	 * @param pVar Die zu entfernende Variable.
	 */
	void remove(final @NotNull Variable pVar) {
		_size--;

		// Spezialfall: "var" ist bereits auf dem letzten Index.
		final @NotNull
		Variable lastV = _data[_size];
		if (lastV == pVar) {
			return;
		}

		// Gehe zur Wurzel und ziehe alle Elternteile eine Ebene tiefer.
		int currentI = pVar.index;
		DeveloperNotificationException.check("FEHLER: Die Variable " + pVar + " ist nicht beim Index " + pVar.index + "!", _data[pVar.index] != pVar);

		while (currentI > 0) {
			final int parentI = (currentI - 1) / 2;
			_data[currentI] = _data[parentI];
			_data[currentI].index = currentI;
			currentI = parentI;
		}

		// Die Wurzel ist nun leer, "lastV" wandert runter.
		int parentI = 0;
		int childI = 1;
		while (childI < _size) {
			// Wechsel vom linken Kind "childI" zum rechten Kind?
			if ((childI + 1 < _size) && (_data[childI + 1].isBetterThan(_data[childI])))
				childI = childI + 1;
			// Wandert das Kind hoch?
			final @NotNull
			Variable child = _data[childI];
			if (child.isBetterThan(lastV)) {
				_data[parentI] = child;
				child.index = parentI;
				// next
				parentI = childI;
				childI = parentI * 2 + 1;
			} else {
				break;
			}
		}

		// Einfügeposition gefunden
		_data[parentI] = lastV;
		lastV.index = parentI;
	}

	/**
	 * Falls sich die Variable pVar im Heap befindet, wird sie entfernt und direkt wieder hinzugefügt. Diese Methode
	 * muss aufgerufen werden, sobald die Variable eine neue Bewertung erhalten hat.
	 *
	 * @param pVar Die Variable, deren Bewertung sich geändert hat.
	 */
	public void update(final @NotNull Variable pVar) {
		if (pVar.index >= 0) {
			remove(pVar);
			if (pVar.negation != null)
				remove(pVar.negation);
			insert(pVar);
			if (pVar.negation != null)
				insert(pVar.negation);
		}
	}

}
