package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** 
 * Dieser Algorithmus hat folgende Strategie - Pseudocode:
 * <pre>
 * Gehe die Klausuren in zufälliger Reihenfolge durch.
 *     Gehe die Termine in zufälliger Reihenfolge durch.
 *         Versuche die Klausur hinzuzufügen.
 *     Keinen Termin gefunden?
 *         Erzeuge einen neuen Termin und füge Klausur hinzu.
 * </pre>
 * 
 * @author Benjamin A. Bartsch */
public class KlausurterminblockungAlgorithmusGreedy1 extends KlausurterminblockungAlgorithmusAbstract {

	/** 
	 * Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public KlausurterminblockungAlgorithmusGreedy1(@NotNull Random pRandom, @NotNull KlausurterminblockungDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren zufällig --> Termine zufällig";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_dynDaten.aktion_Clear_KlausurenZufaellig_TermineZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_Clear_KlausurenZufaellig_TermineZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
