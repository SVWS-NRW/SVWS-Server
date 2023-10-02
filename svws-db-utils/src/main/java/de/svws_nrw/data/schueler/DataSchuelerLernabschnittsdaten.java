package de.svws_nrw.data.schueler;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import de.svws_nrw.core.data.schueler.SchuelerLernabschnittNachpruefung;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittNachpruefungsdaten;
import de.svws_nrw.core.data.schueler.SchuelerLernabschnittsdaten;
import de.svws_nrw.core.types.Note;
import de.svws_nrw.core.types.fach.BilingualeSprache;
import de.svws_nrw.core.types.klassen.Klassenart;
import de.svws_nrw.core.types.schule.AllgemeinbildendOrganisationsformen;
import de.svws_nrw.core.types.schule.BerufskollegOrganisationsformen;
import de.svws_nrw.core.types.schule.SchulabschlussAllgemeinbildend;
import de.svws_nrw.core.types.schule.SchulabschlussBerufsbildend;
import de.svws_nrw.core.types.schule.Schulform;
import de.svws_nrw.core.types.schule.Schulgliederung;
import de.svws_nrw.core.types.schule.WeiterbildungskollegOrganisationsformen;
import de.svws_nrw.data.DataBasicMapper;
import de.svws_nrw.data.DataManager;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOFoerderschwerpunkt;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerPSFachBemerkungen;
import de.svws_nrw.db.dto.current.schild.schule.DTOEigeneSchule;
import de.svws_nrw.db.dto.current.schild.schule.DTOJahrgang;
import de.svws_nrw.db.dto.current.schild.schule.DTOSchuljahresabschnitte;
import de.svws_nrw.db.utils.OperationError;
import de.svws_nrw.json.JsonDaten;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


/**
 * Diese Klasse erweitert den abstrakten {@link DataManager} für den
 * Core-DTO {@link SchuelerLernabschnittsdaten}.
 */
public final class DataSchuelerLernabschnittsdaten extends DataManager<Long> {

