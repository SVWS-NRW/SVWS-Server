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
 * für die Gesamtprüfungen, welche im Fach Mathematik durchgeführt werden.
 */
public final class Abi29BelegpruefungMathematik extends GostBelegpruefung {

	/// Die Belegung für das Fach Mathematik
	private AbiturFachbelegung _mathematik;

	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Abi29BelegpruefungMathematik(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		_mathematik = manager.getRelevanteFachbelegung(GostFachbereich.MATHEMATIK);
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Mathematik überhaupt belegt wurde
		if ((_mathematik == null) || !manager.pruefeBelegungMitSchriftlichkeitEinzeln(_mathematik, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) {
			addFehler(GostBelegungsfehler.M_10);
			return;
		}

		// EF1-Prüfung Punkt 12: Prüfe, ob Mathematik in der EF1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(_mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.M_11);
	}


	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob Mathematik überhaupt belegt wurde
		if (_mathematik == null) {
			addFehler(GostBelegungsfehler.M_10);
			return;
		}

		// Gesamtprüfung Punkt 45: Prüfe, ob Mathematik von EF.1 bis Q2.2 belegt wurde
		if (!manager.pruefeBelegung(_mathematik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.M_10);

		// Gesamtprüfung Punkt 46: Prüfe, ob Mathematik von EF.1 bis Q2.1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeit(_mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11,
				GostHalbjahr.Q12, GostHalbjahr.Q21))
			addFehler(GostBelegungsfehler.M_11);
	}

}
