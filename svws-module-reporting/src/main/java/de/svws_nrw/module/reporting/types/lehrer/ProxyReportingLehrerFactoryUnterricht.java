package de.svws_nrw.module.reporting.types.lehrer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.svws_nrw.core.adt.LongArrayKey;
import de.svws_nrw.core.adt.map.ListMap3DLongKeys;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.repositories.ReportingRepository;
import de.svws_nrw.module.reporting.types.fach.ReportingFach;
import de.svws_nrw.module.reporting.types.lerngruppen.ProxyReportingKlassenunterricht;
import de.svws_nrw.module.reporting.types.lerngruppen.ProxyReportingKursunterricht;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlasse;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKlassenunterricht;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKurs;
import de.svws_nrw.module.reporting.types.lerngruppen.ReportingKursunterricht;
import de.svws_nrw.module.reporting.types.schueler.ReportingSchueler;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.types.schueler.lernabschnitte.ReportingSchuelerLernabschnitt;
import de.svws_nrw.module.reporting.utils.ReportingExceptionUtils;

/**
 * Factory-Klasse zur Erstellung von Unterrichtsstrukturen (Klassen- und Kursunterricht) aus Leistungsdaten für das Reporting.
 * Diese Klasse kapselt die komplexe Logik zur Gruppierung und Aufbereitung von Unterrichtsdaten für einen Lehrer.
 */
public class ProxyReportingLehrerFactoryUnterricht {

	/** Repository mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingRepository reportingRepository;

	/** Der Lehrer, dessen Unterricht von dieser Factory erzeugt werden soll. */
	private final ProxyReportingLehrer factoryLehrer;

	/**
	 * Erstellt eine neue Factory Instanz.
	 *
	 * @param reportingRepository Das Repository mit Zugriff auf Datenbank und Caches.
	 * @param factoryLehrer              Der Lehrer, dessen Unterricht von dieser Factory erzeugt werden soll.
	 */
	public ProxyReportingLehrerFactoryUnterricht(final ReportingRepository reportingRepository, final ProxyReportingLehrer factoryLehrer) {
		this.reportingRepository = reportingRepository;
		this.factoryLehrer = factoryLehrer;
	}

	/**
	 * Diese Methode erzeugt Klassenunterrichtsdaten für den Lehrer als Fachlehrer.
	 *
	 * @return Die Liste der Klassenunterrichte als Fachlehrer.
	 */
	public List<ReportingKlassenunterricht> erstelleKlassenunterrichtAlsFachlehrer() {
		final String query = "SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
				+ "WHERE ld.Abschnitt_ID = a.ID "
				+ "AND a.Schuljahresabschnitts_ID = ?1 "
				+ "AND a.WechselNr = 0 "
				+ "AND ld.Fachlehrer_ID = ?2 "
				+ "AND ld.Kurs_ID IS NULL";
		@SuppressWarnings("unchecked") final List<ReportingKlassenunterricht> result =
				(List<ReportingKlassenunterricht>) erstelleUnterrichtAusLeistungsdaten(query, false);
		return result;
	}

	/**
	 * Diese Methode erzeugt Klassenunterrichtsdaten für den Lehrer als Zusatzlehrer.
	 *
	 * @return Die Liste der Klassenunterrichte als Zusatzlehrer.
	 */
	public List<ReportingKlassenunterricht> erstelleKlassenunterrichtAlsZusatzlehrer() {
		final String query = "SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
				+ "WHERE ld.Abschnitt_ID = a.ID "
				+ "AND a.Schuljahresabschnitts_ID = ?1 "
				+ "AND a.WechselNr = 0 "
				+ "AND ld.Zusatzkraft_ID = ?2 "
				+ "AND ld.Kurs_ID IS NULL";
		@SuppressWarnings("unchecked") final List<ReportingKlassenunterricht> result =
				(List<ReportingKlassenunterricht>) erstelleUnterrichtAusLeistungsdaten(query, false);
		return result;
	}

