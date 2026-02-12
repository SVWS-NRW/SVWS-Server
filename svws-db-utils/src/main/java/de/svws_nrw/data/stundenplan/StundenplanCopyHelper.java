package de.svws_nrw.data.stundenplan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.stundenplan.Stundenplan;
import de.svws_nrw.core.logger.LogLevel;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanAufsichtsbereich;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanKalenderwochenZuordnung;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichten;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenaufsichtenBereiche;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeit;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanPausenzeitKlassenzuordnung;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanSchienen;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterricht;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtKlasse;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtLehrer;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtRaum;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanUnterrichtSchiene;
import de.svws_nrw.db.dto.current.schild.stundenplan.DTOStundenplanZeitraster;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

final class StundenplanCopyHelper {

	private StundenplanCopyHelper() {
		// Hidden constructor
	}

	/**
	 * Kopiert alle Aufsichtsbereiche eines bestehenden Stundenplans in den neuen Stundenplan.
	 *
	 * @param conn               die Datenbank-Verbindung
	 * @param idStundenplanSource die ID des Stundenplans, dessen Aufsichtsbereiche kopiert werden sollen
	 * @param stundenplanNeu     der neu erzeugte Stundenplan, in den kopiert wird
	 *
	 * @return eine Map von alten zu neuen Aufsichtsbereich-IDs
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static Map<Long, Long> kopiereAufsichtsbereiche(final DBEntityManager conn, final long idStundenplanSource, final Stundenplan stundenplanNeu)
			throws ApiOperationException {
		final List<DTOStundenplanAufsichtsbereich> listAlt = DataStundenplanAufsichtsbereiche.getDTOsByStundenplanid(conn, idStundenplanSource);
		final List<DTOStundenplanAufsichtsbereich> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanAufsichtsbereich.class);
		final Map<Long, Long> mapAltNeu = new HashMap<>();
		for (final DTOStundenplanAufsichtsbereich alt : listAlt) {
			final DTOStundenplanAufsichtsbereich neu = new DTOStundenplanAufsichtsbereich(nextID++, stundenplanNeu.id, alt.Kuerzel, alt.Beschreibung);
			listNeu.add(neu);
			mapAltNeu.put(alt.ID, neu.ID);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Aufsichtsbereiche.");
		return mapAltNeu;
	}

	/**
	 * Kopiert die Kalenderwochen-Zuordnungen des angegebenen Stundenplans in den neuen Stundenplan.
	 *
	 * @param conn               die Datenbank-Verbindung
	 * @param idStundenplanSource die ID des Quell-Stundenplans
	 * @param stundenplanNeu     der neue Stundenplan
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static void kopiereKalenderwochenZuordnungen(final DBEntityManager conn, final long idStundenplanSource, final Stundenplan stundenplanNeu)
			throws ApiOperationException {
		final List<DTOStundenplanKalenderwochenZuordnung> listAlt = DataStundenplanKalenderwochenzuordnung.getDTOsByStundenplanid(conn, idStundenplanSource);
		final List<DTOStundenplanKalenderwochenZuordnung> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanKalenderwochenZuordnung.class);
		for (final DTOStundenplanKalenderwochenZuordnung alt : listAlt) {
			final DTOStundenplanKalenderwochenZuordnung neu =
					new DTOStundenplanKalenderwochenZuordnung(nextID++, stundenplanNeu.id, alt.Jahr, alt.KW, alt.Wochentyp);
			listNeu.add(neu);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Kalenderwochenzuordnungen.");
	}

	/**
	 * Kopiert die Pausenzeiten eines Stundenplans und erzeugt eine Zuordnung
	 * von alten zu neuen Pausenzeit-IDs.
	 *
	 * @param conn               die Datenbank-Verbindung
	 * @param idStundenplanSource die ID des Quell-Stundenplans
	 * @param stundenplanNeu     der neue Stundenplan
	 *
	 * @return eine Map von alten zu neuen Pausenzeiten-IDs
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static Map<Long, Long> kopierePausenzeiten(final DBEntityManager conn, final long idStundenplanSource, final Stundenplan stundenplanNeu)
			throws ApiOperationException {
		final List<DTOStundenplanPausenzeit> listAlt =
				DataStundenplanPausenzeiten.getDTOsByStundenplanid(conn, idStundenplanSource);
		final List<DTOStundenplanPausenzeit> listNeu = new ArrayList<>();
		final Map<Long, Long> mapAltNeu = new HashMap<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanPausenzeit.class);
		for (final DTOStundenplanPausenzeit alt : listAlt) {
			final DTOStundenplanPausenzeit neu = new DTOStundenplanPausenzeit(
					nextID++,
					stundenplanNeu.id,
					alt.Tag,
					alt.Beginn,
					alt.Ende,
					alt.Bezeichnung);
			listNeu.add(neu);
			mapAltNeu.put(alt.ID, neu.ID);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Pausenzeiten.");
		return mapAltNeu;
	}

	/**
	 * Kopiert die Pausenaufsichten basierend auf dem Mapping der Pausenzeiten.
	 *
	 * @param conn             die Datenbank-Verbindung
	 * @param pausenzeitMapping Mapping alte → neue Pausenzeit-IDs
	 * @param stundenplanNeu   der neue Stundenplan (für Gültigkeitsbeginn bei Lehrer-Abgangsprüfung)
	 * @param logger           der Logger für Warn- und Statusmeldungen
	 *
	 * @return eine Map von alten zu neuen Pausenaufsicht-IDs
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static Map<Long, Long> kopierePausenaufsichten(final DBEntityManager conn,
			final Map<Long, Long> pausenzeitMapping, final Stundenplan stundenplanNeu, final Logger logger)
			throws ApiOperationException {
		if (pausenzeitMapping.isEmpty())
			return new HashMap<>();
		final Map<Long, String> lehrerLabelCache = new HashMap<>();
		final List<DTOStundenplanPausenaufsichten> listAlt = conn.queryList(DTOStundenplanPausenaufsichten.QUERY_LIST_BY_PAUSENZEIT_ID,
				DTOStundenplanPausenaufsichten.class, pausenzeitMapping.keySet());
		final List<DTOStundenplanPausenaufsichten> listNeu = new ArrayList<>(listAlt.size());
		final Map<Long, Long> mapAltNeu = new HashMap<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanPausenaufsichten.class);
		final LinkedHashSet<String> skippedLehrer = new LinkedHashSet<>();
		for (final DTOStundenplanPausenaufsichten alt : listAlt) {
			final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, alt.Lehrer_ID);
			if ((lehrer == null) || ((lehrer.DatumAbgang != null) && (lehrer.DatumAbgang.compareTo(stundenplanNeu.gueltigAb) < 0))) {
				skippedLehrer.add(lehrerLabel(conn, alt.Lehrer_ID, lehrerLabelCache));
				continue;
			}
			final DTOStundenplanPausenaufsichten neu = new DTOStundenplanPausenaufsichten(
					nextID++,
					pausenzeitMapping.get(alt.Pausenzeit_ID),
					alt.Lehrer_ID);
			listNeu.add(neu);
			mapAltNeu.put(alt.ID, neu.ID);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Pausenaufsichten.");
		if ((logger != null) && (!skippedLehrer.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Pausenaufsichten übersprungen (Lehrer nicht im Zielabschnitt aktiv): " + String.join(", ", skippedLehrer));
		return mapAltNeu;
	}

	/**
	 * Kopiert Klassenzuordnungen zu Pausenzeiten. Bei kopierten Zeiträumen aus
	 * einem Vorgänger-Schuljahresabschnitt erfolgt optional eine Neuzuordnung
	 * der Klassen anhand des Zielabschnitts.
	 *
	 * @param conn             die Datenbank-Verbindung
	 * @param pausenzeitMapping Mapping alte → neue Pausenzeit-IDs
	 * @param abschnittNeu      ID des Ziel-Schuljahresabschnitts oder {@code null}, falls identisch
	 * @param logger           der Logger für Warn- und Statusmeldungen
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static void kopierePausenzeitenKlassenzuordnungen(final DBEntityManager conn,
			final Map<Long, Long> pausenzeitMapping, final Long abschnittNeu, final Logger logger)
			throws ApiOperationException {
		if (pausenzeitMapping.isEmpty())
			return;
		final Map<Long, String> klasseLabelCache = new HashMap<>();
		final List<DTOStundenplanPausenzeitKlassenzuordnung> listAlt = conn.queryList(DTOStundenplanPausenzeitKlassenzuordnung.QUERY_LIST_BY_PAUSENZEIT_ID,
				DTOStundenplanPausenzeitKlassenzuordnung.class, pausenzeitMapping.keySet());
		final Map<Long, Long> klassenMappings = (abschnittNeu != null) ? getKlassenMappings(conn,
				listAlt.stream().map(u -> u.Klassen_ID).collect(Collectors.toSet()),
				abschnittNeu) : null;
		final List<DTOStundenplanPausenzeitKlassenzuordnung> listNeu = new ArrayList<>(listAlt.size());
		long nextID = conn.transactionGetNextID(DTOStundenplanPausenzeitKlassenzuordnung.class);
		final LinkedHashSet<String> skippedKlassen = new LinkedHashSet<>();
		for (final DTOStundenplanPausenzeitKlassenzuordnung alt : listAlt) {
			final Long klasseIdNeu = (klassenMappings == null)
					? alt.Klassen_ID
					: klassenMappings.get(alt.Klassen_ID);
			if (klasseIdNeu == null) {
				skippedKlassen.add(klasseLabel(conn, alt.Klassen_ID, klasseLabelCache));
				continue;
			}
			final DTOStundenplanPausenzeitKlassenzuordnung neu = new DTOStundenplanPausenzeitKlassenzuordnung(
					nextID++,
					pausenzeitMapping.get(alt.Pausenzeit_ID),
					klasseIdNeu);
			listNeu.add(neu);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Pausenaufsichten.");
		if ((logger != null) && (!skippedKlassen.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Pausenzeiten-Klassenzuordnungen übersprungen (Klasse existiert nicht): " + String.join(", ", skippedKlassen));
	}

	/**
	 * Kopiert die Zuordnungen von Pausenaufsichten zu Aufsichtsbereichen.
	 *
	 * @param conn                 die Datenbank-Verbindung
	 * @param pausenaufsichtMapping Mapping alte → neue Pausenaufsicht-IDs
	 * @param aufsichtsbereichMapping Mapping alte → neue Aufsichtsbereich-IDs
	 * @param logger               der Logger für Warn- und Statusmeldungen
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static void kopierePausenaufsichtenBereiche(final DBEntityManager conn,
			final Map<Long, Long> pausenaufsichtMapping, final Map<Long, Long> aufsichtsbereichMapping, final Logger logger)
			throws ApiOperationException {
		if (aufsichtsbereichMapping.isEmpty() || pausenaufsichtMapping.isEmpty())
			return;
		final List<DTOStundenplanPausenaufsichtenBereiche> listAlt = conn.queryList(DTOStundenplanPausenaufsichtenBereiche.QUERY_LIST_BY_AUFSICHTSBEREICH_ID,
				DTOStundenplanPausenaufsichtenBereiche.class, aufsichtsbereichMapping.keySet());
		final List<DTOStundenplanPausenaufsichtenBereiche> listNeu = new ArrayList<>(listAlt.size());
		long nextID = conn.transactionGetNextID(DTOStundenplanPausenaufsichtenBereiche.class);
		final LinkedHashSet<String> skippedMappings = new LinkedHashSet<>();
		for (final DTOStundenplanPausenaufsichtenBereiche alt : listAlt) {
			final Long pausenaufsichtIdNeu = pausenaufsichtMapping.get(alt.Pausenaufsicht_ID);
			final Long aufsichtsbereichIdNeu = aufsichtsbereichMapping.get(alt.Aufsichtsbereich_ID);
			if ((pausenaufsichtIdNeu == null) || (aufsichtsbereichIdNeu == null)) {
				skippedMappings.add("Pausenaufsicht " + alt.Pausenaufsicht_ID + " / Bereich " + alt.Aufsichtsbereich_ID);
				continue;
			}
			final DTOStundenplanPausenaufsichtenBereiche neu = new DTOStundenplanPausenaufsichtenBereiche(
					nextID++,
					pausenaufsichtIdNeu,
					aufsichtsbereichIdNeu,
					alt.Wochentyp);
			listNeu.add(neu);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Pausenaufsichts-Bereiche.");
		if ((logger != null) && (!skippedMappings.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Pausenaufsichten-Bereiche übersprungen (Zuordnung existiert nicht): " + String.join(", ", skippedMappings));
	}

	/**
	 * Kopiert die Räume eines Stundenplans und erzeugt eine Zuordnung
	 * von alten zu neuen Raum-IDs.
	 *
	 * @param conn               die Datenbank-Verbindung
	 * @param idStundenplanSource die ID des Quell-Stundenplans
	 * @param stundenplanNeu     der neue Stundenplan
	 *
	 * @return eine Map von alten zu neuen Raum-IDs
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static Map<Long, Long> kopiereRaeume(final DBEntityManager conn, final long idStundenplanSource, final Stundenplan stundenplanNeu)
			throws ApiOperationException {
		final List<DTOStundenplanRaum> listAlt =
				DataStundenplanRaeume.getDTOsByStundenplanid(conn, idStundenplanSource);
		final List<DTOStundenplanRaum> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanRaum.class);
		final Map<Long, Long> mapAltNeu = new HashMap<>();
		for (final DTOStundenplanRaum alt : listAlt) {
			final DTOStundenplanRaum neu = new DTOStundenplanRaum(
					nextID++,
					stundenplanNeu.id,
					alt.Kuerzel,
					alt.Beschreibung,
					alt.Groesse);
			listNeu.add(neu);
			mapAltNeu.put(alt.ID, neu.ID);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Räume.");
		return mapAltNeu;
	}

	/**
	 * Kopiert die Schienen eines Stundenplans und erzeugt eine Zuordnung
	 * von alten zu neuen Schienen-IDs.
	 *
	 * @param conn               die Datenbank-Verbindung
	 * @param idStundenplanSource die ID des Quell-Stundenplans
	 * @param stundenplanNeu     der neue Stundenplan
	 *
	 * @return eine Map von alten zu neuen Schienen-IDs
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static Map<Long, Long> kopiereSchienen(final DBEntityManager conn, final long idStundenplanSource, final Stundenplan stundenplanNeu)
			throws ApiOperationException {
		final List<DTOStundenplanSchienen> listAlt =
				DataStundenplanSchienen.getDTOsByStundenplanid(conn, idStundenplanSource);
		final List<DTOStundenplanSchienen> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanSchienen.class);
		final Map<Long, Long> mapAltNeu = new HashMap<>();
		for (final DTOStundenplanSchienen alt : listAlt) {
			final DTOStundenplanSchienen neu = new DTOStundenplanSchienen(
					nextID++,
					stundenplanNeu.id,
					alt.Nummer,
					alt.Bezeichnung);
			neu.Jahrgang_ID = alt.Jahrgang_ID;
			listNeu.add(neu);
			mapAltNeu.put(alt.ID, neu.ID);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Schienen.");
		return mapAltNeu;
	}

	/**
	 * Kopiert das Zeitraster eines Stundenplans und erzeugt eine Zuordnung
	 * von alten zu neuen Zeitraster-IDs.
	 *
	 * @param conn               die Datenbank-Verbindung
	 * @param idStundenplanSource die ID des Quell-Stundenplans
	 * @param stundenplanNeu     der neue Stundenplan
	 *
	 * @return eine Map von alten zu neuen Zeitraster-IDs
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static Map<Long, Long> kopiereZeitraster(final DBEntityManager conn, final long idStundenplanSource, final Stundenplan stundenplanNeu)
			throws ApiOperationException {
		final List<DTOStundenplanZeitraster> listAlt =
				conn.queryList(DTOStundenplanZeitraster.QUERY_BY_STUNDENPLAN_ID, DTOStundenplanZeitraster.class, idStundenplanSource);
		final List<DTOStundenplanZeitraster> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanZeitraster.class);
		final Map<Long, Long> mapAltNeu = new HashMap<>();
		for (final DTOStundenplanZeitraster alt : listAlt) {
			final DTOStundenplanZeitraster neu = new DTOStundenplanZeitraster(
					nextID++,
					stundenplanNeu.id,
					alt.Tag,
					alt.Stunde,
					alt.Beginn,
					alt.Ende);
			listNeu.add(neu);
			mapAltNeu.put(alt.ID, neu.ID);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Zeitraster.");
		return mapAltNeu;
	}

	/**
	 * Ermittelt für Kurse eines kopierten Stundenplans die passenden Kurse
	 * des Ziel-Schuljahresabschnitts, basierend auf Kurzbezeichnung und identischer Jahrgangsmenge.
	 *
	 * @param conn        die Datenbank-Verbindung
	 * @param kursIdsAlt  die IDs der alten Kurse, die gemappt werden sollen
	 * @param abschnittNeu die ID des Ziel-Schuljahresabschnitts
	 *
	 * @return eine Map von alten zu neuen Kurs-IDs (nur wenn eindeutiges Matching möglich)
	 */
	static Map<Long, Long> getKursMappings(final DBEntityManager conn,
			final Collection<Long> kursIdsAlt, final long abschnittNeu) {
		if (kursIdsAlt.isEmpty())
			return new HashMap<>();
		final Map<Long, DTOKurs> kurseAlleAlt = conn.queryList(DTOKurs.QUERY_LIST_BY_ID,
				DTOKurs.class, kursIdsAlt).stream()
				.collect(Collectors.toMap(k -> k.ID, k -> k));
		final List<DTOKurs> kurseNeuListe = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID,
				DTOKurs.class, abschnittNeu);
		final Map<String, List<DTOKurs>> kurseNeuByKurzBez = kurseNeuListe.stream()
				.collect(Collectors.groupingBy(k -> k.KurzBez));
		final Map<Long, Long> kursMappings = new HashMap<>();
		for (final DTOKurs kursAlt : kurseAlleAlt.values()) {
			final String kurzbez = kursAlt.KurzBez;
			if (kurzbez == null)
				continue;
			final List<DTOKurs> kandidatenNeu = kurseNeuByKurzBez.get(kurzbez);
			if ((kandidatenNeu == null) || kandidatenNeu.isEmpty())
				continue;
			final Set<Long> jahrgaengeAlt = convertKursJahrgaenge(kursAlt);
			DTOKurs kursNeu = null;
			if (!jahrgaengeAlt.isEmpty())
				for (final DTOKurs kandidat : kandidatenNeu) {
					final Set<Long> jahrgaengeNeu = convertKursJahrgaenge(kandidat);
					if (jahrgaengeNeu.equals(jahrgaengeAlt)) {
						kursNeu = kandidat;
						break;
					}
				}
			if ((kursNeu == null) && jahrgaengeAlt.isEmpty() && (kandidatenNeu.size() == 1))
				kursNeu = kandidatenNeu.getFirst();
			if (kursNeu != null)
				kursMappings.put(kursAlt.ID, kursNeu.ID);
		}
		return kursMappings;
	}

	/**
	 * Wandelt die Jahrgangsinformationen eines Kurses (Jahrgang_ID und Jahrgaenge-CSV)
	 * in eine Menge von Jahrgangs-IDs um.
	 *
	 * @param kurs der Kurs-Datensatz
	 *
	 * @return Menge von Jahrgangs-IDs
	 */
	static Set<Long> convertKursJahrgaenge(final DTOKurs kurs) {
		final Set<Long> result = new HashSet<>();
		if (kurs.Jahrgang_ID != null)
			result.add(kurs.Jahrgang_ID);
		if (kurs.Jahrgaenge != null)
			for (final String jahrgang : kurs.Jahrgaenge.split(","))
				if (jahrgang.matches("^\\d+$"))
					result.add(Long.parseLong(jahrgang));
		return result;
	}

	/**
	 * Ermittelt für Klassen eines kopierten Stundenplans die passenden Klassen
	 * des Ziel-Schuljahresabschnitts auf Basis der Klassenbezeichnung.
	 *
	 * @param conn          die Datenbank-Verbindung
	 * @param klassenIdsAlt die IDs der alten Klassen
	 * @param abschnittNeu  die ID des Ziel-Schuljahresabschnitts
	 *
	 * @return eine Map von alten zu neuen Klassen-IDs
	 */
	static Map<Long, Long> getKlassenMappings(final DBEntityManager conn, final Collection<Long> klassenIdsAlt, final long abschnittNeu) {
		if (klassenIdsAlt.isEmpty())
			return new HashMap<>();
		final Map<Long, DTOKlassen> klassenAlleAlt = conn.queryList(DTOKlassen.QUERY_LIST_BY_ID,
				DTOKlassen.class, klassenIdsAlt).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
		final Map<String, DTOKlassen> klassenAlleNeu = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID,
				DTOKlassen.class, abschnittNeu).stream().collect(Collectors.toMap(k -> k.Klasse, k -> k, (a, b) -> a));
		final Map<Long, Long> klassenMappings = new HashMap<>();
		for (final DTOKlassen klasseAlt : klassenAlleAlt.values()) {
			final DTOKlassen klasseNeu = klassenAlleNeu.get(klasseAlt.Klasse);
			if (klasseNeu != null)
				klassenMappings.put(klasseAlt.ID, klasseNeu.ID);
		}
		return klassenMappings;
	}

	/**
	 * Kopiert alle Unterrichtsstunden eines Stundenplans. Falls ein neuer Schuljahresabschnitt
	 * erreicht wird, werden die Kurse über {@link #getKursMappings} neu zugeordnet.
	 * Stunden ohne passende Kurszuordnung werden ausgelassen.
	 *
	 * @param conn            die Datenbank-Verbindung
	 * @param zeitrasterMapping Mapping alte → neue Zeitraster-IDs
	 * @param abschnittNeu     ID des Ziel-Schuljahresabschnitts oder {@code null}
	 * @param logger          der Logger für Warn- und Statusmeldungen
	 *
	 * @return eine Map von alten zu neuen Unterrichts-IDs
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static Map<Long, DTOStundenplanUnterricht> kopiereUnterrichte(final DBEntityManager conn, final Map<Long, Long> zeitrasterMapping, final Long abschnittNeu,
			final Logger logger)
			throws ApiOperationException {
		if (zeitrasterMapping.isEmpty())
			return new HashMap<>();
		final List<DTOStundenplanUnterricht> listAlt =
				conn.queryList(DTOStundenplanUnterricht.QUERY_LIST_BY_ZEITRASTER_ID, DTOStundenplanUnterricht.class, zeitrasterMapping.keySet());
		final Map<Long, String> kursLabelCache = new HashMap<>();
		final Map<Long, String> klasseLabelCache = new HashMap<>();
		final Map<Long, String> fachLabelCache = new HashMap<>();
		final Map<Long, String> jahrgangLabelCache = new HashMap<>();
		final Map<Long, Long> kursMappings = (abschnittNeu != null) ? getKursMappings(conn,
				listAlt.stream().map(u -> u.Kurs_ID).filter(Objects::nonNull).collect(Collectors.toSet()),
				abschnittNeu) : null;
		long nextID = conn.transactionGetNextID(DTOStundenplanUnterricht.class);
		final Map<Long, DTOStundenplanUnterricht> mapAltNeu = new HashMap<>();
		final LinkedHashSet<String> skippedKurse = new LinkedHashSet<>();
		final LinkedHashSet<String> skippedZeitraster = new LinkedHashSet<>();
		for (final DTOStundenplanUnterricht alt : listAlt) {
			final Long zeitrasterIdNeu = zeitrasterMapping.get(alt.Zeitraster_ID);
			if (zeitrasterIdNeu == null) {
				skippedZeitraster.add(unterrichtLabel(conn, alt, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache));
				continue;
			}
			final DTOStundenplanUnterricht neu = new DTOStundenplanUnterricht(
					nextID++,
					zeitrasterIdNeu,
					alt.Wochentyp,
					alt.Fach_ID);
			neu.Kurs_ID = ((kursMappings == null) || (alt.Kurs_ID == null))
					? alt.Kurs_ID
					: kursMappings.get(alt.Kurs_ID);
			if ((alt.Kurs_ID != null) && (kursMappings != null) && (neu.Kurs_ID == null)) {
				skippedKurse.add(unterrichtLabel(conn, alt, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache));
				continue;
			}
			mapAltNeu.put(alt.ID, neu);
		}
		if (!conn.transactionPersistAll(mapAltNeu.values()))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Unterrichte.");
		if ((logger != null) && (!skippedZeitraster.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichte übersprungen (Zeitraster existiert nicht): " + String.join(", ", skippedZeitraster));
		if ((logger != null) && (!skippedKurse.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichte übersprungen (Kurs existiert nicht): " + String.join(", ", skippedKurse));
		return mapAltNeu;
	}

	private static String unterrichtLabel(final DBEntityManager conn, final DTOStundenplanUnterricht unterricht,
			final Map<Long, String> kursLabelCache, final Map<Long, String> klasseLabelCache, final Map<Long, String> fachLabelCache,
			final Map<Long, String> jahrgangLabelCache) {
		if (unterricht.Kurs_ID != null)
			return kursLabelById(conn, unterricht.Kurs_ID, kursLabelCache, jahrgangLabelCache);
		final List<DTOStundenplanUnterrichtKlasse> listKlassen = conn.queryList(
				DTOStundenplanUnterrichtKlasse.QUERY_BY_UNTERRICHT_ID, DTOStundenplanUnterrichtKlasse.class, unterricht.ID);
		final List<String> klassen = new ArrayList<>();
		for (final DTOStundenplanUnterrichtKlasse uk : listKlassen) {
			final long id = uk.Klasse_ID;
			klassen.add(klasseLabel(conn, id, klasseLabelCache));
		}
		final String fachLabel = fachLabel(conn, unterricht.Fach_ID, fachLabelCache);
		if (!klassen.isEmpty())
			return String.join("/", klassen) + " - " + fachLabel;
		return "Unterricht " + unterricht.ID + " - " + fachLabel;
	}

	private static String unterrichtLabelById(final DBEntityManager conn, final long unterrichtId,
			final Map<Long, String> kursLabelCache, final Map<Long, String> klasseLabelCache, final Map<Long, String> fachLabelCache,
			final Map<Long, String> jahrgangLabelCache) {
		final DTOStundenplanUnterricht unterricht = conn.queryByKey(DTOStundenplanUnterricht.class, unterrichtId);
		if (unterricht == null)
			return "Unterricht " + unterrichtId;
		return unterrichtLabel(conn, unterricht, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache);
	}

	private static String kursLabelById(final DBEntityManager conn, final long kursId,
			final Map<Long, String> kursLabelCache, final Map<Long, String> jahrgangLabelCache) {
		String label = kursLabelCache.get(kursId);
		if (label != null)
			return label;
		final DTOKurs kurs = conn.queryByKey(DTOKurs.class, kursId);
		if (kurs == null) {
			label = "Kurs " + kursId;
			kursLabelCache.put(kursId, label);
			return label;
		}
		final String kurzbez = kurs.KurzBez;
		label = (kurzbez != null) ? kurzbez : ("Kurs " + kursId);
		final Set<Long> jahrgangIds = convertKursJahrgaenge(kurs);
		if (!jahrgangIds.isEmpty()) {
			final List<String> jgLabels = new ArrayList<>();
			for (final Long id : jahrgangIds)
				jgLabels.add(jahrgangLabel(conn, id, jahrgangLabelCache));
			label += " (Jg: " + String.join(",", jgLabels) + ")";
		}
		kursLabelCache.put(kursId, label);
		return label;
	}

	private static String jahrgangLabel(final DBEntityManager conn, final long jahrgangId, final Map<Long, String> cache) {
		String label = cache.get(jahrgangId);
		if (label != null)
			return label;
		final DTOJahrgang jg = conn.queryByKey(DTOJahrgang.class, jahrgangId);
		if (jg == null) {
			label = "Jg " + jahrgangId;
		} else if ((jg.Kurzbezeichnung != null) && !jg.Kurzbezeichnung.isBlank()) {
			label = jg.Kurzbezeichnung;
		} else if ((jg.InternKrz != null) && !jg.InternKrz.isBlank()) {
			label = jg.InternKrz;
		} else if ((jg.ASDBezeichnung != null) && !jg.ASDBezeichnung.isBlank()) {
			label = jg.ASDBezeichnung;
		} else {
			label = "Jg " + jahrgangId;
		}
		cache.put(jahrgangId, label);
		return label;
	}

	private static String klasseLabel(final DBEntityManager conn, final long klasseId, final Map<Long, String> cache) {
		String label = cache.get(klasseId);
		if (label != null)
			return label;
		final DTOKlassen klasse = conn.queryByKey(DTOKlassen.class, klasseId);
		label = (klasse != null) ? klasse.Klasse : ("Klasse " + klasseId);
		cache.put(klasseId, label);
		return label;
	}

	private static String fachLabel(final DBEntityManager conn, final long fachId, final Map<Long, String> cache) {
		String label = cache.get(fachId);
		if (label != null)
			return label;
		final DTOFach fach = conn.queryByKey(DTOFach.class, fachId);
		label = (fach != null) ? fach.Kuerzel : ("Fach " + fachId);
		cache.put(fachId, label);
		return label;
	}

	private static String lehrerLabel(final DBEntityManager conn, final long lehrerId, final Map<Long, String> cache) {
		String label = cache.get(lehrerId);
		if (label != null)
			return label;
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrerId);
		label = (lehrer != null) ? lehrer.Kuerzel : ("Lehrer " + lehrerId);
		cache.put(lehrerId, label);
		return label;
	}

	private static String raumLabel(final DBEntityManager conn, final long raumId, final Map<Long, String> cache) {
		String label = cache.get(raumId);
		if (label != null)
			return label;
		final DTOStundenplanRaum raum = conn.queryByKey(DTOStundenplanRaum.class, raumId);
		label = (raum != null) ? raum.Kuerzel : ("Raum " + raumId);
		cache.put(raumId, label);
		return label;
	}

	private static String schieneLabel(final DBEntityManager conn, final long schieneId, final Map<Long, String> cache,
			final Map<Long, String> jahrgangLabelCache) {
		String label = cache.get(schieneId);
		if (label != null)
			return label;
		final DTOStundenplanSchienen schiene = conn.queryByKey(DTOStundenplanSchienen.class, schieneId);
		if (schiene == null) {
			label = "Schiene " + schieneId;
		} else {
			final String base = (schiene.Bezeichnung != null) ? schiene.Bezeichnung : ("Schiene " + schiene.Nummer);
			if (schiene.Jahrgang_ID != null)
				label = base + " (" + jahrgangLabel(conn, schiene.Jahrgang_ID, jahrgangLabelCache) + ")";
			else
				label = base;
		}
		cache.put(schieneId, label);
		return label;
	}

	/**
	 * Kopiert die Klassenzuordnungen zu Unterrichtsstunden und weist optional
	 * Klassen eines neuen Schuljahresabschnitts zu.
	 *
	 * @param conn              die Datenbank-Verbindung
	 * @param unterrichteMapping Mapping alte → neue Unterrichts-IDs
	 * @param abschnittNeu       ID des Ziel-Schuljahresabschnitts oder {@code null}
	 * @param logger            der Logger für Warn- und Statusmeldungen
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static void kopiereUnterrichteKlassen(final DBEntityManager conn, final Map<Long, DTOStundenplanUnterricht> unterrichteMapping, final Long abschnittNeu,
			final Logger logger)
			throws ApiOperationException {
		if (unterrichteMapping.isEmpty())
			return;
		final Map<Long, String> kursLabelCache = new HashMap<>();
		final Map<Long, String> klasseLabelCache = new HashMap<>();
		final Map<Long, String> fachLabelCache = new HashMap<>();
		final Map<Long, String> jahrgangLabelCache = new HashMap<>();
		final List<DTOStundenplanUnterrichtKlasse> listAlt =
				conn.queryList(DTOStundenplanUnterrichtKlasse.QUERY_LIST_BY_UNTERRICHT_ID, DTOStundenplanUnterrichtKlasse.class, unterrichteMapping.keySet());
		final Map<Long, Long> klassenMappings = (abschnittNeu != null) ? getKlassenMappings(conn,
				listAlt.stream().map(u -> u.Klasse_ID).collect(Collectors.toSet()),
				abschnittNeu) : null;
		long nextID = conn.transactionGetNextID(DTOStundenplanUnterrichtKlasse.class);
		final List<DTOStundenplanUnterrichtKlasse> listNeu = new ArrayList<>();
		final LinkedHashSet<String> skippedKlassen = new LinkedHashSet<>();
		final LinkedHashSet<String> skippedUnterricht = new LinkedHashSet<>();
		for (final DTOStundenplanUnterrichtKlasse alt : listAlt) {
			final Long klasseIdNeu = (klassenMappings == null)
					? alt.Klasse_ID
					: klassenMappings.get(alt.Klasse_ID);
			if (klasseIdNeu == null) {
				final String uLabel = unterrichtLabelById(conn, alt.Unterricht_ID, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache);
				skippedKlassen.add(uLabel + " / " + klasseLabel(conn, alt.Klasse_ID, klasseLabelCache));
				continue;
			}
			final DTOStundenplanUnterricht unterrichtNeu = unterrichteMapping.get(alt.Unterricht_ID);
			if (unterrichtNeu == null) {
				skippedUnterricht.add(unterrichtLabelById(conn, alt.Unterricht_ID, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache));
				continue;
			}
			final DTOStundenplanUnterrichtKlasse neu = new DTOStundenplanUnterrichtKlasse(
					nextID++,
					unterrichtNeu.ID,
					klasseIdNeu);
			listNeu.add(neu);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Unterrichts-Klassen.");
		if ((logger != null) && (!skippedKlassen.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Klassen übersprungen (Klasse existiert nicht): " + String.join(", ", skippedKlassen));
		if ((logger != null) && (!skippedUnterricht.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Klassen übersprungen (Unterricht nicht kopiert): " + String.join(", ", skippedUnterricht));
	}

	/**
	 * Kopiert die Lehrerzuordnungen zu Unterrichtsstunden. Lehrer, die im Ziel-Schuljahresabschnitt
	 * nicht mehr an der Schule tätig sind (Abgang vor Gültigkeitsbeginn des neuen Stundenplans),
	 * werden nicht übernommen. Falls ein äquivalenter Kurs im neuen Abschnitt existiert und dort
	 * ein verantwortlicher Lehrer gesetzt ist, wird dieser stattdessen zugeordnet.
	 *
	 * @param conn              die Datenbank-Verbindung
	 * @param unterrichteMapping Mapping alte → neue Unterrichts-IDs
	 * @param abschnittNeu       ID des Ziel-Schuljahresabschnitts oder {@code null}, falls identisch
	 * @param stundenplanNeu     der neue Stundenplan (für Gültigkeitsbeginn)
	 * @param logger            der Logger für Warn- und Statusmeldungen
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static void kopiereUnterrichteLehrer(final DBEntityManager conn, final Map<Long, DTOStundenplanUnterricht> unterrichteMapping, final Long abschnittNeu,
			final Stundenplan stundenplanNeu, final Logger logger)
			throws ApiOperationException {
		if (unterrichteMapping.isEmpty())
			return;
		final Map<Long, String> kursLabelCache = new HashMap<>();
		final Map<Long, String> klasseLabelCache = new HashMap<>();
		final Map<Long, String> fachLabelCache = new HashMap<>();
		final Map<Long, String> jahrgangLabelCache = new HashMap<>();
		final Map<Long, String> lehrerLabelCache = new HashMap<>();
		final List<DTOStundenplanUnterrichtLehrer> listAlt = conn.queryList(DTOStundenplanUnterrichtLehrer.QUERY_LIST_BY_UNTERRICHT_ID,
				DTOStundenplanUnterrichtLehrer.class, unterrichteMapping.keySet());
		final List<DTOStundenplanUnterrichtLehrer> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanUnterrichtLehrer.class);
		final LinkedHashSet<String> skippedUnterricht = new LinkedHashSet<>();
		final LinkedHashSet<String> skippedLehrer = new LinkedHashSet<>();
		for (final DTOStundenplanUnterrichtLehrer alt : listAlt) {
			final DTOStundenplanUnterricht unterrichtNeu = unterrichteMapping.get(alt.Unterricht_ID);
			if (unterrichtNeu == null) {
				skippedUnterricht.add(unterrichtLabelById(conn, alt.Unterricht_ID, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache));
				continue;
			}
			Long lehrerID = alt.Lehrer_ID;
			final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrerID);
			if ((lehrer == null) || ((lehrer.DatumAbgang != null) && (lehrer.DatumAbgang.compareTo(stundenplanNeu.gueltigAb) < 0))) {
				lehrerID = lookupNewLehrerID(conn, unterrichtNeu,
						(abschnittNeu == null) ? stundenplanNeu.idSchuljahresabschnitt : abschnittNeu);
				if (lehrerID == null) {
					final String uLabel = unterrichtLabel(conn, unterrichtNeu, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache);
					skippedLehrer.add(uLabel + " / " + lehrerLabel(conn, alt.Lehrer_ID, lehrerLabelCache));
					continue;
				}
			}
			final DTOStundenplanUnterrichtLehrer neu = new DTOStundenplanUnterrichtLehrer(
					nextID++,
					unterrichtNeu.ID,
					lehrerID);
			listNeu.add(neu);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Unterrichts-Lehrer.");
		if ((logger != null) && (!skippedUnterricht.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Lehrer übersprungen (Unterricht nicht kopiert): " + String.join(", ", skippedUnterricht));
		if ((logger != null) && (!skippedLehrer.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Lehrer übersprungen (kein Lehrer-Mapping): " + String.join(", ", skippedLehrer));
	}

	/**
	 * Ermittelt eine geeignete Lehrer-ID für einen Unterricht im Ziel-Schuljahresabschnitt.
	 * Es werden zwei Fälle unterschieden:
	 * - Kursunterricht (Kurs_ID != null): Der im Kurs hinterlegte verantwortliche Lehrer wird übernommen.
	 * - Klassenunterricht (kein Kurs, sondern Klasse und Fach):
	 *   Anhand der Schüler-Leistungsdaten im Ziel-Schuljahresabschnitt wird ermittelt,
	 *   welcher Lehrer dieses Fach in dieser Klasse unterrichtet hat. Wird genau ein
	 *   eindeutiger Lehrer gefunden, wird dessen ID zurückgegeben. Bei mehreren oder
	 *   keinen Treffern wird keine Zuordnung vorgenommen.
	 *
	 * @param conn       die Datenbank-Verbindung
	 * @param unterricht der alte Unterrichtseintrag, für den ein Lehrer gesucht wird
	 * @param abschnitt  die ID des Ziel-Schuljahresabschnitts
	 *
	 * @return die neue Lehrer-ID oder null, wenn keine eindeutige Zuordnung möglich ist
	 */
	private static Long lookupNewLehrerID(final DBEntityManager conn,
			final DTOStundenplanUnterricht unterricht,
			final long abschnitt) {
		if (unterricht.Kurs_ID != null) {
			final DTOKurs kurs = conn.queryByKey(DTOKurs.class, unterricht.Kurs_ID);
			return (kurs == null) ? null : kurs.Lehrer_ID;
		}
		final List<DTOStundenplanUnterrichtKlasse> unterrichtKlasse =
				conn.queryList(DTOStundenplanUnterrichtKlasse.QUERY_BY_UNTERRICHT_ID,
						DTOStundenplanUnterrichtKlasse.class, unterricht.ID);
		if (unterrichtKlasse.size() != 1)
			return null;
		final List<DTOSchuelerLeistungsdaten> slaList = conn.queryList(
				"""
				SELECT sld FROM DTOSchuelerLeistungsdaten sld
				  JOIN DTOSchuelerLernabschnittsdaten sla ON sld.Abschnitt_ID = sla.ID
				 WHERE sla.Schuljahresabschnitts_ID = ?1
				   AND sla.Klassen_ID = ?2
				   AND sld.Fach_ID = ?3
				""",
				DTOSchuelerLeistungsdaten.class, abschnitt, unterrichtKlasse.getFirst().Klasse_ID, unterricht.Fach_ID);
		final List<Long> fachlehrerIDs = slaList.stream()
				.map(sld -> sld.Fachlehrer_ID)
				.filter(Objects::nonNull)
				.distinct()
				.toList();
		return (fachlehrerIDs.size() == 1) ? fachlehrerIDs.getFirst() : null;
	}

	/**
	 * Kopiert die Raumzuordnungen zu Unterrichtsstunden unter Verwendung des übergebenen Raum-Mappings.
	 *
	 * @param conn              die Datenbank-Verbindung
	 * @param unterrichteMapping Mapping alte → neue Unterrichts-IDs
	 * @param raeumeMapping      Mapping alte → neue Raum-IDs
	 * @param logger            der Logger für Warn- und Statusmeldungen
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static void kopiereUnterrichteRaeume(final DBEntityManager conn, final Map<Long, DTOStundenplanUnterricht> unterrichteMapping,
			final Map<Long, Long> raeumeMapping, final Logger logger)
			throws ApiOperationException {
		if (unterrichteMapping.isEmpty())
			return;
		final Map<Long, String> kursLabelCache = new HashMap<>();
		final Map<Long, String> klasseLabelCache = new HashMap<>();
		final Map<Long, String> fachLabelCache = new HashMap<>();
		final Map<Long, String> jahrgangLabelCache = new HashMap<>();
		final Map<Long, String> raumLabelCache = new HashMap<>();
		final List<DTOStundenplanUnterrichtRaum> listAlt = conn.queryList(DTOStundenplanUnterrichtRaum.QUERY_LIST_BY_UNTERRICHT_ID,
				DTOStundenplanUnterrichtRaum.class, unterrichteMapping.keySet());
		final List<DTOStundenplanUnterrichtRaum> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanUnterrichtRaum.class);
		final LinkedHashSet<String> skippedRaeume = new LinkedHashSet<>();
		final LinkedHashSet<String> skippedUnterricht = new LinkedHashSet<>();
		for (final DTOStundenplanUnterrichtRaum alt : listAlt) {
			final Long raumIdNeu = raeumeMapping.get(alt.Raum_ID);
			if (raumIdNeu == null) {
				final String uLabel = unterrichtLabelById(conn, alt.Unterricht_ID, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache);
				skippedRaeume.add(uLabel + " / " + raumLabel(conn, alt.Raum_ID, raumLabelCache));
				continue;
			}
			final DTOStundenplanUnterricht unterrichtNeu = unterrichteMapping.get(alt.Unterricht_ID);
			if (unterrichtNeu == null) {
				skippedUnterricht.add(unterrichtLabelById(conn, alt.Unterricht_ID, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache));
				continue;
			}
			final DTOStundenplanUnterrichtRaum neu = new DTOStundenplanUnterrichtRaum(
					nextID++,
					unterrichtNeu.ID,
					raumIdNeu);
			listNeu.add(neu);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Unterrichts-Räume.");
		if ((logger != null) && (!skippedRaeume.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Räume übersprungen (Raum existiert nicht): " + String.join(", ", skippedRaeume));
		if ((logger != null) && (!skippedUnterricht.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Räume übersprungen (Unterricht nicht kopiert): " + String.join(", ", skippedUnterricht));
	}

	/**
	 * Kopiert die Schienenzuordnungen zu Unterrichtsstunden unter Verwendung des übergebenen Schienen-Mappings.
	 *
	 * @param conn              die Datenbank-Verbindung
	 * @param unterrichteMapping Mapping alte → neue Unterrichts-IDs
	 * @param schienenMapping    Mapping alte → neue Schienen-IDs
	 * @param logger            der Logger für Warn- und Statusmeldungen
	 *
	 * @throws ApiOperationException falls das Persistieren fehlschlägt
	 */
	static void kopiereUnterrichteSchienen(final DBEntityManager conn, final Map<Long, DTOStundenplanUnterricht> unterrichteMapping,
			final Map<Long, Long> schienenMapping, final Logger logger)
			throws ApiOperationException {
		if (unterrichteMapping.isEmpty())
			return;
		final Map<Long, String> kursLabelCache = new HashMap<>();
		final Map<Long, String> klasseLabelCache = new HashMap<>();
		final Map<Long, String> fachLabelCache = new HashMap<>();
		final Map<Long, String> jahrgangLabelCache = new HashMap<>();
		final Map<Long, String> schieneLabelCache = new HashMap<>();
		final List<DTOStundenplanUnterrichtSchiene> listAlt = conn.queryList(DTOStundenplanUnterrichtSchiene.QUERY_LIST_BY_UNTERRICHT_ID,
				DTOStundenplanUnterrichtSchiene.class, unterrichteMapping.keySet());
		final List<DTOStundenplanUnterrichtSchiene> listNeu = new ArrayList<>();
		long nextID = conn.transactionGetNextID(DTOStundenplanUnterrichtSchiene.class);
		final LinkedHashSet<String> skippedSchienen = new LinkedHashSet<>();
		final LinkedHashSet<String> skippedUnterricht = new LinkedHashSet<>();
		for (final DTOStundenplanUnterrichtSchiene alt : listAlt) {
			final Long schieneIdNeu = schienenMapping.get(alt.Schiene_ID);
			if (schieneIdNeu == null) {
				final String uLabel = unterrichtLabelById(conn, alt.Unterricht_ID, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache);
				skippedSchienen.add(uLabel + " / " + schieneLabel(conn, alt.Schiene_ID, schieneLabelCache, jahrgangLabelCache));
				continue;
			}
			final DTOStundenplanUnterricht unterrichtNeu = unterrichteMapping.get(alt.Unterricht_ID);
			if (unterrichtNeu == null) {
				skippedUnterricht.add(unterrichtLabelById(conn, alt.Unterricht_ID, kursLabelCache, klasseLabelCache, fachLabelCache, jahrgangLabelCache));
				continue;
			}
			final DTOStundenplanUnterrichtSchiene neu = new DTOStundenplanUnterrichtSchiene(
					nextID++,
					unterrichtNeu.ID,
					schieneIdNeu);
			listNeu.add(neu);
		}
		if (!conn.transactionPersistAll(listNeu))
			throw new ApiOperationException(Response.Status.INTERNAL_SERVER_ERROR, "Fehler beim Persistieren der Unterrichts-Schienen.");
		if ((logger != null) && (!skippedSchienen.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Schienen übersprungen (Schiene existiert nicht): " + String.join(", ", skippedSchienen));
		if ((logger != null) && (!skippedUnterricht.isEmpty()))
			logger.logLn(LogLevel.WARNING, "Unterrichts-Schienen übersprungen (Unterricht nicht kopiert): " + String.join(", ", skippedUnterricht));
	}

}
