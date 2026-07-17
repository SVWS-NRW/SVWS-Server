package de.svws_nrw.service.gost;

import static de.svws_nrw.data.TransactionSupport.transactional;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.svws_nrw.asd.data.schueler.Sprachbelegung;
import de.svws_nrw.asd.data.schueler.Sprachpruefung;
import de.svws_nrw.asd.data.schule.SchuleStammdaten;
import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.core.adt.map.HashMap2D;
import de.svws_nrw.core.data.gost.AbiturFachbelegung;
import de.svws_nrw.core.data.gost.Abiturdaten;
import de.svws_nrw.core.data.gost.GostFach;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Fachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Schueler;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachbelegung;
import de.svws_nrw.core.data.gost.laufbahnplanung.v1.GostLaufbahnplanungExportV1Sprachpruefung;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.core.types.gost.GostHalbjahr;
import de.svws_nrw.core.types.gost.GostKursart;
import de.svws_nrw.core.utils.gost.GostFaecherManager;
import de.svws_nrw.db.dto.current.gost.DTOGostSchueler;
import de.svws_nrw.db.dto.current.gost.DTOGostSchuelerFachbelegungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.repo.benutzer.BenutzerAllgemeinRepository;
import de.svws_nrw.repo.gost.GostSchuelerFachbelegungenRepository;
import de.svws_nrw.repo.gost.GostSchuelerRepository;
import de.svws_nrw.repo.schueler.SchuelerRepository;
import de.svws_nrw.service.crypto.SchuelerCredentialsService;
import jakarta.ws.rs.core.Response.Status;

/**
 * Ein Service für den Import von Laufbahnplanungsdaten in der Version 1
 */
public class GostLaufbahnplanungImportV1Service {

	private final BenutzerAllgemeinRepository benutzerRepository;
	private final SchuelerRepository schuelerRepository;
	private final GostSchuelerRepository gostSchuelerRepository;
	private final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository;

	private final SchuelerCredentialsService schuelerCredentialsService;
	private final GostAbiturdatenService gostAbiturdatenService;
	private final GostFaecherService gostFaecherService;

	/**
	 * Erstellt einen neuen Service.
	 *
	 * @param benutzerRepository                        das Repository für den Zugriff auf Benutzerdaten und den angemeldeten Benutzer
	 * @param schuelerRepository                        das Repository für den Zugriff auf die Schülerdaten
	 * @param gostSchuelerRepository                    das Repository für den Zugriff auf die Schülerdaten mit Bezug zur gymnasialen Oberstufe
	 * @param gostSchuelerFachbelegungenRepository      das Repository für den Zugriff auf die Schüler-Fachbelegungen
	 * @param schuelerCredentialsService                das Repository für den Zugriff auf die Schüler-Credentials
	 * @param gostAbiturdatenService                    der Service für den Zugriff auf die Abiturdaten der gymnasialen Oberstufe
	 * @param gostFaecherService                        der Service für den Zugruff auf die Fächerdaten eines Abiturjahrganges der gymnasialen Oberstufe
	 */
	public GostLaufbahnplanungImportV1Service(final BenutzerAllgemeinRepository benutzerRepository,
			final SchuelerRepository schuelerRepository,
			final GostSchuelerRepository gostSchuelerRepository,
			final GostSchuelerFachbelegungenRepository gostSchuelerFachbelegungenRepository,
			final SchuelerCredentialsService schuelerCredentialsService,
			final GostAbiturdatenService gostAbiturdatenService,
			final GostFaecherService gostFaecherService) {
		this.benutzerRepository = benutzerRepository;
		this.schuelerRepository = schuelerRepository;
		this.gostSchuelerRepository = gostSchuelerRepository;
		this.gostSchuelerFachbelegungenRepository = gostSchuelerFachbelegungenRepository;
		this.schuelerCredentialsService = schuelerCredentialsService;
		this.gostAbiturdatenService = gostAbiturdatenService;
		this.gostFaecherService = gostFaecherService;
	}


