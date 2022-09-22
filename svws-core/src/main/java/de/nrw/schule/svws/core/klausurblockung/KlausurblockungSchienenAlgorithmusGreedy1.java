package de.nrw.schule.svws.core.klausurblockung;

import java.util.Random;

import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Die Strategie 'Greedy1' verteilt die Klausuren in zufälliger Reihenfolge auf bereits existierende Schienen in
 * zufälliger Reihenfolge. Ist dies nicht möglich, wird eine neue Schiene erzeugt.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmusGreedy1 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten. */
	public KlausurblockungSchienenAlgorithmusGreedy1(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pLogger, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren zufällig & Schienen zufällig";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_dynDaten.aktionEntferneAllesSetzeKlausurenZufaelligAufSchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktionEntferneAllesSetzeKlausurenZufaelligAufSchienenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
