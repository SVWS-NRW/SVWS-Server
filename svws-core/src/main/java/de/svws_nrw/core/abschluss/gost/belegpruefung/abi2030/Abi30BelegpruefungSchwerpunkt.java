package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import jakarta.validation.constraints.NotNull;

/*
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 */
/**
 * Diese Klasse enthält die Belegprüfungen bezüglich eines möglichen Schwerpunktes eines Schüler
 * im naturwissenschaftlichen oder sprachlichen Bereich für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen.
 */
public final class Abi30BelegpruefungSchwerpunkt extends GostBelegpruefung {

	/**
	 * Erstellt eine neue Belegprüfung für den Schwerpunkt.
	 *
	 * @param manager            der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt       die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 * @param pruefungSprachen   das Ergebnis für die Belegprüfung der Sprachen
	 * @param pruefungNawi       das Ergebnis für die Belegprüfung der Naturwissenschaften
	 */
	public Abi30BelegpruefungSchwerpunkt(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt,
			final @NotNull Abi30BelegpruefungFremdsprachen pruefungSprachen, final @NotNull Abi30BelegpruefungNaturwissenschaften pruefungNawi) {
		super(manager, pruefungsArt, pruefungSprachen, pruefungNawi);
	}


	@Override
	protected void init() {
		// Keine Initialisierung notwendig
	}




	@Override
	protected void pruefeEF1() {
		final @NotNull Abi30BelegpruefungFremdsprachen pruefung_sprachen = ((@NotNull Abi30BelegpruefungFremdsprachen) pruefungen_vorher[0]);
		final @NotNull Abi30BelegpruefungNaturwissenschaften pruefung_nawi = ((@NotNull Abi30BelegpruefungNaturwissenschaften) pruefungen_vorher[1]);

		// Prüfe, ob insgesamt so viele Fremdsprachen und Naturwissenschaften gewählt wurden, dass zunächst kein Schwerpunkt vorliegt.
		// Dann liegt kein Belegungsfehler vor.
		if ((pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2)
				&& (pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1))
			return;

		// Prüfe, ob ein sprachlicher Schwerpunkt vorliegt
		if (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2) {
			addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}

		// Prüfe, ob ein naturwissenschaftlicher Schwerpunkt vorliegt
		if ((pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1)) {
			addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}

		// Es wurden zu wenig Fremdsprachen und Naturwissenschaften belegt -> Belegungsfehler
		addFehler(GostBelegungsfehler.NW_FS_10);
	}




	@Override
	protected void pruefeGesamt() {
		final @NotNull Abi30BelegpruefungFremdsprachen pruefung_sprachen = ((@NotNull Abi30BelegpruefungFremdsprachen) pruefungen_vorher[0]);
		final @NotNull Abi30BelegpruefungNaturwissenschaften pruefung_nawi = ((@NotNull Abi30BelegpruefungNaturwissenschaften) pruefungen_vorher[1]);

		// Prüfe, ob insgesamt so viele Fremdsprachen und Naturwissenschaften gewählt wurden, dass zunächst kein Schwerpunkt vorliegt.
		// Dann liegt kein Belegungsfehler vor.
		if ((pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2)
				&& (pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1))
			return;

		// Prüfe, ob ein sprachlicher Schwerpunkt vorliegt
		if (pruefung_sprachen.getAnzahlDurchgehendSchritflichBelegt() >= 2) {
			addFehler(GostBelegungsfehler.NW_FS_12_INFO);
			return;
		}

		// Prüfe, ob ein naturwissenschaftlicher Schwerpunkt vorliegt
		if ((pruefung_nawi.getAnzahlDurchgehendBelegt() >= 2) && (pruefung_nawi.getAnzahlDurchgehendSchritflichBelegt() >= 1)) {
			addFehler(GostBelegungsfehler.NW_FS_13_INFO);
			return;
		}

		// Es wurden zu wenig Fremdsprachen und Naturwissenschaften belegt -> Belegungsfehler
		addFehler(GostBelegungsfehler.NW_FS_10);
	}

}
