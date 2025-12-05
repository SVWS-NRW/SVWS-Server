package de.svws_nrw.data.schueler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.svws_nrw.asd.data.schueler.SchuelerFoerderempfehlung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.klassen.DTOKlassen;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerFoerderempfehlung;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernabschnittsdaten;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link SchuelerFoerderempfehlung}.
 */
public final class DataSchuelerFoerderempfehlung extends DataManagerRevised<String, DTOSchuelerFoerderempfehlung, SchuelerFoerderempfehlung> {

	private static final String ID_LERNABSCHNITT = "idLernabschnitt";
	private static final String DATUM_ANGELEGT = "datumAngelegt";

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn   die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataSchuelerFoerderempfehlung(final DBEntityManager conn) {
		super(conn);
		// Eine Änderung der GU_ID oder eine Neu-Zuweisung zu einem anderen Abschnitt ist nicht erlaubt
		setAttributesNotPatchable("guid", ID_LERNABSCHNITT, DATUM_ANGELEGT);
		// Außer der GU_ID sind alle Attribute beim Erzeugen eines neuen Eintrags korrekt zu setzen
		setAttributesRequiredOnCreation(DATUM_ANGELEGT, ID_LERNABSCHNITT);

	}


	@Override
	protected String getID(final Map<String, Object> attributes) throws ApiOperationException {
		return JSONMapper.convertToString(attributes.get("guid"), true, false, null);
	}


	@Override
	protected String getNextID(final String lastID, final Map<String, Object> initAttributes) {
		return UUID.randomUUID().toString();
	}


	@Override
	public void checkBeforeCreation(final String newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Prüfe vor dem Erstellen, ob die verknüpfte Datensätze vorhanden sind.
		final long abschnittID = JSONMapper.convertToLong(initAttributes.get(ID_LERNABSCHNITT), false, ID_LERNABSCHNITT);
		pruefeExistenzAbschnitt(abschnittID);
	}


	@Override
	protected void initDTO(final DTOSchuelerFoerderempfehlung dto, final String newID, final Map<String, Object> initAttributes) {
		dto.GU_ID = newID;
	}


	@Override
	protected SchuelerFoerderempfehlung map(final DTOSchuelerFoerderempfehlung dto) throws ApiOperationException {
		final SchuelerFoerderempfehlung daten = new SchuelerFoerderempfehlung();
		daten.guid = dto.GU_ID;
		daten.idLernabschnitt = dto.Abschnitt_ID;
		daten.idKlasse = dto.Klassen_ID;
		daten.idLehrer = dto.Lehrer_ID;
		daten.datumAngelegt = (dto.DatumAngelegt == null) ? "" : dto.DatumAngelegt;
		daten.datumLetzteAenderung = (dto.DatumAenderungSchildWeb == null) ? "" : dto.DatumAenderungSchildWeb;
		daten.diagnoseKompetenzenInhaltlichProzessbezogen = (dto.Inhaltl_Prozessbez_Komp == null) ? "" : dto.Inhaltl_Prozessbez_Komp;
		daten.diagnoseKompetenzenMethodisch = (dto.Methodische_Komp == null) ? "" : dto.Methodische_Komp;
		daten.diagnoseLernUndArbeitsverhalten = (dto.Lern_Arbeitsverhalten == null) ? "" : dto.Lern_Arbeitsverhalten;
		daten.massnahmeKompetenzenInhaltlichProzessbezogen = (dto.Massn_Inhaltl_Prozessbez_Komp == null) ? "" : dto.Massn_Inhaltl_Prozessbez_Komp;
		daten.massnahmeKompetenzenMethodische = (dto.Massn_Methodische_Komp == null) ? "" : dto.Massn_Methodische_Komp;
		daten.massnahmeLernArbeitsverhalten = (dto.Massn_Lern_Arbeitsverhalten == null) ? "" : dto.Massn_Lern_Arbeitsverhalten;
		daten.verantwortlichkeitEltern = (dto.Verantwortlichkeit_Eltern == null) ? "" : dto.Verantwortlichkeit_Eltern;
		daten.verantwortlichkeitSchueler = (dto.Verantwortlichkeit_Schueler == null) ? "" : dto.Verantwortlichkeit_Schueler;
		daten.datumUmsetzungVon = (dto.Zeitrahmen_von_Datum == null) ? "" : dto.Zeitrahmen_von_Datum;
		daten.datumUmsetzungBis = (dto.Zeitrahmen_bis_Datum == null) ? "" : dto.Zeitrahmen_bis_Datum;
		daten.datumUeberpruefung = (dto.Ueberpruefung_Datum == null) ? "" : dto.Ueberpruefung_Datum;
		daten.datumNaechstesBeratungsgespraech = (dto.Naechstes_Beratungsgespraech == null) ? "" : dto.Naechstes_Beratungsgespraech;
		daten.eingabeFertig = Boolean.TRUE.equals(dto.EingabeFertig);
		daten.faecher = (dto.Faecher == null) ? "" : dto.Faecher;
		daten.abgeschlossen = Boolean.TRUE.equals(dto.Abgeschlossen);
		return daten;
	}


