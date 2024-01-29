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
 * für die Gesamtprüfungen, welche im Fach Mathematik durchgeführt werden.
 */
public final class Mathematik extends GostBelegpruefung {

	/// Die Belegung für das Fach Mathematik
	private AbiturFachbelegung _mathematik;

	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Mathematik(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
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
		if (!manager.pruefeBelegungMitSchriftlichkeit(_mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			addFehler(GostBelegungsfehler.M_11);
	}

}