	/**
	 * Diese Methode erzeugt Kursunterrichtsdaten für den Lehrer als Fachlehrer.
	 *
	 * @return Die Liste der Kursunterrichte als Fachlehrer.
	 */
	public List<ReportingKursunterricht> erstelleKursunterrichtAlsFachlehrer() {
		final String query = "SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
				+ "WHERE ld.Abschnitt_ID = a.ID "
				+ "AND a.Schuljahresabschnitts_ID = ?1 "
				+ "AND a.WechselNr = 0 "
				+ "AND ld.Kurs_ID IS NOT NULL "
				+ "AND ld.Fachlehrer_ID = ?2";
		@SuppressWarnings("unchecked") final List<ReportingKursunterricht> result =
				(List<ReportingKursunterricht>) erstelleUnterrichtAusLeistungsdaten(query, true);
		return result;
	}

	/**
	 * Diese Methode erzeugt Kursunterrichtsdaten für den Lehrer als Zusatzlehrer.
	 *
	 * @return Die Liste der Kursunterrichte als Zusatzlehrer.
	 */
	public List<ReportingKursunterricht> erstellekursunterrichtAlsZusatzlehrer() {
		final String query = "SELECT ld, a.Schueler_ID FROM DTOSchuelerLeistungsdaten ld, DTOSchuelerLernabschnittsdaten a "
				+ "WHERE ld.Abschnitt_ID = a.ID "
				+ "AND a.Schuljahresabschnitts_ID = ?1 "
				+ "AND a.WechselNr = 0 "
				+ "AND ld.Kurs_ID IN (SELECT k.ID FROM DTOKurs k, DTOKursLehrer kl WHERE k.ID = kl.Kurs_ID AND k.Schuljahresabschnitts_ID = ?1 AND kl.Lehrer_ID = ?2) "
				+ "AND ld.Fachlehrer_ID = ?2";
		@SuppressWarnings("unchecked") final List<ReportingKursunterricht> result =
				(List<ReportingKursunterricht>) erstelleUnterrichtAusLeistungsdaten(query, true);
		return result;
	}


	/**
	 * Diese Methode erzeugt Unterrichtsdaten aus einer Query mit generischer Verarbeitung.
	 *
	 * @param query         Die Abfrage, die auf die Tabelle der Schülerleistungsdaten und angewendet wird und die Leistungsdaten und die Schüler-ID zurückgeben
	 *                      muss, also eine JOIN-Abfrage der From "SELECT ld, a.Schueler_ID ...". Hieraus werden die Unterrichte ermittelt.
	 * @param istKurs       True für Kursunterricht, False für Klassenunterricht.
	 *
	 * @return Die Liste der Unterrichte (ReportingKlassenunterricht oder ReportingKursunterricht).
	 */
	private List<?> erstelleUnterrichtAusLeistungsdaten(final String query, final boolean istKurs) {
		final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> dtoSchuelerLeistungsdaten =
				querySchuelerLeistungsdatenToListMap(query, this.reportingRepository.auswahlSchuljahresabschnitt().id(), this.factoryLehrer.id());

		// Erzeuge neue Maps, die im Folgenden gefüllt werden.
		final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapLerngruppeFachLehrerLeistungsdaten = new ListMap3DLongKeys<>();
		final Map<Long, Long> mapIdLeistungsdatenIdSchueler = new HashMap<>();
		final Map<Long, ReportingFach> mapFach = new HashMap<>();
		final Map<Long, ReportingLehrer> mapLehrer = new HashMap<>();

		// Erstelle eine Map der Schüler aus den Leistungsdaten
		final Map<Long, ReportingSchueler> mapSchueler = this.reportingRepository.schueler(dtoSchuelerLeistungsdaten.keySet1().stream().toList()).stream()
				.collect(Collectors.toMap(ReportingSchueler::id, s -> s));

		if (istKurs) {
			final Map<Long, ReportingKurs> mapKurse = new HashMap<>();
			gruppiereLeistungsdatenNachKursFachLehrer(dtoSchuelerLeistungsdaten, mapSchueler, mapIdLeistungsdatenIdSchueler,
					mapLerngruppeFachLehrerLeistungsdaten, mapKurse, mapFach, mapLehrer);
			return erstelleKursunterricht(mapLerngruppeFachLehrerLeistungsdaten, mapKurse, mapLehrer, mapIdLeistungsdatenIdSchueler);
		} else {
			final Map<Long, ReportingKlasse> mapKlasse = new HashMap<>();
			gruppiereLeistungsdatenNachKlasseFachLehrer(dtoSchuelerLeistungsdaten, mapSchueler, mapIdLeistungsdatenIdSchueler,
					mapLerngruppeFachLehrerLeistungsdaten, mapKlasse, mapFach, mapLehrer);
			return erstelleKlassenunterricht(mapLerngruppeFachLehrerLeistungsdaten, mapSchueler, mapIdLeistungsdatenIdSchueler, mapKlasse, mapFach, mapLehrer);
		}
	}


