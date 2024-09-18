package de.svws_nrw.module.reporting.proxytypes.kurs;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.data.jahrgang.JahrgangsDaten;
import de.svws_nrw.core.data.kurse.KursDaten;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.data.jahrgaenge.DataJahrgangsdaten;
import de.svws_nrw.data.lehrer.DataLehrerStammdaten;
import de.svws_nrw.data.schueler.DataSchuelerStammdaten;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.module.reporting.proxytypes.jahrgang.ProxyReportingJahrgang;
import de.svws_nrw.module.reporting.proxytypes.lehrer.ProxyReportingLehrer;
import de.svws_nrw.module.reporting.proxytypes.schueler.ProxyReportingSchueler;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.jahrgang.ReportingJahrgang;
import de.svws_nrw.module.reporting.types.kurs.ReportingKurs;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 *  <p>Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Kurs und erweitert die Klasse {@link ReportingKurs}.</p>
 *
 *  <p>In diesem Kontext besitzt die Proxy-Klasse ausschließlich die gleichen Methoden wie die zugehörige Reporting-Super-Klasse.
 *  Während die Super-Klasse aber als reiner Datentyp konzipiert ist, d. h. ohne Anbindung an die Datenbank,
 *  greift die Proxy-Klassen an verschiedenen Stellen auf die Datenbank zu.</p>
 *
 *  <ul>
 *      <li>Die Proxy-Klasse stellt in der Regel einen zusätzlichen Constructor zur Verfügung, um Reporting-Objekte
 *  		aus Stammdatenobjekten (aus dem Package core.data) erstellen zu können. Darin werden Felder, die Reporting-Objekte
 *  		zurückgegeben und nicht im Stammdatenobjekt enthalten sind, mit null initialisiert.</li>
 * 		<li>Die Proxy-Klasse überschreibt einzelne Getter der Super-Klasse (beispielsweise bei Felder, die mit null initialisiert wurden)
 *  		und lädt dort dann aus der Datenbank die Daten bei Bedarf nach (lazy-loading), um den Umfang der Datenstrukturen gering zu
 *  		halten.</li>
 * 		<li>Die Proxy-Klasse können zudem auf das Repository {@link ReportingRepository} zugreifen. Dieses
 * 			enthält neben den Stammdaten der Schule einige Maps, in der zur jeweiligen ID bereits ausgelesene Stammdaten anderer Objekte
 * 			wie Kataloge, Jahrgänge, Klassen, Lehrer, Schüler usw. gespeichert werden. So sollen Datenbankzugriffe minimiert werden. Werden in der
 * 			Proxy-Klasse Daten nachgeladen, so werden sie dabei auch in der entsprechenden Map des Repository ergänzt.</li>
 *  </ul>
 */
public class ProxyReportingKurs extends ReportingKurs {

	/** Collator für die deutsche Sortierung von Einträgen */
	@JsonIgnore
	private final Collator colGerman = Collator.getInstance(Locale.GERMAN);

	/** Repository für die Reporting */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Reporting-Objekt auf Basis eines Stammdaten-Objektes.
	 * @param reportingRepository Repository für die Reporting.
	 * @param kursDaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingKurs(final ReportingRepository reportingRepository, final KursDaten kursDaten) {
		super(kursDaten.bezeichnungZeugnis,
				null,
				kursDaten.id,
				kursDaten.istEpochalunterricht,
				kursDaten.istSichtbar,
				new ArrayList<>(),
				kursDaten.kuerzel,
				kursDaten.kursartAllg,
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
				if (this.reportingRepository.mapJahrgaenge().containsKey(idJahrgang)) {
					super.jahrgaenge.add(
							new ProxyReportingJahrgang(this.reportingRepository, this.reportingRepository.mapJahrgaenge().get(idJahrgang)));
				} else {
					try {
						final JahrgangsDaten jahrgangsDaten = new DataJahrgangsdaten(this.reportingRepository.conn()).getFromID(idJahrgang);
						final ReportingJahrgang jahrgang = new ProxyReportingJahrgang(
								this.reportingRepository, this.reportingRepository.mapJahrgaenge().computeIfAbsent(idJahrgang, j -> jahrgangsDaten));
						super.jahrgaenge.add(jahrgang);
					} catch (final ApiOperationException e) {
						ReportingExceptionUtils.putStacktraceInLog(
								"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Daten eines Jahrgangs.", e,
								reportingRepository.logger(), LogLevel.INFO, 0);
					}
				}
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
				super.wochenstundenLehrer.put(kursDaten.lehrer, kursDaten.wochenstundenLehrer + mapKursZusatzkraefte.get(kursDaten.lehrer));
			} else {
				super.wochenstundenLehrer.put(kursDaten.lehrer, kursDaten.wochenstundenLehrer);
				if (!mapKursZusatzkraefte.isEmpty())
					super.wochenstundenLehrer.putAll(mapKursZusatzkraefte);
			}
		} else if (!mapKursZusatzkraefte.isEmpty())
			super.wochenstundenLehrer.putAll(mapKursZusatzkraefte);