	/**
	 * Importiert die übergebenen Daten, welche als Liste mit Objekten im Export-Format der Laufbahnplanung vorliegen.
	 *
	 * @param daten    die Importe im Export-Format der Laufbahnplanung
	 * @param logger   das Logger-Objekt, um den Import-Vorgan mit zu protokollieren
	 */
	public void doImport(final GostLaufbahnplanungExportV1 daten, final Logger logger) {
		transactional(() -> {
			final boolean success = this.doImportInternal(List.of(daten), logger);
			if (!success) {
				// Eine Exception ist hier nötig, damit ein Rollback stattfindet - Diese kann im Controller gefangen werden...
				throw new ApiOperationException(Status.BAD_REQUEST, "Fehler wurde bereits protokolliert.");
			}
		});
	}

	/**
	 * Importiert die übergebenen Daten, welche als Liste mit Objekten im Export-Format der Laufbahnplanung vorliegen.
	 *
	 * @param daten    die Importe im Export-Format der Laufbahnplanung
	 * @param logger   das Logger-Objekt, um den Import-Vorgan mit zu protokollieren
	 */
	public void doImport(final Collection<GostLaufbahnplanungExportV1> daten, final Logger logger) {
		transactional(() -> {
			final boolean success = this.doImportInternal(daten, logger);
			if (!success) {
				// Eine Exception ist hier nötig, damit ein Rollback stattfindet - Diese kann im Controller gefangen werden...
				throw new ApiOperationException(Status.BAD_REQUEST, "Fehler wurde bereits protokolliert.");
			}
		});
	}


	private boolean doImportInternal(final Collection<GostLaufbahnplanungExportV1> daten, final Logger logger) {
		// Prüfe, ob die aktuelle Schule eine Gymnasiale Oberstufe hat oder nicht
		if (!benutzerRepository.getAktuellerBenutzer().schuleHatGymOb()) {
			logger.logLn("Fehler: Die Schulform der Schule hat keine gymnasiale Oberstufe.");
			return false;
		}

		// Prüfe zunächst die Dateien anhand der Schulnummer, ob sie zu der Schule passen und ob die Anzahl der Schüler in den Dateien genau einer ist
		final SchuleStammdaten schule = benutzerRepository.getAktuellerBenutzer().schuleGetStammdaten();
		for (final GostLaufbahnplanungExportV1 laufbahnplanungsdaten : daten) {
			// Schulnummer prüfen
			if (laufbahnplanungsdaten.schulNr != schule.schulNr) {
				logger.logLn("Fehler: Die Schulnummer der Planungsdatei simmt nicht mit der Schulnummer der Datenbank überein.");
				return false;
			}

			// Anzahl der Schüler prüfen
			if (laufbahnplanungsdaten.schueler.size() != 1) {
				logger.log("Es wurde keiner oder mehr als ein Schüler-Eintrag in einer lp-Datei gefunden. Dies ist nicht zulässig.");
				return false;
			}
		}

		// Prüfe, ob die Schüler aus den Import-Dateien existieren
		final List<Long> idsSchueler = daten.stream().map(lp -> lp.schueler.getFirst().id).distinct().toList();
		final Map<Long, DTOSchueler> mapSchueler = schuelerRepository.findMapByIds(idsSchueler);
		if (mapSchueler.size() != daten.size()) {
			logger.log("Die Anzahl der gefundenden Schüler stimmt nicht mit der Anzahl der Import-Dateien überein. Dies ist nicht zulässig.");
			return false;
		}

		// Lade die AES-Schlüssel für die Überprüfung der Schüler-ID beim Import
		final Map<Long, AES> mapAES = schuelerCredentialsService.getOrCreateMap(idsSchueler);
		if (mapAES.size() != daten.size()) {
			logger.log(
					"Es konnte nicht für alle Importe ein Verschlüsselungsobjekt erstellt werden. Ein Import ist in diesem Fall nicht mögliche. Der Import wird abgebrochen.");
			return false;
		}

		// Lese die GOST-spezifischen Informationen der Schüler ein
		final Map<Long, DTOGostSchueler> mapGostSchueler = gostSchuelerRepository.findMapByIds(idsSchueler);
		if (mapGostSchueler.size() != daten.size()) {
			logger.log(
					"Die Anzahl der gefundenden Schüler mit Informationen zur gymnasialen Oberstufe stimmt nicht mit der Anzahl der Import-Dateien überein. Dies ist nicht zulässig.");
			return false;
		}

		// Lade die Abiturdaten zu den Schülern
		final Map<Long, Abiturdaten> mapAbiturdaten = gostAbiturdatenService.getMap(idsSchueler);
		if (mapAbiturdaten.size() != daten.size()) {
			logger.log(
					"Die Anzahl der ermittelten Abiturdaten zur gymnasialen Oberstufe stimmt nicht mit der Anzahl der Import-Dateien überein. Dies ist nicht zulässig.");
			return false;
		}
		final List<Integer> abiturjahrgaenge = mapAbiturdaten.values().stream().map(a -> a.abiturjahr).filter(a -> a != null).distinct().toList();
		final Map<Integer, GostFaecherManager> mapGostFaechermanager = gostFaecherService.getMapGostFaecherManager(abiturjahrgaenge, false);

		// Lade die Fachbelegungen, die ggf. aktualisiert werden müssen
		final HashMap2D<Long, Long, DTOGostSchuelerFachbelegungen> mapSchuelerFachbelegungen =
				gostSchuelerFachbelegungenRepository.getMap2DBySchuelerIDAndFachID(idsSchueler);

		// Führe die einzelnen Importe mithilfe der zuvor geladenen Daten aus...
		for (final GostLaufbahnplanungExportV1 laufbahnplanungsdaten : daten) {
			final long idSchueler = laufbahnplanungsdaten.schueler.getFirst().id;
			final DTOSchueler schueler = mapSchueler.get(idSchueler);
			final AES aes = mapAES.get(idSchueler);
			final DTOGostSchueler gostSchueler = mapGostSchueler.get(idSchueler);
			final Abiturdaten abidaten = mapAbiturdaten.get(idSchueler);
			final GostFaecherManager gostFaechermanager = mapGostFaechermanager.get(abidaten.abiturjahr);
			Map<Long, DTOGostSchuelerFachbelegungen> mapFachbelegungen = mapSchuelerFachbelegungen.getSubMapOrNull(idSchueler);
			if (mapFachbelegungen == null) {
				mapFachbelegungen = new HashMap<>();
			}
			final boolean success =
					doImportSingleInternal(laufbahnplanungsdaten, logger, schueler, aes, gostSchueler, abidaten, gostFaechermanager, mapFachbelegungen);
			if (!success) {
				logger.log("Fehler beim Import des Schülers mit der ID %d. Breche den Import ab.".formatted(idSchueler));
				return false;
			}
		}
		gostSchuelerRepository.flush();
		return true;
	}