	/**
	 * Führt eine Abfrage auf die Schülerleistungsdaten durch und sammelt dabei direkt die zugehörigen Schüler-IDs (per JOIN). Dann wird daraus
	 * eine 3-dimensionale Zuordnung in Form von ListMap3DLongKeys, die die zugeordneten Leistungsdaten enthält, erstellt.
	 *
	 * @param query         Die Abfrage, die auf die Tabelle der Schülerleistungsdaten und angewendet wird und die Leistungsdaten und die Schüler-ID zurückgeben
	 *                      muss, also eine JOIN-Abfrage der From "SELECT ld, a.Schueler_ID ...". Hieraus werden die Unterrichte ermittelt.
	 * @param params        Ein Array von Parametern, die in der Abfrage verwendet werden.
	 *
	 * @return Eine 3-dimensionale Map (ListMap3DLongKeys) mit Schüler-IDs, Abschnitts-IDs, Leistungsdaten-IDs und Leistungsdaten, falls die Abfrage
	 * erfolgreich ist. Bei Fehlern wird eine leere Liste zurückgegeben.
	 */
	private ListMap3DLongKeys<DTOSchuelerLeistungsdaten> querySchuelerLeistungsdatenToListMap(final String query, final Object... params) {
		try {
			// Die Abfrage muss Object[]-Arrays liefern, da die Abfrage zwei Werte selektiert (DTOLeistungsdaten + SchuelerID als Long)
			final List<Object[]> results = this.reportingRepository.conn().queryList(query, Object[].class, params);

			final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> listmapLeistungsdaten = new ListMap3DLongKeys<>();

			if (results.isEmpty())
				return listmapLeistungsdaten;

			// Iterieren über die Ergebnisse und Befüllen der Map
			for (final Object[] row : results) {
				// Erstes Objekt sind die Leistungsdaten
				final DTOSchuelerLeistungsdaten ld = (DTOSchuelerLeistungsdaten) row[0];
				// Zweites Objekt ist die Schüler-ID zu den Leistungsdaten
				final Long schuelerId = (Long) row[1];

				if ((schuelerId != null) && (ld != null)) {
					listmapLeistungsdaten.add(schuelerId, ld.Abschnitt_ID, ld.ID, ld);
				}
			}

			return listmapLeistungsdaten;
		} catch (final Exception e) {
			ReportingExceptionUtils.logException(
					("FEHLER: Fehler bei der Ermittlung von Unterrichtsdaten aus den Schülerleistungsdaten für Lehrer %s.").formatted(factoryLehrer.kuerzel()),
					e, reportingRepository.logger(), LogLevel.ERROR, 0);
			return new ListMap3DLongKeys<>();
		}
	}


