package de.svws_nrw.data.kataloge;

import de.svws_nrw.core.data.schule.Sportbefreiung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSportbefreiung;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Sportbefreiung}. */
public final class DataSportbefreiungen extends DataManagerRevised<Long, DTOSportbefreiung, Sportbefreiung> {


	/**
	 * Erstellt einen neuen {@link DataManagerRevised} mit der angegebenen Verbindung
	 *
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSportbefreiungen(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}


	 @Override
	 protected void initDTO(final DTOSportbefreiung dto, final Long newID, final Map<String, Object> initAttributes) {
		 dto.ID = newID;
	 }

	 @Override
	 protected long getLongId(final DTOSportbefreiung dto) {
		 return dto.ID;
	 }

	@Override
	public Sportbefreiung getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für die Sportbefreiung darf nicht null sein.");

		final DTOSportbefreiung dto = conn.queryByKey(DTOSportbefreiung.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde keine Sportbefreiung mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<Sportbefreiung> getAll() {
		final List<DTOSportbefreiung> sportbefreiungen = this.conn.queryAll(DTOSportbefreiung.class);
		return sportbefreiungen.stream().map(this::map).toList();
	}

	@Override
	protected Sportbefreiung map(final DTOSportbefreiung dto) {
		final Sportbefreiung sportbefreiung = new Sportbefreiung();
		sportbefreiung.id = dto.ID;
		sportbefreiung.bezeichnung = dto.Bezeichnung;
		sportbefreiung.istSichtbar = (dto.Sichtbar == null) || dto.Sichtbar;
		sportbefreiung.istAenderbar = (dto.Aenderbar == null) || dto.Aenderbar;
		sportbefreiung.sortierung = (dto.Sortierung == null) ? -1 : dto.Sortierung;
		return sportbefreiung;
	}

	@Override
	protected void mapAttribute(final DTOSportbefreiung dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> updateBezeichnung(dto, value, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "istAenderbar" -> dto.Aenderbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}


	private void updateBezeichnung(final DTOSportbefreiung dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Sportbefreiung.col_Bezeichnung.datenlaenge(), name);
		//Bezeichnung ist unverändert
		if ((dto.Bezeichnung != null) && !dto.Bezeichnung.isBlank() && dto.Bezeichnung.equals(bezeichnung))
			return;

		//theoretischer Fall, der nicht eintreffen sollte
		final List<DTOSportbefreiung> sportbefreiungen = this.conn.queryList(DTOSportbefreiung.QUERY_BY_BEZEICHNUNG, DTOSportbefreiung.class, bezeichnung);
		if (sportbefreiungen.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als eine Sportbefreiung mit der gleichen Bezeichnung vorhanden.");

		//Sportbefreiung mit dieser Bezeichnung bereits vorhanden
		if (!sportbefreiungen.isEmpty()) {
			final DTOSportbefreiung dtoSportbefreiung = sportbefreiungen.getFirst();
			if ((dtoSportbefreiung != null) && (dtoSportbefreiung.ID != dto.ID))
				throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}

		//Bezeichnung wird geändert
		dto.Bezeichnung = bezeichnung;
	}
}
