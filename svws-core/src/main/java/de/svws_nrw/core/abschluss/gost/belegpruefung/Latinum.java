package de.svws_nrw.core.abschluss.gost.belegpruefung;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.utils.schueler.SprachendatenUtils;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche in Bezug auf den Erwerb des Latinums durchgeführt werden.
 */
public final class Latinum extends GostBelegpruefung {

	/// Die Belegung für das Fach Latein
	private AbiturFachbelegung latein;

	/**
	 * Erstellt eine neue Belegprüfung in Bezug auf den Erwerb des Latinums.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Latinum(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}


	@Override
	protected void init() {
		latein = manager.getSprachbelegung("L");
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Latein in der SI belegt wurde, aber nicht in EF.1.
		if (SprachendatenUtils.hatSprachbelegungInSekI(manager.getSprachendaten(), "L") && (!manager.pruefeBelegung(latein, GostHalbjahr.EF1)))
			addFehler(GostBelegungsfehler.L_10_INFO);
	}


	@Override
	protected void pruefeGesamt() {
		/// Prüfe, ob Latein in der SI belegt wurde
		if (!SprachendatenUtils.hatSprachbelegungInSekI(manager.getSprachendaten(), "L")) {
			return;
		}

		if (SprachendatenUtils.hatSprachbelegungInSekIMitDauer(manager.getSprachendaten(), "L", 4)) {
			if (!manager.pruefeBelegung(latein, GostHalbjahr.EF1, GostHalbjahr.EF2)) {
				addFehler(GostBelegungsfehler.L_10_INFO);
			}
			return;
		}

		if (SprachendatenUtils.hatSprachbelegungInSekIMitDauer(manager.getSprachendaten(), "L", 2)) {
			if (!manager.pruefeBelegung(latein, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
				addFehler(GostBelegungsfehler.L_11_INFO);
		}
	}

}
