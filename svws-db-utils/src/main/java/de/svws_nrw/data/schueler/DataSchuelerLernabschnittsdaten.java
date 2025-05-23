package de.svws_nrw.data.schueler;

import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefung;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.asd.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.asd.data.schule.SchulgliederungKatalogEintrag;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.Note;
import de.svws_nrw.core.types.benutzer.BenutzerKompetenz;
import de.svws_nrw.asd.types.fach.BilingualeSprache;
import de.svws_nrw.asd.types.klassen.Klassenart;
import de.svws_nrw.asd.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.asd.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.asd.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.asd.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.asd.types.schule.Schulform;
import de.svws_nrw.asd.types.schule.Schulgliederung;
import de.svws_nrw.asd.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.ApiOperationException;
import de.svws_nrw.json.JsonDaten;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerLernabschnittsdaten}.
 */
public final class DataSchuelerLernabschnittsdaten extends DataManagerRevised<Long, DTOSchuelerLernabschnittsdaten, SchuelerLernabschnittsdaten> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerLernabschnittsdaten}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernabschnittsdaten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("schuelerID", "schuljahresabschnitt");
	}

	@Override
	protected void initDTO(final DTOSchuelerLernabschnittsdaten dto, final Long newId, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newId;
	}


	@Override
	public SchuelerLernabschnittsdaten map(final DTOSchuelerLernabschnittsdaten dto) throws ApiOperationException {
		// Mappe zunächst die Daten des DTO.
		final SchuelerLernabschnittsdaten daten = mapInternalDTOSchuelerLernabschnittsdaten(dto);

		// Ermittle die Fachbemerkungen des Lernabschnitts und ergänze sie im daten-Objekt.
		final List<DTOSchuelerPSFachBemerkungen> bemerkungen = conn.queryList(
				DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, dto.ID);
		if (bemerkungen == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Datensatz mit Bemerkungen zur Abschnitt-ID " + dto.ID + " gefunden.");
		if (bemerkungen.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als einen Datensatz mit Bemerkungen zur Abschnitt-ID " + dto.ID + " gefunden.");
		if (!bemerkungen.isEmpty()) {
			final DTOSchuelerPSFachBemerkungen b = bemerkungen.getFirst();
			daten.bemerkungen.zeugnisASV = b.ASV;
			daten.bemerkungen.zeugnisLELS = b.LELS;
			daten.bemerkungen.zeugnisAUE = b.AUE;
			daten.bemerkungen.uebergangESF = b.ESF;
			daten.bemerkungen.foerderschwerpunkt = b.BemerkungFSP;
			daten.bemerkungen.versetzungsentscheidung = b.BemerkungVersetzung;
		}

		// Ermittle die Leistungsdaten des Lernabschnitts und ergänze sie im daten-Objekt.
		if (!(new DataSchuelerLeistungsdaten(conn).getByLernabschnitt(dto.ID, daten.leistungsdaten)))
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Keine Leistungsdaten zur Abschnitt-ID " + dto.ID + " gefunden.");

		return daten;
	}

	/**
	 * Überführt nur die reinen Daten des DTO eines Schülerlernabschnitts in ein entsprechendes Core-Data-Objekt.
	 * Dabei werden keine zum Lernabschnitt ergänzenden Daten wie Fachbemerkungen oder Leistungsdaten geladen und ergänzt.
	 *
	 * @param dto das DTO mit den SchuelerLernabschnittsdaten.
	 *
	 * @return    ein Core-Data-Objekt mit den reinen SchuelerLernabschnittsdaten.
	 */
	public SchuelerLernabschnittsdaten mapNurDTODaten(final DTOSchuelerLernabschnittsdaten dto) {
		final SchuelerLernabschnittsdaten daten = mapInternalDTOSchuelerLernabschnittsdaten(dto);

		daten.bemerkungen.zeugnisASV = "";
		daten.bemerkungen.zeugnisLELS = "";
		daten.bemerkungen.zeugnisAUE = "";
		daten.bemerkungen.uebergangESF = "";
		daten.bemerkungen.foerderschwerpunkt = "";
		daten.bemerkungen.versetzungsentscheidung = "";

		daten.leistungsdaten = new ArrayList<>();

		return daten;
	}


	/**
	 * Hilfsmethode zum Überführen der reinen Daten des DTO eines Schülerlernabschnitts in ein entsprechendes Core-Data-Objekt.
	 *
	 * @param dto das DTO mit den SchuelerLernabschnittsdaten.
	 *
	 * @return    ein Core-Data-Objekt mit den reinen SchuelerLernabschnittsdaten.
	 */
	private SchuelerLernabschnittsdaten mapInternalDTOSchuelerLernabschnittsdaten(final DTOSchuelerLernabschnittsdaten dto) {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final SchuelerLernabschnittsdaten daten = new SchuelerLernabschnittsdaten();
		daten.id = dto.ID;
		daten.schuelerID = dto.Schueler_ID;
		daten.schuljahresabschnitt = dto.Schuljahresabschnitts_ID;
		daten.wechselNr = dto.WechselNr;
		daten.datumAnfang = dto.DatumVon;
		daten.datumEnde = dto.DatumBis;
		daten.datumKonferenz = dto.Konferenzdatum;
		daten.datumZeugnis = dto.ZeugnisDatum;
		daten.istGewertet = (dto.SemesterWertung == null) || dto.SemesterWertung;
		daten.istWiederholung = (dto.Wiederholung != null) && dto.Wiederholung;
		daten.pruefungsOrdnung = dto.PruefOrdnung;
		daten.tutorID = dto.Tutor_ID;
		daten.klassenID = dto.Klassen_ID;
		daten.folgeklassenID = dto.Folgeklasse_ID;
		// TODO Validierung der Schulgliederung überprüfen...
		final Schulgliederung sgl = (dto.Schulgliederung == null) ? null : Schulgliederung.data().getWertByKuerzel(dto.Schulgliederung);
		SchulgliederungKatalogEintrag sglke = (sgl == null) ? null : sgl.daten(abschnitt.schuljahr);
		if (sglke == null)
			sglke = Schulgliederung.getDefault(conn.getUser().schuleGetSchulform()).daten(abschnitt.schuljahr);
		daten.schulgliederung = sglke.kuerzel;
		daten.jahrgangID = dto.Jahrgang_ID;
		daten.fachklasseID = dto.Fachklasse_ID;
		daten.schwerpunktID = dto.Schwerpunkt_ID;
		daten.organisationsform = dto.OrgFormKrz;
		daten.Klassenart = dto.Klassenart;
		daten.fehlstundenGesamt = (dto.SumFehlStd == null) ? 0 : dto.SumFehlStd;
		daten.fehlstundenUnentschuldigt = (dto.SumFehlStdU == null) ? 0 : dto.SumFehlStdU;
		daten.fehlstundenGrenzwert = dto.FehlstundenGrenzwert;
		daten.hatSchwerbehinderungsNachweis = (dto.Schwerbehinderung != null) && dto.Schwerbehinderung;
		daten.hatAOSF = (dto.AOSF != null) && dto.AOSF;
		daten.hatAutismus = (dto.Autist != null) && dto.Autist;
		daten.hatZieldifferentenUnterricht = (dto.ZieldifferentesLernen != null) && dto.ZieldifferentesLernen;
		daten.foerderschwerpunkt1ID = dto.Foerderschwerpunkt_ID;
		daten.foerderschwerpunkt2ID = dto.Foerderschwerpunkt2_ID;
		daten.sonderpaedagogeID = dto.Sonderpaedagoge_ID;
		daten.bilingualerZweig = dto.BilingualerZweig;
		daten.istFachpraktischerAnteilAusreichend = dto.FachPraktAnteilAusr;
		daten.versetzungsvermerk = dto.VersetzungKrz;
		daten.noteDurchschnitt = dto.DSNote;
		final Note noteLernbereichGSbzwAL = (dto.Gesamtnote_GS == null) ? null : Note.fromNoteSekI(dto.Gesamtnote_GS);
		daten.noteLernbereichGSbzwAL = (noteLernbereichGSbzwAL == null) ? null : dto.Gesamtnote_GS;
		final Note noteLernbereichNW = (dto.Gesamtnote_NW == null) ? null : Note.fromNoteSekI(dto.Gesamtnote_NW);
		daten.noteLernbereichNW = (noteLernbereichNW == null) ? null : dto.Gesamtnote_NW;
		daten.abschlussart = dto.AbschlussArt;
		daten.istAbschlussPrognose = dto.AbschlIstPrognose;
		daten.abschluss = dto.Abschluss;
		daten.abschlussBerufsbildend = dto.Abschluss_B;
		daten.textErgebnisPruefungsalgorithmus = dto.PruefAlgoErgebnis;
		daten.zeugnisart = dto.Zeugnisart;
		if (dto.MoeglNPFaecher != null) {
			final String[] moeglicheNPFaecher = dto.MoeglNPFaecher.split(",");
			if ((moeglicheNPFaecher.length > 0) && (!moeglicheNPFaecher[0].trim().isBlank())) {
				daten.nachpruefungen = new SchuelerLernabschnittNachpruefungsdaten();
				Collections.addAll(daten.nachpruefungen.moegliche, moeglicheNPFaecher);
				if (dto.NPV_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "V";
					np.fachID = dto.NPV_Fach_ID;
					np.datum = dto.NPV_Datum;
					np.note = dto.NPV_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
				if (dto.NPAA_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "A";
					np.fachID = dto.NPAA_Fach_ID;
					np.datum = dto.NPAA_Datum;
					np.note = dto.NPAA_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
				if (dto.NPBQ_Fach_ID != null) {
					final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
					np.grund = "B";
					np.fachID = dto.NPBQ_Fach_ID;
					np.datum = dto.NPBQ_Datum;
					np.note = dto.NPBQ_NoteKrz;
					daten.nachpruefungen.pruefungen.add(np);
				}
			}
		}
		daten.bemerkungen.zeugnisAllgemein = dto.ZeugnisBem;
		return daten;
	}


	@Override
	protected void mapAttribute(final DTOSchuelerLernabschnittsdaten dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dto.Schuljahresabschnitts_ID);
		final Schulform schulform = conn.getUser().schuleGetSchulform();
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST);
			}
			case "schuelerID" -> {
				final long idSchueler = JSONMapper.convertToLong(value, false);
				if (conn.queryByKey(DTOSchueler.class, idSchueler) == null)
					throw new ApiOperationException(Status.CONFLICT);
				dto.Schueler_ID = idSchueler;
			}
			case "schuljahresabschnitt" -> {
				final long schuljahresabschnitt = JSONMapper.convertToLong(value, false);
				if (conn.queryByKey(DTOSchuljahresabschnitte.class, schuljahresabschnitt) == null)
					throw new ApiOperationException(Status.CONFLICT);
				dto.Schuljahresabschnitts_ID = schuljahresabschnitt;
			}
			case "wechselNr" -> dto.WechselNr = JSONMapper.convertToIntegerInRange(value, true, 0, 1000);
			case "datumAnfang" -> {
				final String strDatum = JSONMapper.convertToString(value, true, false, null);
				// TODO Datum validieren
				dto.DatumVon = strDatum;
			}
			case "datumEnde" -> {
				final String strDatum = JSONMapper.convertToString(value, true, false, null);
				// TODO Datum validieren
				dto.DatumBis = strDatum;
			}
			case "datumKonferenz" -> {
				final String strDatum = JSONMapper.convertToString(value, true, false, null);
				// TODO Datum validieren
				dto.Konferenzdatum = strDatum;
			}
			case "datumZeugnis" -> {
				final String strDatum = JSONMapper.convertToString(value, true, false, null);
				// TODO Datum validieren
				dto.ZeugnisDatum = strDatum;
			}
			case "istGewertet" -> dto.SemesterWertung = JSONMapper.convertToBoolean(value, false);
			case "istWiederholung" -> dto.Wiederholung = JSONMapper.convertToBoolean(value, false);
			case "pruefungsOrdnung" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				// TODO Prüfungsordnung anhand des Schild3-Katalogs validieren
				dto.PruefOrdnung = str;
			}
			case "tutorID" -> {
				final Long idLehrer = JSONMapper.convertToLong(value, true);
				if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Tutor_ID = idLehrer;
			}
			case "klassenID" -> {
				final Long idKlasse = JSONMapper.convertToLong(value, true);
				if ((idKlasse != null) && (conn.queryByKey(DTOKlassen.class, idKlasse) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Klassen_ID = idKlasse;
			}
			case "folgeklassenID" -> {
				final Long idKlasse = JSONMapper.convertToLong(value, true);
				if ((idKlasse != null) && (conn.queryByKey(DTOKlassen.class, idKlasse) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Folgeklasse_ID = idKlasse;
			}
			case "schulgliederung" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				if (str != null) {
					final Schulgliederung sgl = Schulgliederung.data().getWertByKuerzel(str);
					if (sgl == null)
						throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s für die Schulgliederung ist ungültig.".formatted(str));
					if (sgl.daten(abschnitt.schuljahr) == null)
						throw new ApiOperationException(Status.BAD_REQUEST,
								"Das Kürzel %s für die Schulgliederung ist in dem Schuljahr %d nicht (mehr) ungültig.".formatted(str, abschnitt.schuljahr));
				}
				dto.Schulgliederung = str;
			}
			case "jahrgangID" -> {
				final Long idJahrgang = JSONMapper.convertToLong(value, true);
				if ((idJahrgang != null) && (conn.queryByKey(DTOJahrgang.class, idJahrgang) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Jahrgang_ID = idJahrgang;
			}
			case "epJahre" -> dto.EPJahre = JSONMapper.convertToIntegerInRange(value, true, 1, 3);
			case "fachklasseID" -> {
				final Long idFachklasse = JSONMapper.convertToLong(value, true);
				if (idFachklasse != null) {
					final var manager = JsonDaten.fachklassenManager;
					if (manager.getDatenByID(idFachklasse) == null)
						throw new ApiOperationException(Status.CONFLICT);
				}
				dto.Fachklasse_ID = idFachklasse;
			}
			case "schwerpunktID" -> {
				final Long idSchwerpunkt = JSONMapper.convertToLong(value, true);
				// TODO Validierung des Schwerpunktes
				dto.Schwerpunkt_ID = idSchwerpunkt;
			}
			case "organisationsform" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				if (str != null) {
					if ((schulform == Schulform.WB) && (WeiterbildungskollegOrganisationsformen.data().getWertByKuerzel(str) == null))
						throw new ApiOperationException(Status.CONFLICT);
					if ((AllgemeinbildendOrganisationsformen.data().getWertByKuerzel(str) == null)
							&& (BerufskollegOrganisationsformen.data().getWertByKuerzel(str) == null))
						throw new ApiOperationException(Status.CONFLICT);
				}
				dto.OrgFormKrz = str;
			}
			case "Klassenart" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				if ((str != null) && (Klassenart.data().getWertByKuerzel(str) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Klassenart = str;
			}
			case "fehlstundenGesamt" -> dto.SumFehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			case "fehlstundenUnentschuldigt" -> dto.SumFehlStdU = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			case "fehlstundenGrenzwert" -> dto.FehlstundenGrenzwert = JSONMapper.convertToIntegerInRange(value, true, 0, 100000);
			case "hatSchwerbehinderungsNachweis" -> dto.Schwerbehinderung = JSONMapper.convertToBoolean(value, false);
			case "hatAOSF" -> dto.AOSF = JSONMapper.convertToBoolean(value, false);
			case "hatAutismus" -> dto.Autist = JSONMapper.convertToBoolean(value, false);
			case "hatZieldifferentenUnterricht" -> dto.ZieldifferentesLernen = JSONMapper.convertToBoolean(value, false);
			case "foerderschwerpunkt1ID" -> {
				final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
				if ((idFoerderschwerpunkt != null) && (conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Foerderschwerpunkt_ID = idFoerderschwerpunkt;
			}
			case "foerderschwerpunkt2ID" -> {
				final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
				if ((idFoerderschwerpunkt != null) && (conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Foerderschwerpunkt2_ID = idFoerderschwerpunkt;
			}
			case "sonderpaedagogeID" -> {
				final Long idLehrer = JSONMapper.convertToLong(value, true);
				if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Sonderpaedagoge_ID = idLehrer;
			}
			case "bilingualerZweig" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				if ((str != null) && (BilingualeSprache.data().getWertByKuerzel(str) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.BilingualerZweig = str;
			}
			case "istFachpraktischerAnteilAusreichend" -> dto.FachPraktAnteilAusr = JSONMapper.convertToBoolean(value, true);
			case "versetzungsvermerk" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				// TODO Prüfung des Versetzungsvermerks
				//if ((str != null) && (???))
				//	throw new ApiOperationException(Status.CONFLICT);
				dto.VersetzungKrz = str;
			}
			case "noteDurchschnitt" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				// TODO Prüfung der Durchschnittsnote
				dto.DSNote = str;
			}
			case "noteLernbereichGSbzwAL" -> {
				final Integer noteSekI = JSONMapper.convertToIntegerInRange(value, true, 1, 6);
				if ((noteSekI != null) && (Note.fromNoteSekI(noteSekI) == null))
					throw new ApiOperationException(Status.BAD_REQUEST, "Der Notenwert für die Lernbereichsnote ist ungültig");
				dto.Gesamtnote_GS = noteSekI;
			}
			case "noteLernbereichNW" -> {
				final Integer noteSekI = JSONMapper.convertToIntegerInRange(value, true, 1, 6);
				if ((noteSekI != null) && (Note.fromNoteSekI(noteSekI) == null))
					throw new ApiOperationException(Status.BAD_REQUEST, "Der Notenwert für die Lernbereichsnote ist ungültig");
				dto.Gesamtnote_NW = noteSekI;
			}
			case "abschlussart" -> {
				final Integer abschlussart = JSONMapper.convertToInteger(value, true);
				// TODO Prüfung der Abschlussart
				dto.AbschlussArt = abschlussart;
			}
			case "istAbschlussPrognose" -> dto.AbschlIstPrognose = JSONMapper.convertToBoolean(value, true);
			case "abschluss" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				if ((str != null) && (SchulabschlussAllgemeinbildend.data().getWertByKuerzel(str) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Abschluss = str;
			}
			case "abschlussBerufsbildend" -> {
				final String str = JSONMapper.convertToString(value, true, false, null);
				if ((str != null) && (SchulabschlussBerufsbildend.data().getWertByKuerzel(str) == null))
					throw new ApiOperationException(Status.CONFLICT);
				dto.Abschluss_B = str;
			}
			case "textErgebnisPruefungsalgorithmus" -> dto.PruefAlgoErgebnis = JSONMapper.convertToString(value, true, false, null);
			case "zeugnisart" -> dto.Zeugnisart = JSONMapper.convertToString(value, true, false, 5);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}


	private void checkFunktionsbezogeneKompetenzAufKlasse(final List<Long> idsKlassen) throws ApiOperationException {
		if (hatBenutzerNurFunktionsbezogeneKompetenz(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_FUNKTIONSBEZOGEN_AENDERN,
				Set.of(BenutzerKompetenz.SCHUELER_LEISTUNGSDATEN_ALLE_AENDERN))) {
			for (final Long idKlasse : idsKlassen)
				checkBenutzerFunktionsbezogeneKompetenzKlasse(idKlasse);
		}
	}


	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Lernabschnittsdaten in dem Lernabschnitt zu erstellen
		final Long idKlasse = JSONMapper.convertToLong(initAttributes.get("klassenID"), true);
		checkFunktionsbezogeneKompetenzAufKlasse(List.of(idKlasse));
	}


	@Override
	public void checkBeforePatch(final DTOSchuelerLernabschnittsdaten dto, final Map<String, Object> patchAttributes) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Lernabschnittsdaten in dem Lernabschnitt zu verändern
		if (patchAttributes.get("klassenID") != null) {
			final Long idKlasse = JSONMapper.convertToLong(patchAttributes.get("klassenID"), true);
			checkFunktionsbezogeneKompetenzAufKlasse(List.of(idKlasse));
		}
		checkFunktionsbezogeneKompetenzAufKlasse(List.of(dto.Klassen_ID));
	}


	@Override
	public void checkBeforeDeletion(final List<DTOSchuelerLernabschnittsdaten> dtos) throws ApiOperationException {
		// Prüfe ggf., ob der Benutzer die Rechte in Abhängigkeit der Klasse hat, um die Lernabschnittsdaten zu löschen
		checkFunktionsbezogeneKompetenzAufKlasse(dtos.stream().map(l -> l.Klassen_ID).toList());
	}


	@Override
	public SchuelerLernabschnittsdaten getById(final Long id) throws ApiOperationException {
		// Prüfe, ob der Lernabschnitt mit der ID existiert
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist keine Abschnitt-ID angegeben worden.");
		final DTOSchuelerLernabschnittsdaten dto = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnittsdaten zur Abschnitt-ID " + id + " gefunden.");
		return map(dto);
	}


	/**
	 * Bestimmt die Lernabschnittsdaten zur Wechsel-Nr. 0 (aktiver Abschnitt im Schuljahresabschnitt)
	 * anhand der übergebenen Schüler-ID und dem angegebenen Schuljahresabschnitt
	 *
	 * @param schueler_id            die Schüler-ID
	 * @param schuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @return die Lernabschnittsdaten
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response get(final Long schueler_id, final long schuljahresabschnitt) throws ApiOperationException {
		final SchuelerLernabschnittsdaten daten = getFromSchuelerIDUndSchuljahresabschnittID(schueler_id, schuljahresabschnitt);
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}


	/**
	 * Bestimmt die Lernabschnittsdaten anhand der übergebenen Schüler-ID und dem angegebenen Schuljahresabschnitt.
	 *
	 * @param schueler_id           	die Schüler-ID
	 * @param schuljahresabschnitt_id   der Schuljahresabschnitt
	 *
	 * @return die Lernabschnittsdaten zu den übergebenen IDs.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public SchuelerLernabschnittsdaten getFromSchuelerIDUndSchuljahresabschnittID(final Long schueler_id, final long schuljahresabschnitt_id)
			throws ApiOperationException {
		// Prüfe, ob der Schüler mit der ID existiert
		if (schueler_id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es ist keine Schueler-ID angegeben worden.");
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Schüler mit der ID " + schueler_id + " gefunden.");

		// Bestimme den aktuellen Lernabschnitt
		final String jpql = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 and e.Schuljahresabschnitts_ID = ?2 and e.WechselNr = 0";
		final List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.queryList(jpql, DTOSchuelerLernabschnittsdaten.class, schueler_id,
				schuljahresabschnitt_id);
		if ((lernabschnittsdaten == null) || lernabschnittsdaten.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Lernabschnitt zum Schüler mit der ID " + schueler_id + " und der Schuljahresabschnitt-ID "
					+ schuljahresabschnitt_id + " gefunden.");
		if (lernabschnittsdaten.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als einen aktuellen Lernabschnitt zum Schüler mit der ID " + schueler_id
					+ " und der Schuljahresabschnitt-ID " + schuljahresabschnitt_id + " gefunden.");

		return getById(lernabschnittsdaten.getFirst().ID);
	}


	/**
	 * Erstellt eine Liste von Lernabschnittsdaten anhand der übergebenen Schüler-IDs gemäß der übergebenen Parameter.
	 *
	 * @param schueler_ids                 die Liste mit Schüler-IDs
	 * @param mitBemerkungenLeistungsdaten legt fest, ob die erzeugten Objekte bereits die Fachbemerkungen und Leistungsdaten des Lernabschnitts enthalten.
	 * @param validiereSchueler            legt fest, ob die übergebenen Schüler-IDs validiert werden sollen. Diese Option sollte, stets true sein, sofern
	 *                                     nicht vorher an der Stelle eine Validierung der IDs vorab stattgefunden hat.
	 *
	 * @return die Lernabschnittsdaten zu den übergebenen IDs.
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public List<SchuelerLernabschnittsdaten> getListFromSchuelerIDs(final List<Long> schueler_ids, final boolean mitBemerkungenLeistungsdaten,
			final boolean validiereSchueler)
			throws ApiOperationException {
		// Prüfe, ob die Liste der Schüler-IDs existiert
		if (schueler_ids == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es sind keine Schueler-ID angegeben worden.");
		if (schueler_ids.isEmpty())
			return new ArrayList<>();

		if (validiereSchueler) {
			final Map<Long, DTOSchueler> mapSchueler = conn.queryByKeyList(DTOSchueler.class, schueler_ids)
					.stream().collect(Collectors.toMap(s -> s.ID, s -> s));
			for (final Long schuelerID : schueler_ids)
				if (mapSchueler.get(schuelerID) == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Ein Schüler mit der ID %d existiert nicht.".formatted(schuelerID));
		}

		// Hole alle Lernabschnitte der übergebenen Schüler-IDs und filtere sie auf den Schuljahresabschnitt und die Wechsel-Nr.
		final List<DTOSchuelerLernabschnittsdaten> dtoLernabschnitte = conn.queryList(DTOSchuelerLernabschnittsdaten.QUERY_LIST_BY_SCHUELER_ID,
				DTOSchuelerLernabschnittsdaten.class, schueler_ids).stream()
				.sorted(Comparator
						.comparing((final DTOSchuelerLernabschnittsdaten a) -> a.Schueler_ID)
						.thenComparing((final DTOSchuelerLernabschnittsdaten a) -> a.Schuljahresabschnitts_ID)
						.thenComparing((final DTOSchuelerLernabschnittsdaten a) -> a.WechselNr))
				.toList();

		final List<SchuelerLernabschnittsdaten> daten = new ArrayList<>();
		for (final DTOSchuelerLernabschnittsdaten a : dtoLernabschnitte)
			daten.add(mitBemerkungenLeistungsdaten ? map(a) : mapNurDTODaten(a));
		return daten;
	}


	/**
	 * Für einen Patch für die angegebenen Bemerkungsfelder aus.
	 *
	 * @param id   die ID des Lernabschnitts
	 * @param is   ein Input-Stream mit den JSON-Daten des Patches
	 *
	 * @return die HTTP-Response für den Patch
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response patchBemerkungen(final Long id, final InputStream is) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Patch mit der ID null ist nicht möglich.");
		final Map<String, Object> attributesToPatch = JSONMapper.toMap(is);
		if (attributesToPatch.isEmpty())
			throw new ApiOperationException(Status.NOT_FOUND, "In dem Patch sind keine Daten enthalten.");
		final DTOSchuelerLernabschnittsdaten dto = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		checkBeforePatch(dto, attributesToPatch);
		final List<DTOSchuelerPSFachBemerkungen> dtoListFachBem = conn.queryList(
				DTOSchuelerPSFachBemerkungen.QUERY_BY_ABSCHNITT_ID, DTOSchuelerPSFachBemerkungen.class, id);
		final DTOSchuelerPSFachBemerkungen dtoFachBem = (dtoListFachBem.isEmpty())
				? new DTOSchuelerPSFachBemerkungen(conn.transactionGetNextID(DTOSchuelerPSFachBemerkungen.class), id)
				: dtoListFachBem.getFirst();
		boolean patchedDTOLernabschitt = false;
		boolean patchedDTOFachBem = false;
		for (final Entry<String, Object> entry : attributesToPatch.entrySet()) {
			final String key = entry.getKey();
			final Object value = entry.getValue();
			switch (key) {
				case "zeugnisAllgemein" -> {
					dto.ZeugnisBem = JSONMapper.convertToString(value, true, true, null);
					patchedDTOLernabschitt = true;
				}
				case "zeugnisASV" -> {
					dtoFachBem.ASV = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "zeugnisLELS" -> {
					dtoFachBem.LELS = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "zeugnisAUE" -> {
					dtoFachBem.AUE = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "uebergangESF" -> {
					dtoFachBem.ESF = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "foerderschwerpunkt" -> {
					dtoFachBem.BemerkungFSP = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				case "versetzungsentscheidung" -> {
					dtoFachBem.BemerkungVersetzung = JSONMapper.convertToString(value, true, true, null);
					patchedDTOFachBem = true;
				}
				default -> {
					/**/ }
			}
		}
		if (patchedDTOLernabschitt) {
			conn.transactionPersist(dto);
			conn.transactionFlush();
		}
		if (patchedDTOFachBem) {
			conn.transactionPersist(dtoFachBem);
			conn.transactionFlush();
		}
		return Response.status(Status.OK).build();
	}

	// TODO Patch für Nachprüfungen als getrennte Patch-Methode - SchuelerLernabschnittNachpruefungsdaten

}
