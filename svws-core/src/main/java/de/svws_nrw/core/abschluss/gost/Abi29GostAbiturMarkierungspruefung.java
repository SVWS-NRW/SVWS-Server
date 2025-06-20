package de.svws_nrw.core.abschluss.gost;

import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * Bei dieser Implementierung handelt es sich um eine Umsetzung in Bezug auf möglichen zukünftigen
 * Änderungen in der APO-GOSt. Diese basiert auf der aktuellen Implementierung und integriert Aspekte
 * aus dem Eckpunktepapier und auf in den Schulleiterdienstbesprechungen erläuterten Vorhaben.
 * Sie dient der Evaluierung von möglichen Umsetzungsvarianten und als Vorbereitung einer späteren
 * Implementierung der Belegprüfung. Insbesondere sollen erste Versuche mit Laufbahnen mit einem
 * 5. Abiturfach und Projektkursen erprobt werden. Detailaspekte können erst nach Erscheinen der APO-GOSt
 * umgesetzt werden.
 * Es handelt sich also um experimentellen Code, der keine Rückschlüsse auf Details einer zukünftigen APO-GOSt
 * erlaubt.
 *
 * Diese Klasse prüft eine Markierung von Halbjahresbelegungen von anrechenbaren Kursen
 * für die Abiturberechnung.
 */
public final class Abi29GostAbiturMarkierungspruefung {

	/** Das Ergebnis der Prüfung */
	private final @NotNull GostAbiturMarkierungspruefungErgebnis ergebnis = new GostAbiturMarkierungspruefungErgebnis();


	/**
	 * Erstellt eine neue Instanz des Markierungsalgorithmus unter Verwendung des übergebenen Abiturdaten-Manager und den zuvor
	 * durchgeführten Belegprüfungen.
	 *
	 * @param manager            der Abiturdaten-Manager
	 * @param belegpruefungen    die durchgeführten Belegprüfungen
	 */
	private Abi29GostAbiturMarkierungspruefung(final @NotNull AbiturdatenManager manager, final @NotNull List<GostBelegpruefung> belegpruefungen) {
		// TODO implementation required
		throw new UnsupportedOperationException();
	}


	/**
	 * Führt eine Prüfung der Markierung von Halbjahresbelegungen zur Verwendung in Block II
	 * von anrechenbaren Kursen für die Abiturberechnung durch. Vorraussetzung hierfür ist, dass
	 * alle anrechenbare Kurse ein gültige Note zugeordnet haben.
	 *
	 * @param manager           der Manager zur Verwaltung der Abiturdaten.
	 * @param belegpruefungen   die zuvor durchgeführten Belegprüfung der Laufbahnplanung
	 *
	 * @return das Ergebnis der Prüfung
	 */
	public static @NotNull GostAbiturMarkierungspruefungErgebnis pruefe(final @NotNull AbiturdatenManager manager,
			final @NotNull List<GostBelegpruefung> belegpruefungen) {
		final @NotNull Abi29GostAbiturMarkierungspruefung pruefung = new Abi29GostAbiturMarkierungspruefung(manager, belegpruefungen);
		// TODO implementation required
		return pruefung.ergebnis;
	}

}
