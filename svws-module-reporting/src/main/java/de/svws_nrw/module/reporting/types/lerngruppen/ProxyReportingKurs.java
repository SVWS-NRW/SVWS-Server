package de.svws_nrw.module.reporting.types.lerngruppen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.kurse.KursDaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ProxyReportingSchuelerLeistungsdatenMatrix;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdatenMatrix;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Kurs und erweitert die Klasse {@link ReportingKurs}.
 */
public class ProxyReportingKurs extends ReportingKurs {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingKurs}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param kursDaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingKurs(final ReportingRepository reportingRepository, final KursDaten kursDaten) {
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

		this.reportingRepository = reportingRepository;

		// Schuljahresabschnitt zum Kurs ermitteln
		super.schuljahresabschnitt = this.reportingRepository.schuljahresabschnitt(kursDaten.idSchuljahresabschnitt);

		// Fach setzen
		super.fach = super.schuljahresabschnitt.fach(kursDaten.idFach);

		// Jahrgänge setzen
		if ((kursDaten.idJahrgaenge != null) && !kursDaten.idJahrgaenge.isEmpty()) {
			for (final Long idJahrgang : kursDaten.idJahrgaenge) {
				if (this.reportingRepository.mapJahrgaenge().containsKey(idJahrgang))
					super.jahrgaenge.add(super.schuljahresabschnitt.jahrgang(idJahrgang));
			}
		}

		// Kurslehrer initialisieren
		initKurslehrer(kursDaten);

		// Schüler setzen. Fülle nur die Liste der IDs. Die ReportingSchueler-Liste wird per lazy-Loading gefüllt, da nicht immer die Kursschüler benötigt werden.
		if ((kursDaten.schueler != null) && !kursDaten.schueler.isEmpty()) {
			super.idsSchueler = kursDaten.schueler.stream().map(s -> s.id).toList();
		}
	}

	private void initKurslehrer(final KursDaten kursDaten) {
		// Bestimme zunächst, ob es mehr als einen Lehrer für den Kurs gibt, und speichere sie dann ggf. in einer Map mit ihren Wochenstunden.
		final List<DTOKursLehrer> dtoKursLehrer = this.reportingRepository.conn().queryList(DTOKursLehrer.QUERY_BY_KURS_ID, DTOKursLehrer.class, super.id);
		Map<Long, Double> mapZusatzKurslehrer = new LinkedHashMap<>();
		if (!dtoKursLehrer.isEmpty())
			mapZusatzKurslehrer = dtoKursLehrer.stream().filter(Objects::nonNull).collect(Collectors.toMap(k -> k.Lehrer_ID, k -> k.Anteil));

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
		if (!mapZusatzKurslehrer.isEmpty())
			mapKurslehrer.putAll(mapZusatzKurslehrer);

		// Erstelle jetzt alle Kurslehrer als Reporting-Lehrer.
		super.lehrer = new ArrayList<>(this.reportingRepository.lehrer(mapKurslehrer.keySet().stream().toList(), false));
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
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Erstellt eine Leistungsdaten-Matrix für die Schüler dieser Gruppe basierend auf dem Schuljahresabschnitt der Gruppe.
	 *
	 * @return Die Leistungsdaten-Matrix für diese Schülergruppe.
	 */
	@Override
	public ReportingSchuelerLeistungsdatenMatrix schuelerLeistungsdatenMatrix() {
		return new ProxyReportingSchuelerLeistungsdatenMatrix(this.reportingRepository, this.schueler(), this.schuljahresabschnitt());
	}

	/**
	 * Stellt eine Liste mit Schülern des Kurses zur Verfügung.
	 *
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler.isEmpty()) {
			final KursDaten kursDaten;
			if (super.idsSchueler().isEmpty()) {
				try {
					kursDaten = DataKurse.getKursdaten(reportingRepository.conn(), super.id());
					if ((kursDaten.schueler != null) && !kursDaten.schueler.isEmpty())
						idsSchueler.addAll(kursDaten.schueler.stream().map(s -> s.id).toList());
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.logException(
							"FEHLER: Fehler bei der Ermittlung der Daten des Kurses %s in %s."
									.formatted(super.kuerzel, super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.schueler();
				}
			}
			if (!idsSchueler.isEmpty())
				super.schueler = this.reportingRepository.schueler(idsSchueler);
		}
		return super.schueler();
	}
}
