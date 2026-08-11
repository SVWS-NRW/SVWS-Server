package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.svws_nrw.core.data.enm.v2.ENMv2Daten;
import de.svws_nrw.core.data.enm.v2.ENMv2Lehrer;
import de.svws_nrw.core.data.enm.v2.ENMv2Leistung;
import de.svws_nrw.core.data.enm.v2.ENMv2Schueler;
import de.svws_nrw.core.data.enm.v2.ENMv2SchuelerAnkreuzkompetenz;
import de.svws_nrw.core.data.enm.v2.ENMv2Teilleistung;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulCredentials;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsNotenmodulCredentials;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerAnkreuzkompetenzen;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.timestamps.DTOTimestampsSchuelerTeilleistungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.TimestampUtils;
import de.svws_nrw.repo.enm.NotenmodulCredentialsRepository;
import de.svws_nrw.repo.enm.NotenmodulCredentialsTimestampsRepository;
import de.svws_nrw.repo.lehrer.LehrerRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzTimestampRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenTimestampsRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittTimestampRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungTimestampRepository;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Import von ENM-Daten
 */
public class EnmV2ImportService {

	/** Das Repository für den Zugriff auf die Lehrerdaten */
	private final LehrerRepository lehrerRepository;

	/** Das Repository für den Zugriff auf die Schüler */
	private final SchuelerRepository schuelerRepository;

	/** Das Repository für den Zugriff auf die Schüler-Lernabschnitte */
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Lernabschnitte */
	private final SchuelerLernabschnittTimestampRepository schuelerLernabschnittTimestampRepository;

	/** Das Repository für den Zugriff auf die Lernabschnittbezogenen Bemerkungen zu Schülern */
	private final SchuelerLernabschnittBemerkungRepository schuelerLernabschnittBemerkungRepository;

	/** Das Repository für den Zugriff auf die Schüler-Leistungsdaten */
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Leistungsdaten */
	private final SchuelerLeistungsdatenTimestampsRepository schuelerLeistungsdatenTimestampsRepository;

	/** Das Repository für den Zugriff auf die Schüler-Teilleistungen */
	private final SchuelerTeilleistungRepository schuelerTeilleistungRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Teilleistungen */
	private final SchuelerTeilleistungTimestampRepository schuelerTeilleistungTimestampRepository;

	/** Das Repository für den Zugriff auf die Schüler-Ankreuzkompetenzen */
	private final SchuelerAnkreuzkompetenzRepository schuelerAnkreuzkompetenzRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Schüler-Ankreuzkompetenzen */
	private final SchuelerAnkreuzkompetenzTimestampRepository schuelerAnkreuzkompetenzTimestampRepository;

	/** Das Repository für den Zugriff auf die Credentials der Lehrer für das Notenmodul */
	private final NotenmodulCredentialsRepository notenmodulCredentialsRepository;

