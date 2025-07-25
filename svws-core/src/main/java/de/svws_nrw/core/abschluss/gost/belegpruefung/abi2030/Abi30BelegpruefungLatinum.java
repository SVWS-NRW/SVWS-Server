package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2030;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
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
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche in Bezug auf den Erwerb des Latinums durchgeführt werden.
 */
public final class Abi30BelegpruefungLatinum extends GostBelegpruefung {

	/// Die Belegung für das Fach Latein
	private AbiturFachbelegung latein;

	/**
	 * Erstellt eine neue Belegprüfung in Bezug auf den Erwerb des Latinums.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi30BelegpruefungLatinum(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		latein = manager.getSprachbelegung("L");
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Latein am Ende der SI belegt mit mind. 2 Jahren belegt wurde, aber nicht in EF.1.
		// Gebe dann einen entsprechenden Hinweis zum Erwerbs des Latinums aus.
		if (SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerEndeSekI(manager.getSprachendaten(), "L")
				&& (!manager.pruefeBelegung(latein, GostHalbjahr.EF1)))
			addFehler(GostBelegungsfehler.L_10_INFO);
	}


	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob Latein am Ende der SI belegt mit mind. 2 Jahren belegt wurde.
		// Wenn ja, prüfe für das Latinum, ob Latein auch in der Oberstufe entsprechend lange belegt wurde.
		// Gebe andernfalls einen passenden Hinweis zum Erwerbs des Latinums aus.
		if (SprachendatenUtils.hatSprachbelegungMitMin2JahrenDauerEndeSekI(manager.getSprachendaten(), "L")) {
			if (SprachendatenUtils.hatSprachbelegungMitMin4JahrenDauerEndeSekI(manager.getSprachendaten(), "L")) {
				if (!manager.pruefeBelegung(latein, GostHalbjahr.EF1, GostHalbjahr.EF2))
					addFehler(GostBelegungsfehler.L_10_INFO);
			} else {
				if (!manager.pruefeBelegung(latein, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
					addFehler(GostBelegungsfehler.L_11_INFO);
			}
		}
	}

}
