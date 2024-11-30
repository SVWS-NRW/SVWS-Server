package de.svws_nrw.module.reporting.proxytypes.kurs;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.schueler.SchuelerStammdaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.kurse.DataKurse;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Kurs und erweitert die Klasse {@link ReportingKurs}.
 */
public class ProxyReportingKurs extends ReportingKurs {

	/** Collator für die deutsche Sortierung von Einträgen */
	@JsonIgnore
	private final Collator colGerman = Collator.getInstance(Locale.GERMAN);

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
		super(ersetzeNullDurchEmpty(kursDaten.bezeichnungZeugnis),
				null,
				kursDaten.id,
				kursDaten.istEpochalunterricht,
				kursDaten.istSichtbar,
				new ArrayList<>(),
				ersetzeNullDurchEmpty(kursDaten.kuerzel),
				ersetzeNullDurchEmpty(kursDaten.kursartAllg),
				kursDaten.schienen,
				new ArrayList<>(),
				new ArrayList<>(),
				null,
				null,
				kursDaten.schulnummer,
				kursDaten.sortierung,
				kursDaten.wochenstunden,
				new HashMap<>(),
				new ArrayList<>());

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

		// Bestimme zunächst, ob es mehr als einen Lehrer für den Kurs gibt und speichere sie dann ggf. in einer Map mit ihren Wochenstunden.
		final List<DTOKursLehrer> dtokursLehrer = this.reportingRepository.conn().queryList(DTOKursLehrer.QUERY_BY_KURS_ID, DTOKursLehrer.class, super.id);
		Map<Long, Double> mapKursZusatzkraefte = new HashMap<>();
		if (!dtokursLehrer.isEmpty())
			mapKursZusatzkraefte = dtokursLehrer.stream().collect(Collectors.toMap(k -> k.Lehrer_ID, k -> k.Anteil));

		// Erstelle die Map zu den Wochenstunden. Prüfe, ob auch Kursleiter bei Zusatzkräften ist und addiere hier die Wochenstunden der Kursleitung.
		if (kursDaten.lehrer != null) {
			if (mapKursZusatzkraefte.containsKey(kursDaten.lehrer)) {
				super.wochenstundenLehrkraefte.put(kursDaten.lehrer, kursDaten.wochenstundenLehrer + mapKursZusatzkraefte.get(kursDaten.lehrer));
			} else {
				super.wochenstundenLehrkraefte.put(kursDaten.lehrer, kursDaten.wochenstundenLehrer);
				if (!mapKursZusatzkraefte.isEmpty())
					super.wochenstundenLehrkraefte.putAll(mapKursZusatzkraefte);
			}
		} else if (!mapKursZusatzkraefte.isEmpty())
			super.wochenstundenLehrkraefte.putAll(mapKursZusatzkraefte);

		// Lehrer-Stammdaten aller Kurslehrkräfte initialisieren
		initLehrer(reportingRepository, kursDaten.lehrer);

		// Schüler setzen. Fülle nur die Liste der IDs. Die ReportingSchueler-Liste wird per lazy-Loading gefüllt, da nicht immer die Kursschüler benötigt werden.
		if ((kursDaten.schueler != null) && !kursDaten.schueler.isEmpty()) {
			super.idsSchueler = kursDaten.schueler.stream().map(s -> s.id).toList();
		}
	}

	// Initialisiert alle Lehrer-Stammdaten des Kurses.
	private void initLehrer(final ReportingRepository reportingRepository, final Long idKursleitung) {
		if (super.wochenstundenLehrkraefte.keySet().stream().filter(Objects::nonNull).toList().isEmpty())
			return;

		// Erstelle jetzt die Kursleitung und die Liste der zusätzlichen Lehrkräfte als Reporting-Lehrer.
		for (final Long idLehrer : super.wochenstundenLehrkraefte.keySet().stream().filter(Objects::nonNull).toList()) {
			if (this.reportingRepository.mapLehrerStammdaten().containsKey(idLehrer)) {
				if (idLehrer.equals(idKursleitung))
					super.kursleitung = new ProxyReportingLehrer(this.reportingRepository, this.reportingRepository.mapLehrerStammdaten().get(idLehrer));
				else
					super.zusatzLehrkraefte.add(
							new ProxyReportingLehrer(this.reportingRepository, this.reportingRepository.mapLehrerStammdaten().get(idLehrer)));
			} else {
				try {
					final LehrerStammdaten lehrerStammdaten = new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(idLehrer);
					final ReportingLehrer lehrer = new ProxyReportingLehrer(
							this.reportingRepository,
							this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(idLehrer, l -> lehrerStammdaten));
					if (idLehrer.equals(idKursleitung))
						super.kursleitung = lehrer;
					else
						super.zusatzLehrkraefte.add(lehrer);
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Daten eines Lehrers.", e,
							reportingRepository.logger(), LogLevel.INFO, 0);
				}
			}
		}
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
	 * @return    true, falls es das gleiche Objekt ist, andernfalls false.
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
					ReportingExceptionUtils.putStacktraceInLog(
							"FEHLER: Fehler bei der Ermittlung der Schülerdaten des Kurses %s in %s."
									.formatted(super.kuerzel, super.schuljahresabschnitt.textSchuljahresabschnittKurz()),
							e, reportingRepository.logger(), LogLevel.ERROR, 0);
					return super.schueler();
				}
			}
			if (!idsSchueler.isEmpty()) {
				final List<SchuelerStammdaten> schuelerStammdaten = DataSchuelerStammdaten.getListStammdaten(this.reportingRepository.conn(),
						super.idsSchueler);
				this.reportingRepository.mapSchuelerStammdaten().putAll(schuelerStammdaten.stream().collect(Collectors.toMap(s -> s.id, s -> s)));
				super.schueler = schuelerStammdaten.stream().map(s -> (ReportingSchueler) new ProxyReportingSchueler(this.reportingRepository, s))
						.sorted(Comparator
								.comparing(ReportingSchueler::nachname, colGerman)
								.thenComparing(ReportingSchueler::vorname, colGerman)
								.thenComparing(ReportingSchueler::vornamen, colGerman)
								.thenComparing(ReportingSchueler::geburtsdatum, colGerman)
								.thenComparing(ReportingSchueler::id, colGerman))
						.toList();
			}
		}
		return super.schueler();
	}
}
