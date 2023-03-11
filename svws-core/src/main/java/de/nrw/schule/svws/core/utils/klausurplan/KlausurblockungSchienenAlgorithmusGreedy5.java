package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** Die Strategie implementiert einen "Simulated Annealing" Algorithmus. Eine zufällige Anzahl an Schienen wird zerstört
 * (d.h. alle Klausuren werden entfernt) und anschließend werden die Klausuren neu verteilt.
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmusGreedy5 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. */
	public KlausurblockungSchienenAlgorithmusGreedy5(@NotNull Random pRandom, @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Simulated Annealing";
	}

	@Override
	public void berechne(long pZeitEnde) {

		// _dynDaten.aktionZustand2Laden();
		_dynDaten.aktionEntferneAllesSetzeKlausurenZufaelligAufSchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktionZerstoereEinigeSchienenUndVerteileNeu();

			if (_dynDaten.gibIstBesserAlsZustand1() == true)
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2() == true)
			_dynDaten.aktionZustand2Speichern();
	}

}