	@Override
	protected void mapAttribute(final DTOSchuelerFoerderempfehlung dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case ID_LERNABSCHNITT -> dto.Abschnitt_ID = JSONMapper.convertToLongInRange(value, false, 1L, null, name);
			case "idKlasse" -> updateklassenID(dto, JSONMapper.convertToLongInRange(value, false, 1L, null, name));
			case "idLehrer" -> updatelehrerID(dto, JSONMapper.convertToLongInRange(value, false, 1L, null, name));
			case DATUM_ANGELEGT -> dto.DatumAngelegt = JSONMapper.convertToString(value, true, true, null, name);
			case "datumLetzteAenderung" -> updateDatumLetzteAenderung(dto, name, value);
			case "diagnoseKompetenzenInhaltlichProzessbezogen" -> dto.Inhaltl_Prozessbez_Komp =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Inhaltl_Prozessbez_Komp.datenlaenge(), name);
			case "diagnoseKompetenzenMethodisch" -> dto.Methodische_Komp =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Methodische_Komp.datenlaenge(), name);
			case "diagnoseLernUndArbeitsverhalten" -> dto.Lern_Arbeitsverhalten =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Lern_Arbeitsverhalten.datenlaenge(), name);
			case "massnahmeKompetenzenInhaltlichProzessbezogen" -> dto.Massn_Inhaltl_Prozessbez_Komp =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Massn_Inhaltl_Prozessbez_Komp.datenlaenge(), name);
			case "massnahmeKompetenzenMethodische" -> dto.Massn_Methodische_Komp =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Massn_Methodische_Komp.datenlaenge(), name);
			case "massnahmeLernArbeitsverhalten" -> dto.Massn_Lern_Arbeitsverhalten =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Massn_Lern_Arbeitsverhalten.datenlaenge(), name);
			case "verantwortlichkeitEltern" -> dto.Verantwortlichkeit_Eltern =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Verantwortlichkeit_Eltern.datenlaenge(), name);
			case "verantwortlichkeitSchueler" -> dto.Verantwortlichkeit_Schueler =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Verantwortlichkeit_Schueler.datenlaenge(), name);
			case "datumUmsetzungVon" -> dto.Zeitrahmen_von_Datum = JSONMapper.convertToString(value, true, true, null, name);
			case "datumUmsetzungBis" -> dto.Zeitrahmen_bis_Datum = JSONMapper.convertToString(value, true, true, null, name);
			case "datumUeberpruefung" -> dto.Ueberpruefung_Datum = JSONMapper.convertToString(value, true, true, null, name);
			case "datumNaechstesBeratungsgespraech" -> dto.Naechstes_Beratungsgespraech = JSONMapper.convertToString(value, true, true, null, name);
			case "eingabeFertig" -> dto.EingabeFertig = JSONMapper.convertToBoolean(value, false);
			case "faecher" -> dto.Faecher =
					JSONMapper.convertToString(value, true, true, Schema.tab_SchuelerFoerderempfehlungen.col_Faecher.datenlaenge(), name);
			case "abgeschlossen" -> dto.Abgeschlossen = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void updateDatumLetzteAenderung(final DTOSchuelerFoerderempfehlung dto, final String name, final Object value) throws ApiOperationException {
		final String datum = JSONMapper.convertToString(value, true, true, null, name);
		if ((datum == null) || datum.isBlank() || datum.isEmpty()) {
			dto.DatumAenderungSchild = null;
			return;
		}

		dto.DatumAenderungSchild = datum;
	}


	@Override
	public SchuelerFoerderempfehlung getById(final String id) throws ApiOperationException {
		if ((id == null) || id.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "Die GUID darf nicht null oder leer sein.");
		final DTOSchuelerFoerderempfehlung dto = conn.queryByKey(DTOSchuelerFoerderempfehlung.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Förderempfehlung mit der GUID %s gefunden.".formatted(id));
		return map(dto);
	}


	/**
	 * Ermittelt die Förderempfehlung für die Schülerlernabschnittsdaten mit der angegebenen ID und gibt diese als Response zurück.
	 *
	 * @param conn                    die Datenbankverbindung
	 * @param idLernabschnittsdaten   die ID der Schülerlernabschnittsdaten
	 *
	 * @return die Response mit der Förderempfehlung oder NOT_FOUND, falls keine vorhanden
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public Response getListByLernabschnittsdatenIdAsResponse(final DBEntityManager conn, final long idLernabschnittsdaten) throws ApiOperationException {
		final List<SchuelerFoerderempfehlung> empfehlungen = new ArrayList<>();
		final List<DTOSchuelerFoerderempfehlung> dtos =
				conn.queryList(DTOSchuelerFoerderempfehlung.QUERY_BY_ABSCHNITT_ID, DTOSchuelerFoerderempfehlung.class, idLernabschnittsdaten);
		// Konventiere sie und füge sie zur Liste hinzu
		for (final DTOSchuelerFoerderempfehlung dto : dtos)
			empfehlungen.add(map(dto));
		return Response.ok(empfehlungen).build();
	}


	/**
	 * Aktualisiert die Klassen-ID im gegebenen DTO nach Prüfung der Existenz der Klasse.
	 *
	 * @param dto         das DTO der Förderempfehlung
	 * @param klassenID   die ID der Klasse
	 *
	 * @throws ApiOperationException wenn die Klasse nicht existiert
	 */
	private void updateklassenID(final DTOSchuelerFoerderempfehlung dto, final long klassenID) throws ApiOperationException {
		pruefeExistenzKlasse(klassenID);
		dto.Klassen_ID = klassenID;
	}


	/**
	 * Aktualisiert die Lehrer-ID im gegebenen DTO nach Prüfung der Existenz des Lehrers.
	 *
	 * @param dto        das DTO der Förderempfehlung
	 * @param lehrerID   die ID des Lehrers
	 *
	 * @throws ApiOperationException wenn der Lehrer nicht existiert
	 */
	private void updatelehrerID(final DTOSchuelerFoerderempfehlung dto, final long lehrerID) throws ApiOperationException {
		pruefeExistenzLehrer(lehrerID);
		dto.Lehrer_ID = lehrerID;
	}


	/**
	 * Prüft die Existenz eines Schülerlernabschnitts mit der angegebenen ID.
	 *
	 * @param abschnittID  die ID des Abschnitts
	 *
	 * @throws ApiOperationException wenn der Abschnitt nicht existiert
	 */
	private void pruefeExistenzAbschnitt(final Long abschnittID) throws ApiOperationException {
		final DTOSchuelerLernabschnittsdaten abschnitte = conn.queryByKey(DTOSchuelerLernabschnittsdaten.class, abschnittID);
		if (abschnitte == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein Schüler-Lernabschnitt mit der ID %d gefunden.".formatted(abschnittID));
	}


	/**
	 * Prüft die Existenz einer Klasse mit der angegebenen ID.
	 *
	 * @param klassenID   die ID der Klasse
	 *
	 * @throws ApiOperationException wenn die Klasse nicht existiert
	 */
	private void pruefeExistenzKlasse(final Long klassenID) throws ApiOperationException {
		final DTOKlassen klassen = conn.queryByKey(DTOKlassen.class, klassenID);
		if (klassen == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Keine Klasse mit der ID %d gefunden.".formatted(klassenID));
	}


	/**
	 * Prüft die Existenz eines Lehrers mit der angegebenen ID.
	 *
	 * @param lehrerID   die ID des Lehrers
	 *
	 * @throws ApiOperationException wenn der Lehrer nicht existiert
	 */
	private void pruefeExistenzLehrer(final Long lehrerID) throws ApiOperationException {
		final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, lehrerID);
		if (lehrer == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Kein Lehrer mit der ID %d gefunden.".formatted(lehrerID));
	}

}
