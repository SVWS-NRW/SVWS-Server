package de.svws_nrw.core.abschluss.gost.belegpruefung.abi2029;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
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
 * für die Gesamtprüfungen, welche im Fach Deutsch durchgeführt werden.
 */
public final class Abi29BelegpruefungDeutsch extends GostBelegpruefung {

	/// Die Belegung für das Fach Deutsch
	private AbiturFachbelegung _deutsch;

	/**
	 * Erstellt eine neue Belegprüfung für das Fach Deutsch.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi29BelegpruefungDeutsch(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		_deutsch = manager.getRelevanteFachbelegung(GostFachbereich.DEUTSCH);
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Deutsch überhaupt in EF.1 belegt wurde
		if ((_deutsch == null) || !manager.pruefeBelegungMitSchriftlichkeitEinzeln(_deutsch, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) {
			addFehler(GostBelegungsfehler.D_10);
			return;
		}

		// EF1-Prüfung Punkt 2: Prüfe, ob Deutsch in der EF1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(_deutsch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.D_11);
	}

	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob Deutsch überhaupt belegt wurde
		if (_deutsch == null) {
			addFehler(GostBelegungsfehler.D_10);
			return;
		}

		// Gesamtprüfung Punkt 2: Prüfe, ob Deutsch von EF.1 bis Q2.2 belegt wurde
		if (!manager.pruefeBelegung(_deutsch, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.D_10);

		// Gesamtprüfung Punkt 25: Prüfe, ob Deutsch von EF.1 bis Q2.1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeit(_deutsch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11,
				GostHalbjahr.Q12, GostHalbjahr.Q21))
			addFehler(GostBelegungsfehler.D_11);
	}


}
