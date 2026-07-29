package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLeistungsdatenMatrix;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdatenMatrix;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Kurs und erweitert die Klasse {@link ReportingKurs}.
 */
public class ProxyReportingKurs extends ReportingKurs {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingKurs}.
	 *
	 * @param reportingContext Repository für das Reporting.
	 * @param kursDaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingKurs(final ReportingContext reportingContext, final KursDaten kursDaten) {
		super(kursDaten.id,
				null,
				ersetzeNullBlankTrim(kursDaten.kuerzel),
				null,
				new ArrayList<>(),
				new HashMap<>(),
				new ArrayList<>(),
				kursDaten.sortierung,
				kursDaten.wochenstunden,
				ersetzeNullBlankTrim(kursDaten.bezeichnungZeugnis),
				kursDaten.istEpochalunterricht,
				kursDaten.istSichtbar,
				new ArrayList<>(),
				ersetzeNullBlankTrim(kursDaten.kursartAllg),
				kursDaten.schienen,
				new ArrayList<>(),
				kursDaten.schulnummer);

		this.reportingContext = reportingContext;

		// Schuljahresabschnitt zum Kurs ermitteln
		super.schuljahresabschnitt = this.reportingContext.repositorySchule().schuljahresabschnitt(kursDaten.idSchuljahresabschnitt);

		// Fach und Jahrgänge initialisieren
		initFachUndJahrgaenge(kursDaten);

		// Kurslehrer initialisieren
		initKurslehrer(kursDaten);

		// Schüler setzen. Fülle nur die Liste der IDs. Die ReportingSchueler-Liste wird per lazy-Loading gefüllt, da nicht immer die Kursschüler benötigt werden.
		if ((kursDaten.schueler != null) && !kursDaten.schueler.isEmpty()) {
			super.idsSchueler = kursDaten.schueler.stream().map(s -> s.id).toList();
		}
	}

	private void initFachUndJahrgaenge(final KursDaten kursDaten) {
		// Fach und Jahrgänge können nur bei vorhandenem Schuljahresabschnitt aufgelöst werden.
		if (super.schuljahresabschnitt == null) {
			return;
		}

		// Fach setzen
		super.fach = super.schuljahresabschnitt.fach(kursDaten.idFach);

		// Jahrgänge setzen
		if ((kursDaten.idJahrgaenge == null) || kursDaten.idJahrgaenge.isEmpty()) {
			return;
		}

		for (final Long idJahrgang : kursDaten.idJahrgaenge) {
			if ((idJahrgang != null) && (this.reportingContext.repositoryKataloge().jahrgang(idJahrgang) != null)) {
				final ReportingJahrgang jahrgang = super.schuljahresabschnitt.jahrgang(idJahrgang);
				if (jahrgang != null) {
					super.jahrgaenge.add(jahrgang);
				}
			}
		}
		super.jahrgaenge.sort(ReportingJahrgang.SORTIERUNG.comparatorStandard());
	}

	private void initKurslehrer(final KursDaten kursDaten) {
		// Bestimme zunächst, ob es mehr als einen Lehrer für den Kurs gibt, und speichere sie dann ggf. in einer Map mit ihren Wochenstunden.
		final Map<Long, Double> mapZusatzKurslehrer = new LinkedHashMap<>(this.reportingContext.repositoryLerngruppen().kurslehrerWochenstunden(super.id));

		// Wenn es einen Kursleiter gibt, prüfe, ob auch er bei den Zusatzkräften ist, und addiere hier seine beiden Wochenstunden.
		final Map<Long, Double> mapKurslehrer = new LinkedHashMap<>();
		if (kursDaten.lehrer != null) {
			if (mapZusatzKurslehrer.containsKey(kursDaten.lehrer)) {
				mapKurslehrer.put(kursDaten.lehrer, kursDaten.wochenstundenLehrer + mapZusatzKurslehrer.get(kursDaten.lehrer));
				mapZusatzKurslehrer.remove(kursDaten.lehrer);
			} else {
				mapKurslehrer.put(kursDaten.lehrer, kursDaten.wochenstundenLehrer);
			}
		}
		if (!mapZusatzKurslehrer.isEmpty()) {
			mapKurslehrer.putAll(mapZusatzKurslehrer);
		}

		// Erstelle jetzt alle Kurslehrer als Reporting-Lehrer.
		super.lehrer = new ArrayList<>(this.reportingContext.repositoryLehrer().lehrer(mapKurslehrer.keySet().stream().toList(), false));
	}



	// ##### Hash und Equals Methoden #####

	/**
	 * Hashcode der Klasse
	 * @return Hashcode der Klasse
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * Equals der Klasse
	 * @param obj Das Vergleichsobjekt
	 * @return    Ergibt true, falls es das gleiche Objekt ist, andernfalls false.
	 */
	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 *
	 * @return Repository für das Reporting
	 */
	public ReportingContext reportingContext() {
		return reportingContext;
	}


	/**
	 * Erstellt eine Leistungsdaten-Matrix für die Schüler dieser Gruppe basierend auf dem Schuljahresabschnitt der Gruppe.
	 *
	 * @return Die Leistungsdaten-Matrix für diese Schülergruppe.
	 */
	@Override
	public ReportingSchuelerLeistungsdatenMatrix schuelerLeistungsdatenMatrix() {
		return new ProxyReportingSchuelerLeistungsdatenMatrix(this.reportingContext, this.schueler(), this.schuljahresabschnitt());
	}

	/**
	 * Stellt eine Liste mit Schülern des Kurses zur Verfügung.
	 *
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler.isEmpty()) {
			if (super.idsSchueler().isEmpty()) {
				idsSchueler.addAll(this.reportingContext.repositoryLerngruppen().kursSchuelerIds(super.id()));
			}
			if (!idsSchueler.isEmpty()) {
				super.schueler = this.reportingContext.repositorySchueler().schueler(idsSchueler);
			}
		}
		return super.schueler();
	}
}
