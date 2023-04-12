package de.svws_nrw.core.abschluss.gost.belegpruefung;

import java.util.List;
import java.util.ArrayList;

import de.svws_nrw.core.abschluss.gost.AbiturdatenManager;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefung;
import de.svws_nrw.core.abschluss.gost.GostBelegpruefungsArt;
import de.svws_nrw.core.abschluss.gost.GostBelegungsfehler;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.types.gost.GostFachbereich;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import jakarta.validation.constraints.NotNull;


/**
 * Diese Klasse gruppiert alle Belegprüfungen für einen Schüler für die Prüfung der EF1 bzw.
 * für die Gesamtprüfungen, welche im Fach Sport durchgeführt werden.
 */
public final class Sport extends GostBelegpruefung {

	/// Die Belegungen für das Fach Sport
	private @NotNull List<@NotNull AbiturFachbelegung> sport = new ArrayList<>();

	/**
	 * Erstellt eine neue Belegprüfung für das Fach Sport.
	 *
	 * @param manager        der Daten-Manager für die Abiturdaten
	 * @param pruefungsArt   die Art der durchzuführenden Prüfung (z.B. EF.1 oder GESAMT)
	 */
	public Sport(final @NotNull AbiturdatenManager manager, final @NotNull GostBelegpruefungsArt pruefungsArt) {
		super(manager, pruefungsArt);
	}



	@Override
	protected void init() {
		sport = manager.getFachbelegungen(GostFachbereich.SPORT);
	}


	@Override
	protected void pruefeEF1() {
		// Prüfe, ob Sport in EF.1 belegt wurde
		if ((sport == null) || (!manager.pruefeBelegungExistiertEinzeln(sport, GostHalbjahr.EF1)))
			addFehler(GostBelegungsfehler.SP_10);
	}


	@Override
	protected void pruefeGesamt() {
		// Prüfe, ob Sport durchgängig von EF.1 bis Q2.2 belegt wurde. Ein Sportattest muss mit
		// Note "AT" eingetragen werden und gilt damit zunächst als belegt.
		if ((sport == null) || (!manager.pruefeBelegungExistiert(sport, GostHalbjahr.EF1, GostHalbjahr.EF2, GostHalbjahr.Q11, GostHalbjahr.Q12, GostHalbjahr.Q21, GostHalbjahr.Q22)))
			addFehler(GostBelegungsfehler.SP_10);
	}

}
