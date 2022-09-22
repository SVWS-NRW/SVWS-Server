package de.nrw.schule.svws.core.abschluss.gost.belegpruefung;

import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehler;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.types.gost.GostFachbereich;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import de.nrw.schule.svws.core.types.gost.GostSchriftlichkeit;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw. 
 * für die Gesamtprüfungen, welche im Fach Mathematik durchgeführt werden.
 */
public class Mathematik extends GostBelegpruefung {

	/// Die Belegung für das Fach Mathematik
	private AbiturFachbelegung mathematik;
	
	/**
	 * Erstellt eine neue Belegprüfung für das Fach Mathematik.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Mathematik(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art) {
		super(manager, pruefungs_art);
	}


	@Override
	protected void init() {
		mathematik = manager.getFachbelegung(GostFachbereich.MATHEMATIK);		
	}
	
	
	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Mathematik überhaupt belegt wurde
		if ((mathematik == null) || !manager.pruefeBelegungMitSchriftlichkeitEinzeln(mathematik, GostSchriftlichkeit.BELIEBIG, GostHalbjahr.EF1)) {
			addFehler(GostBelegungsfehler.M_10);
			return;
		}
		
		// EF1-Prüfung Punkt 12: Prüfe, ob Mathematik in der EF1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeitEinzeln(mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1))
			addFehler(GostBelegungsfehler.M_11);
	}


	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob Mathematik überhaupt belegt wurde
		if (mathematik == null) {
			addFehler(GostBelegungsfehler.M_10);
			return;
		}
		
		// Gesamtprüfung Punkt 45: Prüfe, ob Mathematik von EF.1 bis Q2.2 belegt wurde
		if (!manager.pruefeBelegung(mathematik, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
			addFehler(GostBelegungsfehler.M_10);
		
		// Gesamtprüfung Punkt 46: Prüfe, ob Mathematik von EF.1 bis Q2.1 schriftlich belegt wurde
		if (!manager.pruefeBelegungMitSchriftlichkeit(mathematik, GostSchriftlichkeit.SCHRIFTLICH, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21))
			addFehler(GostBelegungsfehler.M_11);
	}

}
