package de.svws_nrw.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/**
 * Die Strategie 'Greedy2' verteilt die Klausuren <b>mit einem höheren Knotengrad</b> auf bereits existierende Schienen
 * in zufälliger Reihenfolge. Ist dies nicht möglich, wird eine neue Schiene erzeugt.
 *
 * @author Benjamin A. Bartsch
 */
public final class KlausurblockungSchienenAlgorithmusGreedy2 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public KlausurblockungSchienenAlgorithmusGreedy2(final @NotNull Random pRandom, final @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren nach Knotengrad & Schienen zufällig";
	}

	@Override
	public void berechne(final long pZeitEnde) {
		_dynDaten.aktion_EntferneAlles_KlausurenHoherGradZuerst_SchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_EntferneAlles_KlausurenHoherGradZuerst_SchienenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
