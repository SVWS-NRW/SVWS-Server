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
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.module.reporting.repositories.ReportingContext;
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

/**
 * Factory-Klasse zur Erstellung von Unterrichtsstrukturen (Klassen- und Kursunterricht) aus Leistungsdaten für das Reporting.
 * Diese Klasse kapselt die komplexe Logik zur Gruppierung und Aufbereitung von Unterrichtsdaten für einen Lehrer.
 */
public class ProxyReportingLehrerFactoryUnterricht {

	/** Context mit Parametern, Logger und Daten-Cache zur Report-Generierung. */
	@JsonIgnore
	private final ReportingContext reportingContext;

	/** Der Lehrer, dessen Unterricht von dieser Factory erzeugt werden soll. */
	private final ProxyReportingLehrer factoryLehrer;

	/**
	 * Erstellt eine neue Factory Instanz.
	 *
	 * @param reportingContext Das Repository mit Zugriff auf Datenbank und Caches.
	 * @param factoryLehrer              Der Lehrer, dessen Unterricht von dieser Factory erzeugt werden soll.
	 */
	public ProxyReportingLehrerFactoryUnterricht(final ReportingContext reportingContext, final ProxyReportingLehrer factoryLehrer) {
		this.reportingContext = reportingContext;
		this.factoryLehrer = factoryLehrer;
	}


	// ##### Öffentliche Erzeugung von Unterrichtsdaten #####

	/**
	 * Diese Methode erzeugt Klassenunterrichtsdaten für den Lehrer als Fachlehrer.
	 *
	 * @return Die Liste der Klassenunterrichte als Fachlehrer.
	 */
	public List<ReportingKlassenunterricht> klassenunterrichtAlsFachlehrer() {
		final List<Object[]> results = this.reportingContext.repositoryLehrer().leistungsdatenAlsFachlehrerKlassenunterricht(
				this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().id(), this.factoryLehrer.id());
		@SuppressWarnings("unchecked") final List<ReportingKlassenunterricht> result =
				(List<ReportingKlassenunterricht>) erstelleUnterrichtAusLeistungsdaten(results, false);
		return result;
	}

	/**
	 * Diese Methode erzeugt Klassenunterrichtsdaten für den Lehrer als Zusatzlehrer.
	 *
	 * @return Die Liste der Klassenunterrichte als Zusatzlehrer.
	 */
	public List<ReportingKlassenunterricht> klassenunterrichtAlsZusatzlehrer() {
		final List<Object[]> results = this.reportingContext.repositoryLehrer().leistungsdatenAlsZusatzlehrerKlassenunterricht(
				this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().id(), this.factoryLehrer.id());
		@SuppressWarnings("unchecked") final List<ReportingKlassenunterricht> result =
				(List<ReportingKlassenunterricht>) erstelleUnterrichtAusLeistungsdaten(results, false);
		return result;
	}

	/**
	 * Diese Methode erzeugt Kursunterrichtsdaten für den Lehrer als Fachlehrer.
	 *
	 * @return Die Liste der Kursunterrichte als Fachlehrer.
	 */
	public List<ReportingKursunterricht> kursunterrichtAlsFachlehrer() {
		final List<Object[]> results = this.reportingContext.repositoryLehrer().leistungsdatenAlsFachlehrerKursunterricht(
				this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().id(), this.factoryLehrer.id());
		@SuppressWarnings("unchecked") final List<ReportingKursunterricht> result =
				(List<ReportingKursunterricht>) erstelleUnterrichtAusLeistungsdaten(results, true);
		return result;
	}

	/**
	 * Diese Methode erzeugt Kursunterrichtsdaten für den Lehrer als Zusatzlehrer.
	 *
	 * @return Die Liste der Kursunterrichte als Zusatzlehrer.
	 */
	public List<ReportingKursunterricht> kursunterrichtAlsZusatzlehrer() {
		final List<Object[]> results = this.reportingContext.repositoryLehrer().leistungsdatenAlsZusatzlehrerKursunterricht(
				this.reportingContext.repositorySchule().auswahlSchuljahresabschnitt().id(), this.factoryLehrer.id());
		@SuppressWarnings("unchecked") final List<ReportingKursunterricht> result =
				(List<ReportingKursunterricht>) erstelleUnterrichtAusLeistungsdaten(results, true);
		return result;
	}


	// ##### Aufbereitung der Leistungsdaten #####

