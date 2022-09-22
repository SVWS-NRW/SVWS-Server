package de.nrw.schule.svws.core.abschluss.gost.belegpruefung;

import de.nrw.schule.svws.core.SprachendatenManager;
import de.nrw.schule.svws.core.abschluss.gost.AbiturdatenManager;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefung;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegpruefungsArt;
import de.nrw.schule.svws.core.abschluss.gost.GostBelegungsfehler;
import de.nrw.schule.svws.core.data.gost.AbiturFachbelegung;
import de.nrw.schule.svws.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;

/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw. 
 * für die Gesamtprüfungen, welche in Bezug auf den Erwerb des Latinums durchgeführt werden.
 */
public class Latinum extends GostBelegpruefung {
	
	/// Die Belegung für das Fach Latein
	private AbiturFachbelegung latein;	
	
	/**
	 * Erstellt eine neue Belegprüfung in Bezug auf den Erwerb des Latinums.
	 * 
	 * @param manager         der Daten-Manager für die Abiturdaten
	 * @param pruefungs_art   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Latinum(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungs_art) {
		super(manager, pruefungs_art);
	}

	
	@Override
	protected void init() {
		latein = manager.getSprachbelegung("L");		
	}

	
	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Latein in der SI belegt wurde, aber nicht in EF.1.		
		if (SprachendatenManager.hatSprachbelegungInSekI(manager.getSprachendaten(), "L") && (!manager.pruefeBelegung(latein, GostHalbjahr.EF1)))
			addFehler(GostBelegungsfehler.L_10_INFO);
	}

	
	@Override
	protected void pruefeGesamt() {
		/// Prüfe, ob Latein in der SI belegt wurde
        if (!SprachendatenManager.hatSprachbelegungInSekI(manager.getSprachendaten(), "L")){
            return;
        }

        if (SprachendatenManager.hatSprachbelegungInSekIMitDauer(manager.getSprachendaten(), "L", 4)){
            if (!manager.pruefeBelegung(latein, GostHalbjahr.EF1, GostHalbjahr.EF2)) {
                addFehler(GostBelegungsfehler.L_10_INFO);
            }
            return;
        }

        if (SprachendatenManager.hatSprachbelegungInSekIMitDauer(manager.getSprachendaten(), "L", 2)){
            if (!manager.pruefeBelegung(latein, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22))
                addFehler(GostBelegungsfehler.L_11_INFO);
        }
	}
	
}
