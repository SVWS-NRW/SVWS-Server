package de.nrw.schule.svws.core.utils.klausurplan;

import java.util.Random;

import jakarta.validation.constraints.NotNull;

/** Die Klausuren werden rekursiv mit Backtracking auf die Schienen verteilt. Pro Rekursionsschritt wird die freie
 * Klausur gewählt, die die meisten Nachbarsfarben hat. Anschließend wird die Klausur in aufsteigender Reihenfolge auf
 * die Schienen verteilt.
 * 
 * @author Benjamin A. Bartsch
 */
public class KlausurblockungSchienenAlgorithmusGreedy3 extends KlausurblockungSchienenAlgorithmusAbstract {

	/** Die kleinste Schienenanzahl, die bisher gefunden wurde. */
	private int _minSchienen;

	/** Bis zu welchem Zeitpunkt die Rekursion laufen darf. */
	private long _zeitEnde;

	/** TRUE, falls mindestens eine Lösung gefunden wurde. */
	private boolean _saved;

	/** Konstruktor.
	 * 
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten. */
	public KlausurblockungSchienenAlgorithmusGreedy3(@NotNull Random pRandom, @NotNull KlausurblockungSchienenDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Backtracking";
	}

	@Override
	public void berechne(long pZeitEnde) {
		_minSchienen = _dynDaten.gibAnzahlKlausuren();
		_zeitEnde = pZeitEnde;
		_dynDaten.aktionKlausurenAusSchienenEntfernen();
		_saved = false;
		
		berechneRekursiv();

		_dynDaten.aktionZustand1Laden();

		if (_dynDaten.gibIstBesserAlsZustand2() == true)
			_dynDaten.aktionZustand2Speichern();
	}

	private void berechneRekursiv() {

		// Kann das Ergebnis überhaupt noch besser werden?
		if (_dynDaten.gibAnzahlSchienen() >= _minSchienen)
			return;

		// Zeit abgelaufen (aber mindestens eine Speicherung)?
		if ((_saved) && (System.currentTimeMillis() > _zeitEnde))
			return;

		// Erster Aufruf?
		int klausurNr = _dynDaten.gibAnzahlSchienen() == 0 ? //
				_dynDaten.gibKlausurDieFreiIstMitDenMeistenFreienNachbarn() : // 1. 
				_dynDaten.gibKlausurDieFreiIstMitDenMeistenNachbarsfarben(); // 2+.
		
		// Alle Klausuren gesetzt?
		if (klausurNr < 0) {
			_minSchienen = _dynDaten.gibAnzahlSchienen();
			_dynDaten.aktionZustand1Speichern();
			_saved = true;
			return;
		}

		// Rekursion
		for (int schiene = 0; schiene < _minSchienen; schiene++) {
			int schienenAnzahl = _dynDaten.gibAnzahlSchienen();
			int differenz = schiene < schienenAnzahl ? 0 : (schiene - schienenAnzahl + 1);

			_dynDaten.aktionSchienenAnzahlVeraendern(+differenz);
			if (_dynDaten.aktionSetzeKlausurInSchiene(klausurNr, schiene) == true) {
				berechneRekursiv();
				_dynDaten.aktionEntferneKlausurAusSchiene(klausurNr);
			}
			_dynDaten.aktionSchienenAnzahlVeraendern(-differenz);
		}

	}

}