	/**
	 * Diese Methode erzeugt Unterrichtsdaten aus einer Liste von Leistungsdaten-Ergebnissen mit generischer Verarbeitung.
	 *
	 * @param results       Die Liste von Object[]-Arrays mit Leistungsdaten und Schüler-ID, aus denen die Unterrichte ermittelt werden.
	 * @param istKurs       True für Kursunterricht, False für Klassenunterricht.
	 *
	 * @return Die Liste der Unterrichte (ReportingKlassenunterricht oder ReportingKursunterricht).
	 */
	private List<?> erstelleUnterrichtAusLeistungsdaten(final List<Object[]> results, final boolean istKurs) {
		final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> dtoSchuelerLeistungsdaten = leistungsdatenErgebnisToListMap(results);

		// Erzeuge neue Maps, die im Folgenden gefüllt werden.
		final ListMap3DLongKeys<ReportingSchuelerLeistungsdaten> mapLerngruppeFachLehrerLeistungsdaten = new ListMap3DLongKeys<>();
		final Map<Long, Long> mapIdLeistungsdatenIdSchueler = new HashMap<>();
		final Map<Long, ReportingFach> mapFach = new HashMap<>();
		final Map<Long, ReportingLehrer> mapLehrer = new HashMap<>();

		// Erstelle eine Map der Schüler aus den Leistungsdaten
		final Map<Long, ReportingSchueler> mapSchueler =
				this.reportingContext.repositorySchueler().schueler(dtoSchuelerLeistungsdaten.keySet1().stream().toList()).stream()
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
	 * Wandelt eine Liste von Object[]-Ergebnissen aus einer Leistungsdatenabfrage in eine 3-dimensionale Map (ListMap3DLongKeys)
	 * mit Schüler-ID, Abschnitts-ID und Leistungsdaten-ID als Schlüssel um.
	 *
	 * @param results Die Liste der Ergebnisse, wobei jedes Object[] aus den Leistungsdaten ({@link DTOSchuelerLeistungsdaten}) und der Schüler-ID besteht.
	 *
	 * @return Eine 3-dimensionale Map (ListMap3DLongKeys) mit Schüler-IDs, Abschnitts-IDs, Leistungsdaten-IDs und Leistungsdaten.
	 */
	private static ListMap3DLongKeys<DTOSchuelerLeistungsdaten> leistungsdatenErgebnisToListMap(final List<Object[]> results) {
		final ListMap3DLongKeys<DTOSchuelerLeistungsdaten> listmapLeistungsdaten = new ListMap3DLongKeys<>();

		if ((results == null) || results.isEmpty()) {
			return listmapLeistungsdaten;
		}

		for (final Object[] row : results) {
			final DTOSchuelerLeistungsdaten ld = (DTOSchuelerLeistungsdaten) row[0];
			final Long schuelerId = (Long) row[1];

			if ((schuelerId != null) && (ld != null)) {
				listmapLeistungsdaten.add(schuelerId, ld.Abschnitt_ID, ld.ID, ld);
			}
		}

		return listmapLeistungsdaten;
	}


	// ##### Klassenunterricht: Gruppierung und Erzeugung #####

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
			final ReportingSchuelerLernabschnitt lernabschnitt = (schueler == null) ? null : schueler.lernabschnittById(key.getKeyAt(1));
			final ReportingKlasse klasse = (lernabschnitt == null) ? null : lernabschnitt.klasse();
			final ReportingSchuelerLeistungsdaten reportingSchuelerLeistungsdaten =
					(lernabschnitt == null) ? null : lernabschnitt.leistungsdatenZurId(key.getKeyAt(2));
			final ReportingFach fach = (reportingSchuelerLeistungsdaten == null) ? null : reportingSchuelerLeistungsdaten.fach();
			final ReportingLehrer fachlehrer = (reportingSchuelerLeistungsdaten == null) ? null : reportingSchuelerLeistungsdaten.fachlehrer();

			if ((schueler != null) && (lernabschnitt != null) && (klasse != null) && (fach != null) && (fachlehrer != null)) {
				mapIdLeistungsdatenIdSchueler.putIfAbsent(reportingSchuelerLeistungsdaten.id(), schueler.id());
				mapKlasseFachLehrerLeistungsdaten.add(klasse.id(), fach.id(), fachlehrer.id(), reportingSchuelerLeistungsdaten);
				mapKlasse.putIfAbsent(klasse.id(), klasse);
				mapFach.putIfAbsent(fach.id(), fach);
				mapLehrer.putIfAbsent(fachlehrer.id(), fachlehrer);
			}
		}
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

			result.add(new ProxyReportingKlassenunterricht(this.reportingContext, klasse, fach, bewertenderLehrer, lehrer, wochenstundenProLehrer, schueler,
					wochenstundenSchueler,
					mapSchuelerLeistungsdaten));
		}

		result.sort(ReportingKlassenunterricht.SORTIERUNG.comparatorStandard());
		return result;
	}


	// ##### Kursunterricht: Gruppierung und Erzeugung #####

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
			final ReportingSchuelerLernabschnitt lernabschnitt = (schueler == null) ? null : schueler.lernabschnittById(key.getKeyAt(1));
			final ReportingSchuelerLeistungsdaten reportingSchuelerLeistungsdaten =
					(lernabschnitt == null) ? null : lernabschnitt.leistungsdatenZurId(key.getKeyAt(2));
			final ReportingKurs kurs = (reportingSchuelerLeistungsdaten == null) ? null : reportingSchuelerLeistungsdaten.kurs();
			final ReportingFach fach = (reportingSchuelerLeistungsdaten == null) ? null : reportingSchuelerLeistungsdaten.fach();
			final ReportingLehrer fachlehrer = (reportingSchuelerLeistungsdaten == null) ? null : reportingSchuelerLeistungsdaten.fachlehrer();

			if ((schueler != null) && (lernabschnitt != null) && (kurs != null) && (fach != null) && (fachlehrer != null)) {
				mapIdLeistungsdatenIdSchueler.putIfAbsent(reportingSchuelerLeistungsdaten.id(), schueler.id());
				mapKursFachLehrerLeistungsdaten.add(kurs.id(), fach.id(), fachlehrer.id(), reportingSchuelerLeistungsdaten);
				mapKurse.putIfAbsent(kurs.id(), kurs);
				mapFach.putIfAbsent(fach.id(), fach);
				mapLehrer.putIfAbsent(fachlehrer.id(), fachlehrer);
			}
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
				result.add(new ProxyReportingKursunterricht(this.reportingContext, kurs, bewertenderLehrer, mapSchuelerLeistungsdaten));
			}
		}

		result.sort(ReportingKursunterricht.SORTIERUNG.comparatorStandard());
		return result;
	}


	// ##### Hilfsmethoden für Lehrkräfte und Wochenstunden #####

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
				this.reportingContext.repositoryLehrer().lehrer(fehlende).forEach(l -> mapLehrer.putIfAbsent(l.id(), l));
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
		if (fachlehrer != null) {
			lehrer.add(fachlehrer);
		}
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

}
