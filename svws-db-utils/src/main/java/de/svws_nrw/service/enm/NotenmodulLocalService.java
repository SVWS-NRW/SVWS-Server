package de.svws_nrw.service.enm;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.data.benutzer.BenutzerConfigElement;
import de.svws_nrw.core.data.enm.ENMConfigKlasse;
import de.svws_nrw.core.data.enm.ENMConfigKlasseSpalte;
import de.svws_nrw.core.data.enm.ENMServerConfig;
import de.svws_nrw.core.data.enm.ENMServerConfigElement;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationClient;
import de.svws_nrw.db.dto.current.notenmodul.DTONotenmodulKonfigurationServer;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.grundschule.DTOSchuelerAnkreuzfloskeln;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.enm.NotenmodulKonfigurationClientRepository;
import de.svws_nrw.repo.enm.NotenmodulKonfigurationServerRepository;
import de.svws_nrw.repo.schule.kataloge.ankreuzkompetenz.AnkreuzkompetenzRepository;
import de.svws_nrw.repo.schueler.ankreuzkompetenz.SchuelerAnkreuzkompetenzRepository;
import de.svws_nrw.repo.schueler.leistungsdaten.SchuelerLeistungsdatenRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittBemerkungRepository;
import de.svws_nrw.repo.schueler.lernabschnitt.SchuelerLernabschnittRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.repo.schueler.teilleistung.SchuelerTeilleistungRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response.Status;


/**
 * Service für das ausführen von Patches im lokalen Notenmodul
 */
public class NotenmodulLocalService {

	private static final DateTimeFormatter ofPattern =
			new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();

	private final NotenmodulKonfigurationClientRepository notenmodulKonfigurationClientRepository;
	private final NotenmodulKonfigurationServerRepository notenmodulKonfigurationServerRepository;
	private final SchuelerRepository schuelerRepository;
	private final SchuelerLernabschnittRepository schuelerLernabschnittRepository;
	private final SchuelerLernabschnittBemerkungRepository schuelerLernabschnittBemerkungRepository;
	private final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository;
	private final SchuelerTeilleistungRepository schuelerTeilleistungRepository;
	private final SchuelerAnkreuzkompetenzRepository schuelerAnkreuzkompetenzRepository;
	private final AnkreuzkompetenzRepository ankreuzkompetenzRepository;

	/**
	 * Erstellt einen neuen Service für die Notenmodul-Credentials
	 *
	 * @param notenmodulKonfigurationClientRepository      das Repository für den Zugriff auf die lokale Client-Konfiguration des Notenmoduls
	 * @param notenmodulKonfigurationServerRepository      das Repository für den Zugriff auf die lokale Server-Konfiguration des Notenmoduls
	 * @param schuelerRepository                           das Repository für den Zugriff auf die Schüler
	 * @param schuelerLernabschnittRepository              das Repository für den Zugriff auf die Schüler-Lernabschnitte
	 * @param schuelerLernabschnittBemerkungRepository   das Repository für den Zugriff auf die Bemerkungen zu den Schüler-Lernabschnitten
	 * @param schuelerLeistungsdatenRepository             das Repository für den Zugriff auf die Schüler-Leistungsdaten
	 * @param schuelerTeilleistungRepository             das Repository für den Zugriff auf die Schüler-Teilleistungen
	 * @param schuelerAnkreuzkompetenzRepository         das Repository für den Zugriff auf die Schüler-Ankreuzkompetenten
	 * @param ankreuzkompetenzRepository                 das Repository für den Zugriff auf die Ankreuzkompetenzen
	 */
	public NotenmodulLocalService(final NotenmodulKonfigurationClientRepository notenmodulKonfigurationClientRepository,
			final NotenmodulKonfigurationServerRepository notenmodulKonfigurationServerRepository,
			final SchuelerRepository schuelerRepository,
			final SchuelerLernabschnittRepository schuelerLernabschnittRepository,
			final SchuelerLernabschnittBemerkungRepository schuelerLernabschnittBemerkungRepository,
			final SchuelerLeistungsdatenRepository schuelerLeistungsdatenRepository,
			final SchuelerTeilleistungRepository schuelerTeilleistungRepository,
			final SchuelerAnkreuzkompetenzRepository schuelerAnkreuzkompetenzRepository,
			final AnkreuzkompetenzRepository ankreuzkompetenzRepository) {
		this.notenmodulKonfigurationClientRepository = notenmodulKonfigurationClientRepository;
		this.notenmodulKonfigurationServerRepository = notenmodulKonfigurationServerRepository;
		this.schuelerRepository = schuelerRepository;
		this.schuelerLernabschnittRepository = schuelerLernabschnittRepository;
		this.schuelerLernabschnittBemerkungRepository = schuelerLernabschnittBemerkungRepository;
		this.schuelerLeistungsdatenRepository = schuelerLeistungsdatenRepository;
		this.schuelerTeilleistungRepository = schuelerTeilleistungRepository;
		this.schuelerAnkreuzkompetenzRepository = schuelerAnkreuzkompetenzRepository;
		this.ankreuzkompetenzRepository = ankreuzkompetenzRepository;
	}


