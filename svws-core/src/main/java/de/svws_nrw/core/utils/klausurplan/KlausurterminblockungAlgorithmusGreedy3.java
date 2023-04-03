package de.svws_nrw.core.utils.klausurplan;

import java.util.Random;
import java.util.Vector;

import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import jakarta.validation.constraints.NotNull;

/**
 * Die Klausuren werden rekursiv mit Backtracking auf die Schienen verteilt.
 * Pro Rekursionsschritt wird die freie Klausur gewählt, die die meisten Nachbarsfarben hat.
 * Anschließend wird die Klausur in aufsteigender Reihenfolge auf die Schienen verteilt.
 *
 * @author Benjamin A. Bartsch
 */
public final class KlausurterminblockungAlgorithmusGreedy3 extends KlausurterminblockungAlgorithmusAbstract {

	/** Die kleinste Schienenanzahl, die bisher gefunden wurde. */
	private int _minTermine;

	/** Bis zu welchem Zeitpunkt die Rekursion laufen darf. */
	private long _zeitEnde;

	/**
	 * Konstruktor.
	 *
	 * @param pRandom   Ein {@link Random}-Objekt zur Steuerung des Zufalls über einen Anfangs-Seed.
	 * @param pDynDaten Die aktuellen Blockungsdaten.
	 */
	public KlausurterminblockungAlgorithmusGreedy3(final @NotNull Random pRandom, final @NotNull KlausurterminblockungDynDaten pDynDaten) {
		super(pRandom, pDynDaten);
	}

	@Override
	public @NotNull String toString() {
		return "Termine nacheinander, Klausurgruppen mit Backtracking";
	}

	@Override
	public void berechne(final long pZeitEnde) {
		_zeitEnde = pZeitEnde;

		// Bediene dich eines anderen Algorithmus für eine Start-Lösung.
		_dynDaten.aktion_Clear_TermineNacheinander_GruppeNachGrad();
		_minTermine = _dynDaten.gibKlausurgruppenAnzahl();
		_dynDaten.aktionZustand1Speichern();

		// Suche rekursiv nach einer besseren Lösung.
		_dynDaten.aktionClear();
		berechneRekursiv();

		// Lade die beste lokale Lösung.
		_dynDaten.aktionZustand1Laden();

		// Ist sie auch besser als die beste globale Lösung?
		if (_dynDaten.gibIstBesserAlsZustand2())
			_dynDaten.aktionZustand2Speichern();
	}

	private void berechneRekursiv() {

		// Kann das Ergebnis überhaupt noch besser werden?
		if (_dynDaten.gibTerminAnzahl() > _minTermine) return;

		// Ist die Zeit abgelaufen?
		if (System.currentTimeMillis() > _zeitEnde) return;

		// Sind alle Klausuren verteilt?
		if (!_dynDaten.gibExistierenNichtverteilteKlausuren()) {
			if (_dynDaten.gibIstBesserAlsZustand1()) {
				_minTermine = _dynDaten.gibTerminAnzahl();
				_dynDaten.aktionZustand1Speichern();
			}
			return;
		}

		// Wähle eine nächste Klausurgruppe und verteile sie rekursiv.
		final @NotNull Vector<@NotNull Integer> gruppe = _dynDaten.gibKlausurgruppeMitMinimalenTerminmoeglichkeiten();

		// 1. Fall: Die Gruppe passt noch in einen vorhandenen Termin.
		for (int terminNr = 0; terminNr < _dynDaten.gibTerminAnzahl(); terminNr++) {
			if (_dynDaten.aktionSetzeKlausurgruppeInTermin(gruppe, terminNr)) {
				berechneRekursiv();
				_dynDaten.aktionEntferneKlausurgruppeAusTermin(gruppe, terminNr);
			}
		}

		// 2. Fall: Die Gruppe muss in einen neu erzeugten Termin.
		final int terminNr = _dynDaten.gibErzeugeNeuenTermin();
		if (!_dynDaten.aktionSetzeKlausurgruppeInTermin(gruppe, terminNr)) throw new DeveloperNotificationException("Ein Setzen muss hier möglich sein!");
		berechneRekursiv();
		_dynDaten.aktionEntferneKlausurgruppeAusTermin(gruppe, terminNr);
		_dynDaten.entferneLetztenTermin();
	}

}
