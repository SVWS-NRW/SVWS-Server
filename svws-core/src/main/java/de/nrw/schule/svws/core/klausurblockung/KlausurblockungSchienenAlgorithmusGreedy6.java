package de.nrw.schule.svws.core.klausurblockung;

import java.util.Random;

import de.nrw.schule.svws.core.adt.collection.LinkedCollection;
import de.nrw.schule.svws.logger.Logger;
import jakarta.validation.constraints.NotNull;

/** Die Strategie implementiert den Algorithmus "Recursive Largest First (RLF)".
 * 
 * @author Benjamin A. Bartsch */
public class KlausurblockungSchienenAlgorithmusGreedy6 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pLogger   Logger für Benutzerhinweise, Warnungen und Fehler.
	 * @param pDynDaten Die aktuellen Blockungsdaten. */
	public KlausurblockungSchienenAlgorithmusGreedy6(@NotNull Random pRandom, @NotNull Logger pLogger,
			@NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pLogger, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Recursive Largest First (RLF)";
	}

	@Override
	public void berechne(long pZeitEnde) {
		berechne();
		_dynDaten.aktionZustand1Speichern();

		while (System.currentTimeMillis() < pZeitEnde) {
			berechne();

			if (_dynDaten.gibIstBesserAlsZustand1() == true) 
				_dynDaten.aktionZustand1Speichern();
			 else 
				_dynDaten.aktionZustand1Laden();
		}

		if (_dynDaten.gibIstBesserAlsZustand2() == true)
			_dynDaten.aktionZustand2Speichern();
	}

	private void berechne() {
		// reset
		_dynDaten.aktionKlausurenAusSchienenEntfernen();
		
		@NotNull LinkedCollection<@NotNull Integer> setS = new LinkedCollection<>();

		while (_dynDaten.gibAnzahlNichtverteilterKlausuren() > 0) {
			setS.clear();
			// Starte mit dem Knoten (Klausur), der die meistens Nachbarn hat.
			int nr1 = _dynDaten.gibKlausurDieFreiIstMitDenMeistenFreienNachbarn();
			int s = _dynDaten.aktionSetzeKlausurInNeueSchiene(nr1);
			setS.addLast(nr1);

			// Wähle den Knoten (Klausur), der nicht zu allen Knoten in setS benachbart ist,
			// und gleichzeitig die meisten Nachbarn hat, die aber Verbindungen zu setS haben.
			int nr2 = _dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
			while (nr2 >= 0) {
				setS.addLast(nr2);
				if (_dynDaten.aktionSetzeKlausurInSchiene(nr2, s) == false)
					System.out.println("FEHLER");
				nr2 = _dynDaten.gibKlausurDieFreiIstUndNichtBenachbartZurMengeAberDerenNachbarnMaximalBenachbartSind(setS);
			}
		}

	}

}