	/**
	 * Liest die Konfiguration des lokalen Notenmoduls für den Client aus der Datenbank.
	 *
	 * @return die Konfiguration des Notenmoduls für den Client
	 */
	public List<BenutzerConfigElement> getClientConfig() {
		return transactional(
				() -> notenmodulKonfigurationClientRepository.getAll().stream().map(e -> new BenutzerConfigElement(e.schluessel, e.wert)).toList());
	}


	/**
	 * Liest die Konfiguration des lokalen Notenmoduls aus der Datenbank.
	 *
	 * @return die Konfiguration des Notenmoduls
	 */
	public ENMServerConfig getConfig() {
		return transactional(() -> {
			final ENMServerConfig res = new ENMServerConfig();
			res.server.addAll(notenmodulKonfigurationServerRepository.getAll().stream().map(e -> new BenutzerConfigElement(e.schluessel, e.wert)).toList());
			res.global.addAll(notenmodulKonfigurationClientRepository.getAll().stream().map(e -> new BenutzerConfigElement(e.schluessel, e.wert)).toList());
			return res;
		});
	}


	/**
	 * Schreibt ein Konfigurationselement in die Notenmodul-Konfiguration des Servers.
	 *
	 * @param elem   das Konfigurationselement
	 */
	public void setConfigElement(final ENMServerConfigElement elem) {
		transactional(() -> {
			// Prüfe, ob das Konfigurationselement gültig ist
			if (elem == null) {
				throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde kein gültiges Konfigurationselement übergeben.");
			}

			// Wenn es sich um einen Server-Konfigurationseintrag handelt, dann füge den Konfiguationswert in der Server-Konfiguration hinzu
			if ("server".equals(elem.type)) {
				final var dto = notenmodulKonfigurationServerRepository.findById(elem.key)
						.orElse(new DTONotenmodulKonfigurationServer(elem.key, elem.value));
				dto.wert = elem.value;
				notenmodulKonfigurationServerRepository.update(dto);
				return;
			}

			// Wenn es sich um einen globalen Client-Konfigurationseintrag handelt, dann füge den Konfiguationswert in der Client-Konfiguration hinzu
			if ("global".equals(elem.type)) {
				// ... in der allgemeinen Client-Konfiguration
				final var dto = notenmodulKonfigurationClientRepository.findById(elem.key)
						.orElse(new DTONotenmodulKonfigurationClient(elem.key, elem.value));
				dto.wert = elem.value;
				notenmodulKonfigurationClientRepository.update(dto);
				return;
			}

			// Wenn ein unbekannter Typ für das Konfigurationselement übergeben wurde
			throw new ApiOperationException(Status.BAD_REQUEST, "Es wurde ein ungültiger Typ für das Konfigurations übergeben.");
		});
	}

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
		return Timestamp.valueOf(LocalDateTime.parse(iso, ofPattern));
	}


	/**
	 * Gibt den aktuellen Zeitpunkt als Timestamp zurück.
	 *
	 * @return der aktuelle Timestamp
	 */
	private static Timestamp getTimeStampNow() {
		return Timestamp.valueOf(LocalDateTime.now());
	}


	/**
	 * Prüfe, ob die zeitliche Eingabebeschränkung für den Eingabebeginn die Notenanpassung erlaubt oder nicht.
	 *
	 * @param config  die Konfiguration für die Klasse
	 * @param now     der aktuelle Zeitpunkt
	 *
	 * @throws ApiOperationException   falls die Eingabe von der Zeiteinschränkung her nicht erlaubt ist
	 */
	private static void pruefeEingabebeginn(final @NotNull ENMConfigKlasse config, final @NotNull Timestamp now) throws ApiOperationException {
		if (config.tsEingabeAb == null) {
			return;
		}
		try {
			final Timestamp beginn = getTimeStampFromIso(config.tsEingabeAb);
			if (beginn == null) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Fehlerhaftes Datumsformat beim Eingabebeginn für die Klasse mit der ID " + config.id);
			}
			if (!now.after(beginn)) {
				throw new ApiOperationException(Status.FORBIDDEN,
						"Die Eingabe ist noch nicht freigegeben. (Das Datum für den Eingabebeginn liegt in der Zukunft).");
			}
		} catch (final DateTimeParseException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"Fehlerhaftes Datumsformat beim Eingabebeginn für die Klasse mit der ID " + config.id);
		}
	}


	/**
	 * Prüfe, ob die zeitliche Eingabebeschränkung die Notenanpassung erlaubt oder nicht.
	 *
	 * @param config  die Konfiguration für die Klasse
	 * @param now     der aktuelle Zeitpunkt
	 *
	 * @throws ApiOperationException   falls die Eingabe von der Zeiteinschränkung her nicht erlaubt ist
	 */
	private static void pruefeEingabeende(final @NotNull ENMConfigKlasse config, final @NotNull Timestamp now) throws ApiOperationException {
		if (config.tsEingabeBis == null) {
			return;
		}
		try {
			final Timestamp ende = getTimeStampFromIso(config.tsEingabeBis);
			if (ende == null) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Fehlerhaftes Datumsformat beim Eingabeende für die Klasse mit der ID " + config.id);
			}
			if (!now.before(ende)) {
				throw new ApiOperationException(Status.FORBIDDEN,
						"Die Eingabe ist nicht mehr freigegeben. (Das Datum für das Eingabeende liegt in der Vergangenheit).");
			}
		} catch (final DateTimeParseException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e,
					"Fehlerhaftes Datumsformat beim Eingabeende für die Klasse mit der ID " + config.id);
		}
	}


	/**
	 * Lädt die Konfiguration, inwiefern Anpassungen an den Daten für die Klasse zulässig ist oder nicht.
	 *
	 * @param idKlasse   die ID der Klasse, für welche die Konfiguation geladen werden soll
	 *
	 * @return die Konfigurationen für die Klasse oder null, falls keine existiert
	 *
	 * @throws ApiOperationException   falls ein Fehler beim Deserialisieren der Konfiguration auftritt
	 */
	private Optional<ENMConfigKlasse> getKonfigurationErlaubt(final long idKlasse) throws ApiOperationException {
		try {
			final Optional<DTONotenmodulKonfigurationClient> config = notenmodulKonfigurationClientRepository.findById("noteneingabe.gesperrt");
			if (config.isEmpty()) {
				return Optional.empty();
			}
			final List<ENMConfigKlasse> list = JSONMapper.mapper.readerForListOf(ENMConfigKlasse.class).readValue(config.get().wert);
			if (list == null) {
				return Optional.empty();
			}
			for (final ENMConfigKlasse cfg : list) {
				if (cfg.id == idKlasse) {
					return Optional.of(cfg);
				}
			}
			return Optional.empty();
		} catch (final @NotNull IOException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, e, "Fehler beim Einlesen der Konfiguration.");
		}
	}


	private void pruefeKonfigurationPatchErlaubt(final long idKlasse, final @NotNull String attribute) throws ApiOperationException {
		final ENMConfigKlasse config = getKonfigurationErlaubt(idKlasse)
				.orElseThrow(() -> new ApiOperationException(Status.FORBIDDEN, "Es liegt keine Konfiguration für die Eingabe von Noten für diese Klasse vor."));
		// Prüfe generelle Berechtigung bei der Eingabespalte
		boolean allowed = false;
		for (final @NotNull ENMConfigKlasseSpalte col : config.spalten) {
			if (attribute.equals(col.name) && (!col.gesperrt)) {
				allowed = true;
				break;
			}
		}
		if (!allowed) {
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung wurde nicht explizit für diese Klasse erlaubt.");
		}
		// Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
		final Timestamp now = getTimeStampNow();
		pruefeEingabebeginn(config, now);
		pruefeEingabeende(config, now);
	}


	private void pruefeKonfigurationPatchErlaubtFehlstunden(final long idKlasse, final boolean istGesamtFS) throws ApiOperationException {
		final ENMConfigKlasse config = getKonfigurationErlaubt(idKlasse)
				.orElseThrow(() -> new ApiOperationException(Status.FORBIDDEN, "Es liegt keine Konfiguration für die Eingabe von Noten für diese Klasse vor."));
		// Prüfe generelle Berechtigung bei den Fehlstunden
		boolean allowed = false;
		for (final @NotNull ENMConfigKlasseSpalte col : config.spalten) {
			if ("Fehlstunden".equals(col.name) && (!col.gesperrt)) {
				allowed = true;
				break;
			}
		}
		if (!allowed) {
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung von Fehlstunden wurde nicht explizit für diese Klasse erlaubt.");
		}

		// TODO prüfe auch die Information, ob nur Gesamtfehlstunden eingegeben werden sollen oder auf Basis von Lerngruppen

		// Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
		final Timestamp now = getTimeStampNow();
		pruefeEingabebeginn(config, now);
		pruefeEingabeende(config, now);
	}


	private void pruefeKonfigurationPatchErlaubtTeilleistung(final long idKlasse, final long idTeilleistungsart) throws ApiOperationException {
		final ENMConfigKlasse config = getKonfigurationErlaubt(idKlasse)
				.orElseThrow(() -> new ApiOperationException(Status.FORBIDDEN, "Es liegt keine Konfiguration für die Eingabe von Noten für diese Klasse vor."));
		// Prüfe generelle Berechtigung bei der Eingabespalte
		boolean allowed = false;
		boolean allowedSpecial = false;
		for (final @NotNull ENMConfigKlasseSpalte col : config.spalten) {
			if ("Teilnoten".equals(col.name) && (!col.gesperrt)) {
				allowed = true;
			} else if ((col.idTeilleistung != null) && (col.idTeilleistung == idTeilleistungsart) && (!col.gesperrt)) {
				allowedSpecial = true;
			}
		}
		if (!allowed) {
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung von Teilleistungen wurde nicht explizit für diese Klasse erlaubt.");
		}
		if (!allowedSpecial) {
			throw new ApiOperationException(Status.FORBIDDEN, "Eine Änderung der Teilleistungsart wurde nicht explizit für diese Klasse erlaubt.");
		}
		// Prüfe die zeitliche Einschränkung für die Eingabe, sofern eine gesetzt wurde
		final Timestamp now = getTimeStampNow();
		pruefeEingabebeginn(config, now);
		pruefeEingabeende(config, now);
	}


	/**
	 * Prüft, ob der angemeldete Benutzer eine Berechtigung zum Patchen von Leistungsdaten hat oder nicht.
	 *
	 * @param leistungen          die Leistungsdaten.
	 * @param lernabschnitt       der Lenrabschnitt der Leistungsdaten
	 * @param authenticatedUser   der angemeldete Benutzer
	 *
	 * @return der Grund für die Berechtigung (0 - allgemeine Kompetenz, 1 - funktionsbezogen als Fachlehrer,
	 *                                         2 - funktionsbezogen als Klassenlehrer oder Abteilungsleiter)
	 */
	private static int pruefeBerechtigungPatchLeistung(final List<DTOSchuelerLeistungsdaten> leistungen, final DTOSchuelerLernabschnittsdaten lernabschnitt,
			final Benutzer authenticatedUser) {
		// Prüfe, ob der Lernabschnitt im aktuellen Schuljahresabschnitt der Schule liegt
		if (authenticatedUser.schuleGetSchuljahresabschnitt().id != lernabschnitt.Schuljahresabschnitts_ID) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Leistungsdaten sind nicht dem aktuellen Schuljahresabschnitt der Schule zugeordnet.");
		}

		// Prüfe, ob der angemeldete Benutzer eine allgemeine Berechtigung hat, um die Leistungsdaten anzupassen
		if (authenticatedUser.istAdmin() || authenticatedUser.hatVerwendeteKompetenz(BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN)) {
			return 0;
		}

		// Prüfe, ob der angemeldete Benutzer eine funktionsbezogene Berechtigung hat, um die Leistungsdaten anzupassen
		final Long idLehrer = authenticatedUser.getIdLehrer();
		if (idLehrer == null) {
			throw new ApiOperationException(Status.FORBIDDEN, "Ein funktionsbezogener Zugriff ist nur für Lehrer-Benutzer möglich.");
		}

		// Prüfe, ob er diese als Fachlehrer besitzt
		final List<Long> setFachlehrer = leistungen.stream().map(l -> l.Fachlehrer_ID).filter(l -> l != null).toList();
		if ((!setFachlehrer.isEmpty()) && (setFachlehrer.contains(idLehrer.longValue()))) {
			return 1;
		}

		// Prüfe, ob der angemeldete Lehrer als Klassenlehrer oder Abteilungsleiter die nötigen Rechte besitzt.
		if (authenticatedUser.getKlassenIDs().contains(lernabschnitt.Klassen_ID)) {
			return 2;
		}

		// ... ansonsten ist kein funktionsbezogener Zugriff erlaubt.
		throw new ApiOperationException(Status.FORBIDDEN, "Der Lehrer hat keinen funktionsbezogenen Zugriff auf die ENM-Daten.");
	}


	/**
	 * Prüft, ob der angemeldete Benutzer eine Berechtigung zum Patchen von Lernabschnittsdaten hat oder nicht.
	 *
	 * @param lernabschnitt   die Lernabschnittsdaten.
	 * @param authenticatedUser   der angemeldete Benutzer
	 *
	 * @return der Grund für die Berechtigung (0 - allgemeine Kompetenz, 2 - funktionsbezogen als Klassenlehrer oder Abteilungsleiter)
	 */
	private static int pruefeBerechtigungPatchLernabschnitt(final DTOSchuelerLernabschnittsdaten lernabschnitt, final Benutzer authenticatedUser) {
		// Prüfe, ob der Lernabschnitt im aktuellen Schuljahresabschnitt der Schule liegt
		if (authenticatedUser.schuleGetSchuljahresabschnitt().id != lernabschnitt.Schuljahresabschnitts_ID) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Lernabschnittsdaten sind nicht dem aktuellen Schuljahresabschnitt der Schule zugeordnet.");
		}

		// Prüfe, ob der angemeldete Benutzer eine allgemeine Berechtigung hat, um die Leistungsdaten anzupassen
		if (authenticatedUser.istAdmin() || authenticatedUser.hatVerwendeteKompetenz(BenutzerKompetenz.NOTENMODUL_NOTEN_AENDERN_ALLGEMEIN)) {
			return 0;
		}

		// Prüfe, ob der angemeldete Benutzer eine funktionsbezogene Berechtigung hat, um die Leistungsdaten anzupassen
		final Long idLehrer = authenticatedUser.getIdLehrer();
		if (idLehrer == null) {
			throw new ApiOperationException(Status.FORBIDDEN, "Ein funktionsbezogener Zugriff ist nur für Lehrer-Benutzer möglich.");
		}

		// Prüfe, ob der angemeldete Lehrer als Klassenlehrer oder Abteilungsleiter die nötigen Rechte besitzt.
		if (authenticatedUser.getKlassenIDs().contains(lernabschnitt.Klassen_ID)) {
			return 2;
		}

		// ... ansonsten ist kein funktionsbezogener Zugriff erlaubt.
		throw new ApiOperationException(Status.FORBIDDEN, "Der Lehrer hat keinen funktionsbezogenen Zugriff auf die ENM-Daten.");
	}


	/**
	 * Prüft, ob ein Patchen der Leistungsdaten durch den aktuell angemeldeten Benutzer erlaubt ist
	 * und passt die Leistungsdaten eines Schüler dann ggf. an.
	 *
	 * @param patch               der Patch mit den Leistungsdaten
	 * @param authenticatedUser   der angemeldete Benutzer
	 */
	public void patchLeistung(final NotenmodulLocalLeistungPatchRequest patch, final Benutzer authenticatedUser) {
		transactional(() -> {
			// Bestimme die Leistungsdaten anhand der ID des Patches
			final DTOSchuelerLeistungsdaten leistung = schuelerLeistungsdatenRepository.findById(patch.id)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Die ID %d ist für Leistungsdaten ungültig.".formatted(patch.id)));

			// Bestimme den Lernabschnitt und die Klasse des Lernabschnittes
			final DTOSchuelerLernabschnittsdaten lernabschnitt = schuelerLernabschnittRepository.findById(leistung.Abschnitt_ID)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND, "Der Lernabschnitt-ID %d existiert nicht.".formatted(leistung.Abschnitt_ID)));
			if (lernabschnitt.Klassen_ID == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Der Lernabschnitt %d ist keiner Klasse zugeordnet.".formatted(leistung.Abschnitt_ID));
			}

			// Prüfe die Berechtigung für das Patchen der Leistungsdaten
			pruefeBerechtigungPatchLeistung(List.of(leistung), lernabschnitt, authenticatedUser); // final int berechtigung =

			// Durchführen des Patches
			patch.noteQuartal.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Quartal");
				if ((val != null) && (Note.fromKuerzel(val) == Note.KEINE)) {
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Zeichenkette '%s' ist keine gültige Note.".formatted(val));
				}
				leistung.NotenKrzQuartal = val;
			});
			patch.note.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Note");
				if ((val != null) && (Note.fromKuerzel(val) == Note.KEINE)) {
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Zeichenkette '%s' ist keine gültige Note.".formatted(val));
				}
				leistung.NotenKrz = val;
			});
			patch.fehlstundenFach.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubtFehlstunden(lernabschnitt.Klassen_ID, false);
				leistung.FehlStd = val;
			});
			patch.fehlstundenUnentschuldigtFach.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubtFehlstunden(lernabschnitt.Klassen_ID, false);
				leistung.uFehlStd = val;
			});
			patch.fachbezogeneBemerkungen.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "FB");
				leistung.Lernentw = val;
			});
			patch.istGemahnt.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Mahnung");
				if ((leistung.Warndatum != null) && (!"".equals(leistung.Warndatum.trim()))) {
					throw new ApiOperationException(Status.BAD_REQUEST, "Patchen, ob gemahnt wurde, ist nicht erlaubt, da bereits ein Warndatum gesetzt ist.");
				}
				leistung.Warnung = val;
			});

			// Prüfen, ob die Werte für die Fehlstunden so zulässig sind.
			final int fs = (leistung.FehlStd == null) ? 0 : leistung.FehlStd;
			final int fsu = (leistung.uFehlStd == null) ? 0 : leistung.uFehlStd;
			if (fsu > fs) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die nicht entschuldigten Fehlstunden (%d) dürfen nicht mehr sein, als die Anzahl der Fehlstunden (%d) in dem Fach".formatted(fsu, fs));
			}

			schuelerLeistungsdatenRepository.update(leistung);
			schuelerLeistungsdatenRepository.flush();
		});
	}


	/**
	 * Prüft, ob ein Patchen der Teilleistungen durch den aktuell angemeldeten Benutzer erlaubt ist
	 * und passt die Teilleistung eines Schüler dann ggf. an.
	 *
	 * @param patch               der Patch mit den Lernabschnittsdaten
	 * @param authenticatedUser   der angemeldete Benutzer
	 */
	public void patchTeilleistung(final NotenmodulLocalTeilleistungPatchRequest patch, final Benutzer authenticatedUser) {
		transactional(() -> {
			// Bestimme die Teilleistung anhand der ID des Patches und die Leistungsdaten anhand der Teilleistung
			final DTOSchuelerTeilleistung teilleistung = schuelerTeilleistungRepository.findById(patch.id)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Die ID %d ist für Teilleistungsdaten ungültig.".formatted(patch.id)));

			// Bestimme die zugehörigen Leistungsdaten anhand der ID aus den Daten der Teilleistung
			final DTOSchuelerLeistungsdaten leistung = schuelerLeistungsdatenRepository.findById(teilleistung.Leistung_ID)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND,
									"Die ID %d ist für Leistungsdaten ungültig.".formatted(teilleistung.Leistung_ID)));

			// Bestimme den Lernabschnitt und die Klasse des Lernabschnittes
			final DTOSchuelerLernabschnittsdaten lernabschnitt = schuelerLernabschnittRepository.findById(leistung.Abschnitt_ID)
					.orElseThrow(
							() -> new ApiOperationException(Status.NOT_FOUND, "Der Lernabschnitt-ID %d existiert nicht.".formatted(leistung.Abschnitt_ID)));
			if (lernabschnitt.Klassen_ID == null) {
				throw new ApiOperationException(Status.NOT_FOUND, "Der Lernabschnitt %d ist keiner Klasse zugeordnet.".formatted(leistung.Abschnitt_ID));
			}

			// Prüfe die Berechtigung für das Patchen der Teilleistungsdaten anhand der zugehörigen Leistungsdaten
			pruefeBerechtigungPatchLeistung(List.of(leistung), lernabschnitt, authenticatedUser); // final int berechtigung =

			// Durchführen des Patches
			patch.note.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubtTeilleistung(lernabschnitt.Klassen_ID, teilleistung.Art_ID);
				if ((val != null) && (Note.fromKuerzel(val) == Note.KEINE)) {
					throw new ApiOperationException(Status.BAD_REQUEST, "Die Zeichenkette '%s' ist keine gültige Note.".formatted(val));
				}
				teilleistung.NotenKrz = val;
			});

			schuelerTeilleistungRepository.update(teilleistung);
			schuelerTeilleistungRepository.flush();
		});
	}


	/**
	 * Ermittelt für die übergebene Schüler-ID die Lernabschnittsdaten des aktuellen Schuljahresabschnittes der Schule.
	 *
	 * @param id    die ID des Schülers
	 * @param sja   der aktuelle Schuljahresabschnitt der Schule
	 *
	 * @return die Lernabschnittsdaten
	 */
	private DTOSchuelerLernabschnittsdaten getLernabschnitt(final Long id, final Schuljahresabschnitt sja) {
		// Bestimme zunächst den zugehörigen Schüler id der Datenbank
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Bei der Anfrage muss eine Schüler-ID angegeben werden.");
		}
		final DTOSchueler schueler = schuelerRepository.findById(id)
				.orElseThrow(() -> new ApiOperationException(Status.BAD_REQUEST, "Es existiert kein Schüler mit der ID %d.".formatted(id)));

		// Bestimme den Lernabschnitt mit der Wechsel-Nr 0 des Schülers in dem aktuellen Schuljahresabschnitt der Schule
		final Collection<DTOSchuelerLernabschnittsdaten> slas =
				schuelerLernabschnittRepository.getMapBySchuelerIDsAndSchuljahreabschnitt(List.of(schueler.ID), sja.id).values();
		if (slas.isEmpty()) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es existiert kein Lernabschnitt im aktuellen Schuljahresabschnitt der Schule für den Schüler mit der ID %d.".formatted(id));
		}
		if (slas.size() > 1) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Es gibt mehrere Lernabschnitte im aktuellen Schuljahresabschnitt der Schule für den Schüler mit der ID %d und der WechselNr 0."
							.formatted(id));
		}
		return slas.iterator().next();
	}


	/**
	 * Prüft, ob ein Patchen der Bemerkungen zu einem Schüler-Lernabschnitt durch den aktuell angemeldeten
	 * Benutzer erlaubt ist und passt die Bemerkungen dann ggf. an.
	 *
	 * @param id                  die ID des Schülers, dessen Bemerkungen angepasst werden sollen
	 * @param patch               der Patch zu den Bemerkungen
	 * @param authenticatedUser   der angemeldete Benutzer
	 */
	public void patchBemerkungen(final Long id, final NotenmodulLocalLeistungBemerkungenPatchRequest patch, final Benutzer authenticatedUser) {
		transactional(() -> {
			// Bestimme den Lernabschnitt des Schülers im aktuellen Schuljahresabschnitt der Schule.
			final DTOSchuelerLernabschnittsdaten lernabschnitt = getLernabschnitt(id, authenticatedUser.schuleGetSchuljahresabschnitt());

			// Prüfe die Berechtigung für das Patchen der Bemerkungen anhand des Lernabschnittes des Schülers
			pruefeBerechtigungPatchLernabschnitt(lernabschnitt, authenticatedUser); // final int berechtigung =

			// Bestimme die Bemerkungen, welche dem Schüler zugeordnet sind.
			final var sbs = schuelerLernabschnittBemerkungRepository.findMapByLernabschnittID(List.of(lernabschnitt.ID)).values();
			if (sbs.size() > 1) {
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Es gibt mehrere Einträge für Fachbemerkungen zu dem Lernabschnitt mit der ID %d.".formatted(lernabschnitt.ID));
			}
			final DTOSchuelerPSFachBemerkungen sb = (sbs.isEmpty())
					? schuelerLernabschnittBemerkungRepository.create(new DTOSchuelerPSFachBemerkungen(-1, lernabschnitt.ID))
					: sbs.iterator().next();

			// Durchführen des Patches
			patch.ASV.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "ASV");
				sb.ASV = val;
			});
			patch.AUE.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "AUE");
				sb.AUE = val;
			});
			patch.ZB.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "ZB");
				lernabschnitt.ZeugnisBem = val;
			});
			patch.LELS.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "LELS");
				sb.LELS = val;
			});
			patch.schulformEmpf.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "SchulformEmpfehlung");
				sb.ESF = val;
			});
			patch.individuelleVersetzungsbemerkungen.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Versetzungsbemerkungen");
				sb.BemerkungVersetzung = val;
			});
			patch.foerderbemerkungen.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubt(lernabschnitt.Klassen_ID, "Förderbemerkungen");
				sb.BemerkungFSP = val;
			});

			schuelerLernabschnittRepository.update(lernabschnitt);
			schuelerLernabschnittBemerkungRepository.update(sb);
			schuelerLernabschnittBemerkungRepository.flush();
		});
	}


	/**
	 * Prüft, ob ein Patchen eines Schüler-Lernabschnittes durch den aktuell angemeldeten
	 * Benutzer erlaubt ist und passt diesen dann ggf. an.
	 *
	 * @param patch               der Patch zu dem Lernabschnitt
	 * @param authenticatedUser   der angemeldete Benutzer
	 */
	public void patchLernabschnitt(final NotenmodulLocalLernabschnittPatchRequest patch, final Benutzer authenticatedUser) {
		transactional(() -> {
			// Bestimme den Lernabschnitt des Schülers im aktuellen Schuljahresabschnitt der Schule.
			final DTOSchuelerLernabschnittsdaten lernabschnitt = schuelerLernabschnittRepository.findById(patch.id)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Der Lernabschnitt-ID %d existiert nicht.".formatted(patch.id)));

			// Prüfe die Berechtigung für das Patchen der Bemerkungen anhand des Lernabschnittes des Schülers
			pruefeBerechtigungPatchLernabschnitt(lernabschnitt, authenticatedUser); // final int berechtigung =

			// Durchführen des Patches
			patch.fehlstundenGesamt.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubtFehlstunden(lernabschnitt.Klassen_ID, true);
				lernabschnitt.SumFehlStd = val;
			});
			patch.fehlstundenGesamtUnentschuldigt.ifPresent(val -> {
				pruefeKonfigurationPatchErlaubtFehlstunden(lernabschnitt.Klassen_ID, true);
				lernabschnitt.SumFehlStdU = val;
			});

			// Prüfen, ob die Werte für die Fehlstunden so zulässig sind.
			final int fs = (lernabschnitt.SumFehlStd == null) ? 0 : lernabschnitt.SumFehlStd;
			final int fsu = (lernabschnitt.SumFehlStdU == null) ? 0 : lernabschnitt.SumFehlStdU;
			if (fsu > fs) {
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Die nicht entschuldigten Fehlstunden (%d) dürfen nicht mehr sein, als die Anzahl der Fehlstunden (%d) in dem Fach".formatted(fsu, fs));
			}

			schuelerLernabschnittRepository.update(lernabschnitt);
			schuelerLernabschnittRepository.flush();
		});
	}


	/**
	 * Prüft, ob ein Patchen einer Schüler-Ankreuzkompetenz durch den aktuell angemeldeten
	 * Benutzer erlaubt ist und passt diese dann ggf. an.
	 *
	 * @param patch               der Patch zu der Ankreuzkompetenz
	 * @param authenticatedUser   der angemeldete Benutzer
	 */
	public void patchAnkreuzkompetenz(final NotenmodulLocalAnkreuzkompetenzPatchRequest patch, final Benutzer authenticatedUser) {
		transactional(() -> {
			// Bestimme die Ankreuzkompetenz des Schülers im aktuellen Schuljahresabschnitt der Schule.
			final DTOSchuelerAnkreuzfloskeln schuelerankreuzkompetenz = schuelerAnkreuzkompetenzRepository.findById(patch.id)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Die Schüler-Ankreuzkompetenz-ID %d existiert nicht.".formatted(patch.id)));

			final DTOAnkreuzfloskeln ankreuzkompetenz = ankreuzkompetenzRepository.findById(schuelerankreuzkompetenz.Floskel_ID)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND,
							"Die Ankreuzkompetenz-ID %d existiert nicht.".formatted(schuelerankreuzkompetenz.Floskel_ID)));

			final DTOSchuelerLernabschnittsdaten lernabschnitt = schuelerLernabschnittRepository.findById(schuelerankreuzkompetenz.Abschnitt_ID)
					.orElseThrow(() -> new ApiOperationException(Status.NOT_FOUND, "Der Lernabschnitt-ID %d existiert nicht.".formatted(patch.id)));

			final List<DTOSchuelerLeistungsdaten> leistungsdaten =
					schuelerLeistungsdatenRepository.findListByLernabschnittAndFach(List.of(lernabschnitt.ID), List.of(ankreuzkompetenz.Fach_ID));

			// Prüfe die Berechtigung für das Patchen der Bemerkungen anhand des Lernabschnittes des Schülers
			pruefeBerechtigungPatchLeistung(leistungsdaten, lernabschnitt, authenticatedUser); // final int berechtigung =

			// Durchführen des Patches
			schuelerankreuzkompetenz.Stufe1 = patch.stufen[0];
			schuelerankreuzkompetenz.Stufe2 = patch.stufen[1];
			schuelerankreuzkompetenz.Stufe3 = patch.stufen[2];
			schuelerankreuzkompetenz.Stufe4 = patch.stufen[3];
			schuelerankreuzkompetenz.Stufe5 = patch.stufen[4];
			schuelerAnkreuzkompetenzRepository.update(schuelerankreuzkompetenz);
			schuelerAnkreuzkompetenzRepository.flush();
		});
	}

}
