package de.nrw.schule.svws.core.klausurblockung;

import java.util.Random;

import de.nrw.schule.svws.core.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Die Strategie implementiert den DSatur-Algorithmus. Der Algorithmus "degree of saturation (DSatur)" selektiert die
 * Klausuren nacheinander und priorisiert die Klausuren dynamisch. Von allen Klausuren die noch nicht einer Schiene
 * zugeordnet sind, wird diejenige gewählt, die die meisten bereits zugeordneten Nachbarn hat.
 * 
 * @see    <a href= "https://en.wikipedia.org/wiki/DSatur">Wikipedia - DSatur</a>
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmusGreedy4 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten. */
	public KlausurblockungSchienenAlgorithmusGreedy4(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pLogger, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "DSatur";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_dynDaten.aktionEntferneAllesSetzeKlausurenMitDenMeistenNachbarsfarbenAufSchienenZufaellig();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			_dynDaten.aktionEntferneAllesSetzeKlausurenMitDenMeistenNachbarsfarbenAufSchienenZufaellig();

			if (_dynDaten.gibIstBesserAlsZustand1() == true)
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2() == true)
			_dynDaten.aktionZustand2Speichern();
	}

}
