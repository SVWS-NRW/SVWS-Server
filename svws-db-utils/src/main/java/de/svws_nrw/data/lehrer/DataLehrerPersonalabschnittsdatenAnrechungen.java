package de.svws_nrw.data.lehrer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.svws_nrw.asd.data.CoreTypeException;
import de.svws_nrw.asd.data.lehrer.LehrerAnrechnungsgrundKatalogEintrag;
import de.svws_nrw.asd.data.lehrer.LehrerPersonalabschnittsdatenAnrechnungsstunden;
import de.svws_nrw.asd.data.schule.Schuljahresabschnitt;
import de.svws_nrw.asd.types.lehrer.LehrerAnrechnungsgrund;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAbschnittsdaten;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerAnrechnungsstunde;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;



public final class DataLehrerPersonalabschnittsdatenAnrechungen extends DataManagerRevised<Long, DTOLehrerAnrechnungsstunde, LehrerPersonalabschnittsdatenAnrechnungsstunden> {

	/**
	 * Erstellt einen neuen Datenmanager mit der angegebenen Verbindung
	 *
	 * @param conn               die Datenbank-Verbindung, welche vom Daten-Manager benutzt werden soll
	 */
	public DataLehrerPersonalabschnittsdatenAnrechungen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id", "idAbschnittsdaten");
		setAttributesRequiredOnCreation("idAbschnittsdaten", "idGrund", "anzahl");
	}
	@Override
	protected void initDTO(final DTOLehrerAnrechnungsstunde dto, final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		dto.ID = newID;
	}
	protected static LehrerPersonalabschnittsdatenAnrechnungsstunden mapInternal(final DTOLehrerAnrechnungsstunde dto, final DBEntityManager conn) throws ApiOperationException {
		final LehrerPersonalabschnittsdatenAnrechnungsstunden daten = new LehrerPersonalabschnittsdatenAnrechnungsstunden();
		daten.id = dto.ID;
		daten.idAbschnittsdaten = dto.Abschnitt_ID;
		final LehrerAnrechnungsgrund art = LehrerAnrechnungsgrund.data().getWertByKuerzel(dto.AnrechnungsgrundKrz);
		final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, dto.Abschnitt_ID);
		if (dtoAbschnitt == null)
			throw new ApiOperationException(Status.NOT_FOUND,
					"Die Lehrerabschnittdaten konnten für die ID %d nicht gefunden werden.".formatted(dto.Abschnitt_ID));
		final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
		final LehrerAnrechnungsgrundKatalogEintrag artEintrag = (art == null) ? null : art.daten(abschnitt.schuljahr);
		if (artEintrag == null)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Das Kürzel für die Art der Minderleistung, welches in der Datenbank gespeichert ist, ist im Schuljahr %d nicht gültig."
							.formatted(abschnitt.schuljahr));
		daten.idGrund = artEintrag.id;
		daten.anzahl = (dto.AnrechnungStd == null) ? 0.0 : dto.AnrechnungStd;
		return daten;
	}
	@Override
	protected LehrerPersonalabschnittsdatenAnrechnungsstunden map(final DTOLehrerAnrechnungsstunde dto) throws ApiOperationException {
		return mapInternal(dto, conn);
	}
	@Override
	protected void mapAttribute(final DTOLehrerAnrechnungsstunde dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "idAbschnittsdaten" -> updateAbschnittID(dto, JSONMapper.convertToLong(value, true, "idAbschnittsdaten"));
			case "idGrund" -> updateAnrechnungsgrundKrz(dto, JSONMapper.convertToLong(value, true, "idGrund"));
			case "anzahl" -> dto.AnrechnungStd = JSONMapper.convertToDouble(value, true);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}
	@Override
	public LehrerPersonalabschnittsdatenAnrechnungsstunden getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Keine Anrechnungsstunden mit der ID %d gefunden.".formatted(id));
		final DTOLehrerAnrechnungsstunde dto = conn.queryByKey(DTOLehrerAnrechnungsstunde.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND);
		return map(dto);
	}
	/**
	 * Ermittelt die Anrechungen für die Lehrerabschnittsdaten mit der angegebenen ID und und gibt diese zurück.
	 *
	 * @param conn                      die Datenbankverbindung
	 * @param idLehrerabschnittsdaten   die ID der Lehrerabschnittsdaten
	 *
	 * @return die Liste mit den Anrechnungen
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static List<LehrerPersonalabschnittsdatenAnrechnungsstunden> getByLehrerabschnittsdatenId(final DBEntityManager conn,
			final long idLehrerabschnittsdaten) throws ApiOperationException {
		final List<LehrerPersonalabschnittsdatenAnrechnungsstunden> result = new ArrayList<>();
		// Bestimme die Anrechungen für die Lehrerabschnittsdaten
		final List<DTOLehrerAnrechnungsstunde> dtos = conn.queryList(DTOLehrerAnrechnungsstunde.QUERY_BY_ABSCHNITT_ID,
				DTOLehrerAnrechnungsstunde.class, idLehrerabschnittsdaten);
		if (dtos == null)
			return result;
		// Konvertiere sie und füge sie zur Liste hinzu
		for (final DTOLehrerAnrechnungsstunde dto : dtos)
			result.add(mapInternal(dto, conn));
		return result;
	}
	private void updateAnrechnungsgrundKrz(final DTOLehrerAnrechnungsstunde dto, final Long idGrund) throws ApiOperationException {
		try {
			final LehrerAnrechnungsgrund grund = LehrerAnrechnungsgrund.data().getWertByID(idGrund);
			final DTOLehrerAbschnittsdaten dtoAbschnitt = conn.queryByKey(DTOLehrerAbschnittsdaten.class, dto.Abschnitt_ID);
			if (dtoAbschnitt == null)
				throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
						"Die Lehrer-Abschnittsdaten für den Abschnitt mit der ID %d konnten nicht geladen werden".formatted(dto.Abschnitt_ID));
			final Schuljahresabschnitt abschnitt = conn.getUser().schuleGetSchuljahresabschnittByIdOrDefault(dtoAbschnitt.Schuljahresabschnitts_ID);
			final LehrerAnrechnungsgrundKatalogEintrag eintrag = grund.daten(abschnitt.schuljahr);
			if (eintrag == null)
				throw new ApiOperationException(Status.BAD_REQUEST,
						"Der Minderleistungsgrund mit der ID %d ist im Schuljahr %d nicht gültig.".formatted(idGrund, abschnitt.schuljahr));
			dto.AnrechnungsgrundKrz = eintrag.kuerzel;
		} catch (@SuppressWarnings("unused") final CoreTypeException cte) {
			throw new ApiOperationException(Status.NOT_FOUND, "Der Minderleistungsgrund mit der ID %d ist nicht vorhanden.".formatted(idGrund));
		}
	}
	private void updateAbschnittID(final DTOLehrerAnrechnungsstunde dto, final Long abschnittID) throws ApiOperationException {
		if ((abschnittID != null) && ((abschnittID < 0 || (conn.queryByKey(DTOLehrerAbschnittsdaten.class, abschnittID)) == null)))
				throw new ApiOperationException(Status.CONFLICT, "AbschnittID %d ungültig.".formatted(abschnittID));
		if (abschnittID == null)
			throw new ApiOperationException(Status.NO_CONTENT, "AbschnittID darf nicht null sein.".formatted(abschnittID));
		dto.Abschnitt_ID = abschnittID;
	}

}
