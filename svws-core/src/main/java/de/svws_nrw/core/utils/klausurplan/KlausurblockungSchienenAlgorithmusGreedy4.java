package de.svws_nrw.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/**
 * Die Strategie implementiert den DSatur-Algorithmus. Der Algorithmus "degree of saturation (DSatur)"
 * selektiert die Klausuren nacheinander und priorisiert die Klausuren dynamisch. <br>
 *
 * Von allen Klausuren die noch nicht einer Schiene zugeordnet sind,
 * wird diejenige gewählt, die die meisten bereits zugeordneten Nachbarn hat.
 *
 * @see    <a href= "https://en.wikipedia.org/wiki/DSatur">Wikipedia - DSatur</a>
 * @author Benjamin A. Bartsch
 */
public final class KlausurblockungSchienenAlgorithmusGreedy4 extends KlausurblockungSchienenAlgorithmusAbstract {

	/**
	 * Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public KlausurblockungSchienenAlgorithmusGreedy4(final @NotNull Random pRandom, final @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "DSatur";
	}

	@Override
	public void berechne(final long pZeitEnde) {
		_dynDaten.aktion_EntferneAlles_KlausurenMitDenMeistenNachbarsfarben_SchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktion_EntferneAlles_KlausurenMitDenMeistenNachbarsfarben_SchienenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

}
