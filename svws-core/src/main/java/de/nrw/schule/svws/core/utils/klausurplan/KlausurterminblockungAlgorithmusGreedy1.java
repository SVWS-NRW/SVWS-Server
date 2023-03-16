package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** 
 * Die Strategie 'Greedy1' verteilt die Klausuren in zufälliger Reihenfolge auf bereits 
 * existierende Schienen in zufälliger Reihenfolge. Ist dies nicht möglich, wird eine neue Schiene erzeugt.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurterminblockungAlgorithmusGreedy1 extends KlausurterminblockungAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public KlausurterminblockungAlgorithmusGreedy1(@NotNull Random pRandom, @NotNull KlausurterminblockungDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren zufällig & Schienen zufällig";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_dynDaten.aktion_EntferneAlles_KlausurenZufaellig_SchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_EntferneAlles_KlausurenZufaellig_SchienenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
