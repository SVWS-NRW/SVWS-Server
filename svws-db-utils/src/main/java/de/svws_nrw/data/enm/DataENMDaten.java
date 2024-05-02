package de.svws_nrw.data.enm;

import java.io.IOException;
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
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
import de.svws_nrw.core.data.enm.ENMTeilleistungsart;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.SchuelerStatus;
import de.svws_nrw.core.types.kurse.ZulaessigeKursart;
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
	private static final String ENM_TRUNCATE_PATH = "/api/truncate";


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
		final ENMDaten daten = getDaten(null);
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
		final ENMDaten daten = getDaten(null);
		return JSONMapper.gzipFileResponseFromObject(daten, "enm.json.gz");
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response get(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		final ENMDaten daten = getDaten(id);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	@Override
	public Response patch(final Long id, final InputStream is) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gibt die ENM-Daten aller Lehrer als mit GZIP komprimiertes byte[] zurück.
	 *
	 * @return das GZIP-komprimierte byte[].
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public byte[] getAllGZIPBytes() throws ApiOperationException {
		final ENMDaten daten = getDaten(null);
		try {
			return JSONMapper.gzipByteArrayFromObject(daten);
		} catch (final CompressionException ce) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, ce);
		}
	}

	/**
	 * Ermittelt die ENM-Daten zu dem Lehrer mit der angegebenen ID.
	 * Ist die ID null so werden die ENM-Daten für alle Lehrer des aktuellen
	 * Schuljahresabschnitts generiert.
	 *
	 * @param id   die ID des Lehrers oder null
	 *
	 * @return die ENMDaten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private ENMDaten getDaten(final Long id) throws ApiOperationException {
		// Lese die Daten aus der Datenbank ein
		final DTOEigeneSchule schule = DBUtilsSchule.get(conn);
    	final DTOSchuljahresabschnitte abschnitt = getSchuljahresabschnitt(schule);
    	final Map<Long, DTOLehrer> mapLehrer = getLehrerListe();
		final DTOLehrer dtoLehrer = (id == null) ? null : mapLehrer.get(id);   // Ermittle den Lehrer nur, falls ENM-Daten für einen speziellen Lehrer bestimt werden.
    	if ((id != null) && (dtoLehrer == null))
    		throw new ApiOperationException(Status.NOT_FOUND);
    	final Map<Long, DTOSchueler> mapSchueler = getSchuelerListe(schule);
    	final Map<Long, DTOFach> mapFaecher = getFaecherListe();
    	final Map<Long, DTOJahrgang> mapJahrgaenge = getJahrgangsListe();
    	final Map<String, DTOKlassen> mapKlassen = getKlassenListe(schule);
    	final Map<Long, List<DTOKlassenLeitung>> mapKlassenLeitung = getKlassenleitungen(mapKlassen);
    	final Map<Long, DTOKurs> mapKurse = getKurse(schule);
		final Map<Long, DTOTeilleistungsarten> mapTeilleistungsarten = conn.queryAll(DTOTeilleistungsarten.class).stream().collect(Collectors.toMap(a -> a.ID, a -> a));

    	// Erstelle einen ENM-Daten-Manager und füge ggf. den Lehrer hinzu für welchen die ENM-Daten erzeugt werden
    	final ENMDatenManager manager = new ENMDatenManager(id);
    	if (dtoLehrer != null)
    		manager.addLehrer(manager.daten.lehrerID, dtoLehrer.Kuerzel, dtoLehrer.Nachname, dtoLehrer.Vorname, dtoLehrer.Geschlecht, dtoLehrer.eMailDienstlich);
		initManager(manager, schule, abschnitt);

   	    // Aggregiert aus den Schülerabschnittsdaten und -leistungsdaten die einzelnen Informationen für das ENM.
    	// Dabei wird unter anderem auch ermittelt, welche Kataloginformationen (Klassen, Lehrer, Jahrgänge)
   	    // bei den Daten für das ENM einzutragen sind.
    	final List<DTOENMLehrerSchuelerAbschnittsdaten> schuelerabschnitte = dtoLehrer == null
    			? DTOENMLehrerSchuelerAbschnittsdaten.queryAll(conn, schule.Schuljahresabschnitts_ID)
    			: DTOENMLehrerSchuelerAbschnittsdaten.query(conn, schule.Schuljahresabschnitts_ID, dtoLehrer.Kuerzel);
    	// Bestimme zunächst noch eine Übersicht zu den Einzelleistungen zu den Leistungsdaten
    	final List<Long> idsLeistungen = schuelerabschnitte.stream().map(a -> a.leistungID).toList();
    	final List<DTOSchuelerTeilleistung> listTeilleistungen = idsLeistungen.isEmpty() ? new ArrayList<>()
    			: conn.queryNamed("DTOSchuelerTeilleistung.leistung_id.multiple", idsLeistungen, DTOSchuelerTeilleistung.class);
    	final Map<Long, List<DTOSchuelerTeilleistung>> mapTeilleistungen = listTeilleistungen.stream().collect(Collectors.groupingBy(st -> st.Leistung_ID));
    	final List<Long> idsTeilleistungen = listTeilleistungen.stream().map(t -> t.ID).toList();
    	final Map<Long, DTOEnmTeilleistungen> mapTeilleistungenTimestamps = idsTeilleistungen.isEmpty() ? new HashMap<>()
    			: conn.queryByKeyList(DTOEnmTeilleistungen.class, idsTeilleistungen).stream().collect(Collectors.toMap(t -> t.ID, t -> t));
    	for (final DTOENMLehrerSchuelerAbschnittsdaten schuelerabschnitt : schuelerabschnitte) {
    		final DTOKlassen dtoKlasse = mapKlassen.get(schuelerabschnitt.klasse);
			if (dtoKlasse == null) {
				// TODO DB-Error -> should not happen but must be handled
				throw new NullPointerException();
			}
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

    		final ZulaessigeKursart kursart = (schuelerabschnitt.kursID == null) ? null : ZulaessigeKursart.getByASDKursart(schuelerabschnitt.kursart);

    		ENMSchueler enmSchueler = manager.getSchueler(schuelerabschnitt.schuelerID);
    		if (enmSchueler == null) {
    			final var dtoSchueler = mapSchueler.get(schuelerabschnitt.schuelerID);
    			ENMJahrgang enmJahrgang = manager.getJahrgang(schuelerabschnitt.jahrgangID);
    			if (enmJahrgang == null) {
    				final DTOJahrgang dtoJahrgang = mapJahrgaenge.get(schuelerabschnitt.jahrgangID);
    				if (dtoJahrgang == null) {
    					// TODO DB-Error -> should not happen but must be handled
    					throw new NullPointerException();
    				}
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
    			enmSchueler.lernabschnitt.lernbereich1note = schuelerabschnitt.lernbereich1note == null ? null : schuelerabschnitt.lernbereich1note.kuerzel;
    			enmSchueler.lernabschnitt.lernbereich2note = schuelerabschnitt.lernbereich2note == null ? null : schuelerabschnitt.lernbereich2note.kuerzel;
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
    			? "Klasse:" + schuelerabschnitt.klasse + "/" + schuelerabschnitt.fachID
    			: "Kurs:" + schuelerabschnitt.kursID;
    		ENMLerngruppe lerngruppe = manager.getLerngruppe(strLerngruppenID);
    		if (lerngruppe == null) {
				final DTOFach fach = mapFaecher.get(schuelerabschnitt.fachID);
				final String kursartAllg = (kursart == null) ? null : (kursart.daten.kuerzelAllg == null) || "".equals(kursart.daten.kuerzelAllg)
						? kursart.daten.kuerzel : kursart.daten.kuerzelAllg;
				// Fach zu ENMDaten hinzufügen ?
				if (manager.getFach(fach.ID) == null)
					manager.addFach(fach.ID, fach.StatistikFach.daten.kuerzelASD, fach.Kuerzel, fach.SortierungAllg, fach.IstFremdsprache);
				// Unterscheidung zwischen den beiden Lerngruppen-Typen...
    			if (schuelerabschnitt.kursID == null) {  // es ist eine Klasse
    				manager.addLerngruppe(strLerngruppenID, dtoKlasse.ID, schuelerabschnitt.fachID, null,
        					            fach.Kuerzel, kursartAllg, fach.Unterichtssprache,
        					            schuelerabschnitt.wochenstunden == null ? 0 : schuelerabschnitt.wochenstunden);
    			} else {  // es ist ein Kurs
    				final DTOKurs kurs = mapKurse.get(schuelerabschnitt.kursID);
    				manager.addLerngruppe(strLerngruppenID, schuelerabschnitt.kursID, schuelerabschnitt.fachID,
        					kursart == null ? -1 : Integer.parseInt(kursart.daten.nummer), kurs.KurzBez, kursartAllg,
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

    		final int halbjahr = (schule.AnzahlAbschnitte == 4) ? abschnitt.Abschnitt / 2 : abschnitt.Abschnitt;
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
    				schuelerabschnitt.leistungID, lerngruppe.id, schuelerabschnitt.note.kuerzel, schuelerabschnitt.tsNote,
    				schuelerabschnitt.noteQuartal.kuerzel, schuelerabschnitt.tsNoteQuartal, istSchriftlich, abiFach,
    				schuelerabschnitt.fehlstundenGesamt, schuelerabschnitt.tsFehlstundenGesamt,
    				schuelerabschnitt.fehlstundenUnentschuldigt, schuelerabschnitt.tsFehlstundenUnentschuldigt,
    				schuelerabschnitt.fachbezogeneBemerkungen, schuelerabschnitt.tsFachbezogeneBemerkungen, null,
    				istGemahnt, schuelerabschnitt.tsIstGemahnt, mahndatum);

    		// Teilleistungen und deren Arten hinzufügen
    		final List<DTOSchuelerTeilleistung> teilleistungen = mapTeilleistungen.get(schuelerabschnitt.leistungID);
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
    						dtoArt.Sortierung == null ? 32000 : dtoArt.Sortierung,
    						dtoArt.Gewichtung == null ? 1.0 : dtoArt.Gewichtung);
    				enmTeilleistungsart = manager.getTeilleistungsart(teilleistung.Art_ID);
    			}
    			// Füge die Teilleistung hinzu
    			final DTOEnmTeilleistungen teilleistungTimestamps = mapTeilleistungenTimestamps.get(teilleistung.ID);
    			if (teilleistungTimestamps == null)
    				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Es konnten keine Zeitstempel für die Teilleistungen ausgelesen werden. Dies deutet auf einen Fehler in der Datenbank hin.");
    			manager.addSchuelerTeilleistung(enmLeistung, teilleistung.ID, teilleistung.Art_ID, teilleistungTimestamps.tsArt_ID,
    					teilleistung.Datum, teilleistungTimestamps.tsDatum, teilleistung.Bemerkung, teilleistungTimestamps.tsBemerkung,
    					teilleistung.NotenKrz, teilleistungTimestamps.tsNotenKrz);
    		}

    		// TODO check and add ZP10 - Data
    		// TODO check and add BKAbschluss - Data
    	}

    	// Kopiere den Floskel-Katalog in die ENM-Daten
    	getFloskeln(manager, mapJahrgaenge);

    	return manager.daten;
	}


	private static void initManager(final ENMDatenManager manager, final DTOEigeneSchule schule, final DTOSchuljahresabschnitte abschnitt) {
		// Setze die grundlegenden Schuldaten
    	manager.setSchuldaten(schule.SchulNr, abschnitt.Jahr, schule.AnzahlAbschnitte, abschnitt.Abschnitt,
    			/* TODO */ null, /* TODO */ true, /* TODO */ false, /* TODO */ true,
    			schule.Schulform.daten.kuerzel, /* TODO */ null);
    	// Kopiere den Noten-Katalog aus dem Core-type in die ENM-Daten
		manager.addNoten();
    	// Kopiere den Förderschwerpunkt-Katalog aus dem Core-type in die ENM-Daten
		manager.addFoerderschwerpunkte(schule.Schulform);
	}

	private DTOSchuljahresabschnitte getSchuljahresabschnitt(final DTOEigeneSchule schule) throws ApiOperationException {
		final DTOSchuljahresabschnitte abschnitt = conn.queryByKey(DTOSchuljahresabschnitte.class, schule.Schuljahresabschnitts_ID);
		if (abschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return abschnitt;
	}

	// TODO Optimierung: Lese nur die aktiven Lehrer aus der Datenbank aus.
	private Map<Long, DTOLehrer> getLehrerListe() throws ApiOperationException {
		final List<DTOLehrer> lehrer = conn.queryAll(DTOLehrer.class);
		if (lehrer.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return lehrer.stream().collect(Collectors.toMap(e -> e.ID, e -> e));
	}

	private Map<Long, DTOSchueler> getSchuelerListe(final DTOEigeneSchule schule) throws ApiOperationException {
		final List<DTOSchueler> schueler = conn.queryNamed("DTOSchueler.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOSchueler.class);
		if (schueler.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return schueler.stream()
				.filter(s -> s.Status == SchuelerStatus.AKTIV || s.Status == SchuelerStatus.EXTERN)
				.collect(Collectors.toMap(s -> s.ID, s -> s));
	}

	private Map<Long, DTOFach> getFaecherListe() throws ApiOperationException {
		final List<DTOFach> faecher = conn.queryAll(DTOFach.class);
		if (faecher.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return faecher.stream().collect(Collectors.toMap(f -> f.ID, f -> f));
	}

	private Map<Long, DTOJahrgang> getJahrgangsListe() throws ApiOperationException {
		final List<DTOJahrgang> jahrgaenge = conn.queryAll(DTOJahrgang.class);
		if (jahrgaenge.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return jahrgaenge.stream().collect(Collectors.toMap(j -> j.ID, j -> j));
	}

	private Map<String, DTOKlassen> getKlassenListe(final DTOEigeneSchule schule) throws ApiOperationException {
		final List<DTOKlassen> klassen = conn.queryNamed("DTOKlassen.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOKlassen.class);
		if (klassen.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND);
		return klassen.stream().collect(Collectors.toMap(e -> e.Klasse, e -> e));
	}

	private Map<Long, List<DTOKlassenLeitung>> getKlassenleitungen(final Map<String, DTOKlassen> mapKlassen) {
		final List<Long> klassenIDs = mapKlassen.values().stream().map(kl -> kl.ID).toList();
		return conn.queryNamed("DTOKlassenLeitung.klassen_id.multiple", klassenIDs, DTOKlassenLeitung.class).stream()
				.collect(Collectors.groupingBy(kl -> kl.Klassen_ID));
	}

	private Map<Long, DTOKurs> getKurse(final DTOEigeneSchule schule) throws ApiOperationException {
		final List<DTOKurs> kurse = conn.queryNamed("DTOKurs.schuljahresabschnitts_id", schule.Schuljahresabschnitts_ID, DTOKurs.class);
		if (kurse == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return conn.queryAll(DTOKurs.class).stream().collect(Collectors.toMap(k -> k.ID, k -> k));
	}


	/**
	 * Füllt die Datenstruktur für die Floskelgruppen des ENM mit den in der SVWS-DB hinterlegten
	 * Floskelgruppen und den zugehörigen Floskeln.
	 *
	 * @param manager         der ENM-Daten-Manager
	 * @param mapJahrgaenge   eine Map mit den jeweiligen Jahrgängen
	 */
	private void getFloskeln(final ENMDatenManager manager, final Map<Long, DTOJahrgang> mapJahrgaenge) {
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


	private ENMSchueler[] importParseByteArray(final byte[] enmBytes) throws ApiOperationException {
		try {
			return JSONMapper.mapper.readValue(enmBytes, ENMSchueler[].class);
			// TODO kein ZIP auf diesem Endpunkt
			// return JSONMapper.toObjectGZip(httpResponse.body(), ENMSchueler[].class);
		} catch (@SuppressWarnings("unused") final IOException e) {
			throw new ApiOperationException(Status.BAD_GATEWAY, "Antwort des ENM-Servers nicht parsebar.");
		}
	}


	/**
	 * Importiert die gegebenen ENMSchueler-Daten in die SVWS-Datenbank. Prüft dazu die Zeitstempel
	 * der einzelnen Felder und aktualisiert neuere Datensätze und deren Zeitstempel.
	 *
	 * @param enmBytes   das byte[] mit dem JSON-Array der zu importierenden Schüler
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public void importEnmDaten(final byte[] enmBytes) throws ApiOperationException {
		conn.transactionBegin();
		final ENMSchueler[] arrayEnmSchueler = importParseByteArray(enmBytes);
		final DTOEigeneSchule schule = DBUtilsSchule.get(conn);
		// Bestimme alle Schüler-Lernabschnittsdaten des aktuellen Schuljahresabschnittes der Schule aus der DB
		// TODO bestimme die Schüler-Lernabschnittsdaten alternativ anhand der IDs aus den zu importierenden Daten und validiere die Schuljahresabschnitts-ID
		final List<DTOSchuelerLernabschnittsdaten> slaDatenList = conn.queryNamed("DTOSchuelerLernabschnittsdaten.schuljahresabschnitts_id",
				schule.Schuljahresabschnitts_ID, DTOSchuelerLernabschnittsdaten.class);
		if ((slaDatenList == null) || slaDatenList.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Lernabschnittsdaten für Schuljahresabschnitt nicht gefunden.");
		final Map<Long, DTOSchuelerLernabschnittsdaten> slaBySchuelerId = slaDatenList.stream()
				.collect(Collectors.toMap((sla) -> sla.Schueler_ID, Function.identity()));
		// TODO Die Schüler-Leistungsdaten müssen anhand der Abschnitts-IDs aus
		final List<DTOSchuelerLeistungsdaten> slDatenList = conn.queryNamed("DTOSchuelerLeistungsdaten.abschnitt_id",
				schule.Schuljahresabschnitts_ID, DTOSchuelerLeistungsdaten.class);
		if (slDatenList == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Leistungsdaten für Schuljahresabschnitt nicht gefunden.");

		// Durchwandere die importierten ENM-Daten und gleiche diese mit den Daten in der Datenbank ab.
		for (final ENMSchueler enmSchueler : arrayEnmSchueler) {
			final DTOSchuelerLernabschnittsdaten sla = slaBySchuelerId.get(enmSchueler.id);
			final List<DTOEnmLernabschnittsdaten> dtoEnmLAList = conn.queryNamed("DTOEnmLernabschnittsdaten.id", sla.ID,
					DTOEnmLernabschnittsdaten.class);
			if ((dtoEnmLAList == null) || (dtoEnmLAList.size() != 1))
				throw new ApiOperationException(Status.NOT_FOUND, "Lernabschnittsdaten nicht gefunden.");
			final DTOEnmLernabschnittsdaten enmLA = dtoEnmLAList.get(0);

			final List<DTOSchuelerPSFachBemerkungen> dtoFachbemerkungenList = conn.queryNamed(
					"DTOSchuelerPSFachBemerkungen.abschnitt_id", sla.ID, DTOSchuelerPSFachBemerkungen.class);
			if ((dtoFachbemerkungenList == null) || (dtoFachbemerkungenList.size() != 1))
				throw new ApiOperationException(Status.NOT_FOUND, "Fachbemerkungen nicht gefunden.");
			final DTOSchuelerPSFachBemerkungen fachBemerkungen = dtoFachbemerkungenList.get(0);
			boolean laUpdaten = false;
			boolean bemerkungenUpdaten = false;
			bemerkungenUpdaten |= isEnmLatestThenAction(enmSchueler.bemerkungen.tsASV, enmLA.tsASV, () -> {
				fachBemerkungen.ASV = enmSchueler.bemerkungen.ASV;
				enmLA.tsASV = enmSchueler.bemerkungen.tsASV;
			});
			bemerkungenUpdaten |= isEnmLatestThenAction(enmSchueler.bemerkungen.tsAUE, enmLA.tsAUE, () -> {
				fachBemerkungen.AUE = enmSchueler.bemerkungen.AUE;
				enmLA.tsAUE = enmSchueler.bemerkungen.tsAUE;
			});
			bemerkungenUpdaten |= isEnmLatestThenAction(enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen,
					enmLA.tsBemerkungVersetzung, () -> {
						fachBemerkungen.BemerkungVersetzung = enmSchueler.bemerkungen.individuelleVersetzungsbemerkungen;
						enmLA.tsBemerkungVersetzung = enmSchueler.bemerkungen.tsIndividuelleVersetzungsbemerkungen;
					});

			laUpdaten |= isEnmLatestThenAction(enmSchueler.bemerkungen.tsZB, enmLA.tsZeugnisBem, () -> {
				sla.ZeugnisBem = enmSchueler.bemerkungen.ZB;
				enmLA.tsZeugnisBem = enmSchueler.bemerkungen.tsZB;
			});

			laUpdaten |= isEnmLatestThenAction(enmSchueler.lernabschnitt.tsFehlstundenGesamt, enmLA.tsSumFehlStd,
					() -> {
						sla.SumFehlStd = enmSchueler.lernabschnitt.fehlstundenGesamt;
						enmLA.tsSumFehlStd = enmSchueler.lernabschnitt.tsFehlstundenGesamt;
					});

			laUpdaten |= isEnmLatestThenAction(enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt,
					enmLA.tsSumFehlStdU, () -> {
						sla.SumFehlStdU = enmSchueler.lernabschnitt.fehlstundenGesamtUnentschuldigt;
						enmLA.tsSumFehlStdU = enmSchueler.lernabschnitt.tsFehlstundenGesamtUnentschuldigt;
					});

			if (bemerkungenUpdaten)
				conn.transactionPersist(fachBemerkungen);
			if (laUpdaten)
				conn.transactionPersist(sla);
			if (bemerkungenUpdaten || laUpdaten)
				conn.transactionPersist(enmLA);

			for (final ENMLeistung leistung : enmSchueler.leistungsdaten) {
				final DTOSchuelerLeistungsdaten dtoSchuelerLeistungsdaten = conn
						.queryNamed("DTOSchuelerLeistungsdaten.id", leistung.id, DTOSchuelerLeistungsdaten.class)
						.get(0);
				final List<DTOEnmLeistungsdaten> dtoEnmLDList = conn.queryNamed("DTOEnmLeistungsdaten.id", leistung.id,
						DTOEnmLeistungsdaten.class);
				if (dtoSchuelerLeistungsdaten == null || dtoEnmLDList == null || dtoEnmLDList.size() != 1) {
					throw new ApiOperationException(Status.NOT_FOUND, "Schuelerleistungsdaten nicht gefunden.");
				}
				final var dtoEnmLD = dtoEnmLDList.get(0);
				boolean dtoUpdaten = false;

				dtoUpdaten |= isEnmLatestThenAction(leistung.tsFachbezogeneBemerkungen, dtoEnmLD.tsLernentw, () -> {
					dtoSchuelerLeistungsdaten.Lernentw = leistung.fachbezogeneBemerkungen;
					dtoEnmLD.tsLernentw = leistung.tsFachbezogeneBemerkungen;
				});
				dtoUpdaten |= isEnmLatestThenAction(leistung.tsFehlstundenFach, dtoEnmLD.tsFehlStd, () -> {
					dtoSchuelerLeistungsdaten.FehlStd = leistung.fehlstundenFach;
					dtoEnmLD.tsFehlStd = leistung.tsFehlstundenFach;
				});
				dtoUpdaten |= isEnmLatestThenAction(leistung.tsFehlstundenUnentschuldigtFach, dtoEnmLD.tsuFehlStd,
						() -> {
							dtoSchuelerLeistungsdaten.uFehlStd = leistung.fehlstundenUnentschuldigtFach;
							dtoEnmLD.tsuFehlStd = leistung.tsFehlstundenUnentschuldigtFach;
						});
				dtoUpdaten |= isEnmLatestThenAction(leistung.tsIstGemahnt, dtoEnmLD.tsWarnung, () -> {
					dtoSchuelerLeistungsdaten.Warnung = leistung.istGemahnt;
					dtoEnmLD.tsWarnung = leistung.tsIstGemahnt;
				});
				dtoUpdaten |= isEnmLatestThenAction(leistung.tsNote, dtoEnmLD.tsNotenKrz, () -> {
					dtoSchuelerLeistungsdaten.NotenKrz = Note.fromKuerzel(leistung.note);
					dtoEnmLD.tsNotenKrz = leistung.tsNote;
				});
				dtoUpdaten |= isEnmLatestThenAction(leistung.tsNoteQuartal, dtoEnmLD.tsNotenKrzQuartal, () -> {
					dtoSchuelerLeistungsdaten.NotenKrzQuartal = Note.fromKuerzel(leistung.noteQuartal);
					dtoEnmLD.tsNotenKrzQuartal = leistung.tsNoteQuartal;
				});

				if (dtoUpdaten) {
					conn.transactionPersist(dtoSchuelerLeistungsdaten);
					conn.transactionPersist(dtoEnmLD);
				}
			}
		}
		conn.transactionFlush();
		conn.transactionCommitOrThrow();
	}


	/**
	 * Prüft, ob der gegebene Timestamp-String des ENM nach dem Timestamp String des SVWS ist und führt in diesem Fall
	 * das gegebene Runnable aus
	 *
	 * @param tsEnmStr    der Timestamp aus dem ENM
	 * @param tsSvwsStr   der Timestamp aus dem SVWS-Server
	 * @param actionIf    die auszuführende Aktion, wenn der Timestamp des ENM nach dem des SVWS ist, vgl.
	 *                    {@link Timestamp#after(Timestamp)}
	 *
	 * @return ob der Timestamp des ENM nach dem des SVWS ist
	 */
	private static boolean isEnmLatestThenAction(final String tsEnmStr, final String tsSvwsStr, final Runnable actionIf) {
		if (tsEnmStr == null || tsEnmStr.isBlank())
			return false;
		final DateTimeFormatter ofPattern = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss")
				.appendFraction(ChronoField.MILLI_OF_SECOND, 0, 3, true).toFormatter();
		final Timestamp tsEnm = Timestamp.valueOf(LocalDateTime.parse(tsEnmStr, ofPattern));
		if (tsSvwsStr == null || tsSvwsStr.isBlank())
			return true;
		final Timestamp tsSvws = Timestamp.valueOf(LocalDateTime.parse(tsSvwsStr, ofPattern));
		final boolean after = tsEnm.after(tsSvws);
		if (after)
			actionIf.run();
		return after;
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
	 * @param client  der OAuthClient
	 *
	 * @param dataENM der ENM DataManager
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void downloadENMDaten(final OAuth2Client client, final DataENMDaten dataENM) throws ApiOperationException {
		final HttpResponse<byte[]> httpResponse = client.get(ENM_DOWNLOAD_PATH, BodyHandlers.ofByteArray());
		if (httpResponse.statusCode() != Status.OK.getStatusCode()) {
			throw new ApiOperationException(Status.BAD_GATEWAY, httpResponse.body());
		}
		dataENM.importEnmDaten(httpResponse.body());
	}

	/**
	 * Lädt die ENM-Daten beim ENM-Server hoch
	 *
	 * @param client       der OAuth-Client zur Verbindung mit dem ENM
	 * @param dataENMDaten der DataManager für ENM-Daten
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	private static void uploadENMDaten(final OAuth2Client client, final DataENMDaten dataENMDaten) throws ApiOperationException {
		final byte[] daten = dataENMDaten.getAllGZIPBytes();
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
		final DataENMDaten dataENMDaten = new DataENMDaten(conn);
		uploadENMDaten(client, dataENMDaten);
		downloadENMDaten(client, dataENMDaten);
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
		final DataENMDaten dataENMDaten = new DataENMDaten(conn);
		uploadENMDaten(client, dataENMDaten);
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
		final DataENMDaten dataENM = new DataENMDaten(conn);
		downloadENMDaten(client, dataENM);
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
		final HttpResponse<String> response = client.get(ENM_TRUNCATE_PATH, BodyHandlers.ofString());
		if (response.statusCode() != Status.OK.getStatusCode())
			throw new ApiOperationException(Status.BAD_GATEWAY, response.body());
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(Boolean.TRUE).build();
	}

}
