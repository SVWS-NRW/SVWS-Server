package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.lehrer.LehrerMehrleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.lehrer.LehrerMehrleistungsarten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;



/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
 */
public final class DataLehrerPersonalabschnittsdatenMehrleistungen
		extends DataManagerRevised<Long, DTOLehrerMehrleistung, LehrerPersonalabschnittsdatenAnrechnungsstunden> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataLehrerPersonalabschnittsdatenMehrleistungen(final DBEntityManager conn) {
		super(conn);
		// Eine Änderung der ID oder eine Neu-Zuweisung zu einem anderen Abschnitt eines ggf. anderen Lehrers ist nicht erlaubt
		setAttributesNotPatchable("id", "idAbschnittsdaten");
		// Außer der ID sind alle Attribute beim Erzeugen eines neuen Grundes korrekt zu setzen
		setAttributesRequiredOnCreation("idAbschnittsdaten", "idGrund", "anzahl");
	}


	@Override
	public void checkBeforeCreation(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		// Prüfe vor dem Erstellen, ob bereits ein entsprechender Grund für den Abschnitt in der Datenbank eingetragen ist.
		final long idAbschnittsdaten = JSONMapper.convertToLong(initAttributes.get("idAbschnittsdaten"), false);
		final long idGrund = JSONMapper.convertToLong(initAttributes.get("idGrund"), false);
		final String mehrleistungsgrundKrz = getMehrleistungsgrundKrz(idAbschnittsdaten, idGrund);
		final List<DTOLehrerMehrleistung> funktionen = conn.queryList(
				"SELECT p FROM DTOLehrerMehrleistung p WHERE p.Abschnitt_ID = ?1 AND p.MehrleistungsgrundKrz = ?2", DTOLehrerMehrleistung.class,
				idAbschnittsdaten, mehrleistungsgrundKrz);
		// Wenn ja, dann ist das Anlegen eines neuen Eintrages unzulässig
		if (!funktionen.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Lehrermehrleistung mit der ID %s in den Abschnittdaten mit der ID %s existiert bereits.".formatted(idGrund, idAbschnittsdaten));
	}


	@Override
	protected void initDTO(final DTOLehrerMehrleistung dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}


	@Override
	public void checkBeforePersist(final DTOLehrerMehrleistung dto, final Map<String, Object> patchedAttributes) throws ApiOperationException {
		// Wenn idAbschnittsdaten oder idGrund nicht bereits in das DTO eingetragen sind, dann liegt hier ein Fehler vor. Ein Patch muss hier bereits angewendet sein.
		if (dto.Abschnitt_ID < 0)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Eine negative ID %d für den Abschnitt des Lehrers ist nicht zulässig.".formatted(dto.Abschnitt_ID));
		if (dto.MehrleistungsgrundKrz == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist für den Grund nicht zulässig.");
		// Überprüfe, ob ein anderes DTO-Objekt mit dem Abschnitt und dem Grund in der DB bereits existiert
		final List<DTOLehrerMehrleistung> funktionen = conn.queryList(
				"SELECT p FROM DTOLehrerMehrleistung p WHERE p.Abschnitt_ID = ?1 AND p.MehrleistungsgrundKrz = ?2 AND p.ID != ?3",
				DTOLehrerMehrleistung.class, dto.Abschnitt_ID, dto.MehrleistungsgrundKrz, dto.ID);
		if (!funktionen.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Grund mit der ID %s existiert bereits im Abschnitt mit der ID %s."
					.formatted(patchedAttributes.get("idGrund"), dto.Abschnitt_ID));
	}


	protected static LehrerPersonalabschnittsdatenAnrechnungsstunden mapInternal(final DTOLehrerMehrleistung dto, final DBEntityManager conn)
			throws ApiOperationException {
		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;
		final LehrerMehrleistungsarten art = LehrerMehrleistungsarten.data().getWertByKuerzel(dto.MehrleistungsgrundKrz);
		final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, dto.Abschnitt_ID);
		if (dtoAbschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Lehrerabschnittdaten konnten für die ID %d nicht gefunden werden.".formatted(dto.Abschnitt_ID));
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
		final LehrerMehrleistungsartKatalogEintrag artEintrag = (art == null) ? null : art.daten(abschnitt.schuljahr);
		if (artEintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Kürzel für die Art der Mehrleistung, welches in der Datenbank gespeichert ist, ist im Schuljahr %d nicht gültig."
							.formatted(abschnitt.schuljahr));
		daten.idGrund = artEintrag.id;
		daten.anzahl = (dto.MehrleistungStd == null) ? 0.0 : dto.MehrleistungStd;
		return daten;
	}


	@Override
	protected LehrerPersonalabschnittsdatenAnrechnungsstunden map(final DTOLehrerMehrleistung dto) throws ApiOperationException {
		return mapInternal(dto, conn);
	}


	@Override
	protected void mapAttribute(final DTOLehrerMehrleistung dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idAbschnittsdaten" -> updateAbschnittID(dto, JSONMapper.convertToLong(value, false));
			case "idGrund" -> dto.MehrleistungsgrundKrz = getMehrleistungsgrundKrz(dto.Abschnitt_ID, JSONMapper.convertToLong(value, false, "idGrund"));
			case "anzahl" -> dto.MehrleistungStd = JSONMapper.convertToDouble(value, false);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}


	@Override
	public LehrerPersonalabschnittsdatenAnrechnungsstunden getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Mehrleistungsstunden mit der ID %d gefunden.".formatted(id));
		final DTOLehrerMehrleistung dto = conn.queryByKey(DTOLehrerMehrleistung.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return map(dto);
	}


	/**
	 * Ermittelt die Mehrleistungen für die Lehrerabschnittsdaten mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Mehrleistungen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getByLehrerabschnittsdatenId(final DBEntityManager conn,
			final long idLehrerabschnittsdaten) throws ApiOperationException {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = new ArrayList<>();
		// Bestimme die Mehrleistungen für die Lehrerabschnittsdaten
		final List<DTOLehrerMehrleistung> dtos = conn.queryList(DTOLehrerMehrleistung.QUERY_BY_ABSCHNITT_ID,
				DTOLehrerMehrleistung.class, idLehrerabschnittsdaten);
		if (dtos == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerMehrleistung dto : dtos)
			result.add(mapInternal(dto, conn));
		return result;
	}


	private String getMehrleistungsgrundKrz(final long Abschnitt_ID, final long idGrund) throws ApiOperationException {
		try {
			final LehrerMehrleistungsarten grund = LehrerMehrleistungsarten.data().getWertByID(idGrund);
			final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, Abschnitt_ID);
			if (dtoAbschnitt == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die Lehrer-Abschnittsdaten für den Abschnitt mit der ID %d konnten nicht geladen werden".formatted(Abschnitt_ID));
			final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
			final LehrerMehrleistungsartKatalogEintrag eintrag = grund.daten(abschnitt.schuljahr);
			if (eintrag == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Der Mehrleistungesgrund mit der ID %d ist im Schuljahr %d nicht gültig.".formatted(idGrund, abschnitt.schuljahr));
			return eintrag.kuerzel;
		} catch (@SuppressWarnings("unused") final CoreTypeException cte) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Mehrleistungsgrund mit der ID %d ist nicht vorhanden.".formatted(idGrund));
		}
	}


	private void updateAbschnittID(final DTOLehrerMehrleistung dto, final long abschnittID) throws ApiOperationException {
		if (((abschnittID < 0) || (conn.queryByKey(DTOLehrerAbschnittsdaten.class, abschnittID)) == null))
			throw new ApiOperationException(Status.CONFLICT, "AbschnittID %d ungültig.".formatted(abschnittID));
		dto.Abschnitt_ID = abschnittID;
	}

}
