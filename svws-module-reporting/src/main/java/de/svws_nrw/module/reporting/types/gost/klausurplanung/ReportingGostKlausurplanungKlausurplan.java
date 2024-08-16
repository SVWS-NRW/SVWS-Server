package de.svws_nrw.module.reporting.types.gost.klausurplanung;

import java.util.ArrayList;
import java.util.List;

import de.svws_nrw.core.utils.gost.klausurplanung.GostKlausurplanManager;
import de.svws_nrw.module.reporting.proxytypes.gost.klausurplanung.ProxyReportingGostKlausurplanungKlausurplan;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;


/**
 * <p>Basis-Klasse im Rahmen des Reportings für Daten vom Typ GostKlausurplanungKlausurplan.</p>
 * <p>Sie enthält die Daten zu einem Klausurplan, d. h. beispielsweise zu den Terminen.</p>
 * <p>Diese Klasse ist als reiner Datentyp konzipiert, d. h. sie hat keine Anbindung an die Datenbank. Sie dient als Super-Klasse
 * einer Proxy-Klasse, welche die Getter in Teilen überschreibt und dort die Daten aus der Datenbank nachlädt.</p>
 */
public class ReportingGostKlausurplanungKlausurplan extends ProxyReportingGostKlausurplanungKlausurplan {

	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis dieser Klasse.
	 *
 	 * @param reportingRepository		Repository für die Reporting.
	 * @param gostKlausurplanManager 	Der Manager der Klausuren zu diesem Klausurplan

	 */
	public ReportingGostKlausurplanungKlausurplan(final ReportingRepository reportingRepository, final GostKlausurplanManager gostKlausurplanManager) {

		super(reportingRepository, gostKlausurplanManager);

		// Fülle die Basislisten mit den übergebenen Daten.
		this.schueler = (schueler != null) ? schueler : new ArrayList<>();
		this.kurse = (kurse != null) ? kurse : new ArrayList<>();
	}


	// ##### Berechnete Methoden #####

	/**
	 * Eine Liste vom Typ String, die alle vorhandenen Datumsangaben der Termine des Klausurplanes beinhaltet (distinct).
	 * @return Liste der Datumsangaben der Klausurtermine
	 */
	public List<String> datumsangabenKlausurtermine() {
		return klausurtermineMitDatum().stream().map(t -> t.datum()).toList();
	}

	/**
	 * Gibt den Kurs zur übergebenen ID zurück
	 * @param  id 	Die ID des Kurses
	 * @return 		Der Kurs zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingKurs kurs(final long id) {
		if (id < 0)
			return null;
		return this.kurse.stream().filter(k -> id == k.id()).findFirst().orElse(null);
	}

	/**
	 * Gibt den Schüler zur übergebenen ID zurück
	 * @param  id 	Die ID des Schülers
	 * @return 		Der Schüler zur ID oder null, wenn nicht vorhanden.
	 */
	public ReportingSchueler schueler(final long id) {
		if (id < 0)
			return null;
		return this.schueler.stream().filter(s -> id == s.id()).findFirst().orElse(null);
	}


	// ##### Getter #####


	/**
	 * Eine Liste, die alle Kurse des Klausurplanes beinhaltet.
	 * @return Liste der Kurse
	 */
	public List<ReportingKurs> kurse() {
		return this.kurse;
	}

	/**
	 * Eine Liste, die alle Schüler des Klausurplanes beinhaltet.
	 * @return Liste der Schüler
	 */
	public List<ReportingSchueler> schueler() {
		return this.schueler;
	}

}
