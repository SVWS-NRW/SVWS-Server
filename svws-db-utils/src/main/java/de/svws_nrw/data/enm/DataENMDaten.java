package de.svws_nrw.data.enm;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.base.compression.CompressionException;
import de.svws_nrw.core.data.enm.ENMDaten;
import de.svws_nrw.core.data.enm.ENMFach;
import de.svws_nrw.core.data.enm.ENMFloskel;
import de.svws_nrw.core.data.enm.ENMFloskelgruppe;
import de.svws_nrw.core.data.enm.ENMJahrgang;
import de.svws_nrw.core.data.enm.ENMKlasse;
import de.svws_nrw.core.data.enm.ENMLeistung;
import de.svws_nrw.core.data.enm.ENMLerngruppe;
import de.svws_nrw.core.data.enm.ENMSchueler;
import de.svws_nrw.core.data.enm.ENMTeilleistung;
import de.svws_nrw.core.data.enm.ENMTeilleistungsart;
import de.svws_nrw.asd.data.kurse.ZulaessigeKursartKatalogEintrag;
import de.svws_nrw.asd.types.schueler.SchuelerStatus;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.kurse.ZulaessigeKursart;
import de.svws_nrw.core.types.oauth2.OAuth2ServerTyp;
import de.svws_nrw.core.utils.enm.ENMDatenManager;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.data.oauth2.DataOauthClientSecrets;
import de.svws_nrw.data.oauth2.OAuth2Client;
import de.svws_nrw.data.schule.DBUtilsSchule;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.faecher.DTOFach;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFloskelgruppen;
import de.svws_nrw.db.dto.current.schild.katalog.DTOFloskeln;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassenLeitung;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKurs;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLeistungsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerTeilleistung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOTeilleistungsarten;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.dto.current.svws.auth.DTOSchuleOAuthSecrets;
import de.svws_nrw.db.dto.current.svws.enm.DTOEnmLeistungsdaten;
import de.svws_nrw.db.dto.current.svws.enm.DTOEnmLernabschnittsdaten;
import de.svws_nrw.db.dto.current.svws.enm.DTOEnmTeilleistungen;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.db.utils.dto.enm.DTOENMLehrerSchuelerAbschnittsdaten;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link ENMDaten}.
 */
public final class DataENMDaten extends DataManager<Long> {

