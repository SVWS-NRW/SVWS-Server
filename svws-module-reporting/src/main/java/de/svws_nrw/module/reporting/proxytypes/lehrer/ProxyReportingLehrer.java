package de.svws_nrw.module.reporting.proxytypes.lehrer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.asd.data.lehrer.LehrerStammdaten;
import de.svws_nrw.asd.types.Geschlecht;
import de.svws_nrw.asd.types.schule.Nationalitaeten;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.types.PersonalTyp;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lehrer.ReportingLehrer;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlassenunterricht;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKursunterricht;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;
import jakarta.persistence.Query;

/**
 * Proxy-Klasse im Rahmen des Reportings für Daten vom Typ Lehrer und erweitert die Klasse {@link ReportingLehrer}.
 */
public class ProxyReportingLehrer extends ReportingLehrer {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;


	/**
	 * Erstellt ein neues Proxy-Reporting-Objekt für {@link ReportingLehrer}.
	 *
	 * @param reportingRepository Repository für das Reporting.
	 * @param lehrerStammdaten Stammdaten-Objekt aus der DB.
	 */
	public ProxyReportingLehrer(final ReportingRepository reportingRepository, final LehrerStammdaten lehrerStammdaten) {
		super(ersetzeNullBlankTrim(lehrerStammdaten.amtsbezeichnung),
				ersetzeNullBlankTrim(lehrerStammdaten.anrede),
				ersetzeNullBlankTrim(lehrerStammdaten.emailPrivat),
				ersetzeNullBlankTrim(lehrerStammdaten.emailDienstlich),
				"",
				lehrerStammdaten.foto,
				ersetzeNullBlankTrim(lehrerStammdaten.geburtsdatum),
				"",
				"",
				"",
				Geschlecht.fromValue(lehrerStammdaten.geschlecht),
				ersetzeNullBlankTrim(lehrerStammdaten.hausnummer),
				ersetzeNullBlankTrim(lehrerStammdaten.hausnummerZusatz),
				lehrerStammdaten.id,
				ersetzeNullBlankTrim(lehrerStammdaten.kuerzel),
				new ArrayList<>(),
				ersetzeNullBlankTrim(lehrerStammdaten.nachname),
				PersonalTyp.fromKuerzel(lehrerStammdaten.personalTyp),
				Nationalitaeten.getByDESTATIS(lehrerStammdaten.staatsangehoerigkeitID),
				null,
				ersetzeNullBlankTrim(lehrerStammdaten.strassenname),
				ersetzeNullBlankTrim(lehrerStammdaten.telefon),
				ersetzeNullBlankTrim(lehrerStammdaten.telefonMobil),
				"",
				"",
				ersetzeNullBlankTrim(lehrerStammdaten.titel),
				ersetzeNullBlankTrim(lehrerStammdaten.vorname),
				ersetzeNullBlankTrim(lehrerStammdaten.vorname),
				(lehrerStammdaten.wohnortID != null) ? reportingRepository.katalogOrte().get(lehrerStammdaten.wohnortID) : null,
				(lehrerStammdaten.ortsteilID != null) ? reportingRepository.katalogOrtsteile().get(lehrerStammdaten.ortsteilID) : null);

		this.reportingRepository = reportingRepository;

		lehrerStammdaten.leitungsfunktionen
				.forEach(leitungsfunktion -> super.leitungsfunktionen.add(new ProxyReportingLehrerLeitungsfunktion(reportingRepository, leitungsfunktion)));

		// Füge Stammdaten des Lehrers für weitere Verwendung in der Map im Repository hinzu.
		reportingRepository.mapLehrerStammdaten().putIfAbsent(super.id(), lehrerStammdaten);
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
	 * Liste der Klassenunterrichte, in denen der Lehrer als Fachlehrer in den Leistungsdaten eingetragen ist.
	 *
	 * @return Die Liste der Klassenunterrichte mit dem Lehrer als Fachlehrer.
	 */
	@Override
	public List<ReportingKlassenunterricht> klassenunterrichtAlsFachlehrer() {
		if (super.klassenunterrichtAlsFachlehrer.isEmpty()) {
			final String sqlQuery = """
					SELECT * FROM schuelerleistungsdaten INNER JOIN schuelerlernabschnittsdaten ON schuelerlernabschnittsdaten.ID = schuelerleistungsdaten.Abschnitt_ID
					WHERE (schuelerlernabschnittsdaten.Schuljahresabschnitts_ID = ?1) AND (schuelerleistungsdaten.Fachlehrer_ID = ?2)
					      AND (ISNULL(schuelerleistungsdaten.Kurs_ID)) AND (schuelerlernabschnittsdaten.WechselNr = 0)""";
			super.klassenunterrichtAlsFachlehrer = erstelleKlassenunterrichtAusLeistungsdaten(sqlQuery);
		}
		return super.klassenunterrichtAlsFachlehrer;
	}

	/**
	 * Liste der Klassenunterrichte, in denen der Lehrer als zusätzliche Lehrkraft eingetragen ist.
	 *
	 * @return Die Liste der Klassenunterrichte mit dem Lehrer als Zusatzkraft.
	 */
	@Override
	public List<ReportingKlassenunterricht> klassenunterrichtAlsZusatzlehrer() {
		if (super.klassenunterrichtAlsZusatzlehrer.isEmpty()) {
			final String sqlQuery = """
					SELECT * FROM schuelerleistungsdaten INNER JOIN schuelerlernabschnittsdaten ON schuelerlernabschnittsdaten.ID = schuelerleistungsdaten.Abschnitt_ID
					WHERE (schuelerlernabschnittsdaten.Schuljahresabschnitts_ID = ?1) AND (schuelerleistungsdaten.Zusatzkraft_ID = ?2)
					      AND (ISNULL(schuelerleistungsdaten.Kurs_ID)) AND (schuelerlernabschnittsdaten.WechselNr = 0)""";
			super.klassenunterrichtAlsZusatzlehrer = erstelleKlassenunterrichtAusLeistungsdaten(sqlQuery);
		}
		return super.klassenunterrichtAlsZusatzlehrer;
	}

	/**
	 * Liste der Kursunterrichte, in denen der Lehrer der Fachlehrer laut Leistungsdaten ist, in der Regel der Kursleiter.
	 *
	 * @return Die Kursunterrichte mit dem Lehrer als Fachlehrer.
	 */
	@Override
	public List<ReportingKursunterricht> kursunterrichtAlsFachlehrer() {
		if (super.kursunterrichtAlsFachlehrer.isEmpty()) {
			final String sqlQuery = """
					SELECT * FROM schuelerleistungsdaten INNER JOIN schuelerlernabschnittsdaten ON schuelerlernabschnittsdaten.ID = schuelerleistungsdaten.Abschnitt_ID
					WHERE (schuelerlernabschnittsdaten.Schuljahresabschnitts_ID = ?1) AND (schuelerleistungsdaten.Fachlehrer_ID = ?2)
					      AND (NOT ISNULL(schuelerleistungsdaten.Kurs_ID)) AND (schuelerlernabschnittsdaten.WechselNr = 0)""";
			super.kursunterrichtAlsFachlehrer = erstelleKursunterrichtAusLeistungsdaten(sqlQuery);
		}
		return super.kursunterrichtAlsFachlehrer;
	}

	/**
	 * Liste der Kursunterrichte, in denen der Lehrer als zusätzliche Lehrkraft eingetragen ist.
	 *
	 * @return Die Kursunterrichte mit dem Lehrer als Zusatzkraft.
	 */
	@Override
	public List<ReportingKursunterricht> kursunterrichtAlsZusatzlehrer() {
		if (super.kursunterrichtAlsZusatzlehrer.isEmpty()) {
			final String sqlQuery = """
					SELECT * FROM schuelerleistungsdaten INNER JOIN schuelerlernabschnittsdaten ON schuelerlernabschnittsdaten.ID = schuelerleistungsdaten.Abschnitt_ID
					WHERE (schuelerlernabschnittsdaten.Schuljahresabschnitts_ID = ?1) AND (schuelerleistungsdaten.Fachlehrer_ID = ?2)
					      AND (schuelerlernabschnittsdaten.WechselNr = 0) AND (schuelerleistungsdaten.Kurs_ID IN (
					          SELECT DISTINCT kurse.ID FROM kurse LEFT JOIN kurslehrer ON kurse.ID = kurslehrer.Kurs_ID
					          WHERE (kurse.Schuljahresabschnitts_ID = ?1) AND (kurslehrer.Lehrer_ID = ?2)
					      ))""";
			super.kursunterrichtAlsZusatzlehrer = erstelleKursunterrichtAusLeistungsdaten(sqlQuery);
		}
		return super.kursunterrichtAlsZusatzlehrer;
	}


	// ##### Hilfsfunktionen zur Ermittlung der Unterrichte #####

	/**
	 * Diese Methode erzeugt Klassenunterrichtsdata aus einer SQL-Query.
	 *
	 * @param sqlQuery Die SQL-Abfrage für die Leistungsdaten
	 *
	 * @return Die Liste der Klassenunterrichte
	 */
	private List<ReportingKlassenunterricht> erstelleKlassenunterrichtAusLeistungsdaten(final String sqlQuery) {
		@SuppressWarnings("unchecked") final List<ReportingKlassenunterricht> result =
				(List<ReportingKlassenunterricht>) erstelleUnterrichtAusLeistungsdaten(sqlQuery, false);
		return result;
	}

	/**
	 * Diese Methode erzeugt Kursunterrichtsdata aus einer SQL-Query.
	 *
	 * @param sqlQuery Die SQL-Abfrage für die Leistungsdaten
	 *
	 * @return Die Liste der Kursunterrichte
	 */
	private List<ReportingKursunterricht> erstelleKursunterrichtAusLeistungsdaten(final String sqlQuery) {
		@SuppressWarnings("unchecked") final List<ReportingKursunterricht> result =
				(List<ReportingKursunterricht>) erstelleUnterrichtAusLeistungsdaten(sqlQuery, true);
		return result;
	}

	/**
	 * Diese Methode erzeugt Unterrichtsdaten aus einer SQL-Query mit generischer Verarbeitung.
	 *
	 * @param sqlQuery Die SQL-Abfrage für die Leistungsdaten
	 * @param isKurs True für Kursunterricht, False für Klassenunterricht
	 *
	 * @return Die Liste der Unterrichte (ReportingKlassenunterricht oder ReportingKursunterricht)
	 */
	private List<?> erstelleUnterrichtAusLeistungsdaten(final String sqlQuery, final boolean isKurs) {
		final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> dtoSchuelerLeistungsdaten =
				querySchuelerLeistungsdatenToListMap(sqlQuery, this.reportingRepository.auswahlSchuljahresabschnitt().id(), super.id());

// Erzeuge neue Maps, die im Folgenden gefüllt werden.
		final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapLerngruppeFachLehrerLeistungsdaten = new ListMap3DLongKeys<>();
		final Map<Long, Long> mapIdLeistungsdatenIdSchueler = new HashMap<>();
		final Map<Long, ReportingFach> mapFach = new HashMap<>();
		final Map<Long, ReportingLehrer> mapLehrer = new HashMap<>();

		// Erstelle eine Map der Schüler aus den Leistungsdaten
		final Map<Long, ReportingSchueler> mapSchueler = this.reportingRepository.schueler(dtoSchuelerLeistungsdaten.keySet1().stream().toList()).stream()
				.collect(Collectors.toMap(ReportingSchueler::id, s -> s));

		if (isKurs) {
			final Map<Long, ReportingKurs> mapKurse = new HashMap<>();
			gruppiereLeistungsdatenNachKursFachLehrer(dtoSchuelerLeistungsdaten, mapSchueler, mapIdLeistungsdatenIdSchueler,
					mapLerngruppeFachLehrerLeistungsdaten,
					mapKurse, mapFach, mapLehrer);
			return erstelleKursunterricht(mapLerngruppeFachLehrerLeistungsdaten, mapKurse, mapLehrer);
		} else {
			final Map<Long, ReportingKlasse> mapKlasse = new HashMap<>();
			gruppiereLeistungsdatenNachKlasseFachLehrer(dtoSchuelerLeistungsdaten, mapSchueler, mapIdLeistungsdatenIdSchueler,
					mapLerngruppeFachLehrerLeistungsdaten, mapKlasse, mapFach, mapLehrer);
			return erstelleKlassenunterricht(mapLerngruppeFachLehrerLeistungsdaten, mapSchueler, mapIdLeistungsdatenIdSchueler, mapKlasse, mapFach, mapLehrer);
		}
	}


	/**
	 * Führt eine Abfrage auf die Schülerleistungsdaten durch, ergänzt sie mit den zugehörigen Schüler-IDs anhand der Lernabschnitts-IDs und erstellt daraus
	 * eine 3-dimensionale Zuordnung in Form von ListMap3DLongKeys, die die zugeordneten Leistungsdaten enthält.
	 *
	 * @param sqlQuery Die SQL-Abfrage, die auf die Tabelle der Schülerleistungsdaten angewendet wird.
	 * @param params   Ein Array von Parametern, die in der SQL-Abfrage verwendet werden.
	 *
	 * @return Eine 3-dimensionale Map (ListMap3DLongKeys) mit Schüler-IDs, Abschnitts-IDs, Leistungsdaten-IDs und Leistungsdaten, falls die Abfrage
	 *         erfolgreich ist. Bei Fehlern wird eine leere Liste zurückgegeben.
	 */
	private ListMap3DLongKeys<DTOSchuelerLeistungsdaten> querySchuelerLeistungsdatenToListMap(final String sqlQuery, final Object... params) {
		try {
			// Hole die Leistungsdaten aus der DB gemäß der übergebenen Query.
			final List<DTOSchuelerLeistungsdaten> schuelerLeistungsdaten =
					this.reportingRepository.conn().queryList(sqlQuery, DTOSchuelerLeistungsdaten.class, params);

			// Lade zusätzlich die Schüler-IDs aus der DB über die Lernabschnitt-IDs ...
			final List<Long> idsLernabschnittsdaten = schuelerLeistungsdaten.stream().map(ld -> ld.Abschnitt_ID).distinct().toList();
			final String sqlSchueler = """
				SELECT schuelerlernabschnittsdaten.ID, schuelerlernabschnittsdaten.Schueler_ID FROM schuelerlernabschnittsdaten
				WHERE schuelerlernabschnittsdaten.Schuljahresabschnitts_ID = ?1 AND schuelerlernabschnittsdaten.ID in (?2)
				      AND schuelerlernabschnittsdaten.WechselNr = 0""";
			final Query nativeQuery = this.reportingRepository.conn().getNativeQuery(sqlSchueler)
					.setParameter(1, this.reportingRepository.auswahlSchuljahresabschnitt().id())
					.setParameter(2, idsLernabschnittsdaten);
			@SuppressWarnings("unchecked") final List<Object[]> rows = nativeQuery.getResultList();
			final Map<Long, Long> lernabschnittIdToSchuelerId = rows.stream()
					.collect(Collectors.toMap(r -> ((Number) r[0]).longValue(), r -> ((Number) r[1]).longValue()));

			// ... und baue damit eine ListMap3D auf: idSchueler, idLernabschnitt, idLeistungsdaten > Leistungsdaten.
			// Überspringe Einträge ohne zugehörigen Lernabschnitt.
			final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> listmapLeistungsdaten = new ListMap3DLongKeys<>();
			schuelerLeistungsdaten.forEach(ld -> {
				final Long schuelerId = lernabschnittIdToSchuelerId.get(ld.Abschnitt_ID);
				if (schuelerId != null) {
					listmapLeistungsdaten.add(schuelerId, ld.Abschnitt_ID, ld.ID, ld);
				}
			});
			return listmapLeistungsdaten;
		} catch (final Exception e) {
			ReportingExceptionUtils.putStacktraceInLog(
					("FEHLER: Fehler bei der Ermittlung von Unterrichtsdaten aus den Schülerleistungsdaten für Lehrer %s.")
							.formatted(super.kuerzel, sqlQuery),
					e, reportingRepository.logger(), LogLevel.ERROR, 0);
			return new ListMap3DLongKeys<>();
		}
	}


	/**
	 * Gruppiert Leistungsdaten nach Klasse, Fach und Lehrer und erstellt Maps für diese Entitäten.
	 *
	 * @param dtoSchuelerLeistungsdaten Die zu verarbeitenden Leistungsdaten (3D-Map: SchülerID, AbschnittID, LeistungsdatenID)
	 * @param mapSchueler Die Map der verfügbaren Schüler
	 * @param mapIdLeistungsdatenIdSchueler Die Map, die zu den IDs der Leistungsdaten die Schüler-ID liefert.
	 * @param mapKlasseFachLehrerLeistungsdaten Die zu befüllende 3D-Map für die Gruppierung
	 * @param mapKlasse Die zu befüllende Map der Klassen
	 * @param mapFach Die zu befüllende Map der Fächer
	 * @param mapLehrer Die zu befüllende Map der Lehrer
	 */
	private void gruppiereLeistungsdatenNachKlasseFachLehrer(final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> dtoSchuelerLeistungsdaten,
			final Map<Long, ReportingSchueler> mapSchueler, final Map<Long, Long> mapIdLeistungsdatenIdSchueler,
			final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapKlasseFachLehrerLeistungsdaten,
			final Map<Long, ReportingKlasse> mapKlasse, final Map<Long, ReportingFach> mapFach, final Map<Long, ReportingLehrer> mapLehrer) {

		for (final LongArrayKey key : dtoSchuelerLeistungsdaten.keySet123()) {
			final ReportingSchueler schueler = mapSchueler.get(key.getKeyAt(0));
			if (schueler == null)
				continue;
			final ReportingSchuelerLernabschnitt lernabschnitt = schueler.mapLernabschnitte().getSingle3OrNull(key.getKeyAt(1));
			if (lernabschnitt == null)
				continue;
			final ReportingKlasse klasse = lernabschnitt.klasse();
			final ReportingSchuelerLeistungsdaten reportingSchuelerLeistungsdaten = lernabschnitt.leistungsdatenZurId(key.getKeyAt(2));
			final ReportingFach fach = reportingSchuelerLeistungsdaten.fach();
			final ReportingLehrer fachlehrer = reportingSchuelerLeistungsdaten.fachlehrer();

			if ((klasse == null) || (fach == null) || (fachlehrer == null))
				continue;

			mapIdLeistungsdatenIdSchueler.putIfAbsent(reportingSchuelerLeistungsdaten.id(), schueler.id());
			mapKlasseFachLehrerLeistungsdaten.add(klasse.id(), fach.id(), fachlehrer.id(), reportingSchuelerLeistungsdaten);
			mapKlasse.putIfAbsent(klasse.id(), klasse);
			mapFach.putIfAbsent(fach.id(), fach);
			mapLehrer.putIfAbsent(fachlehrer.id(), fachlehrer);
		}
	}

	/**
	 * Ermittelt alle Zusatzlehrer aus einer Liste von Leistungsdaten.
	 *
	 * @param reportingSchuelerLeistungsdaten Die Leistungsdaten
	 * @param mapLehrer Die verfügbaren Lehrer (wird mit Zusatzlehrern ergänzt, falls diese noch nicht enthalten sind)
	 *
	 * @return Liste der IDs der Zusatzlehrer
	 */
	private List<Long> getIdsZusatzlehrer(final List<ReportingSchuelerLeistungsdaten> reportingSchuelerLeistungsdaten,
			final Map<Long, ReportingLehrer> mapLehrer) {

		final Set<Long> idsZusatzLehrer = reportingSchuelerLeistungsdaten.stream()
				.flatMap(ld -> Stream.ofNullable(ld.zusatzLehrer()).flatMap(List::stream))
				.map(ReportingLehrer::id)
				.collect(Collectors.toSet());

		if (!idsZusatzLehrer.isEmpty()) {
			final List<Long> fehlende = idsZusatzLehrer.stream().filter(id -> !mapLehrer.containsKey(id)).toList();

			if (!fehlende.isEmpty()) {
				this.reportingRepository.lehrer(fehlende).forEach(l -> mapLehrer.putIfAbsent(l.id(), l));
			}
		}

		return idsZusatzLehrer.stream().toList();
	}

	/**
	 * Erstellt die Liste der Lehrkräfte mit dem Fachlehrer an erster Stelle gefolgt von Zusatzlehrern.
	 *
	 * @param idFachlehrer Die ID des Fachlehrers
	 * @param idsZusatzLehrer Die IDs der Zusatzlehrer
	 * @param mapLehrer Die Map der verfügbaren Lehrer
	 *
	 * @return Die geordnete Liste der Lehrkräfte
	 */
	private List<ReportingLehrer> erstelleLehrerliste(final long idFachlehrer, final List<Long> idsZusatzLehrer, final Map<Long, ReportingLehrer> mapLehrer) {
		final List<ReportingLehrer> lehrer = new ArrayList<>();
		final ReportingLehrer fachlehrer = mapLehrer.get(idFachlehrer);
		if (fachlehrer != null)
			lehrer.add(fachlehrer);
		lehrer.addAll(idsZusatzLehrer.stream().map(mapLehrer::get).filter(Objects::nonNull).toList());
		return lehrer;
	}

	/**
	 * Berechnet die maximalen Wochenstunden aus einer Liste von Leistungsdaten.
	 *
	 * @param reportingSchuelerLeistungsdaten Die Leistungsdaten
	 *
	 * @return Die maximalen Wochenstunden
	 */
	private int getMaximalWochenstundenSchueler(final List<ReportingSchuelerLeistungsdaten> reportingSchuelerLeistungsdaten) {
		return reportingSchuelerLeistungsdaten.stream()
				.map(ReportingSchuelerLeistungsdaten::wochenstundenSchueler)
				.max(Integer::compare)
				.orElse(0);
	}

	/**
	 * Erstellt eine Map der Wochenstunden pro Lehrkraft für einen Unterricht.
	 *
	 * @param idFachlehrer Die ID des Fachlehrers
	 * @param wochenstundenSchueler Die Wochenstunden der Schüler
	 * @param idsZusatzLehrer Die IDs der Zusatzlehrer
	 * @param reportingSchuelerLeistungsdaten Die Leistungsdaten zur Berechnung der Zusatzlehrer-Wochenstunden
	 *
	 * @return Eine Map mit Lehrer-IDs und ihren Wochenstunden
	 */
	private Map<Long, Double> getWochenstundenProLehrer(final long idFachlehrer, final int wochenstundenSchueler, final List<Long> idsZusatzLehrer,
			final List<ReportingSchuelerLeistungsdaten> reportingSchuelerLeistungsdaten) {

		final Map<Long, Double> wochenstundenProLehrer = new HashMap<>();
		wochenstundenProLehrer.put(idFachlehrer, (double) wochenstundenSchueler);

		for (final Long idZusatz : idsZusatzLehrer) {
			final int wsZusatz = reportingSchuelerLeistungsdaten.stream()
					.filter(ld -> (ld.zusatzLehrer() != null) && ld.zusatzLehrer().stream().anyMatch(z -> z.id() == idZusatz))
					.map(ld -> {
						final Double wStd = ld.wochenstundenLehrer().get(idZusatz);
						return (wStd == null) ? 0 : wStd.intValue();
					})
					.max(Integer::compare)
					.orElse(0);
			wochenstundenProLehrer.put(idZusatz, (double) wsZusatz);
		}

		return wochenstundenProLehrer;
	}

	/**
	 * Erstellt ReportingKlassenunterrichte aus den gruppierten Leistungsdaten.
	 *
	 * @param mapKlasseFachLehrerLeistungsdaten Die 3D-Map mit gruppierten Leistungsdaten nach Klasse, Fach und Lehrer.
	 * @param mapSchueler Die Map der Schüler
	 * @param mapIdLeistungsdatenIdSchueler Die Map, die zu den IDs der Leistungsdaten die Schüler-ID liefert.
	 * @param mapKlasse Die Map der Klassen
	 * @param mapFach Die Map der Fächer
	 * @param mapLehrer Die Map der Lehrer
	 *
	 * @return Eine Liste der erstellten ReportingKlassenunterrichte
	 */
	private List<ReportingKlassenunterricht> erstelleKlassenunterricht(
			final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapKlasseFachLehrerLeistungsdaten,
			final Map<Long, ReportingSchueler> mapSchueler, final Map<Long, Long> mapIdLeistungsdatenIdSchueler, final Map<Long, ReportingKlasse> mapKlasse,
			final Map<Long, ReportingFach> mapFach, final Map<Long, ReportingLehrer> mapLehrer) {

		final List<ReportingKlassenunterricht> result = new ArrayList<>();

		for (final LongArrayKey key123 : mapKlasseFachLehrerLeistungsdaten.keySet123()) {
			final long idKlasse = key123.getKeyAt(0);
			final long idFach = key123.getKeyAt(1);
			final long idLehrer = key123.getKeyAt(2);

			final ReportingKlasse klasse = mapKlasse.get(idKlasse);
			final ReportingFach fach = mapFach.get(idFach);
			final List<ReportingSchuelerLeistungsdaten> leistungsdaten = mapKlasseFachLehrerLeistungsdaten.get123(idKlasse, idFach, idLehrer);
			final List<ReportingSchueler> schueler = leistungsdaten.stream()
					.map(ReportingSchuelerLeistungsdaten::id)
					.map(mapIdLeistungsdatenIdSchueler::get)
					.map(mapSchueler::get)
					.filter(Objects::nonNull)
					.toList();

			final List<Long> idsZusatzLehrer = getIdsZusatzlehrer(leistungsdaten, mapLehrer);
			final ReportingLehrer bewertenderLehrer = mapLehrer.get(idLehrer);
			final List<ReportingLehrer> lehrer = erstelleLehrerliste(idLehrer, idsZusatzLehrer, mapLehrer);
			final int wochenstundenSchueler = getMaximalWochenstundenSchueler(leistungsdaten);
			final Map<Long, Double> wochenstundenProLehrer = getWochenstundenProLehrer(idLehrer, wochenstundenSchueler, idsZusatzLehrer, leistungsdaten);

			result.add(new ReportingKlassenunterricht(klasse, fach, bewertenderLehrer, lehrer, wochenstundenProLehrer, schueler, wochenstundenSchueler));
		}

		return result;
	}



	/**
	 * Gruppiert Leistungsdaten nach Kurs, Fach und Lehrer und erstellt Maps für diese Entitäten.
	 *
	 * @param dtoSchuelerLeistungsdaten Die zu verarbeitenden Leistungsdaten (3D-Map: SchülerID, AbschnittID, LeistungsdatenID)
	 * @param mapSchueler Die Map der verfügbaren Schüler
	 * @param mapIdLeistungsdatenIdSchueler Die Map, die zu den IDs der Leistungsdaten die Schüler-ID liefert.
	 * @param mapKursFachLehrerLeistungsdaten Die zu befüllende 3D-Map für die Gruppierung
	 * @param mapKurse Die zu befüllende Map der Kurse
	 * @param mapFach Die zu befüllende Map der Fächer
	 * @param mapLehrer Die zu befüllende Map der Lehrer
	 */
	private void gruppiereLeistungsdatenNachKursFachLehrer(final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> dtoSchuelerLeistungsdaten,
			final Map<Long, ReportingSchueler> mapSchueler, final Map<Long, Long> mapIdLeistungsdatenIdSchueler,
			final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapKursFachLehrerLeistungsdaten,
			final Map<Long, ReportingKurs> mapKurse, final Map<Long, ReportingFach> mapFach, final Map<Long, ReportingLehrer> mapLehrer) {

		for (final LongArrayKey key : dtoSchuelerLeistungsdaten.keySet123()) {
			final ReportingSchueler schueler = mapSchueler.get(key.getKeyAt(0));
			if (schueler == null)
				continue;
			final ReportingSchuelerLernabschnitt lernabschnitt = schueler.mapLernabschnitte().getSingle3OrNull(key.getKeyAt(1));
			if (lernabschnitt == null)
				continue;
			final ReportingSchuelerLeistungsdaten reportingSchuelerLeistungsdaten = lernabschnitt.leistungsdatenZurId(key.getKeyAt(2));
			final ReportingKurs kurs = reportingSchuelerLeistungsdaten.kurs();
			if (kurs == null)
				continue;
			final ReportingFach fach = reportingSchuelerLeistungsdaten.fach();
			final ReportingLehrer fachlehrer = reportingSchuelerLeistungsdaten.fachlehrer();

			if ((fach == null) || (fachlehrer == null))
				continue;

			mapIdLeistungsdatenIdSchueler.putIfAbsent(reportingSchuelerLeistungsdaten.id(), schueler.id());
			mapKursFachLehrerLeistungsdaten.add(kurs.id(), fach.id(), fachlehrer.id(), reportingSchuelerLeistungsdaten);
			mapKurse.putIfAbsent(kurs.id(), kurs);
			mapFach.putIfAbsent(fach.id(), fach);
			mapLehrer.putIfAbsent(fachlehrer.id(), fachlehrer);
		}
	}

	/**
	 * Erstellt ReportingKursunterrichte aus den gruppierten Leistungsdaten.
	 *
	 * @param mapKursFachLehrerLeistungsdaten Die 3D-Map mit gruppierten Leistungsdaten nach Kurs, Fach und Lehrer.
	 * @param mapKurse Die Map der Kurse
	 * @param mapLehrer Die Map der Lehrer
	 *
	 * @return Eine Liste der erstellten ReportingKursunterrichte
	 */
	private List<ReportingKursunterricht> erstelleKursunterricht(final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapKursFachLehrerLeistungsdaten,
			final Map<Long, ReportingKurs> mapKurse, final Map<Long, ReportingLehrer> mapLehrer) {

		final List<ReportingKursunterricht> result = new ArrayList<>();

		for (final LongArrayKey key123 : mapKursFachLehrerLeistungsdaten.keySet123()) {
			final long idKurs = key123.getKeyAt(0);
			final long idLehrer = key123.getKeyAt(2);

			final ReportingKurs kurs = mapKurse.get(idKurs);
			final ReportingLehrer bewertenderLehrer = mapLehrer.get(idLehrer);

			if ((kurs != null) && (bewertenderLehrer != null)) {
				result.add(new ReportingKursunterricht(kurs, bewertenderLehrer));
			}
		}

		return result;
	}

}