	private boolean doImportSingleInternal(final GostLaufbahnplanungExportV1 laufbahnplanungsdaten, final Logger logger,
			final DTOSchueler dtoSchueler, final AES aes, final DTOGostSchueler gostSchueler, final Abiturdaten abidaten,
			final GostFaecherManager gostFaecher, final Map<Long, DTOGostSchuelerFachbelegungen> mapFachwahlen) {
		// Prüfe zunächst, ob der Abiturjahrgang in der Datenbank existiert und mit dem des Schülers übereinstimmt
		if (abidaten.abiturjahr != laufbahnplanungsdaten.abiturjahr) {
			logger.logLn("Fehler: Der Abiturjahrgang der Planungsdatei stimmt nicht mit dem Abiturjahrgang des Schülers überein.");
			return false;
		}

		// Bestimme die Daten des Schülers in den Laufbahnplanungsdaten
		final GostLaufbahnplanungExportV1Schueler daten = laufbahnplanungsdaten.schueler.stream().filter(s -> s.id == dtoSchueler.ID).findFirst().orElse(null);
		if (daten == null) {
			logger.logLn("Fehler: Die Laufbahnplanungsdatei enthält keinen Schüler mit der ID " + dtoSchueler.ID + ".");
			return false;
		}

		// Überprüfe, ob die Schüler-ID in den Laufbahnplanungsdaten manipuliert wurde und damit eine falsch Zuordnung vorliegen würde
		try {
			final long idDec = ByteBuffer.wrap(aes.decryptBase64(daten.idEnc)).getLong();
			if (idDec != daten.id) {
				logger.logLn("Fehler: Die ID des Schülers wurde verändert oder der AES-Schlüssel in der Datenbank wurde zwischenzeitlich angepasst. Die Daten können daher nicht geladen werden.");
				return false;
			}
		} catch (@SuppressWarnings("unused") final AESException e) {
			logger.logLn("Fehler: Die ID des Schülers wurde verändert oder der AES-Schlüssel in der Datenbank wurde zwischenzeitlich angepasst. Die Daten können daher nicht geladen werden.");
			return false;
		}

		// Prüfe den Bilingualen Bildungsgang
		if (((daten.bilingualeSprache == null) && (abidaten.bilingualeSprache != null))
				|| ((daten.bilingualeSprache != null) && (abidaten.bilingualeSprache == null))
				|| ((daten.bilingualeSprache != null) && !daten.bilingualeSprache.equals(abidaten.bilingualeSprache))) {
			logger.logLn("Hinweis: Die Angaben zum Bilingualen Bildungsgang stimmen nicht überein.");
		}
		// Überprüfe die Sprachenfolge
		if (abidaten.sprachendaten.belegungen.size() != daten.sprachendaten.belegungen.size()) {
			logger.logLn("Hinweis: Die Anzahl der Sprachbelegungen stimmen nicht überein.");
		}
		if (abidaten.sprachendaten.pruefungen.size() != daten.sprachendaten.pruefungen.size()) {
			logger.logLn("Hinweis: Die Anzahl der Sprachprüfungen stimmen nicht überein.");
		}
		final Map<String, Sprachbelegung> sprachBelegungen = abidaten.sprachendaten.belegungen.stream().collect(Collectors.toMap(b -> b.sprache, b -> b));
		for (final GostLaufbahnplanungExportV1Sprachbelegung belegung : daten.sprachendaten.belegungen) {
			final Sprachbelegung vergleich = sprachBelegungen.get(belegung.sprache);
			if (vergleich == null) {
				logger.logLn("Hinweis: Die Sprachbelegung für die Sprache " + belegung.sprache + " wurde in der Datenbank nicht gefunden.");
				continue;
			}
			final boolean vglReihenfolge = ((belegung.reihenfolge == null) && (vergleich.reihenfolge == null))
					|| ((belegung.reihenfolge != null) && (vergleich.reihenfolge != null)
							&& (belegung.reihenfolge.intValue() == vergleich.reihenfolge.intValue()));
			final boolean vglVonJg = ((belegung.belegungVonJahrgang == null) && (vergleich.belegungVonJahrgang == null))
					|| ((belegung.belegungVonJahrgang != null) && (vergleich.belegungVonJahrgang != null)
							&& (belegung.belegungVonJahrgang.equals(vergleich.belegungVonJahrgang)));
			final boolean vglVonAbschnitt = ((belegung.belegungVonAbschnitt == null) && (vergleich.belegungVonAbschnitt == null))
					|| ((belegung.belegungVonAbschnitt != null) && (vergleich.belegungVonAbschnitt != null)
							&& (belegung.belegungVonAbschnitt.equals(vergleich.belegungVonAbschnitt)));
			if (!vglReihenfolge || !vglVonJg || !vglVonAbschnitt) {
				logger.logLn("Hinweis: Die Sprachbelegung für die Sprache " + belegung.sprache + " stimmt nicht mit der Eintragung in der Datenbank überein.");
			}
		}
		for (final GostLaufbahnplanungExportV1Sprachpruefung pruefung : daten.sprachendaten.pruefungen) {
			boolean found = false;
			for (final Sprachpruefung vergleich : abidaten.sprachendaten.pruefungen) {
				if (Objects.equals(pruefung.sprache, vergleich.sprache)) {
					final boolean vglNiveau = ((pruefung.anspruchsniveauId == null) && (vergleich.anspruchsniveauId == null))
							|| ((pruefung.anspruchsniveauId != null) && (vergleich.anspruchsniveauId != null)
									&& (pruefung.anspruchsniveauId.intValue() == vergleich.anspruchsniveauId.intValue()));
					final boolean vglErsSprache = ((pruefung.ersetzteSprache == null) && (vergleich.ersetzteSprache == null))
							|| ((pruefung.ersetzteSprache != null) && (vergleich.ersetzteSprache != null)
									&& (pruefung.ersetzteSprache.equals(vergleich.ersetzteSprache)));
					if (vglNiveau && vglErsSprache
							&& (pruefung.kannErstePflichtfremdspracheErsetzen == vergleich.kannErstePflichtfremdspracheErsetzen)
							&& (pruefung.kannZweitePflichtfremdspracheErsetzen == vergleich.kannZweitePflichtfremdspracheErsetzen)
							&& (pruefung.kannWahlpflichtfremdspracheErsetzen == vergleich.kannWahlpflichtfremdspracheErsetzen)
							&& (pruefung.kannBelegungAlsFortgefuehrteSpracheErlauben == vergleich.kannBelegungAlsFortgefuehrteSpracheErlauben)) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				logger.logLn("Hinweis: Eine Sprachprüfung für die Sprache %s wurde in der Datenbank nicht gefunden.".formatted(pruefung.sprache));
			}
		}
		// Prüfe die Fachbelegungen bei den Fachbelegungen, wo bereits Leistungsdaten in der Datenbank hinterlegt sind und übernehme die restlichen Fachwahlen
		final Map<Long, AbiturFachbelegung> dbBelegungen = abidaten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		final Map<Long, GostLaufbahnplanungExportV1Fachbelegung> dateiBelegungen =
				daten.fachbelegungen.stream().collect(Collectors.toMap(b -> b.fachID, b -> b));
		for (final Long idFach : dateiBelegungen.keySet()) {
			final GostFach fach = gostFaecher.get(idFach);
			if (fach == null) {
				logger.logLn(
						"Fehler: Das Fach mit der ID %d wird in der Datei verwendet, existiert aber nicht als Fach der gymnasialen Oberstufe in der Datenbank."
								.formatted(idFach));
				return false;
			}
		}
		Set<Long> beide = dbBelegungen.keySet().stream().filter(dateiBelegungen::containsKey).collect(Collectors.toSet());
		final Set<Long> nurDB = dbBelegungen.keySet().stream().filter(id -> !dateiBelegungen.containsKey(id)).collect(Collectors.toSet());
		final Set<Long> nurDatei = dateiBelegungen.keySet().stream().filter(id -> !dbBelegungen.containsKey(id)).collect(Collectors.toSet());
		// ... erster Durchgang: Zulässigkeit der Daten in der Datei prüfen
		final HashSet<Long> tmp = new HashSet<>();
		for (final Long idFach : beide) {
			// Prüfe, ob sich Fachbelegungen in Halbjahren unterscheiden, die bereits Leistungsdaten enthalten
			final AbiturFachbelegung db = dbBelegungen.get(idFach);
			final GostLaufbahnplanungExportV1Fachbelegung datei = dateiBelegungen.get(idFach);
			boolean identisch = true;
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				final String dbKursart = (db.belegungen[halbjahr.id] == null) ? null : db.belegungen[halbjahr.id].kursartKuerzel;
				final boolean dbSchriftlich = (db.belegungen[halbjahr.id] != null) && db.belegungen[halbjahr.id].schriftlich;
				final boolean istGleich = ((dbKursart == null) && (datei.kursart[halbjahr.id] == null))
						|| ((dbKursart != null) && (datei.kursart[halbjahr.id] != null)
								&& (dbKursart.equals(datei.kursart[halbjahr.id])) && (dbSchriftlich == datei.schriftlich[halbjahr.id]));
				if (abidaten.bewertetesHalbjahr[halbjahr.id]) {
					if (!istGleich) {
						logger.logLn(
								"Fehler: Das Halbjahr %s ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung muss hier für einen Import übereinstimmen."
										.formatted(halbjahr.kuerzel));
						return false;
					}
					continue;
				}
				if (!istGleich) {
					identisch = false;
					break;
				}
			}
			if (!identisch || ((db.abiturFach != null) && !db.abiturFach.equals(datei.abiturFach)) || ((db.abiturFach == null) && (datei.abiturFach != null))) {
				tmp.add(idFach);
			}
		}
		beide = tmp;
		for (final Long idFach : nurDatei) {
			// Prüfe, ob Fachbelegungen zu einem Halbjahr hinzugefügt werden sollen, die bereits Leistungsdaten enthalten
			final GostLaufbahnplanungExportV1Fachbelegung datei = dateiBelegungen.get(idFach);
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if ((abidaten.bewertetesHalbjahr[halbjahr.id]) && (datei.kursart[halbjahr.id] != null)) {
					logger.logLn(
							"Fehler: Das Halbjahr %s ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung kann hier für einen Import keine Fachwahlen ergänzen."
									.formatted(halbjahr.kuerzel));
					return false;
				}
			}
		}
		for (final Long idFach : nurDB) {
			// Prüfe, ob Fachbelegungen aus Halbjahres entfernt werden sollen, die bereits Leistungsdaten enthalten
			final AbiturFachbelegung db = dbBelegungen.get(idFach);
			for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
				if ((abidaten.bewertetesHalbjahr[halbjahr.id]) && (db.belegungen[halbjahr.id] != null)) {
					logger.logLn(
							"Fehler: Das Halbjahr %s ist in der Datenbank bereits in den Leistungsdaten enthalten. Die Laufbahnplanung kann hier für einen Import keine Fachwahlen entfernen."
									.formatted(halbjahr.kuerzel));
					return false;
				}
			}
		}
		// ... zweiter Durchgang: Anpassungen der Fachwahlen in der Datenbank durchführen
		final HashSet<Long> alle = new HashSet<>();
		alle.addAll(beide);
		alle.addAll(nurDB);
		alle.addAll(nurDatei);
		if (!alle.isEmpty()) {
			final ArrayList<DTOGostSchuelerFachbelegungen> fachwahlenGeaendert = new ArrayList<>();
			for (final Long idFach : Stream.concat(beide.stream(), nurDatei.stream()).collect(Collectors.toSet())) {
				final GostLaufbahnplanungExportV1Fachbelegung datei = dateiBelegungen.get(idFach);
				DTOGostSchuelerFachbelegungen fachwahl = mapFachwahlen.get(idFach);
				// Ergänze ggf. Fachwahl-Einträge, welche zwar durch Leistungsdaten bestehen, aber nicht wirklich in der DB abgelegt sind.
				if (fachwahl == null) {
					fachwahl = new DTOGostSchuelerFachbelegungen(dtoSchueler.ID, idFach);
				}
				for (final GostHalbjahr halbjahr : GostHalbjahr.values()) {
					final String dateiKursart = datei.kursart[halbjahr.id];
					final String kursart = (dateiKursart == null) ? null
							: ("AT".equals(dateiKursart) ? "AT"
									: (GostKursart.LK.kuerzel.equals(dateiKursart) ? "LK"
											: (GostKursart.ZK.kuerzel.equals(dateiKursart) ? "ZK"
													: (GostKursart.PJK.kuerzel.equals(dateiKursart) ? "M"
															: (GostKursart.VTF.kuerzel.equals(dateiKursart) ? "M"
																	: (datei.schriftlich[halbjahr.id] ? "S" : "M"))))));
					switch (halbjahr) {
						case EF1 -> fachwahl.EF1_Kursart = kursart;
						case EF2 -> fachwahl.EF2_Kursart = kursart;
						case Q11 -> fachwahl.Q11_Kursart = kursart;
						case Q12 -> fachwahl.Q12_Kursart = kursart;
						case Q21 -> fachwahl.Q21_Kursart = kursart;
						case Q22 -> fachwahl.Q22_Kursart = kursart;
					}
				}
				fachwahl.AbiturFach = datei.abiturFach;
				fachwahlenGeaendert.add(fachwahl);
			}
			if (!fachwahlenGeaendert.isEmpty()) {
				gostSchuelerFachbelegungenRepository.update(fachwahlenGeaendert);
			}
			for (final Long idFach : nurDB) {
				final DTOGostSchuelerFachbelegungen fachwahl = mapFachwahlen.get(idFach);
				gostSchuelerFachbelegungenRepository.delete(fachwahl);
			}
			// Und setzen des Rücklaufdatums
			gostSchueler.DatumRuecklauf = DateTimeFormatter.ISO_DATE.format(LocalDate.now(ZoneId.of("Europe/Berlin")));
			gostSchuelerRepository.update(gostSchueler);
		} else {
			logger.logLn("Keine Änderungen für den Schüler mit der ID " + dtoSchueler.ID + " gegenüber der Datenbank in der Datei enthalten.");
		}
		return true;
	}

}
