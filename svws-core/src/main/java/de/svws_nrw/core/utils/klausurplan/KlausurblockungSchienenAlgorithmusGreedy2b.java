package de.svws_nrw.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** Die Strategie verteilt wie die Strategie {@link KlausurblockungSchienenAlgorithmusGreedy2}, nur werden bei dieser
 * Strategie die Schienen nacheinander aufgefüllt.
 * 
 * @author Benjamin A. Bartsch 
 */
public class KlausurblockungSchienenAlgorithmusGreedy2b extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. 
	 */
	public KlausurblockungSchienenAlgorithmusGreedy2b(final @NotNull Random pRandom, final @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Klausuren nach Knotengrad & Schienen nacheinander";
	}

	@Override
	public void berechne(final long pZeitEnde) {
		_dynDaten.aktion_EntferneAlles_SchienenNacheinande_KlausurenHoherGradZuerst();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_EntferneAlles_SchienenNacheinande_KlausurenHoherGradZuerst();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