	/**
	 * Erstellt einen neuen {@link DataManager} für den Core-DTO {@link SchuelerLernabschnittsdaten}.
	 *
	 * @param conn                   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerLernabschnittsdaten(final DBEntityManager conn) {
		super(conn);
	}

	@Override
	public Response getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response getList() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Bestimmt die Lernabschnittsdaten anhand der übergebenen Schüler-ID und dem
	 * angegebenen Schuljahresabschnitts
	 *
	 * @param schueler_id            die Schüler-ID
	 * @param schuljahresabschnitt   der Schuljahresabschnitt
	 *
	 * @return die Lernabschnittsdaten
	 */
	public Response get(final Long schueler_id, final long schuljahresabschnitt) {
		// Prüfe, ob der Schüler mit der ID existiert
		if (schueler_id == null)
			return OperationError.NOT_FOUND.getResponse();
    	final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, schueler_id);
    	if (schueler == null)
    		return OperationError.NOT_FOUND.getResponse();
    	// Bestimme den aktuellen Lernabschnitt
    	final String jpql = "SELECT e FROM DTOSchuelerLernabschnittsdaten e WHERE e.Schueler_ID = ?1 and e.Schuljahresabschnitts_ID = ?2 and e.WechselNr = 0";
    	final List<DTOSchuelerLernabschnittsdaten> lernabschnittsdaten = conn.queryList(jpql, DTOSchuelerLernabschnittsdaten.class, schueler_id, schuljahresabschnitt);
    	if ((lernabschnittsdaten == null) || lernabschnittsdaten.isEmpty())
    		return OperationError.NOT_FOUND.getResponse();
    	if (lernabschnittsdaten.size() > 1)
    		return OperationError.INTERNAL_SERVER_ERROR.getResponse();
    	final DTOSchuelerLernabschnittsdaten aktuell = lernabschnittsdaten.get(0);
		return this.get(aktuell.ID);
	}

	@Override
	public Response get(final Long id) {
		// Prüfe, ob der Lernabschnitt mit der ID existiert
		if (id == null)
			return OperationError.NOT_FOUND.getResponse();
		final DTOSchuelerLernabschnittsdaten aktuell = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, id);
    	if (aktuell == null)
    		return OperationError.NOT_FOUND.getResponse();
    	// Ermittle die Fachbemerkungen
    	final List<DTOSchuelerPSFachBemerkungen> bemerkungen = conn.queryNamed("DTOSchuelerPSFachBemerkungen.abschnitt_id", aktuell.ID, DTOSchuelerPSFachBemerkungen.class);
    	if (bemerkungen == null)
    		return OperationError.NOT_FOUND.getResponse();
    	if (bemerkungen.size() > 1)
    		return OperationError.INTERNAL_SERVER_ERROR.getResponse();

    	final SchuelerLernabschnittsdaten daten = new SchuelerLernabschnittsdaten();
    	daten.id = aktuell.ID;
    	daten.schuelerID = aktuell.Schueler_ID;
    	daten.schuljahresabschnitt = aktuell.Schuljahresabschnitts_ID;
    	daten.wechselNr = aktuell.WechselNr;
    	daten.datumAnfang = aktuell.DatumVon;
    	daten.datumEnde = aktuell.DatumBis;
    	daten.datumKonferenz = aktuell.Konferenzdatum;
    	daten.datumZeugnis = aktuell.ZeugnisDatum;
    	daten.anzahlSchulbesuchsjahre = aktuell.Schulbesuchsjahre;
    	daten.istGewertet = aktuell.SemesterWertung == null || aktuell.SemesterWertung;
    	daten.istWiederholung = aktuell.Wiederholung != null && aktuell.Wiederholung;
    	daten.pruefungsOrdnung = aktuell.PruefOrdnung;
    	daten.tutorID = aktuell.Tutor_ID;
    	daten.klassenID = aktuell.Klassen_ID;
    	daten.folgeklassenID = aktuell.Folgeklasse_ID;
    	daten.schulgliederung = aktuell.Schulgliederung.daten.kuerzel;
    	daten.jahrgangID = aktuell.Jahrgang_ID;
    	daten.fachklasseID = aktuell.Fachklasse_ID;
    	daten.schwerpunktID = aktuell.Schwerpunkt_ID;
    	daten.organisationsform = aktuell.OrgFormKrz;
    	daten.Klassenart = aktuell.Klassenart;
    	daten.fehlstundenGesamt = aktuell.SumFehlStd == null ? 0 : aktuell.SumFehlStd;
    	daten.fehlstundenUnentschuldigt = aktuell.SumFehlStdU == null ? 0 : aktuell.SumFehlStdU;
    	daten.fehlstundenGrenzwert = aktuell.FehlstundenGrenzwert;
    	daten.hatSchwerbehinderungsNachweis = aktuell.Schwerbehinderung != null && aktuell.Schwerbehinderung;
    	daten.hatAOSF = aktuell.AOSF != null && aktuell.AOSF;
    	daten.hatAutismus = aktuell.Autist != null && aktuell.Autist;
    	daten.hatZieldifferentenUnterricht = aktuell.ZieldifferentesLernen != null && aktuell.ZieldifferentesLernen;
    	daten.foerderschwerpunkt1ID = aktuell.Foerderschwerpunkt_ID;
    	daten.foerderschwerpunkt2ID = aktuell.Foerderschwerpunkt2_ID;
    	daten.sonderpaedagogeID = aktuell.Sonderpaedagoge_ID;
    	daten.bilingualerZweig = aktuell.BilingualerZweig;
    	daten.istFachpraktischerAnteilAusreichend = aktuell.FachPraktAnteilAusr;
    	daten.versetzungsvermerk = aktuell.VersetzungKrz;
    	daten.noteDurchschnitt = aktuell.DSNote;
    	daten.noteLernbereichGSbzwAL = aktuell.Gesamtnote_GS == null ? null : aktuell.Gesamtnote_GS.getNoteSekI();
    	daten.noteLernbereichNW = aktuell.Gesamtnote_NW == null ? null : aktuell.Gesamtnote_NW.getNoteSekI();
    	daten.abschlussart = aktuell.AbschlussArt;
    	daten.istAbschlussPrognose = aktuell.AbschlIstPrognose;
    	daten.abschluss = aktuell.Abschluss;
    	daten.abschlussBerufsbildend = aktuell.Abschluss_B;
    	daten.textErgebnisPruefungsalgorithmus = aktuell.PruefAlgoErgebnis;
    	daten.zeugnisart = aktuell.Zeugnisart;
    	if (aktuell.MoeglNPFaecher != null) {
    		final String[] moeglicheNPFaecher = aktuell.MoeglNPFaecher.split(",");
    		if ((moeglicheNPFaecher.length > 0) && (!"".equals(moeglicheNPFaecher[0].trim()))) {
    			daten.nachpruefungen = new SchuelerLernabschnittNachpruefungsdaten();
    			for (final String fach : moeglicheNPFaecher)
    				daten.nachpruefungen.moegliche.add(fach);
    			if (aktuell.NPV_Fach_ID != null) {
    				final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
    				np.grund = "V";
    				np.fachID = aktuell.NPV_Fach_ID;
    				np.datum = aktuell.NPV_Datum;
    				np.note = aktuell.NPV_NoteKrz;
    				daten.nachpruefungen.pruefungen.add(np);
    			}
    			if (aktuell.NPAA_Fach_ID != null) {
    				final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
    				np.grund = "A";
    				np.fachID = aktuell.NPAA_Fach_ID;
    				np.datum = aktuell.NPAA_Datum;
    				np.note = aktuell.NPAA_NoteKrz;
    				daten.nachpruefungen.pruefungen.add(np);
    			}
    			if (aktuell.NPBQ_Fach_ID != null) {
    				final SchuelerLernabschnittNachpruefung np = new SchuelerLernabschnittNachpruefung();
    				np.grund = "B";
    				np.fachID = aktuell.NPBQ_Fach_ID;
    				np.datum = aktuell.NPBQ_Datum;
    				np.note = aktuell.NPBQ_NoteKrz;
    				daten.nachpruefungen.pruefungen.add(np);
    			}
    		}
    	}
    	daten.bemerkungen.zeugnisAllgemein = aktuell.ZeugnisBem;
    	if (!bemerkungen.isEmpty()) {
    		final DTOSchuelerPSFachBemerkungen b = bemerkungen.get(0);
	    	daten.bemerkungen.zeugnisASV = b.ASV;
	    	daten.bemerkungen.zeugnisLELS = b.LELS;
	    	daten.bemerkungen.zeugnisAUE = b.AUE;
	    	daten.bemerkungen.uebergangESF = b.ESF;
	    	daten.bemerkungen.foerderschwerpunkt = b.BemerkungFSP;
	    	daten.bemerkungen.versetzungsentscheidung = b.BemerkungVersetzung;
    	}

    	if (!(new DataSchuelerLeistungsdaten(conn).getByLernabschnitt(aktuell.ID, daten.leistungsdaten)))
    		return OperationError.INTERNAL_SERVER_ERROR.getResponse();

        return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(daten).build();
	}

	private static final Map<String, DataBasicMapper<DTOSchuelerLernabschnittsdaten>> patchMappings = Map.ofEntries(
		Map.entry("id", (conn, dto, value, map) -> {
			final Long patch_id = JSONMapper.convertToLong(value, true);
			if ((patch_id == null) || (patch_id.longValue() != dto.ID))
				throw OperationError.BAD_REQUEST.exception();
		}),
		Map.entry("schuelerID", (conn, dto, value, map) -> {
			final long idSchueler = JSONMapper.convertToLong(value, false);
			if (conn.queryByKey(DTOSchueler.class, idSchueler) == null)
				throw OperationError.CONFLICT.exception();
			dto.Schueler_ID = idSchueler;
		}),
		Map.entry("schuljahresabschnitt", (conn, dto, value, map) -> {
			final long schuljahresabschnitt = JSONMapper.convertToLong(value, false);
			if (conn.queryByKey(DTOSchuljahresabschnitte.class, schuljahresabschnitt) == null)
				throw OperationError.CONFLICT.exception();
			dto.Schuljahresabschnitts_ID = schuljahresabschnitt;
		}),
		Map.entry("wechselNr", (conn, dto, value, map) -> dto.WechselNr = JSONMapper.convertToIntegerInRange(value, true, 0, 1000)),
		Map.entry("datumAnfang", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.DatumVon = strDatum;
		}),
		Map.entry("datumEnde", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.DatumBis = strDatum;
		}),
		Map.entry("datumKonferenz", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.Konferenzdatum = strDatum;
		}),
		Map.entry("datumZeugnis", (conn, dto, value, map) -> {
			final String strDatum = JSONMapper.convertToString(value, true, false, null);
			// TODO Datum validieren
			dto.ZeugnisDatum = strDatum;
		}),
		Map.entry("anzahlSchulbesuchsjahre", (conn, dto, value, map) -> dto.Schulbesuchsjahre = JSONMapper.convertToIntegerInRange(value, true, 0, 100)),
		Map.entry("istGewertet", (conn, dto, value, map) -> dto.SemesterWertung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("istWiederholung", (conn, dto, value, map) -> dto.Wiederholung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("pruefungsOrdnung", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			// TODO Prüfungsordnung anhand des Schild3-Katalogs validieren
			dto.PruefOrdnung = str;
		}),
		Map.entry("tutorID", (conn, dto, value, map) -> {
			final Long idLehrer = JSONMapper.convertToLong(value, true);
			if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
				throw OperationError.CONFLICT.exception();
			dto.Tutor_ID = idLehrer;
		}),
		Map.entry("klassenID", (conn, dto, value, map) -> {
			final Long idKlasse = JSONMapper.convertToLong(value, true);
			if ((idKlasse != null) && (conn.queryByKey(DTOKlassen.class, idKlasse) == null))
				throw OperationError.CONFLICT.exception();
			dto.Klassen_ID = idKlasse;
		}),
		Map.entry("folgeklassenID", (conn, dto, value, map) -> {
			final Long idKlasse = JSONMapper.convertToLong(value, true);
			if ((idKlasse != null) && (conn.queryByKey(DTOKlassen.class, idKlasse) == null))
				throw OperationError.CONFLICT.exception();
			dto.Folgeklasse_ID = idKlasse;
		}),
		Map.entry("schulgliederung", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			dto.Schulgliederung = Schulgliederung.getByKuerzel(str);
		}),
		Map.entry("jahrgangID", (conn, dto, value, map) -> {
			final Long idJahrgang = JSONMapper.convertToLong(value, true);
			if ((idJahrgang != null) && (conn.queryByKey(DTOJahrgang.class, idJahrgang) == null))
				throw OperationError.CONFLICT.exception();
			dto.Jahrgang_ID = idJahrgang;
		}),
		Map.entry("fachklasseID", (conn, dto, value, map) -> {
			final Long idFachklasse = JSONMapper.convertToLong(value, true);
			if (idFachklasse != null) {
				final var manager = JsonDaten.fachklassenManager;
				if (manager.getDaten(idFachklasse) == null)
					throw OperationError.CONFLICT.exception();
			}
			dto.Fachklasse_ID = idFachklasse;
		}),
		Map.entry("schwerpunktID", (conn, dto, value, map) -> {
			final Long idSchwerpunkt = JSONMapper.convertToLong(value, true);
			// TODO Validierung des Schwerpunktes
			dto.Schwerpunkt_ID = idSchwerpunkt;
		}),
		Map.entry("organisationsform", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			final DTOEigeneSchule schule = conn.querySingle(DTOEigeneSchule.class);
			if (str != null) {
				if ((schule.Schulform == Schulform.WB) && (WeiterbildungskollegOrganisationsformen.getByKuerzel(str) == null))
					throw OperationError.CONFLICT.exception();
				if ((AllgemeinbildendOrganisationsformen.getByKuerzel(str) == null) && (BerufskollegOrganisationsformen.getByKuerzel(str) == null))
					throw OperationError.CONFLICT.exception();
			}
			dto.OrgFormKrz = str;
		}),
		Map.entry("Klassenart", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (Klassenart.getByKuerzel(str) == null))
				throw OperationError.CONFLICT.exception();
			dto.Klassenart = str;
		}),
		Map.entry("fehlstundenGesamt", (conn, dto, value, map) -> dto.SumFehlStd = JSONMapper.convertToIntegerInRange(value, true, 0, 100000)),
		Map.entry("fehlstundenUnentschuldigt", (conn, dto, value, map) -> dto.SumFehlStdU = JSONMapper.convertToIntegerInRange(value, true, 0, 100000)),
		Map.entry("fehlstundenGrenzwert", (conn, dto, value, map) -> dto.FehlstundenGrenzwert = JSONMapper.convertToIntegerInRange(value, true, 0, 100000)),
		Map.entry("hatSchwerbehinderungsNachweis", (conn, dto, value, map) -> dto.Schwerbehinderung = JSONMapper.convertToBoolean(value, false)),
		Map.entry("hatAOSF", (conn, dto, value, map) -> dto.AOSF = JSONMapper.convertToBoolean(value, false)),
		Map.entry("hatAutismus", (conn, dto, value, map) -> dto.Autist = JSONMapper.convertToBoolean(value, false)),
		Map.entry("hatZieldifferentenUnterricht", (conn, dto, value, map) -> dto.ZieldifferentesLernen = JSONMapper.convertToBoolean(value, false)),
		Map.entry("foerderschwerpunkt1ID", (conn, dto, value, map) -> {
			final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
			if ((idFoerderschwerpunkt != null) && (conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt) == null))
					throw OperationError.CONFLICT.exception();
			dto.Foerderschwerpunkt_ID = idFoerderschwerpunkt;
		}),
		Map.entry("foerderschwerpunkt2ID", (conn, dto, value, map) -> {
			final Long idFoerderschwerpunkt = JSONMapper.convertToLong(value, true);
			if ((idFoerderschwerpunkt != null) && (conn.queryByKey(DTOFoerderschwerpunkt.class, idFoerderschwerpunkt) == null))
					throw OperationError.CONFLICT.exception();
			dto.Foerderschwerpunkt2_ID = idFoerderschwerpunkt;
		}),
		Map.entry("sonderpaedagogeID", (conn, dto, value, map) -> {
			final Long idLehrer = JSONMapper.convertToLong(value, true);
			if ((idLehrer != null) && (conn.queryByKey(DTOLehrer.class, idLehrer) == null))
				throw OperationError.CONFLICT.exception();
			dto.Sonderpaedagoge_ID = idLehrer;
		}),
		Map.entry("bilingualerZweig", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (BilingualeSprache.getByKuerzel(str) == null))
				throw OperationError.CONFLICT.exception();
			dto.BilingualerZweig = str;
		}),
		Map.entry("istFachpraktischerAnteilAusreichend", (conn, dto, value, map) -> dto.FachPraktAnteilAusr = JSONMapper.convertToBoolean(value, true)),
		Map.entry("versetzungsvermerk", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			// TODO Prüfung des Versetzungsvermerks
			//if ((str != null) && (???))
			//	throw OperationError.CONFLICT.exception();
			dto.VersetzungKrz = str;
		}),
		Map.entry("noteDurchschnitt", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			// TODO Prüfung der Durchschnittsnote
			dto.DSNote = str;
		}),
		Map.entry("noteLernbereichGSbzwAL", (conn, dto, value, map) -> dto.Gesamtnote_GS = Note.fromNoteSekI(JSONMapper.convertToIntegerInRange(value, true, 1, 6))),
		Map.entry("noteLernbereichNW", (conn, dto, value, map) -> dto.Gesamtnote_NW = Note.fromNoteSekI(JSONMapper.convertToIntegerInRange(value, true, 1, 6))),
		Map.entry("abschlussart", (conn, dto, value, map) -> {
			final Integer abschlussart = JSONMapper.convertToInteger(value, true);
			// TODO Prüfung der Abschlussart
			dto.AbschlussArt = abschlussart;
		}),
		Map.entry("istAbschlussPrognose", (conn, dto, value, map) -> dto.AbschlIstPrognose = JSONMapper.convertToBoolean(value, true)),
		Map.entry("abschluss", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (SchulabschlussAllgemeinbildend.getByKuerzel(str) == null))
				throw OperationError.CONFLICT.exception();
			dto.Abschluss = str;
		}),
		Map.entry("abschlussBerufsbildend", (conn, dto, value, map) -> {
			final String str = JSONMapper.convertToString(value, true, false, null);
			if ((str != null) && (SchulabschlussBerufsbildend.getByKuerzel(str) == null))
				throw OperationError.CONFLICT.exception();
			dto.Abschluss_B = str;
		}),
		Map.entry("textErgebnisPruefungsalgorithmus", (conn, dto, value, map) -> dto.PruefAlgoErgebnis = JSONMapper.convertToString(value, true, false, null)),
		Map.entry("zeugnisart", (conn, dto, value, map) -> dto.Zeugnisart = JSONMapper.convertToString(value, true, false, 5))
	);

	@Override
	public Response patch(final Long id, final InputStream is) {
		return super.patchBasic(id, is, DTOSchuelerLernabschnittsdaten.class, patchMappings);
	}

	// TODO Patch für Fachbemerkungen als getrennte Patch-Methode

	// TODO Patch für Nachprüfungen als getrennte Patch-Methode

}