	/** Das Repository für den Zugriff auf die Zeitstempel für die Credentials der Lehrer für das Notenmodul */
	private final NotenmodulCredentialsTimestampsRepository notenmodulCredentialsTimestampsRepository;


	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param lehrerRepository                                 das Repository für den Zugriff auf die Lehrerdaten
	 * @param schuelerRepository                               das Repository für den Zugriff auf die Schüler
	 * @param schuelerLernabschnittRepository                  das Repository für den Zugriff auf die Schüler-Lernabschnitte
	 * @param schuelerLernabschnittTimestampRepository        das Repository für den Zugriff auf die Zeitstempel für die Schüler-Lernabschnitte
	 * @param schuelerLernabschnittBemerkungRepository       das Repository für den Zugriff auf die Lernabschnittbezogenen Bemerkungen zu Schülern
	 * @param schuelerLeistungsdatenRepository                 das Repository für den Zugriff auf die Schüler-Leistungsdaten
	 * @param schuelerLeistungsdatenTimestampsRepository       das Repository für den Zugriff auf die Zeitstempel für die Schüler-Leistungsdaten
	 * @param schuelerTeilleistungRepository                 das Repository für den Zugriff auf die Schüler-Teilleistungen
	 * @param schuelerTeilleistungTimestampRepository       das Repository für den Zugriff auf die Zeitstempel für die Schüler-Teilleistungen
	 * @param schuelerAnkreuzkompetenzRepository             das Repository für den Zugriff auf die Schüler-Ankreuzkompetenzen
	 * @param schuelerAnkreuzkompetenzTimestampRepository   das Repository für den Zugriff auf die Zeitstempel für die Schüler-Ankreuzkompetenzen
	 * @param notenmodulCredentialsRepository                  das Repository für den Zugriff auf die Credentials der Lehrer für das Notenmodul
	 * @param notenmodulCredentialsTimestampsRepository        das Repository für den Zugriff auf die Zeitstempel für die Credentials
	 *                                                         der Lehrer für das Notenmodul
	 */
	public EnmV2ImportService(final LehrerRepository lehrerRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLernabschnittTimestampRepository schuelerLernabschnittTimestampRepository,
			final SchuelerLernabschnittBemerkungRepository schuelerLernabschnittBemerkungRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerLeistungsdatenTimestampsRepository schuelerLeistungsdatenTimestampsRepository,
			final SchuelerTeilleistungRepository schuelerTeilleistungRepository,
			final SchuelerTeilleistungTimestampRepository schuelerTeilleistungTimestampRepository,
			final SchuelerAnkreuzkompetenzRepository schuelerAnkreuzkompetenzRepository,
			final SchuelerAnkreuzkompetenzTimestampRepository schuelerAnkreuzkompetenzTimestampRepository,
			final NotenmodulCredentialsRepository notenmodulCredentialsRepository,
			final NotenmodulCredentialsTimestampsRepository notenmodulCredentialsTimestampsRepository) {
		this.lehrerRepository = lehrerRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLernabschnittTimestampRepository = schuelerLernabschnittTimestampRepository;
		this.schuelerLernabschnittBemerkungRepository = schuelerLernabschnittBemerkungRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.schuelerLeistungsdatenTimestampsRepository = schuelerLeistungsdatenTimestampsRepository;
		this.schuelerTeilleistungRepository = schuelerTeilleistungRepository;
		this.schuelerTeilleistungTimestampRepository = schuelerTeilleistungTimestampRepository;
		this.schuelerAnkreuzkompetenzRepository = schuelerAnkreuzkompetenzRepository;
		this.schuelerAnkreuzkompetenzTimestampRepository = schuelerAnkreuzkompetenzTimestampRepository;
		this.notenmodulCredentialsRepository = notenmodulCredentialsRepository;
		this.notenmodulCredentialsTimestampsRepository = notenmodulCredentialsTimestampsRepository;
	}



	/** Das Zeitstempel-Format für den Vergleich. */
	private static final DateTimeFormatter timestampPattern =
			new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();

	/**
	 * Wandelt die im ISO-Format übergeben Zeitangabe in einen Timestamp um.
	 *
	 * @param iso   der ISO-String
	 *
	 * @return der Timestamp oder null, falls der ISO-String ungültig ist
	 */
	private static Timestamp getTimeStampFromIso(final String iso) {
		if ((iso == null) || iso.isBlank()) {
			return null;
		}
		return Timestamp.valueOf(LocalDateTime.parse(iso, timestampPattern));
	}


	/**
	 * Prüft, ob der gegebene Timestamp-String tsCheckStr nach dem Timestamp-String tsOtherStr
	 * liegt.
	 *
	 * @param tsCheckStr   der zu prüfende Timestamp-String
	 * @param tsOtherStr   der andere Timestamp-String
	 *
	 * @return true, wenn tsCheckStr nach tsOtherStr liegt
	 */
	private static boolean isTimestampAfter(final String tsCheckStr, final String tsOtherStr) {
		final Timestamp tsCheck = getTimeStampFromIso(tsCheckStr);
		if (tsCheck == null) {
			return false;
		}
		final Timestamp tsOther = getTimeStampFromIso(tsOtherStr);
		if (tsOther == null) {
			return true;
		}
		return tsCheck.after(tsOther);
	}