	private static final String ENM_UPLOAD_PATH = "/api/secure/import";
	private static final String ENM_DOWNLOAD_PATH = "/api/secure/export";
	private static final String ENM_TRUNCATE_PATH = "/api/secure/truncate";


	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link ENMDaten}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataENMDaten(final DBEntityManager conn) {
		super(conn);
	}


	@Override
	public Response getAll() throws ApiOperationException {
		final ENMDaten daten = getDaten(conn, null);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Gibt die ENM-Daten als mit GZIP komprimiertes JSON-Objekt zurück.
	 *
	 * @return das GZIP-komprimierte JSON-Objekt.
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getAllGZip() throws ApiOperationException {
		final ENMDaten daten = getDaten(conn, null);
		return JSONMapper.gzipFileResponseFromObject(daten, "enm.json.gz");
	}


	/**
	 * Gibt die ENM-Daten aller Lehrer als mit GZIP komprimiertes byte[] zurück.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return das GZIP-komprimierte byte[].
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static byte[] getAllGZIPBytes(final DBEntityManager conn) throws ApiOperationException {
		final ENMDaten daten = getDaten(conn, null);
		try {
			return JSONMapper.gzipByteArrayFromObject(daten);
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce);
		}
	}


	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}


	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final ENMDaten daten = getDaten(conn, id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}



	/**
	 * Ermittelt die ENM-Daten zu dem Lehrer mit der angegebenen ID.
	 * Ist die ID null so werden die ENM-Daten für alle Lehrer des aktuellen
	 * Schuljahresabschnitts generiert.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Lehrers oder null
	 *
	 * @return die ENMDaten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static ENMDaten getDaten(final DBEntityManager conn, final Long id) throws ApiOperationException {
		// Lese die Daten aus der Datenbank ein
		final DTOEigeneSchule schule = DBUtilsSchule.get(conn);
		final DTOSchuljahresabschnitte abschnitt = getSchuljahresabschnitt(conn, schule);
		final Map<Long, DTOLehrer> mapLehrer = getLehrerListe(conn);
		final DTOLehrer dtoLehrer = (id == null) ? null : mapLehrer.get(id);   // Ermittle den Lehrer nur, falls ENM-Daten für einen speziellen Lehrer bestimt werden.
		if ((id != null) && (dtoLehrer == null))
			throw new ApiOperationException(Status.NOT_FOUND);
		final Map<Long, DTOSchueler> mapSchueler = getSchuelerListe(conn, schule, abschnitt);
		final Map<Long, DTOFach> mapFaecher = getFaecherListe(conn);
		final Map<Long, DTOJahrgang> mapJahrgaenge = getJahrgangsListe(conn);
		final Map<String, DTOKlassen> mapKlassen = getKlassenListe(conn, schule);
		final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung = getKlassenleitungen(conn, mapKlassen);
		final Map<Long, DTOKurs> mapKurse = getKurse(conn, schule);
		final Map<Long, DTOTeilleistungsarten> mapTeilleistungsarten =
				conn.queryAll(DTOTeilleistungsarten.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));

		// Erstelle einen ENM-Daten-Manager und füge ggf. den Lehrer hinzu für welchen die ENM-Daten erzeugt werden
		final ENMDatenManager manager = new ENMDatenManager(id);
		if (dtoLehrer != null)
			manager.addLehrer(manager.daten.lehrerID, dtoLehrer.Kuerzel, dtoLehrer.Nachname, dtoLehrer.Vorname, dtoLehrer.Geschlecht,
					dtoLehrer.eMailDienstlich);
		initManager(manager, schule, abschnitt);

		// Aggregiert aus den Schülerabschnittsdaten und -leistungsdaten die einzelnen Informationen für das ENM.
		// Dabei wird unter anderem auch ermittelt, welche Kataloginformationen (Klassen, Lehrer, Jahrgänge)
		// bei den Daten für das ENM einzutragen sind.
		final List<DTOENMLehrerSchuelerAbschnittsdaten> schuelerabschnitte = (dtoLehrer == null)
				? DTOENMLehrerSchuelerAbschnittsdaten.queryAll(conn, schule.Schuljahresabschnitts_ID)
				: DTOENMLehrerSchuelerAbschnittsdaten.query(conn, schule.Schuljahresabschnitts_ID, dtoLehrer.Kuerzel);
		// Bestimme zunächst noch eine Übersicht zu den Einzelleistungen zu den Leistungsdaten
		final List<Long> idsLeistungen = schuelerabschnitte.stream().map(a -> a.leistungID).toList();
		final List<DTOSchuelerTeilleistung> listTeilleistungen = idsLeistungen.isEmpty() ? new ArrayList<>()
				: conn.queryList(DTOSchuelerTeilleistung.QUERY_LIST_BY_LEISTUNG_ID, DTOSchuelerTeilleistung.class, idsLeistungen);
		final Map<Long, List<DTOSchuelerTeilleistung>> mapTeilleistungen = listTeilleistungen.stream().collect(Collectors.groupingBy(st -> st.Leistung_ID));
		final List<Long> idsTeilleistungen = listTeilleistungen.stream().map(t -> t.ID).toList();
		final Map<Long, DTOEnmTeilleistungen> mapTeilleistungenTimestamps = idsTeilleistungen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOEnmTeilleistungen.class, idsTeilleistungen).stream().collect(Collectors.toMap(t -> t.ID, t -> t));
		for (final DTOENMLehrerSchuelerAbschnittsdaten schuelerabschnitt : schuelerabschnitte) {
			final DTOKlassen dtoKlasse = mapKlassen.get(schuelerabschnitt.klasse);
			if (dtoKlasse == null)
				throw new NullPointerException();
			ENMKlasse enmKlasse = manager.getKlasse(dtoKlasse.ID);
			if (enmKlasse == null) {
				manager.addKlasse(dtoKlasse.ID, dtoKlasse.ASDKlasse, dtoKlasse.Klasse, dtoKlasse.Jahrgang_ID, dtoKlasse.Sortierung);
				enmKlasse = manager.getKlasse(dtoKlasse.ID);
				final List<DTOKlassenLeitung> klassenleitungen = mapKlassenLeitung.get(dtoKlasse.ID);
				if (klassenleitungen != null) {
					for (final DTOKlassenLeitung kl : klassenleitungen) {
						if (manager.getLehrer(kl.Lehrer_ID) == null) {
							final DTOLehrer dtoKlassenlehrer = mapLehrer.get(kl.Lehrer_ID);
							if (dtoKlassenlehrer != null) {
								manager.addLehrer(dtoKlassenlehrer.ID, dtoKlassenlehrer.Kuerzel,
										dtoKlassenlehrer.Nachname, dtoKlassenlehrer.Vorname, dtoKlassenlehrer.Geschlecht, dtoKlassenlehrer.eMailDienstlich);
							}
						}
						enmKlasse.klassenlehrer.add(kl.Lehrer_ID);
					}
				}
			}

			final ZulaessigeKursart kursart = (schuelerabschnitt.kursID == null) ? null : ZulaessigeKursart.data().getWertByKuerzel(schuelerabschnitt.kursart);

			ENMSchueler enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
			if (enmSchueler == null) {
				final var dtoSchueler = mapSchueler.get(schuelerabschnitt.schuelerID);
				ENMJahrgang enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
				if (enmJahrgang == null) {
					final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(schuelerabschnitt.jahrgangID);
					if (dtoJahrgang == null)
						throw new NullPointerException();
					manager.addJahrgang(dtoJahrgang.ID, dtoJahrgang.ASDJahrgang, dtoJahrgang.InternKrz,
							dtoJahrgang.ASDBezeichnung, dtoJahrgang.Sekundarstufe, dtoJahrgang.Sortierung);
					enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
				}

				manager.addSchueler(dtoSchueler.ID, schuelerabschnitt.jahrgangID, dtoKlasse.ID, dtoSchueler.Nachname, dtoSchueler.Vorname,
						dtoSchueler.Geschlecht, schuelerabschnitt.BilingualerZweig, schuelerabschnitt.ZieldifferentesLernen,
						dtoSchueler.Lernstandsbericht);  // Deutsch als Fremdsprache liegt vor, wenn für den Schüler Lernstandsberichte geschrieben werden...
				enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
				enmSchueler.lernabschnitt.id = schuelerabschnitt.abschnittID;
				enmSchueler.lernabschnitt.fehlstundenGesamt = schuelerabschnitt.fehlstundenSummeGesamt;
				enmSchueler.lernabschnitt.tsFehlstundenGesamt = schuelerabschnitt.tsFehlstundenSummeGesamt;
				enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt = schuelerabschnitt.fehlstundenSummeUnentschuldigt;
				enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt = schuelerabschnitt.tsFehlstundenSummeUnentschuldigt;
				enmSchueler.lernabschnitt.pruefungsordnung = schuelerabschnitt.pruefungsordnung;
				enmSchueler.lernabschnitt.lernbereich1note = (schuelerabschnitt.lernbereich1notenKuerzel == null)
						|| schuelerabschnitt.lernbereich1notenKuerzel.isBlank()
								? null : schuelerabschnitt.lernbereich1notenKuerzel;
				enmSchueler.lernabschnitt.lernbereich2note = (schuelerabschnitt.lernbereich2notenKuerzel == null)
						|| schuelerabschnitt.lernbereich2notenKuerzel.isBlank()
								? null : schuelerabschnitt.lernbereich2notenKuerzel;
				enmSchueler.lernabschnitt.foerderschwerpunkt1 = schuelerabschnitt.foerderschwerpunkt1Kuerzel;
				enmSchueler.lernabschnitt.foerderschwerpunkt2 = schuelerabschnitt.foerderschwerpunkt2Kuerzel;
				enmSchueler.bemerkungen.ASV = schuelerabschnitt.ASV;
				enmSchueler.bemerkungen.tsASV = schuelerabschnitt.tsASV;
				enmSchueler.bemerkungen.AUE = schuelerabschnitt.AUE;
				enmSchueler.bemerkungen.tsAUE = schuelerabschnitt.tsAUE;
				enmSchueler.bemerkungen.ZB = schuelerabschnitt.zeugnisBemerkungen;
				enmSchueler.bemerkungen.tsZB = schuelerabschnitt.tsZeugnisBemerkungen;
				enmSchueler.bemerkungen.LELS = schuelerabschnitt.LELS;
				// TODO Schulform-Empfehlung enmSchueler.bemerkungen.schulformEmpf = ...
				enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen = schuelerabschnitt.bemerkungVersetzung;
				enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen = schuelerabschnitt.tsBemerkungVersetzung;
				enmSchueler.bemerkungen.foerderbemerkungen = schuelerabschnitt.bemerkungFSP;
			}

			// Hier wird die temporäre LerngruppenID erstellt, mit der in der Klasse ENMDaten gearbeitet wird.
			// Es wird eine eindeutige ID benötigt, die Kurs- und Klassenübergreifend diese Lerngruppe identifiziert.
			final String strLerngruppenID = (schuelerabschnitt.kursID == null)
					? ("Klasse:" + schuelerabschnitt.klasse + "/" + schuelerabschnitt.fachID)
					: ("Kurs:" + schuelerabschnitt.kursID);
			ENMLerngruppe lerngruppe = manager.getLerngruppe(strLerngruppenID);
			if (lerngruppe == null) {
				final DTOFach fach = mapFaecher.get(schuelerabschnitt.fachID);
				final String kursartAllg;
				if (kursart == null)
					kursartAllg = null;
				else {
					final ZulaessigeKursartKatalogEintrag kursartEintrag = kursart.daten(abschnitt.Jahr);
					if ((kursartEintrag.kuerzelAllg == null) || "".equals(kursartEintrag.kuerzelAllg))
						kursartAllg = kursartEintrag.kuerzel;
					else
						kursartAllg = kursartEintrag.kuerzelAllg;
				}
				// Fach zu ENMDaten hinzufügen ?
				if (manager.getFach(fach.ID) == null)
					manager.addFach(fach.ID, fach.StatistikKuerzel, fach.Kuerzel, fach.SortierungAllg, fach.IstFremdsprache);
				// Unterscheidung zwischen den beiden Lerngruppen-Typen...
				if (schuelerabschnitt.kursID == null) {  // es ist eine Klasse
					manager.addLerngruppe(strLerngruppenID, dtoKlasse.ID, schuelerabschnitt.fachID, null,
							fach.Kuerzel, kursartAllg, fach.Unterichtssprache,
							(schuelerabschnitt.wochenstunden == null) ? 0 : schuelerabschnitt.wochenstunden);
				} else {  // es ist ein Kurs
					final DTOKurs kurs = mapKurse.get(schuelerabschnitt.kursID);
					manager.addLerngruppe(strLerngruppenID, schuelerabschnitt.kursID, schuelerabschnitt.fachID,
							(kursart == null) ? -1 : Integer.parseInt(kursart.daten(abschnitt.Jahr).nummer), kurs.KurzBez, kursartAllg,
							fach.Unterichtssprache, kurs.WochenStd);
				}
				lerngruppe = manager.getLerngruppe(strLerngruppenID);
				lerngruppe.lehrerID.add(schuelerabschnitt.lehrerID);
				if (manager.getLehrer(schuelerabschnitt.lehrerID) == null) {
					final DTOLehrer dtoFachlehrer = mapLehrer.get(schuelerabschnitt.lehrerID);
					if (dtoFachlehrer != null) {
						manager.addLehrer(dtoFachlehrer.ID, dtoFachlehrer.Kuerzel,
								dtoFachlehrer.Nachname, dtoFachlehrer.Vorname, dtoFachlehrer.Geschlecht, dtoFachlehrer.eMailDienstlich);
					}
				}
				// TODO ggf. im Team-Teaching unterrichtende Lehrer hinzufügen (Zusatzkraft in Leistungsdaten bzw. weitere Lehrkraft bei Kursen)
			}

			final int halbjahr = (schule.AnzahlAbschnitte == 4) ? (abschnitt.Abschnitt / 2) : abschnitt.Abschnitt;
			final boolean istSchriftlich = (schuelerabschnitt.kursID != null)
					&& ((kursart == ZulaessigeKursart.LK1) || (kursart == ZulaessigeKursart.LK2)
							|| (kursart == ZulaessigeKursart.GKS) || (kursart == ZulaessigeKursart.AB3)
							|| ((kursart == ZulaessigeKursart.AB4) && (!("Q2".equals(dtoKlasse.ASDKlasse) && (halbjahr == 2)))));

			final String tmpAbiFach = schuelerabschnitt.AbiturFach;
			final Integer abiFach = (tmpAbiFach == null) ? null : switch (tmpAbiFach) {
				case "1" -> 1;
				case "2" -> 2;
				case "3" -> 3;
				case "4" -> 4;
				default -> null;
			};
			final boolean istGemahnt = (schuelerabschnitt.istGemahnt != null) && schuelerabschnitt.istGemahnt;
			final String mahndatum = schuelerabschnitt.mahndatum;
			final ENMLeistung enmLeistung = manager.addSchuelerLeistungsdaten(enmSchueler,
					schuelerabschnitt.leistungID, lerngruppe.id, schuelerabschnitt.noteKuerzel, schuelerabschnitt.tsNote,
					schuelerabschnitt.noteKuerzelQuartal, schuelerabschnitt.tsNoteQuartal, istSchriftlich, abiFach,
					schuelerabschnitt.fehlstundenGesamt, schuelerabschnitt.tsFehlstundenGesamt,
					schuelerabschnitt.fehlstundenUnentschuldigt, schuelerabschnitt.tsFehlstundenUnentschuldigt,
					schuelerabschnitt.fachbezogeneBemerkungen, schuelerabschnitt.tsFachbezogeneBemerkungen, null,
					istGemahnt, schuelerabschnitt.tsIstGemahnt, mahndatum);

			// Teilleistungen und deren Arten hinzufügen
			final List<DTOSchuelerTeilleistung> teilleistungen = mapTeilleistungen.get(schuelerabschnitt.leistungID);
			if (teilleistungen != null) {
				for (final DTOSchuelerTeilleistung teilleistung : teilleistungen) {
					if (teilleistung.Art_ID == null)
						continue;
					// Prüfe die Teilleistungsart und ergänze sie ggf.
					ENMTeilleistungsart enmTeilleistungsart = manager.getTeilleistungsart(teilleistung.Art_ID);
					if (enmTeilleistungsart == null) {
						final DTOTeilleistungsarten dtoArt = mapTeilleistungsarten.get(teilleistung.Art_ID);
						if (dtoArt == null) // DB-Error -> should not happen
							throw new NullPointerException();
						manager.addTeilleistungsart(dtoArt.ID, dtoArt.Bezeichnung,
								(dtoArt.Sortierung == null) ? 32000 : dtoArt.Sortierung,
								(dtoArt.Gewichtung == null) ? 1.0 : dtoArt.Gewichtung);
						enmTeilleistungsart = manager.getTeilleistungsart(teilleistung.Art_ID);
					}
					// Füge die Teilleistung hinzu
					final DTOEnmTeilleistungen teilleistungTimestamps = mapTeilleistungenTimestamps.get(teilleistung.ID);
					if (teilleistungTimestamps == null)
						throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
								"Es konnten keine Zeitstempel für die Teilleistungen ausgelesen werden. Dies deutet auf einen Fehler in der Datenbank hin.");
					manager.addSchuelerTeilleistung(enmLeistung, teilleistung.ID, teilleistung.Art_ID, teilleistungTimestamps.tsArt_ID,
							teilleistung.Datum, teilleistungTimestamps.tsDatum, teilleistung.Bemerkung, teilleistungTimestamps.tsBemerkung,
							teilleistung.NotenKrz, teilleistungTimestamps.tsNotenKrz);
				}
			}
			// TODO check and add ZP10 - Data
			// TODO check and add BKAbschluss - Data
		}

		// Kopiere den Floskel-Katalog in die ENM-Daten
		getFloskeln(conn, manager, mapJahrgaenge);

		return manager.daten;
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}


	private static void initManager(final ENMDatenManager manager, final DTOEigeneSchule schule, final DTOSchuljahresabschnitte abschnitt) {
		// Setze die grundlegenden Schuldaten
		manager.setSchuldaten(schule.SchulNr, abschnitt.Jahr, schule.AnzahlAbschnitte, abschnitt.Abschnitt,
				/* TODO */ null, /* TODO */ true, /* TODO */ false, /* TODO */ true,
				schule.SchulformKuerzel, /* TODO */ null);
		// Kopiere den Noten-Katalog aus dem Core-type in die ENM-Daten
		manager.addNoten(abschnitt.Jahr);
		// Kopiere den Förderschwerpunkt-Katalog aus dem Core-type in die ENM-Daten
		manager.addFoerderschwerpunkte(abschnitt.Jahr, Schulform.data().getWertByKuerzel(schule.SchulformKuerzel));
	}

	private static DTOSchuljahresabschnitte getSchuljahresabschnitt(final DBEntityManager conn, final DTOEigeneSchule schule) throws ApiOperationException {
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return abschnitt;
	}

	// TODO Optimierung: Lese nur die aktiven Lehrer aus der Datenbank aus.
	private static Map<Long, DTOLehrer> getLehrerListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return lehrer.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	private static Map<Long, DTOSchueler> getSchuelerListe(final DBEntityManager conn, final DTOEigeneSchule schule, final DTOSchuljahresabschnitte abschnitt)
			throws ApiOperationException {
		final List<DTOSchueler> schueler = conn.queryList(DTOSchueler.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOSchueler.class, schule.Schuljahresabschnitts_ID);
		if (schueler.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return schueler.stream()
				.filter(s -> (s.idStatus == SchuelerStatus.AKTIV.daten(abschnitt.Jahr).id) || (s.idStatus == SchuelerStatus.EXTERN.daten(abschnitt.Jahr).id))
				.collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	private static Map<Long, DTOFach> getFaecherListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
	}

	private static Map<Long, DTOJahrgang> getJahrgangsListe(final DBEntityManager conn) throws ApiOperationException {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
	}

	private static Map<String, DTOKlassen> getKlassenListe(final DBEntityManager conn, final DTOEigeneSchule schule) throws ApiOperationException {
		final List<DTOKlassen> klassen = conn.queryList(DTOKlassen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKlassen.class, schule.Schuljahresabschnitts_ID);
		if (klassen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return klassen.stream().collect(Collectors.toMap(e -> e.Klasse, e -> e));
	}

	private static Map<Long, List<DTOKlassenLeitung>> getKlassenleitungen(final DBEntityManager conn, final Map<String, DTOKlassen> mapKlassen) {
		final List<Long> klassenIDs = mapKlassen.values().stream().map(kl -> kl.ID).toList();
		return conn.queryList(DTOKlassenLeitung.QUERY_LIST_BY_KLASSEN_ID, DTOKlassenLeitung.class, klassenIDs).stream()
				.collect(Collectors.groupingBy(kl -> kl.Klassen_ID));
	}

	private static Map<Long, DTOKurs> getKurse(final DBEntityManager conn, final DTOEigeneSchule schule) throws ApiOperationException {
		final List<DTOKurs> kurse = conn.queryList(DTOKurs.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOKurs.class, schule.Schuljahresabschnitts_ID);
		if (kurse == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return conn.queryAll(DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
	}


	/**
	 * Füllt die Datenstruktur für die Floskelgruppen des ENM mit den in der SVWS-DB hinterlegten
	 * Floskelgruppen und den zugehörigen Floskeln.
	 *
	 * @param conn            die Datenbank-Verbindung
	 * @param manager         der ENM-Daten-Manager
	 * @param mapJahrgaenge   eine Map mit den jeweiligen Jahrgängen
	 */
	private static void getFloskeln(final DBEntityManager conn, final ENMDatenManager manager, final Map<Long, DTOJahrgang> mapJahrgaenge) {
		final Map<String, DTOJahrgang> mapJG = mapJahrgaenge.values().stream().collect(Collectors.toMap(j -> j.ASDJahrgang, j -> j));
		final List<DTOFloskelgruppen> dtoFloskelgruppen = conn.queryAll(DTOFloskelgruppen.class);
		final HashMap<String, ENMFloskelgruppe> map = new HashMap<>();
		for (final DTOFloskelgruppen dto : dtoFloskelgruppen) {
			final ENMFloskelgruppe enmFG = new ENMFloskelgruppe();
			enmFG.kuerzel = dto.Kuerzel;
			enmFG.bezeichnung = dto.Bezeichnung;
			enmFG.hauptgruppe = dto.Hauptgruppe;
			map.put(enmFG.kuerzel, enmFG);
			manager.daten.floskelgruppen.add(enmFG);
		}
		final List<DTOFloskeln> dtoFloskeln = conn.queryAll(DTOFloskeln.class);
		for (final DTOFloskeln dto : dtoFloskeln) {
			final ENMFach fach = manager.getFachByKuerzel(dto.FloskelFach);
			final ENMFloskel enmFl = new ENMFloskel();
			enmFl.kuerzel = dto.Kuerzel;
			enmFl.text = dto.FloskelText;
			enmFl.fachID = (fach == null) ? null : fach.id;
			try {
				enmFl.niveau = Long.parseLong(dto.FloskelNiveau);
			} catch (@SuppressWarnings("unused") final NumberFormatException e) {
				enmFl.niveau = null;
			}
			final DTOJahrgang jg = mapJG.get(dto.FloskelJahrgang);
			enmFl.jahrgangID = (jg == null) ? null : jg.ID;
			final ENMFloskelgruppe enmFG = map.get(dto.FloskelGruppe);
			if (enmFG != null) {
				enmFG.floskeln.add(enmFl);
			}
			// TODO else ... Fehlerbehandlung: Wie ordnet man die Floskel zu? -> allgemein
		}
	}


	/**
	 * Integriert die Veränderungen bei den importierten ENM-Daten gegenüber dem Stand
	 * der SVWS-DB in die SVWS-DB.
	 *
	 * @param conn    die Datenbank-Verbindung
	 * @param daten   die importierten ENMDaten GZIP-komprimiert als Byte-Array
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void importDatenGZip(final DBEntityManager conn, final byte[] daten) throws ApiOperationException {
		try {
			importDaten(conn, JSONMapper.toObjectGZip(daten, ENMDaten.class));
		} catch (final CompressionException e) {
			throw new ApiOperationException(Status.BAD_REQUEST, e, "Fehler bei entpacken der komprimierten ENM-Daten: " + e.getMessage());
		}
	}


	/**
	 * Integriert die Veränderungen bei den importierten ENM-Daten gegenüber dem Stand
	 * der SVWS-DB in die SVWS-DB.
	 *
	 * @param conn    die Datenbank-Verbindung
	 * @param daten   die importierten Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void importDaten(final DBEntityManager conn, final ENMDaten daten) throws ApiOperationException {
		// TODO Prüfe (daten.lehrer == null) - dann ist es eine Gesamt-ENM-Datei und nicht lehrerspezifisch
		// TODO Führe eine Prüfung der Integrität der Daten durch
		importEnmSchueler(conn, daten.schueler);
	}


	/**
	 * Importiert die gegebenen ENMSchueler-Daten in die SVWS-Datenbank. Prüft dazu die Zeitstempel
	 * der einzelnen Felder und aktualisiert neuere Datensätze und deren Zeitstempel.
	 *
	 * @param conn              die Datenbank-Verbindung
	 * @param listEnmSchueler   die zu importierenden Schüler
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void importEnmSchueler(final DBEntityManager conn, final List<ENMSchueler> listEnmSchueler) throws ApiOperationException {
		DBUtilsSchule.get(conn);

		// Bestimme die Schüler-IDs, anhand der zu importierenden Daten
		if (listEnmSchueler.isEmpty())
			return; // nichts zu tun, da keine Daten vorhanden sind
		final List<Long> idsSchueler = listEnmSchueler.stream().map(s -> s.id).distinct().toList();
		if (idsSchueler.size() != listEnmSchueler.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ENM-Daten haben mindestens eine Schüler-ID doppelt enthalten. Dies ist nicht zulässig.");
		final Map<Long, DTOSchueler> mapSchuelerDTOs = conn.queryByKeyList(DTOSchueler.class,
				idsSchueler).stream().collect(Collectors.toMap(s -> s.ID, s -> s));
		if (mapSchuelerDTOs.keySet().size() != idsSchueler.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Schüler in den ENM-Daten konnten auch in der Datenbank gefunden werden.");

		// Bestimme die Schüler-Lernabschnittsdaten anhand der IDs aus den zu importierenden Daten und validiere die Schuljahresabschnitts-ID
		final List<Long> idsLernabschnitte = listEnmSchueler.stream().map(s -> s.lernabschnitt.id).distinct().toList();
		if (idsLernabschnitte.size() != idsSchueler.size())
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ENM-Daten enthalten nicht genügend Lernabschnitte. Dies ist nicht zulässig.");
		final List<DTOSchuelerLernabschnittsdaten> listLernabschnitte = conn.queryByKeyList(DTOSchuelerLernabschnittsdaten.class, idsLernabschnitte);
		if (listLernabschnitte.size() != idsLernabschnitte.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Lernabschnitte aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTOSchuelerLernabschnittsdaten> mapLernabschnitte = listLernabschnitte.stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		// Bestimme die Fachbemerkungen zu den Lernabschnitten und erzeuge eine Map für die Abschnitts-ID
		final List<DTOSchuelerPSFachBemerkungen> listLernabschnittsbemerkungen = conn.queryList(DTOSchuelerPSFachBemerkungen.QUERY_LIST_BY_ABSCHNITT_ID,
				DTOSchuelerPSFachBemerkungen.class, idsLernabschnitte);
		final Map<Long, DTOSchuelerPSFachBemerkungen> mapLernabschnittsbemerkungen = listLernabschnittsbemerkungen.isEmpty() ? new HashMap<>()
				: listLernabschnittsbemerkungen.stream().collect(Collectors.toMap(b -> b.Abschnitt_ID, b -> b));

		// Bestimme die Schüler-Leistungsdaten in der DB anhand der IDs auf dem Import
		final List<ENMLeistung> enmLeistungen = listEnmSchueler.stream().flatMap(s -> s.leistungsdaten.stream()).toList();
		final List<Long> idsLeistungen = enmLeistungen.stream().map(l -> l.id).toList();
		final List<DTOSchuelerLeistungsdaten> listLeistungen = idsLeistungen.isEmpty() ? new ArrayList<>()
				: conn.queryByKeyList(DTOSchuelerLeistungsdaten.class, idsLeistungen);
		if (listLeistungen.size() != enmLeistungen.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Leistungsdaten aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTOSchuelerLeistungsdaten> mapLeistungen = listLeistungen.stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		// Bestimme die alle Teilleistungen von Schülern in der DB anhand des Imports
		final List<ENMTeilleistung> enmTeilleistungen = enmLeistungen.stream().flatMap(l -> l.teilleistungen.stream()).toList();
		final List<Long> idsTeilleistungen = enmTeilleistungen.stream().map(l -> l.id).toList();
		final List<DTOSchuelerTeilleistung> listTeilleistungen = idsTeilleistungen.isEmpty() ? new ArrayList<>()
				: conn.queryByKeyList(DTOSchuelerTeilleistung.class, idsTeilleistungen);
		if (listTeilleistungen.size() != enmTeilleistungen.size())
			throw new ApiOperationException(Status.NOT_FOUND, "Nicht alle Teilleistungen aus den ENM-Daten konnten auch in der Datenbank gefunden werden.");
		final Map<Long, DTOSchuelerTeilleistung> mapTeilleistungen = listTeilleistungen.stream().collect(Collectors.toMap(l -> l.ID, l -> l));

		// Bestimme die ENM-Timestamps für die Lernabschnitte, die Leistungen und die Teilleistungen
		final Map<Long, DTOEnmLernabschnittsdaten> mapLernabschnitteTimestamps = idsLernabschnitte.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOEnmLernabschnittsdaten.class, idsLernabschnitte).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final Map<Long, DTOEnmLeistungsdaten> mapLeistungenTimestamps = idsLeistungen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOEnmLeistungsdaten.class, idsLeistungen).stream().collect(Collectors.toMap(l -> l.ID, l -> l));
		final Map<Long, DTOEnmTeilleistungen> mapTeilleistungenTimestamps = idsTeilleistungen.isEmpty() ? new HashMap<>()
				: conn.queryByKeyList(DTOEnmTeilleistungen.class, idsTeilleistungen).stream().collect(Collectors.toMap(t -> t.ID, t -> t));

		// Sets, um die veränderten DTOs zwischenzuspeichern und später in einem Rutsch in der DB zu persistieren
		final Set<DTOSchuelerLernabschnittsdaten> setLernabschnitte = new HashSet<>();
		final Set<DTOSchuelerPSFachBemerkungen> setLernabschnittsbemerkungen = new HashSet<>();
		final Set<DTOSchuelerPSFachBemerkungen> setLernabschnittsbemerkungenNeu = new HashSet<>();
		final Set<DTOEnmLernabschnittsdaten> setLernabschnitteTimestamps = new HashSet<>();
		final Set<DTOSchuelerLeistungsdaten> setLeistungen = new HashSet<>();
		final Set<DTOEnmLeistungsdaten> setLeistungenTimestamps = new HashSet<>();
		final Set<DTOSchuelerTeilleistung> setTeilleistungen = new HashSet<>();
		final Set<DTOEnmTeilleistungen> setTeilleistungenTimestamps = new HashSet<>();
		long idNeueFachbemerkung = conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class);

		// Durchwandere die importierten ENM-Daten und gleiche diese mit den Daten in der Datenbank ab.
		for (final ENMSchueler enmSchueler : listEnmSchueler) {
			final DTOSchuelerLernabschnittsdaten lernabschnitt = mapLernabschnitte.get(enmSchueler.lernabschnitt.id);
			final DTOEnmLernabschnittsdaten lernabschnittTS = mapLernabschnitteTimestamps.get(enmSchueler.lernabschnitt.id);
			boolean neuBemerkungen = false;
			DTOSchuelerPSFachBemerkungen lernabschnittsbemerkungen = mapLernabschnittsbemerkungen.get(enmSchueler.lernabschnitt.id);
			if (lernabschnittsbemerkungen == null) {
				lernabschnittsbemerkungen = new DTOSchuelerPSFachBemerkungen(idNeueFachbemerkung++, enmSchueler.lernabschnitt.id);
				neuBemerkungen = true;
			}

			boolean updatedBemerkungen = false;
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsASV, lernabschnittTS.tsASV)) {
				lernabschnittsbemerkungen.ASV = enmSchueler.bemerkungen.ASV;
				lernabschnittTS.tsASV = enmSchueler.bemerkungen.tsASV;
				updatedBemerkungen = true;
			}
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsAUE, lernabschnittTS.tsAUE)) {
				lernabschnittsbemerkungen.AUE = enmSchueler.bemerkungen.AUE;
				lernabschnittTS.tsAUE = enmSchueler.bemerkungen.tsAUE;
				updatedBemerkungen = true;
			}
			if (neuBemerkungen || isTimestampAfter(enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen, lernabschnittTS.tsBemerkungVersetzung)) {
				lernabschnittsbemerkungen.BemerkungVersetzung = enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen;
				lernabschnittTS.tsBemerkungVersetzung = enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen;
				updatedBemerkungen = true;
			}

			boolean updatedLernabschnitt = false;
			if (isTimestampAfter(enmSchueler.bemerkungen.tsZB, lernabschnittTS.tsZeugnisBem)) {
				lernabschnitt.ZeugnisBem = enmSchueler.bemerkungen.ZB;
				lernabschnittTS.tsZeugnisBem = enmSchueler.bemerkungen.tsZB;
				updatedLernabschnitt = true;
			}
			if (isTimestampAfter(enmSchueler.lernabschnitt.tsFehlstundenGesamt, lernabschnittTS.tsSumFehlStd)) {
				lernabschnitt.SumFehlStd = enmSchueler.lernabschnitt.fehlstundenGesamt;
				lernabschnittTS.tsSumFehlStd = enmSchueler.lernabschnitt.tsFehlstundenGesamt;
				updatedLernabschnitt = true;
			}
			if (isTimestampAfter(enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt, lernabschnittTS.tsSumFehlStdU)) {
				lernabschnitt.SumFehlStdU = enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt;
				lernabschnittTS.tsSumFehlStdU = enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt;
				updatedLernabschnitt = true;
			}

			if (updatedBemerkungen && !neuBemerkungen)
				setLernabschnittsbemerkungen.add(lernabschnittsbemerkungen);
			if (neuBemerkungen)
				setLernabschnittsbemerkungenNeu.add(lernabschnittsbemerkungen);
			if (updatedLernabschnitt)
				setLernabschnitte.add(lernabschnitt);
			if (updatedBemerkungen || updatedLernabschnitt)
				setLernabschnitteTimestamps.add(lernabschnittTS);

			for (final ENMLeistung enmLeistung : enmSchueler.leistungsdaten) {
				final DTOSchuelerLeistungsdaten leistung = mapLeistungen.get(enmLeistung.id);
				final DTOEnmLeistungsdaten leistungTS = mapLeistungenTimestamps.get(enmLeistung.id);

				boolean updatedLeistung = false;
				if (isTimestampAfter(enmLeistung.tsFachbezogeneBemerkungen, leistungTS.tsLernentw)) {
					leistung.Lernentw = enmLeistung.fachbezogeneBemerkungen;
					leistungTS.tsLernentw = enmLeistung.tsFachbezogeneBemerkungen;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsFehlstundenFach, leistungTS.tsFehlStd)) {
					leistung.FehlStd = enmLeistung.fehlstundenFach;
					leistungTS.tsFehlStd = enmLeistung.tsFehlstundenFach;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsFehlstundenUnentschuldigtFach, leistungTS.tsuFehlStd)) {
					leistung.uFehlStd = enmLeistung.fehlstundenUnentschuldigtFach;
					leistungTS.tsuFehlStd = enmLeistung.tsFehlstundenUnentschuldigtFach;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsIstGemahnt, leistungTS.tsWarnung)) {
					leistung.Warnung = enmLeistung.istGemahnt;
					leistungTS.tsWarnung = enmLeistung.tsIstGemahnt;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsNote, leistungTS.tsNotenKrz)) {
					leistung.NotenKrz = enmLeistung.note;
					leistungTS.tsNotenKrz = enmLeistung.tsNote;
					updatedLeistung = true;
				}
				if (isTimestampAfter(enmLeistung.tsNoteQuartal, leistungTS.tsNotenKrzQuartal)) {
					leistung.NotenKrzQuartal = enmLeistung.noteQuartal;
					leistungTS.tsNotenKrzQuartal = enmLeistung.tsNoteQuartal;
					updatedLeistung = true;
				}

				if (updatedLeistung) {
					setLeistungen.add(leistung);
					setLeistungenTimestamps.add(leistungTS);
				}

				for (final ENMTeilleistung enmTeilleistung : enmLeistung.teilleistungen) {
					final DTOSchuelerTeilleistung teilleistung = mapTeilleistungen.get(enmTeilleistung.id);
					final DTOEnmTeilleistungen teilleistungTS = mapTeilleistungenTimestamps.get(enmTeilleistung.id);

					boolean updatedTeilleistung = false;
					if (isTimestampAfter(enmTeilleistung.tsArtID, teilleistungTS.tsArt_ID)) {
						teilleistung.Art_ID = enmTeilleistung.artID;
						teilleistungTS.tsArt_ID = enmTeilleistung.tsArtID;
						updatedTeilleistung = true;
					}
					if (isTimestampAfter(enmTeilleistung.tsDatum, teilleistungTS.tsDatum)) {
						teilleistung.Datum = enmTeilleistung.datum;
						teilleistungTS.tsDatum = enmTeilleistung.tsDatum;
						updatedTeilleistung = true;
					}
					if (isTimestampAfter(enmTeilleistung.tsBemerkung, teilleistungTS.tsBemerkung)) {
						teilleistung.Bemerkung = enmTeilleistung.bemerkung;
						teilleistungTS.tsBemerkung = enmTeilleistung.tsBemerkung;
						updatedTeilleistung = true;
					}
					if (isTimestampAfter(enmTeilleistung.tsNote, teilleistungTS.tsNotenKrz)) {
						teilleistung.NotenKrz = enmTeilleistung.note;
						teilleistungTS.tsNotenKrz = enmTeilleistung.tsNote;
						updatedTeilleistung = true;
					}

					if (updatedTeilleistung) {
						setTeilleistungen.add(teilleistung);
						setTeilleistungenTimestamps.add(teilleistungTS);
					}
				}
			}
		}
		if (!setLernabschnittsbemerkungenNeu.isEmpty())
			conn.transactionPersistAll(setLernabschnittsbemerkungenNeu);
		if (!setLernabschnittsbemerkungen.isEmpty())
			conn.transactionPersistAll(setLernabschnittsbemerkungen);
		if (!setLernabschnitte.isEmpty())
			conn.transactionPersistAll(setLernabschnitte);
		if (!setLeistungen.isEmpty())
			conn.transactionPersistAll(setLeistungen);
		if (!setTeilleistungen.isEmpty())
			conn.transactionPersistAll(setTeilleistungen);
		conn.transactionFlush();

		// TODO Prüfen, ob das Neusetzen der Timestamps nicht einen Konflikt mit den Triggern durch die Persistierung oben hervorruft...
		if (!setLernabschnitteTimestamps.isEmpty())
			conn.transactionPersistAll(setLernabschnitteTimestamps);
		if (!setLeistungenTimestamps.isEmpty())
			conn.transactionPersistAll(setLeistungenTimestamps);
		if (!setTeilleistungenTimestamps.isEmpty())
			conn.transactionPersistAll(setTeilleistungenTimestamps);
		conn.transactionFlush();
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
		if ((tsCheckStr == null) || tsCheckStr.isBlank())
			return false;
		final DateTimeFormatter ofPattern = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss")
				.appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();
		final Timestamp tsCheck = Timestamp.valueOf(LocalDateTime.parse(tsCheckStr, ofPattern));
		if ((tsOtherStr == null) || tsOtherStr.isBlank())
			return true;
		final Timestamp tsOther = Timestamp.valueOf(LocalDateTime.parse(tsOtherStr, ofPattern));
		return tsCheck.after(tsOther);
	}


	/**
	 * Erstellt mit einer gegebenen Datenbankverbindung einen {@link OAuthClient}
	 *
	 * @param conn die Datenbankverbindung
	 *
	 * @return der OAuthClient
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static OAuth2Client getWenomOAuthClient(final DBEntityManager conn) throws ApiOperationException {
		final DTOSchuleOAuthSecrets dto = new DataOauthClientSecrets(conn).getDto(OAuth2ServerTyp.WENOM);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return OAuth2Client.getClient(dto);
	}


	/**
	 * Lädt die ENM-Daten über den gegebenen OAuthClient vom ENM-Server und mit dem gegebenen DataManager in die
	 * Datenbank
	 *
	 * @param conn    die Datenbank-Verbindung
	 * @param client  der OAuthClient
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void downloadENMDaten(final DBEntityManager conn, final OAuth2Client client) throws ApiOperationException {
		final HttpResponse<byte[]> httpResponse = client.get(ENM_DOWNLOAD_PATH, BodyHandlers.ofByteArray());
		if (httpResponse.statusCode() != Status.OK.getStatusCode()) {
			throw new ApiOperationException(Status.BAD_GATEWAY, httpResponse.body());
		}
		importDatenGZip(conn, httpResponse.body());
	}


	/**
	 * Lädt die ENM-Daten beim ENM-Server hoch
	 *
	 * @param conn         die Datenbank-Verbindung
	 * @param client       der OAuth-Client zur Verbindung mit dem ENM
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void uploadENMDaten(final DBEntityManager conn, final OAuth2Client client) throws ApiOperationException {
		final byte[] daten = getAllGZIPBytes(conn);
		final HttpResponse<String> response = client.postMultipart(ENM_UPLOAD_PATH, "json.gz", daten,
				BodyHandlers.ofString());
		if (response.statusCode() != Status.OK.getStatusCode()) {
			throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
		}
	}


	/**
	 * Synchronisiert die Daten des Externen Notenmoduls (ENM) mit dem WeNoM-Server und lädt
	 * dabei diese als ZIP beim ENM hoch und anschließend wieder von diesem herunter und speichert
	 * diese in der Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response synchronize(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		uploadENMDaten(conn, client);
		downloadENMDaten(conn, client);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}


	/**
	 * Lädt die ENM-Daten aus der Datenbank zu dem WeNoM-Server hoch.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response upload(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		uploadENMDaten(conn, client);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}


	/**
	 * Importiert die ENM-Daten von dem WeNoM-Server und schreibt diese in die Datenbank.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response download(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		downloadENMDaten(conn, client);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}


	/**
	 * Entfernt die ENM-Daten von dem WeNoM-Server.
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static Response truncate(final DBEntityManager conn) throws ApiOperationException {
		final OAuth2Client client = getWenomOAuthClient(conn);
		final HttpResponse<String> response = client.postEmpty(ENM_TRUNCATE_PATH, BodyHandlers.ofString());
		if (response.statusCode() != Status.OK.getStatusCode())
			throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}

}
