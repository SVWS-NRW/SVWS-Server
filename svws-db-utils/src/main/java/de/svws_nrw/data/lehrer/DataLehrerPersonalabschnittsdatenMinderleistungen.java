package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.lehrer.LehrerMinderleistungsartKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.lehrer.LehrerMinderleistungsarten;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerEntlastungsstunde;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerMehrleistung;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;



/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das
 * Core-DTO {@link LehrerPersonalabschnittsdatenAnrechnungsstunden}.
 */
public final class DataLehrerPersonalabschnittsdatenMinderleistungen
		extends DataManagerRevised<Long, DTOLehrerEntlastungsstunde, LehrerPersonalabschnittsdatenAnrechnungsstunden> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataLehrerPersonalabschnittsdatenMinderleistungen(final DBEntityManager conn) {
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
		final String entlastungsgrundKrz = getEntlastungsgrundKrz(idAbschnittsdaten, idGrund);
		final List<DTOLehrerMehrleistung> funktionen = conn.queryList(
				"SELECT p FROM DTOLehrerEntlastungsstunde p WHERE p.Abschnitt_ID = ?1 AND p.EntlastungsgrundKrz = ?2", DTOLehrerMehrleistung.class,
				idAbschnittsdaten, entlastungsgrundKrz);
		// Wenn ja, dann ist das Anlegen eines neuen Eintrages unzulässig
		if (!funktionen.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Lehrerminderleistung mit der idGrund %s im Abschnitt mit der ID %s existiert bereits.".formatted(idGrund, idAbschnittsdaten));
	}


	@Override
	protected void initDTO(final DTOLehrerEntlastungsstunde dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}


	@Override
	public void checkBeforePersist(final DTOLehrerEntlastungsstunde dto, final Map<String, Object> patchedAttributes) throws ApiOperationException {
		// Wenn idAbschnittsdaten oder idGrund nicht bereits in das DTO eingetragen sind, dann liegt hier ein Fehler vor. Ein Patch muss hier bereits angewendet sein.
		if (dto.Abschnitt_ID < 0)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Eine negative ID %d für den Abschnitt des Lehrers ist nicht zulässig.".formatted(dto.Abschnitt_ID));
		if (dto.EntlastungsgrundKrz == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Wert null ist für den Grund nicht zulässig.");
		// Überprüfe, ob ein anderes DTO-Objekt mit dem Abschnitt und dem Grund in der DB bereits existiert
		final List<DTOLehrerMehrleistung> funktionen = conn.queryList(
				"SELECT p FROM DTOLehrerEntlastungsstunde p WHERE p.Abschnitt_ID = ?1 AND p.EntlastungsgrundKrz = ?2 AND p.ID != ?3",
				DTOLehrerMehrleistung.class, dto.Abschnitt_ID, dto.EntlastungsgrundKrz, dto.ID);
		if (!funktionen.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Grund mit der ID %s existiert bereits im Abschnitt mit der ID %s."
					.formatted(patchedAttributes.get("idGrund"), dto.Abschnitt_ID));
	}


	protected static LehrerPersonalabschnittsdatenAnrechnungsstunden mapInternal(final DTOLehrerEntlastungsstunde dto, final DBEntityManager conn)
			throws ApiOperationException {
		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;
		final LehrerMinderleistungsarten art = LehrerMinderleistungsarten.data().getWertByKuerzel(dto.EntlastungsgrundKrz);
		final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, dto.Abschnitt_ID);
		if (dtoAbschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Lehrerabschnittdaten konnten für die ID %d nicht gefunden werden.".formatted(dto.Abschnitt_ID));
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
		final LehrerMinderleistungsartKatalogEintrag artEintrag = (art == null) ? null : art.daten(abschnitt.schuljahr);
		if (artEintrag == null)
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Kürzel für die Art der Minderleistung, welches in der Datenbank gespeichert ist, ist im Schuljahr %d nicht gültig."
							.formatted(abschnitt.schuljahr));
		daten.idGrund = artEintrag.id;
		daten.anzahl = (dto.EntlastungStd == null) ? 0.0 : dto.EntlastungStd;
		return daten;
	}


	@Override
	protected LehrerPersonalabschnittsdatenAnrechnungsstunden map(final DTOLehrerEntlastungsstunde dto) throws ApiOperationException {
		return mapInternal(dto, conn);
	}


	@Override
	protected void mapAttribute(final DTOLehrerEntlastungsstunde dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idAbschnittsdaten" -> updateAbschnittID(dto, JSONMapper.convertToLong(value, false));
			case "idGrund" -> dto.EntlastungsgrundKrz = getEntlastungsgrundKrz(dto.Abschnitt_ID, JSONMapper.convertToLong(value, false, "idGrund"));
			case "anzahl" -> dto.EntlastungStd = JSONMapper.convertToDouble(value, false);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}


	@Override
	public LehrerPersonalabschnittsdatenAnrechnungsstunden getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Minderleistungsstunden mit der ID %d gefunden.".formatted(id));
		final DTOLehrerEntlastungsstunde dto = conn.queryByKey(DTOLehrerEntlastungsstunde.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return map(dto);
	}


	/**
	 * Ermittelt die Minderleistungen für die Lehrerabschnittsdaten mit der angegebenen ID und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Minderleistungen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getByLehrerabschnittsdatenId(final DBEntityManager conn,
			final long idLehrerabschnittsdaten) throws ApiOperationException {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = new ArrayList<>();
		// Bestimme die Minderleistungen für die Lehrerabschnittsdaten
		final List<DTOLehrerEntlastungsstunde> dtos = conn.queryList(DTOLehrerEntlastungsstunde.QUERY_BY_ABSCHNITT_ID,
				DTOLehrerEntlastungsstunde.class, idLehrerabschnittsdaten);
		if (dtos == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerEntlastungsstunde dto : dtos)
			result.add(mapInternal(dto, conn));
		return result;
	}


	private String getEntlastungsgrundKrz(final long Abschnitt_ID, final long idGrund) throws ApiOperationException {
		try {
			final LehrerMinderleistungsarten grund = LehrerMinderleistungsarten.data().getWertByID(idGrund);
			final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, Abschnitt_ID);
			if (dtoAbschnitt == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die Lehrer-Abschnittsdaten für den Abschnitt mit der ID %d konnten nicht geladen werden".formatted(Abschnitt_ID));
			final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
			final LehrerMinderleistungsartKatalogEintrag eintrag = grund.daten(abschnitt.schuljahr);
			if (eintrag == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Der Minderleistungsgrund mit der ID %d ist im Schuljahr %d nicht gültig.".formatted(idGrund, abschnitt.schuljahr));
			return eintrag.kuerzel;
		} catch (@SuppressWarnings("unused") final CoreTypeException cte) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Minderleistungsgrund mit der ID %d ist nicht vorhanden.".formatted(idGrund));
		}
	}


	private void updateAbschnittID(final DTOLehrerEntlastungsstunde dto, final long abschnittID) throws ApiOperationException {
		if (((abschnittID < 0) || (conn.queryByKey(DTOLehrerAbschnittsdaten.class, abschnittID)) == null))
			throw new ApiOperationException(Status.CONFLICT, "AbschnittID %d ungültig.".formatted(abschnittID));
		dto.Abschnitt_ID = abschnittID;
	}

}