	private class EnmKontextdaten {
		// Maps mit den Daten aus den DTOs
		private final Map<Long, DTOLehrer> mapLehrer;
		private final Map<Long, DTONotenmodulCredentials> mapLehrerCreds;
		private final Map<Long, DTOTimestampsNotenmodulCredentials> mapLehrerCredsTimestamps;
		private final Map<Long, DTOSchueler> mapSchueler;
		private final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte;
		private final Map<Long, DTOTimestampsSchuelerLernabschnittsdaten> mapLernabschnitteTimestamps;
		private final Map<Long, DTOSchuelerPSFachBemerkungen> mapLernabschnittsbemerkungen;
		private final Map<Long, DTOSchuelerLeistungsdaten> mapLeistungen;
		private final Map<Long, DTOTimestampsSchuelerLeistungsdaten> mapLeistungenTimestamps;
		private final Map<Long, DTOSchuelerTeilleistung> mapTeilleistungen;
		private final Map<Long, DTOTimestampsSchuelerTeilleistungen> mapTeilleistungenTimestamps;
		private final Map<Long, DTOSchuelerAnkreuzfloskeln> mapAnkreuzkompetenzen;
		private final Map<Long, DTOTimestampsSchuelerAnkreuzkompetenzen> mapAnkreuzkompetenzenTimestamps;

		// Sets, um veränderte Daten zwischenzuspeichern
		private final Set<DTOSchuelerLernabschnittsdaten> setLernabschnitte = new HashSet<>();
		private final Set<DTOSchuelerPSFachBemerkungen> setLernabschnittsbemerkungen = new HashSet<>();
		private final Set<DTOSchuelerPSFachBemerkungen> setLernabschnittsbemerkungenNeu = new HashSet<>();
		private final Set<DTOTimestampsSchuelerLernabschnittsdaten> setLernabschnitteTimestamps = new HashSet<>();
		private final Set<DTOSchuelerLeistungsdaten> setLeistungen = new HashSet<>();
		private final Set<DTOTimestampsSchuelerLeistungsdaten> setLeistungenTimestamps = new HashSet<>();
		private final Set<DTOSchuelerTeilleistung> setTeilleistungen = new HashSet<>();
		private final Set<DTOTimestampsSchuelerTeilleistungen> setTeilleistungenTimestamps = new HashSet<>();
		private final Set<DTOSchuelerAnkreuzfloskeln> setAnkreuzkompetenzen = new HashSet<>();
		private final Set<DTOTimestampsSchuelerAnkreuzkompetenzen> setAnkreuzkompetenzenTimestamps = new HashSet<>();

		EnmKontextdaten(final ENMv2Daten daten) {
			// Bestimme die IDs zu den Lehrer-Daten, anhand der zu importierenden Daten (vermeide doppeltes Laden aufgrund von ID-Duplikaten
			final List<Long> idsLehrer = daten.lehrer.stream().map(s -> s.id).distinct().toList();

			// Bestimme die IDs zu den Schüler-Daten und die zugehörigen IDs, anhand der zu importierenden Daten (vermeide doppeltes Laden aufgrund von ID-Duplikaten)
			final List<Long> idsSchueler = new ArrayList<>();
			final List<Long> idsLernabschnitte = new ArrayList<>();
			final List<Long> idsSchuelerAnkreuzkompetenz = new ArrayList<>();
			final List<Long> idsLeistungen = new ArrayList<>();
			final List<Long> idsTeilleistungen = new ArrayList<>();
			for (final ENMv2Schueler s : daten.schueler) {
				idsSchueler.add(s.id);
				if (s.lernabschnitt != null) {
					idsLernabschnitte.add(s.lernabschnitt.id);
				}
				for (final var a : s.ankreuzkompetenzen) {
					idsSchuelerAnkreuzkompetenz.add(a.id);
				}
				for (final ENMv2Leistung l : s.leistungsdaten) {
					idsLeistungen.add(l.id);
					for (final ENMv2Teilleistung tl : l.teilleistungen) {
						idsTeilleistungen.add(tl.id);
					}
				}
			}

			// Lade die Daten aus den Repositories
			mapLehrer = lehrerRepository.findMapByIds(idsLehrer);
			mapLehrerCreds = notenmodulCredentialsRepository.findMapByIds(idsLehrer);
			mapLehrerCredsTimestamps = notenmodulCredentialsTimestampsRepository.findMapByIds(idsLehrer);
			mapSchueler = schuelerRepository.findMapByIds(idsSchueler);
			mapLernabschnitte = schuelerLernabschnittRepository.findMapByIds(idsLernabschnitte);
			mapLernabschnitteTimestamps = schuelerLernabschnittTimestampRepository.findMapByIds(idsLernabschnitte);
			mapLernabschnittsbemerkungen = schuelerLernabschnittBemerkungRepository.findMapByLernabschnittID(idsLernabschnitte);
			mapLeistungen = schuelerLeistungsdatenRepository.findMapByIds(idsLeistungen);
			mapLeistungenTimestamps = schuelerLeistungsdatenTimestampsRepository.findMapByIds(idsLeistungen);
			mapTeilleistungen = schuelerTeilleistungRepository.findMapByIds(idsTeilleistungen);
			mapTeilleistungenTimestamps = schuelerTeilleistungTimestampRepository.findMapByIds(idsTeilleistungen);
			mapAnkreuzkompetenzen = schuelerAnkreuzkompetenzRepository.findMapByIds(idsSchuelerAnkreuzkompetenz);
			mapAnkreuzkompetenzenTimestamps = schuelerAnkreuzkompetenzTimestampRepository.findMapByIds(idsSchuelerAnkreuzkompetenz);

			// Prüfe, ob alle Daten erfolgreich in den Kontext geladen wurden
			verify(daten);
		}