	/**
	 * Gruppiert Leistungsdaten nach Klasse, Fach und Lehrer und erstellt Maps für diese Entitäten.
	 *
	 * @param dtoSchuelerLeistungsdaten     Die zu verarbeitenden Leistungsdaten (3D-Map: SchülerID, AbschnittID, LeistungsdatenID)
	 * @param mapSchueler                   Die Map der verfügbaren Schüler
	 * @param mapIdLeistungsdatenIdSchueler Die Map, die zu den IDs der Leistungsdaten die Schüler-ID liefert.
	 * @param mapKlasseFachLehrerLeistungsdaten       Die zu befüllende 3D-Map für die Gruppierung
	 * @param mapKlasse                     Die zu befüllende Map der Klassen
	 * @param mapFach                       Die zu befüllende Map der Fächer
	 * @param mapLehrer                     Die zu befüllende Map der Lehrer
	 */
	private void gruppiereLeistungsdatenNachKlasseFachLehrer(final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> dtoSchuelerLeistungsdaten,
			final Map<Long, ReportingSchueler> mapSchueler, final Map<Long, Long> mapIdLeistungsdatenIdSchueler,
			final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapKlasseFachLehrerLeistungsdaten,
			final Map<Long, ReportingKlasse> mapKlasse, final Map<Long, ReportingFach> mapFach, final Map<Long, ReportingLehrer> mapLehrer) {

		for (final LongArrayKey key : dtoSchuelerLeistungsdaten.keySet123()) {
			final ReportingSchueler schueler = mapSchueler.get(key.getKeyAt(0));
			if (schueler == null)
				continue;
			final ReportingSchuelerLernabschnitt lernabschnitt = schueler.lernabschnittById(key.getKeyAt(1));
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
	 * @param mapLehrer                       Die verfügbaren Lehrer (wird mit Zusatzlehrern ergänzt, falls diese noch nicht enthalten sind)
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
	 * @param idFachlehrer    Die ID des Fachlehrers
	 * @param idsZusatzLehrer Die IDs der Zusatzlehrer
	 * @param mapLehrer       Die Map der verfügbaren Lehrer
	 *
	 * @return Die geordnete Liste der Lehrkräfte
	 */
	private List<ReportingLehrer> erstelleLehrerliste(final long idFachlehrer, final List<Long> idsZusatzLehrer,
			final Map<Long, ReportingLehrer> mapLehrer) {
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
	 * @param idFachlehrer                    Die ID des Fachlehrers
	 * @param wochenstundenSchueler           Die Wochenstunden der Schüler
	 * @param idsZusatzLehrer                 Die IDs der Zusatzlehrer
	 * @param reportingSchuelerLeistungsdaten Die Leistungsdaten zur Berechnung der Zusatzlehrer-Wochenstunden
	 *
	 * @return Eine Map mit Lehrer-IDs und ihren Wochenstunden
	 */
	private Map<Long, Double> getWochenstundenProLehrer(final long idFachlehrer, final int wochenstundenSchueler, final List<Long> idsZusatzLehrer,
			final List<ReportingSchuelerLeistungsdaten> reportingSchuelerLeistungsdaten) {

		final Map<Long, Double> wochenstundenProLehrer = new HashMap<>();
		wochenstundenProLehrer.put(idFachlehrer, (double) wochenstundenSchueler);

		// Vorab-Berechnung der maximalen Wochenstunden pro Zusatzlehrer Map<LehrerID, MaxWochenstunden>
		final Map<Long, Integer> maxWochenstundenZusatz = new HashMap<>();
		for (final ReportingSchuelerLeistungsdaten ld : reportingSchuelerLeistungsdaten) {
			if (ld.zusatzLehrer() != null) {
				for (final ReportingLehrer lehrer : ld.zusatzLehrer()) {
					final Long zId = lehrer.id();
					final Double wStd = ld.wochenstundenLehrer().get(zId);
					final int currentWStd = (wStd == null) ? 0 : wStd.intValue();
					maxWochenstundenZusatz.merge(zId, currentWStd, Math::max);
				}
			}
		}

		for (final Long idZusatz : idsZusatzLehrer) {
			wochenstundenProLehrer.put(idZusatz, (double) maxWochenstundenZusatz.getOrDefault(idZusatz, 0));
		}

		return wochenstundenProLehrer;
	}

	/**
	 * Erstellt ReportingKlassenunterrichte aus den gruppierten Leistungsdaten.
	 *
	 * @param mapKlasseFachLehrerLeistungsdaten Die 3D-Map mit gruppierten Leistungsdaten nach Klasse, Fach und Lehrer.
	 * @param mapSchueler                       Die Map der Schüler
	 * @param mapIdLeistungsdatenIdSchueler     Die Map, die zu den IDs der Leistungsdaten die Schüler-ID liefert.
	 * @param mapKlasse                         Die Map der Klassen
	 * @param mapFach                           Die Map der Fächer
	 * @param mapLehrer                         Die Map der Lehrer
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
			final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten = new HashMap<>();
			mapSchuelerLeistungsdaten.putAll(leistungsdaten.stream().collect(Collectors.toMap(ld -> mapIdLeistungsdatenIdSchueler.get(ld.id()), ld -> ld)));

			result.add(new ProxyReportingKlassenunterricht(this.reportingRepository, klasse, fach, bewertenderLehrer, lehrer, wochenstundenProLehrer, schueler, wochenstundenSchueler,
					mapSchuelerLeistungsdaten));
		}

		return result;
	}


	/**
	 * Gruppiert Leistungsdaten nach Kurs, Fach und Lehrer und erstellt Maps für diese Entitäten.
	 *
	 * @param dtoSchuelerLeistungsdaten     Die zu verarbeitenden Leistungsdaten (3D-Map: SchülerID, AbschnittID, LeistungsdatenID)
	 * @param mapSchueler                   Die Map der verfügbaren Schüler
	 * @param mapIdLeistungsdatenIdSchueler Die Map, die zu den IDs der Leistungsdaten die Schüler-ID liefert.
	 * @param mapKursFachLehrerLeistungsdaten         Die zu befüllende 3D-Map für die Gruppierung
	 * @param mapKurse                      Die zu befüllende Map der Kurse
	 * @param mapFach                       Die zu befüllende Map der Fächer
	 * @param mapLehrer                     Die zu befüllende Map der Lehrer
	 */
	private void gruppiereLeistungsdatenNachKursFachLehrer(final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> dtoSchuelerLeistungsdaten,
			final Map<Long, ReportingSchueler> mapSchueler, final Map<Long, Long> mapIdLeistungsdatenIdSchueler,
			final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapKursFachLehrerLeistungsdaten,
			final Map<Long, ReportingKurs> mapKurse, final Map<Long, ReportingFach> mapFach, final Map<Long, ReportingLehrer> mapLehrer) {

		for (final LongArrayKey key : dtoSchuelerLeistungsdaten.keySet123()) {
			final ReportingSchueler schueler = mapSchueler.get(key.getKeyAt(0));
			if (schueler == null)
				continue;
			final ReportingSchuelerLernabschnitt lernabschnitt = schueler.lernabschnittById(key.getKeyAt(1));
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
	 * @param mapKurse                        Die Map der Kurse
	 * @param mapLehrer                       Die Map der Lehrer
	 * @param mapIdLeistungsdatenIdSchueler   Eine Map, die die ID des Schüler zur ID des Leistungsdateneintrags liefert.
	 *
	 * @return Eine Liste der erstellten ReportingKursunterrichte
	 */
	private List<ReportingKursunterricht> erstelleKursunterricht(final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapKursFachLehrerLeistungsdaten,
			final Map<Long, ReportingKurs> mapKurse, final Map<Long, ReportingLehrer> mapLehrer, final Map<Long, Long> mapIdLeistungsdatenIdSchueler) {

		final List<ReportingKursunterricht> result = new ArrayList<>();

		for (final LongArrayKey key123 : mapKursFachLehrerLeistungsdaten.keySet123()) {
			final long idKurs = key123.getKeyAt(0);
			final long idLehrer = key123.getKeyAt(2);

			final ReportingKurs kurs = mapKurse.get(idKurs);
			final ReportingLehrer bewertenderLehrer = mapLehrer.get(idLehrer);
			final Map<Long, ReportingSchuelerLeistungsdaten> mapSchuelerLeistungsdaten = new HashMap<>();
			final List<ReportingSchuelerLeistungsdaten> leistungsdaten = mapKursFachLehrerLeistungsdaten.get123(idKurs, key123.getKeyAt(1), idLehrer);
			mapSchuelerLeistungsdaten.putAll(leistungsdaten.stream().collect(Collectors.toMap(ld -> mapIdLeistungsdatenIdSchueler.get(ld.id()), ld -> ld)));

			if ((kurs != null) && (bewertenderLehrer != null)) {
				result.add(new ProxyReportingKursunterricht(this.reportingRepository, kurs, bewertenderLehrer, mapSchuelerLeistungsdaten));
			}
		}

		return result;
	}

}
