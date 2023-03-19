package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** 
 * Dieser Algorithmus hat folgende Strategie - Pseudocode:
 * <pre>
 * Solange nicht alle Klausuren verteilt sind
 *     Erzeuge einen neuen Termin 
 *     Gehe die Klausurgruppen in zufälliger Reihenfolge durch.
 *         Versuche die Klausurgruppe hinzuzufügen.
 * </pre>
 * 
 * 
 * @author Benjamin A. Bartsch 
 */
public class KlausurterminblockungAlgorithmusGreedy1b extends KlausurterminblockungAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public KlausurterminblockungAlgorithmusGreedy1b(@NotNull Random pRandom, @NotNull KlausurterminblockungDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren zufällig & Schienen nacheinander";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_dynDaten.aktion_Clear_TermineNacheinander_KlausurenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_Clear_TermineNacheinander_KlausurenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
