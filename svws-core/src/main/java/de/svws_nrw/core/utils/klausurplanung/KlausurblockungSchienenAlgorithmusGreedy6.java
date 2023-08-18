package de.svws_nrw.core.utils.klausurplanung;

import java.util.Random;

import de.svws_nrw.core.adt.collection.LinkedCollection;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Die Strategie implementiert den Algorithmus "Recursive Largest First (RLF)".
 *
 * @author Benjamin A. Bartsch
 */
public final class KlausurblockungSchienenAlgorithmusGreedy6 extends KlausurblockungSchienenAlgorithmusAbstract {

	/**
	 * Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public KlausurblockungSchienenAlgorithmusGreedy6(final @NotNull Random pRandom, final @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Recursive Largest First (RLF)";
	}

	@Override
	public void berechne(final long pZeitEnde) {
		berechne();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			berechne();

			if (_dynDaten.gibIstBesserAlsZustand1())
				_dynDaten.aktionZustand1Speichern();
			else
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

	private void berechne() {
		// reset
		_dynDaten.aktionKlausurenAusSchienenEntfernen();

		final @NotNull LinkedCollection<@NotNull Integer> setS = new LinkedCollection<>();

		while (_dynDaten.gibAnzahlNichtverteilterKlausuren() > 0) {
			setS.clear();
			// Starte mit dem Knoten (Klausur), der die meistens Nachbarn hat.
			final int nr1 = _dynDaten.gibKlausurDieFreiIstMitDenMeistenFreienNachbarn();
			final int s = _dynDaten.aktionSetzeKlausurInNeueSchiene(nr1);
			setS.addLast(nr1);

			// Wähle den Knoten (Klausur), der nicht zu allen Knoten in setS benachbart ist,
			// und gleichzeitig die meisten Nachbarn hat, die aber Verbindungen zu setS haben.
			int nr2 = _dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
			while (nr2 >= 0) {
				setS.addLast(nr2);
				if (!_dynDaten.aktionSetzeKlausurInSchiene(nr2, s))
					throw new DeveloperNotificationException("Fehler im Algorithmus Greedy6!");
				nr2 = _dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
			}
		}

	}

}
