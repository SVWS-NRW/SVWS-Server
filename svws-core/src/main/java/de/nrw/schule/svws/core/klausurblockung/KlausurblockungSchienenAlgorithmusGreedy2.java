package de.nrw.schule.svws.core.klausurblockung;

import java.util.Random;

import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Die Strategie 'Greedy2' verteilt die Klausuren <b>mit einem höheren Knotengrad</b> auf bereits existierende Schienen
 * in zufälliger Reihenfolge. Ist dies nicht möglich, wird eine neue Schiene erzeugt.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmusGreedy2 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten. */
	public KlausurblockungSchienenAlgorithmusGreedy2(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pLogger, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren nach Knotengrad & Schienen zufällig";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_dynDaten.aktionEntferneAllesSetzeKlausurenHoherGradAufSchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktionEntferneAllesSetzeKlausurenHoherGradAufSchienenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