		private void verify(final ENMv2Daten daten) {
			// Prüfe, ob die Daten zu den Lehrern korrekt geladen wurden
			if (this.mapLehrer.size() != daten.lehrer.size()) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Nicht alle Lehrer in den ENM-Daten konnten auch in der Datenbank gefunden werden oder es waren Lehrer-IDs doppelt vorhanden.");
			}

			// Prüfe, ob die Daten zu den Schülern korrekt geladen wurden
			if (this.mapSchueler.size() != daten.schueler.size()) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Nicht alle Schüler in den ENM-Daten konnten auch in der Datenbank gefunden werden oder es waren Schüler-IDs doppelt vorhanden.");
			}

			// Prüfe, ob für jeden Schüler genau ein Lernabschnitt geladen werden konnte
			if (this.mapLernabschnitte.size() != this.mapSchueler.size()) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Die ENM-Daten enthalten nicht genügend Lernabschnitte. Dies ist nicht zulässig.");
			}

			// Prüfe, on alle Ankreuzkompetenzen aus der Datenbank geladen wurden
			final long countAnkreuzkompetenzen = daten.schueler.stream().mapToLong(s -> s.ankreuzkompetenzen.size()).sum();
			if (this.mapAnkreuzkompetenzen.size() != countAnkreuzkompetenzen) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Nicht alle Ankreuzkompetenzen aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
			}

			// Prüfe, ob alle Leistungsdaten aus der Datenbank geladen wurden
			final List<ENMv2Leistung> enmLeistungen = daten.schueler.stream().<ENMv2Leistung>mapMulti((s, consumer) -> s.leistungsdaten.forEach(consumer)).toList();
			if (this.mapLeistungen.size() != enmLeistungen.size()) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Leistungsdaten aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
			}

			// Prüfe, ob alle Teilleistungen aus der Datenbank geladen wurden
			final long countTeilleistungen = enmLeistungen.stream().mapToLong(l -> l.teilleistungen.size()).sum();
			if (this.mapTeilleistungen.size() != countTeilleistungen) {
				throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Teilleistungen aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
			}
		}
	}


	/**
	 * Importiert die Hashes aus den gegebenen ENMLehrer-Daten in die SVWS-Datenbank. Prüft dazu die Zeitstempel
	 * und aktualisiert neuere Datensätze und deren Zeitstempel.
	 *
	 * @param daten     die zu importierenden ENM-Daten
	 * @param kontext   der Kontext zu den ENM-Daten mit den aus der Datenbank geladenen Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void applyLehrerCredentials(final ENMv2Daten daten, final EnmKontextdaten kontext) throws ApiOperationException {
		// Gehe die einzelnen Lehrer durch und aktualisiere ggf. die Credentials
		for (final ENMv2Lehrer enmLehrer : daten.lehrer) {
			final DTOLehrer dtoLehrer = kontext.mapLehrer.get(enmLehrer.id);
			if (dtoLehrer == null) {
				throw new ApiOperationException(Status.NOT_FOUND,
						"Der Lehrer in den ENM-Daten mit der ID %d konnte in der Datenbank nicht gefunden werden.".formatted(enmLehrer.id));
			}
			DTONotenmodulCredentials cred = kontext.mapLehrerCreds.get(enmLehrer.id);
			final DTOTimestampsNotenmodulCredentials credTS = kontext.mapLehrerCredsTimestamps.get(enmLehrer.id);
			if (isTimestampAfter(enmLehrer.tsPasswordHash, credTS == null ? null : TimestampUtils.convertUtcToLocal(credTS.tsPasswordHash))) {
				if (cred == null) {
					cred = new DTONotenmodulCredentials(enmLehrer.id, "", enmLehrer.passwordHash, 0, true);
				} else {
					cred.passwordHash = enmLehrer.passwordHash;
				}
				notenmodulCredentialsRepository.update(cred);
			}
		}
		notenmodulCredentialsRepository.flush();
	}


	private static boolean updateIfNewerOr(final String tsNew, final String tsOld, final boolean force, final Runnable task) {
		if (force || isTimestampAfter(tsNew, tsOld)) {
			task.run();
			return true;
		}
		return false;
	}


	private static boolean updateIfNewer(final String tsNew, final String tsOld, final Runnable task) {
		if (isTimestampAfter(tsNew, tsOld)) {
			task.run();
			return true;
		}
		return false;
	}


	private static boolean pruefeBemerkungen(final ENMv2Schueler enmSchueler, final DTOSchuelerLernabschnittsdaten lernabschnitt,
			final DTOTimestampsSchuelerLernabschnittsdaten lernabschnittTS, final long idNeueFachbemerkung, final EnmKontextdaten kontext) {
		final boolean neuBemerkungen = !kontext.mapLernabschnittsbemerkungen.containsKey(enmSchueler.lernabschnitt.id);
		final DTOSchuelerPSFachBemerkungen lernabschnittsbemerkungen = kontext.mapLernabschnittsbemerkungen.getOrDefault(enmSchueler.lernabschnitt.id,
				new DTOSchuelerPSFachBemerkungen(idNeueFachbemerkung, enmSchueler.lernabschnitt.id));

		boolean updatedBemerkungen = updateIfNewerOr(enmSchueler.bemerkungen.tsASV, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsASV), neuBemerkungen, () -> {
			lernabschnittsbemerkungen.ASV = enmSchueler.bemerkungen.ASV;
			lernabschnittTS.tsASV = TimestampUtils.convertLocalToUtc(enmSchueler.bemerkungen.tsASV);
		});
		updatedBemerkungen |= updateIfNewerOr(enmSchueler.bemerkungen.tsAUE, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsAUE), neuBemerkungen, () -> {
			lernabschnittsbemerkungen.AUE = enmSchueler.bemerkungen.AUE;
			lernabschnittTS.tsAUE = TimestampUtils.convertLocalToUtc(enmSchueler.bemerkungen.tsAUE);
		});
		updatedBemerkungen |= updateIfNewerOr(enmSchueler.bemerkungen.tsLELS, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsLELS), neuBemerkungen, () -> {
			lernabschnittsbemerkungen.LELS = enmSchueler.bemerkungen.LELS;
			lernabschnittTS.tsLELS = TimestampUtils.convertLocalToUtc(enmSchueler.bemerkungen.tsLELS);
		});
		updatedBemerkungen |= updateIfNewerOr(enmSchueler.bemerkungen.tsSchulformEmpf, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsESF), neuBemerkungen, () -> {
			lernabschnittsbemerkungen.ESF = enmSchueler.bemerkungen.schulformEmpf;
			lernabschnittTS.tsESF = TimestampUtils.convertLocalToUtc(enmSchueler.bemerkungen.tsSchulformEmpf);
		});
		updatedBemerkungen |= updateIfNewerOr(enmSchueler.bemerkungen.tsFoerderbemerkungen, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsBemerkungFSP), neuBemerkungen, () -> {
			lernabschnittsbemerkungen.BemerkungFSP = enmSchueler.bemerkungen.foerderbemerkungen;
			lernabschnittTS.tsBemerkungFSP = TimestampUtils.convertLocalToUtc(enmSchueler.bemerkungen.tsFoerderbemerkungen);
		});
		updatedBemerkungen |= updateIfNewerOr(enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsBemerkungVersetzung),
				neuBemerkungen, () -> {
					lernabschnittsbemerkungen.BemerkungVersetzung = enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen;
					lernabschnittTS.tsBemerkungVersetzung = TimestampUtils.convertLocalToUtc(enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen);
				});

		boolean updatedLernabschnitt = updateIfNewer(enmSchueler.bemerkungen.tsZB, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsZeugnisBem), () -> {
			lernabschnitt.ZeugnisBem = enmSchueler.bemerkungen.ZB;
			lernabschnittTS.tsZeugnisBem = TimestampUtils.convertLocalToUtc(enmSchueler.bemerkungen.tsZB);
		});
		updatedLernabschnitt |= updateIfNewer(enmSchueler.lernabschnitt.tsFehlstundenGesamt, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsSumFehlStd), () -> {
			lernabschnitt.SumFehlStd = enmSchueler.lernabschnitt.fehlstundenGesamt;
			lernabschnittTS.tsSumFehlStd = TimestampUtils.convertLocalToUtc(enmSchueler.lernabschnitt.tsFehlstundenGesamt);
		});
		updatedLernabschnitt |= updateIfNewer(enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt, TimestampUtils.convertUtcToLocal(lernabschnittTS.tsSumFehlStdU), () -> {
			lernabschnitt.SumFehlStdU = enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt;
			lernabschnittTS.tsSumFehlStdU = TimestampUtils.convertLocalToUtc(enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt);
		});

		if (updatedBemerkungen && !neuBemerkungen) {
			kontext.setLernabschnittsbemerkungen.add(lernabschnittsbemerkungen);
		}
		if (neuBemerkungen) {
			kontext.setLernabschnittsbemerkungenNeu.add(lernabschnittsbemerkungen);
		}
		if (updatedLernabschnitt) {
			kontext.setLernabschnitte.add(lernabschnitt);
		}
		if (updatedBemerkungen || updatedLernabschnitt) {
			kontext.setLernabschnitteTimestamps.add(lernabschnittTS);
		}
		return neuBemerkungen;
	}


	private static void pruefeAnkreuzkompetenzen(final ENMv2Schueler enmSchueler, final EnmKontextdaten kontext) {
		for (final ENMv2SchuelerAnkreuzkompetenz enmAnkreuzkompetenz : enmSchueler.ankreuzkompetenzen) {
			final DTOSchuelerAnkreuzfloskeln ankreuzkompetenz = kontext.mapAnkreuzkompetenzen.get(enmAnkreuzkompetenz.id);
			final DTOTimestampsSchuelerAnkreuzkompetenzen ankreuzkompetenzTS = kontext.mapAnkreuzkompetenzenTimestamps.get(enmAnkreuzkompetenz.id);

			final boolean updatedAnkreuzkompetenz = updateIfNewer(enmAnkreuzkompetenz.tsStufe, TimestampUtils.convertUtcToLocal(ankreuzkompetenzTS.tsStufe), () -> {
				ankreuzkompetenz.Stufe1 = enmAnkreuzkompetenz.stufen[0];
				ankreuzkompetenz.Stufe2 = enmAnkreuzkompetenz.stufen[1];
				ankreuzkompetenz.Stufe3 = enmAnkreuzkompetenz.stufen[2];
				ankreuzkompetenz.Stufe4 = enmAnkreuzkompetenz.stufen[3];
				ankreuzkompetenz.Stufe5 = enmAnkreuzkompetenz.stufen[4];
				ankreuzkompetenzTS.tsStufe = TimestampUtils.convertLocalToUtc(enmAnkreuzkompetenz.tsStufe);
			});

			if (updatedAnkreuzkompetenz) {
				kontext.setAnkreuzkompetenzen.add(ankreuzkompetenz);
				kontext.setAnkreuzkompetenzenTimestamps.add(ankreuzkompetenzTS);
			}
		}
	}


	private static void pruefeTeilleistungen(final ENMv2Leistung enmLeistung, final EnmKontextdaten kontext) {
		for (final ENMv2Teilleistung enmTeilleistung : enmLeistung.teilleistungen) {
			final DTOSchuelerTeilleistung teilleistung = kontext.mapTeilleistungen.get(enmTeilleistung.id);
			final DTOTimestampsSchuelerTeilleistungen teilleistungTS = kontext.mapTeilleistungenTimestamps.get(enmTeilleistung.id);

			boolean updatedTeilleistung = updateIfNewer(enmTeilleistung.tsArtID, TimestampUtils.convertUtcToLocal(teilleistungTS.tsArt_ID), () -> {
				teilleistung.Art_ID = enmTeilleistung.artID;
				teilleistungTS.tsArt_ID = TimestampUtils.convertLocalToUtc(enmTeilleistung.tsArtID);
			});
			updatedTeilleistung |= updateIfNewer(enmTeilleistung.tsDatum, TimestampUtils.convertUtcToLocal(teilleistungTS.tsDatum), () -> {
				teilleistung.Datum = enmTeilleistung.datum;
				teilleistungTS.tsDatum = TimestampUtils.convertLocalToUtc(enmTeilleistung.tsDatum);
			});
			updatedTeilleistung |= updateIfNewer(enmTeilleistung.tsBemerkung, TimestampUtils.convertUtcToLocal(teilleistungTS.tsBemerkung), () -> {
				teilleistung.Bemerkung = enmTeilleistung.bemerkung;
				teilleistungTS.tsBemerkung = TimestampUtils.convertLocalToUtc(enmTeilleistung.tsBemerkung);
			});
			updatedTeilleistung |= updateIfNewer(enmTeilleistung.tsNote, TimestampUtils.convertUtcToLocal(teilleistungTS.tsNotenKrz), () -> {
				teilleistung.NotenKrz = enmTeilleistung.note;
				teilleistungTS.tsNotenKrz = TimestampUtils.convertLocalToUtc(enmTeilleistung.tsNote);
			});

			if (updatedTeilleistung) {
				kontext.setTeilleistungen.add(teilleistung);
				kontext.setTeilleistungenTimestamps.add(teilleistungTS);
			}
		}
	}

	private static void pruefeLeistungsdaten(final ENMv2Schueler enmSchueler, final EnmKontextdaten kontext) {
		for (final ENMv2Leistung enmLeistung : enmSchueler.leistungsdaten) {
			final DTOSchuelerLeistungsdaten leistung = kontext.mapLeistungen.get(enmLeistung.id);
			final DTOTimestampsSchuelerLeistungsdaten leistungTS = kontext.mapLeistungenTimestamps.get(enmLeistung.id);

			boolean updatedLeistung = updateIfNewer(enmLeistung.tsFachbezogeneBemerkungen, TimestampUtils.convertUtcToLocal(leistungTS.tsLernentw), () -> {
				leistung.Lernentw = enmLeistung.fachbezogeneBemerkungen;
				leistungTS.tsLernentw = TimestampUtils.convertLocalToUtc(enmLeistung.tsFachbezogeneBemerkungen);
			});
			updatedLeistung |= updateIfNewer(enmLeistung.tsFehlstundenFach, TimestampUtils.convertUtcToLocal(leistungTS.tsFehlStd), () -> {
				leistung.FehlStd = enmLeistung.fehlstundenFach;
				leistungTS.tsFehlStd = TimestampUtils.convertLocalToUtc(enmLeistung.tsFehlstundenFach);
			});
			updatedLeistung |= updateIfNewer(enmLeistung.tsFehlstundenUnentschuldigtFach, TimestampUtils.convertUtcToLocal(leistungTS.tsuFehlStd), () -> {
				leistung.uFehlStd = enmLeistung.fehlstundenUnentschuldigtFach;
				leistungTS.tsuFehlStd = TimestampUtils.convertLocalToUtc(enmLeistung.tsFehlstundenUnentschuldigtFach);
			});
			updatedLeistung |= updateIfNewer(enmLeistung.tsIstGemahnt, TimestampUtils.convertUtcToLocal(leistungTS.tsWarnung), () -> {
				leistung.Warnung = enmLeistung.istGemahnt;
				leistungTS.tsWarnung = TimestampUtils.convertLocalToUtc(enmLeistung.tsIstGemahnt);
			});
			updatedLeistung |= updateIfNewer(enmLeistung.tsNote, TimestampUtils.convertUtcToLocal(leistungTS.tsNotenKrz), () -> {
				leistung.NotenKrz = enmLeistung.note;
				leistungTS.tsNotenKrz = TimestampUtils.convertLocalToUtc(enmLeistung.tsNote);
			});
			updatedLeistung |= updateIfNewer(enmLeistung.tsNoteQuartal, TimestampUtils.convertUtcToLocal(leistungTS.tsNotenKrzQuartal), () -> {
				leistung.NotenKrzQuartal = enmLeistung.noteQuartal;
				leistungTS.tsNotenKrzQuartal = TimestampUtils.convertLocalToUtc(enmLeistung.tsNoteQuartal);
			});

			if (updatedLeistung) {
				kontext.setLeistungen.add(leistung);
				kontext.setLeistungenTimestamps.add(leistungTS);
			}

			pruefeTeilleistungen(enmLeistung, kontext);
		}
	}


	private void doUpdate(final EnmKontextdaten kontext) {
		if (!kontext.setLernabschnittsbemerkungenNeu.isEmpty()) {
			schuelerLernabschnittBemerkungRepository.update(kontext.setLernabschnittsbemerkungenNeu);
		}
		if (!kontext.setLernabschnittsbemerkungen.isEmpty()) {
			schuelerLernabschnittBemerkungRepository.update(kontext.setLernabschnittsbemerkungen);
		}
		if (!kontext.setLernabschnitte.isEmpty()) {
			schuelerLernabschnittRepository.update(kontext.setLernabschnitte);
		}
		if (!kontext.setLeistungen.isEmpty()) {
			schuelerLeistungsdatenRepository.update(kontext.setLeistungen);
		}
		if (!kontext.setTeilleistungen.isEmpty()) {
			schuelerTeilleistungRepository.update(kontext.setTeilleistungen);
		}
		if (!kontext.setAnkreuzkompetenzen.isEmpty()) {
			schuelerAnkreuzkompetenzRepository.update(kontext.setAnkreuzkompetenzen);
		}
		schuelerRepository.flush();

		if (!kontext.setLernabschnitteTimestamps.isEmpty()) {
			schuelerLernabschnittTimestampRepository.update(kontext.setLernabschnitteTimestamps);
		}
		if (!kontext.setLeistungenTimestamps.isEmpty()) {
			schuelerLeistungsdatenTimestampsRepository.update(kontext.setLeistungenTimestamps);
		}
		if (!kontext.setTeilleistungenTimestamps.isEmpty()) {
			schuelerTeilleistungTimestampRepository.update(kontext.setTeilleistungenTimestamps);
		}
		if (!kontext.setAnkreuzkompetenzenTimestamps.isEmpty()) {
			schuelerAnkreuzkompetenzTimestampRepository.update(kontext.setAnkreuzkompetenzenTimestamps);
		}
		schuelerRepository.flush();
	}


	/**
	 * Importiert die gegebenen ENMSchueler-Daten in die SVWS-Datenbank. Prüft dazu die Zeitstempel
	 * der einzelnen Felder und aktualisiert neuere Datensätze und deren Zeitstempel.
	 *
	 * @param daten     die zu importierenden ENM-Daten
	 * @param kontext   der Kontext zu den ENM-Daten mit den aus der Datenbank geladenen Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private void applySchuelerdaten(final ENMv2Daten daten, final EnmKontextdaten kontext) throws ApiOperationException {
		long idNeueFachbemerkung = schuelerLernabschnittBemerkungRepository.getNextID();

		// Durchwandere die importierten ENM-Daten und gleiche diese mit den Daten in der Datenbank ab.
		for (final ENMv2Schueler enmSchueler : daten.schueler) {
			final DTOSchuelerLernabschnittsdaten lernabschnitt = kontext.mapLernabschnitte.get(enmSchueler.lernabschnitt.id);
			final DTOTimestampsSchuelerLernabschnittsdaten lernabschnittTS = kontext.mapLernabschnitteTimestamps.get(enmSchueler.lernabschnitt.id);

			if (pruefeBemerkungen(enmSchueler, lernabschnitt, lernabschnittTS, idNeueFachbemerkung, kontext)) {
				idNeueFachbemerkung++;
			}

			pruefeAnkreuzkompetenzen(enmSchueler, kontext);

			pruefeLeistungsdaten(enmSchueler, kontext);
		}

		doUpdate(kontext);
	}


	/**
	 * Integriert die Veränderungen bei den importierten ENM-Daten gegenüber dem Stand
	 * der SVWS-DB in die SVWS-DB.
	 *
	 * @param daten   die importierten Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void applyLatest(final ENMv2Daten daten) throws ApiOperationException {
		transactional(() -> {
			final EnmKontextdaten kontext = new EnmKontextdaten(daten);
			applyLehrerCredentials(daten, kontext);
			applySchuelerdaten(daten, kontext);
		});
	}

}