		// Lehrer-Stammdaten aller Kurslehrkräfte initialisieren
		initLehrer(reportingRepository, kursDaten);

		// Schüler setzen. Fülle nur die Liste der IDs. Die ReportingSchueler-Liste wird per Lasy-Loading gefüllt, da nicht immer die Kursschüler benötigt werden.
		if ((kursDaten.schueler != null) && !kursDaten.schueler.isEmpty()) {
			super.idsSchueler = kursDaten.schueler.stream().map(s -> s.id).toList();
		}
	}

	// Initialisiert alle Lehrer-Stammdaten des Kurses.
	private void initLehrer(final ReportingRepository reportingRepository, final KursDaten kursDaten) {
		// Lehrer setzen, sowohl den Kursleiter aus der Kurstabelle (erster Lehrer) als auch die Lehrer aus der KurseLehrer-Tabelle
		// Erstelle eine Liste der IDs aller Lehrer des Kurses, wobei der erste Lehrer die Kursleitung darstellt, wenn eingetragen.
		final List<Long> idsKurslehrer = new ArrayList<>();
		if (kursDaten.lehrer != null) {
			idsKurslehrer.add(kursDaten.lehrer);
			for (final Long idKurslehrer : wochenstundenLehrer.keySet()) {
				if (!kursDaten.lehrer.equals(idKurslehrer))
					idsKurslehrer.add(kursDaten.lehrer);
			}
		} else if (!wochenstundenLehrer.isEmpty()) {
			idsKurslehrer.addAll(wochenstundenLehrer.keySet());
		}
		// Erstelle jetzt den kursLehrer und die Liste der zusatzLehrer mit Reporting-Lehrer zu den Lehrer-IDs
		for (final Long idKurslehrer : idsKurslehrer) {
			if (this.reportingRepository.mapLehrerStammdaten().containsKey(idKurslehrer)) {
				if (idKurslehrer.equals(kursDaten.lehrer))
					super.kursLehrer = new ProxyReportingLehrer(this.reportingRepository, this.reportingRepository.mapLehrerStammdaten().get(idKurslehrer));
				else
					super.zusatzLehrer.add(
							new ProxyReportingLehrer(this.reportingRepository, this.reportingRepository.mapLehrerStammdaten().get(idKurslehrer)));
			} else {
				try {
					final LehrerStammdaten lehrerStammdaten = new DataLehrerStammdaten(this.reportingRepository.conn()).getFromID(idKurslehrer);
					final ReportingLehrer lehrer = new ProxyReportingLehrer(
							this.reportingRepository,
							this.reportingRepository.mapLehrerStammdaten().computeIfAbsent(idKurslehrer, l -> lehrerStammdaten));
					if (idKurslehrer.equals(kursDaten.lehrer))
						super.kursLehrer = lehrer;
					else
						super.zusatzLehrer.add(lehrer);
				} catch (final ApiOperationException e) {
					ReportingExceptionUtils.putStacktraceInLog(
							"INFO: Fehler mit definiertem Rückgabewert abgefangen bei der Bestimmung der Daten eines Lehrers.", e,
							reportingRepository.logger(), LogLevel.INFO, 0);
				}
			}
		}
	}


	/**
	 * Gibt das Repository mit den Daten der Schule und den zwischengespeicherten Daten zurück.
	 * @return Repository für die Reporting
	 */
	public ReportingRepository reportingRepository() {
		return reportingRepository;
	}


	/**
	 * Stellt eine Liste mit Schülern der Klasse zur Verfügung.
	 * @return	Liste mit Schülern
	 */
	@Override
	public List<ReportingSchueler> schueler() {
		if (super.schueler.isEmpty() && !super.idsSchueler.isEmpty()) {
			super.schueler =
					DataSchuelerStammdaten.getListStammdaten(this.reportingRepository.conn(), super.idsSchueler).stream()
							.map(s -> this.reportingRepository.mapSchuelerStammdaten().computeIfAbsent(s.id, k -> s))
							.map(s -> (ReportingSchueler) new ProxyReportingSchueler(
									this.reportingRepository,
									s))
							.sorted(Comparator
									.comparing(ReportingSchueler::nachname, colGerman)
									.thenComparing(ReportingSchueler::vorname, colGerman)
									.thenComparing(ReportingSchueler::vornamen, colGerman)
									.thenComparing(ReportingSchueler::geburtsdatum, colGerman)
									.thenComparing(ReportingSchueler::id, colGerman))
							.toList();
		}
		return super.schueler();
	}
}
