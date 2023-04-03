package de.svws_nrw.core.abschluss.gost.belegpruefung;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche im Fach Deutsch durchgeführt werden.
 */
public final class Deutsch extends GostBelegpruefung {

	/// Die Belegung für das Fach Deutsch
	private AbiturFachbelegung deutsch;

	/**
	 * Erstellt eine neue Belegprüfung für das Fach Deutsch.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Deutsch(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		deutsch = manager.getFachbelegung(GostFachbereich.DEUTSCH);
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Deutsch überhaupt in EF.1 belegt wurde
		if ((deutsch == null) || !manager.pruefeBelegungMitSchriftlichkeitEinzeln(deutsch, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) {
			addFehler(GostBelegungsfehler.D_10);
			return;
		}

		// EF1-Prüfung Punkt 2: Prüfe, ob Deutsch in der EF1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(deutsch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.D_11);
	}

	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob Deutsch überhaupt belegt wurde
		if (deutsch == null) {
			addFehler(GostBelegungsfehler.D_10);
			return;
		}

		// Gesamtprüfung Punkt 2: Prüfe, ob Deutsch von EF.1 bis Q2.2 belegt wurde
		if (!manager.pruefeBelegung(deutsch, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.D_10);

		// Gesamtprüfung Punkt 25: Prüfe, ob Deutsch von EF.1 bis Q2.1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeit(deutsch, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21 ))
			addFehler(GostBelegungsfehler.D_11);
	}


}
