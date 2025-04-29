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
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

public final class DataLehrerPersonalabschnittsdatenMinderleistungen extends DataManagerRevised<Long, DTOLehrerEntlastungsstunde, LehrerPersonalabschnittsdatenAnrechnungsstunden> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataLehrerPersonalabschnittsdatenMinderleistungen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", "idAbschnittsdaten");
		setAttributesRequiredOnCreation("idAbschnittsdaten", "idGrund", "anzahl");
	}
	@Override
	protected void initDTO(final DTOLehrerEntlastungsstunde dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}

	protected static LehrerPersonalabschnittsdatenAnrechnungsstunden mapInternal(final DTOLehrerEntlastungsstunde dto, final DBEntityManager conn) throws ApiOperationException {
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
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
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
			case "idAbschnittsdaten" -> updateAbschnittID(dto,  JSONMapper.convertToLong(value, false));
			case "idGrund" -> updateAnrechnungsgrundKrz(dto, JSONMapper.convertToLong(value, true, "idGrund"));
			case "anzahl" -> dto.EntlastungStd = JSONMapper.convertToDouble(value, true);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}
	@Override
	public LehrerPersonalabschnittsdatenAnrechnungsstunden getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Anrechnungsstunden mit der ID %d gefunden.".formatted(id));
		final DTOLehrerEntlastungsstunde dto = conn.queryByKey(DTOLehrerEntlastungsstunde.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return map(dto);
	}
	/**
	 * Ermittelt die Entlastungen für die Lehrerabschnittsdaten mit der angegebenen ID und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Entlastungen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getByLehrerabschnittsdatenId(final DBEntityManager conn, final long idLehrerabschnittsdaten) throws ApiOperationException {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = new ArrayList<>();
		// Bestimme die Anrechungen für die Lehrerabschnittsdaten
		final List<DTOLehrerEntlastungsstunde> dtos = conn.queryList(DTOLehrerEntlastungsstunde.QUERY_BY_ABSCHNITT_ID,
				DTOLehrerEntlastungsstunde.class, idLehrerabschnittsdaten);
		if (dtos == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerEntlastungsstunde dto : dtos)
			result.add(mapInternal(dto, conn));
		return result;
	}
	private void updateAnrechnungsgrundKrz(final DTOLehrerEntlastungsstunde dto, final Long idGrund) throws ApiOperationException {
		try {
			final LehrerMinderleistungsarten grund = LehrerMinderleistungsarten.data().getWertByID(idGrund);
			final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, dto.Abschnitt_ID);
			if (dtoAbschnitt == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die Lehrer-Abschnittsdaten für den Abschnitt mit der ID %d konnten nicht geladen werden".formatted(dto.Abschnitt_ID));
			final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
			final LehrerMinderleistungsartKatalogEintrag eintrag = grund.daten(abschnitt.schuljahr);
			if (eintrag == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Der Minderleistungsgrund mit der ID %d ist im Schuljahr %d nicht gültig.".formatted(idGrund, abschnitt.schuljahr));
			dto.EntlastungsgrundKrz = eintrag.kuerzel;
		} catch (@SuppressWarnings("unused") final CoreTypeException cte) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Minderleistungsgrund mit der ID %d ist nicht vorhanden.".formatted(idGrund));
		}
	}
	private void updateAbschnittID(final DTOLehrerEntlastungsstunde dto, final Long abschnittID) throws ApiOperationException {
		if ((abschnittID != null) && ((abschnittID < 0 || (conn.queryByKey(DTOLehrerAbschnittsdaten.class, abschnittID)) == null)))
				throw new ApiOperationException(Status.CONFLICT, "AbschnittID %d ungültig.".formatted(abschnittID));
		if (abschnittID == null)
			throw new ApiOperationException(Status.NOT_FOUND, "AbschnittID darf nicht null sein.");
		dto.Abschnitt_ID = abschnittID;
	}

}
