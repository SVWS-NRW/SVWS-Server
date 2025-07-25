package de.svws_nrw.data.kurse;

import de.svws_nrw.asd.data.kurse.KursLehrer;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.kurse.DTOKursLehrer;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link KursLehrer} */
public final class DataKursLehrer extends DataManagerRevised<Long[], DTOKursLehrer, KursLehrer> {

	private final Long idKurs;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link KursLehrer}.
	 *
	 * @param conn						die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idKurs					Kurs ID
	 */
	public DataKursLehrer(final DBEntityManager conn, final Long idKurs) {
		super(conn);
		this.idKurs = idKurs;
		setAttributesNotPatchable("idKurs", "idLehrer");
		setAttributesRequiredOnCreation("idKurs", "idLehrer");
	}

	@Override
	protected void initDTO(final DTOKursLehrer dto, final Long[] ids, final Map<String, Object> initAttributes) {
		dto.Kurs_ID = this.idKurs;
		dto.Lehrer_ID = ids[1];
	}

	@Override
	public void checkBeforeCreation(final Long[] newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Long kursId = JSONMapper.convertToLong(initAttributes.get("idKurs"), false, "idKurs");
		final Long idLehrer = JSONMapper.convertToLong(initAttributes.get("idLehrer"), false, "idLehrer");
		final DTOKursLehrer dto = this.conn.queryByKey(DTOKursLehrer.class, kursId, idLehrer);
		if (dto != null)
			throw new ApiOperationException(
					Response.Status.BAD_REQUEST,
					"Es existiert bereits eine Eintrag für die Kombination aus Kurs-ID %d und Lehrer-ID %d.".formatted(kursId, idLehrer)
			);
	}

	@Override
	protected Long[] getID(final Map<String, Object> attributes) throws ApiOperationException {
		final Long kursId = JSONMapper.convertToLong(attributes.get("idKurs"), false, "idKurs");
		final Long idLehrer = JSONMapper.convertToLong(attributes.get("idLehrer"), false, "idLehrer");
		return new Long[]{kursId, idLehrer};
	}

	@Override
	public KursLehrer getById(final Long[] id) throws ApiOperationException {
		if ((id == null) || (id[0] == null) || (id[1] == null))
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "IDs can not be null.");
		final DTOKursLehrer dto = this.conn.queryByKey(DTOKursLehrer.class, id[0], id[1]);
		if (dto == null)
			throw new ApiOperationException(
					Response.Status.NOT_FOUND,
					("Es existiert kein Eintrag für die Kombination aus Kurs-ID %d und Lehrer-ID %d.").formatted(id[0], id[1])
			);
		return map(dto);
	}

	@Override
	public List<KursLehrer> getAll() {
		final List<DTOKursLehrer> result = this.conn.queryAll(DTOKursLehrer.class);
		return result.stream().map(this::map).toList();
	}

	@Override
	public List<KursLehrer> getList() {
		final List<DTOKursLehrer> result = this.conn.queryList(DTOKursLehrer.QUERY_BY_KURS_ID, DTOKursLehrer.class, this.idKurs);
		return result.stream().map(this::map).toList();
	}

	@Override
	protected KursLehrer map(final DTOKursLehrer dtoKursLehrer) {
		final KursLehrer kursLehrer = new KursLehrer();
		kursLehrer.idKurs = dtoKursLehrer.Kurs_ID;
		kursLehrer.idLehrer = dtoKursLehrer.Lehrer_ID;
		kursLehrer.wochenstundenLehrer = (dtoKursLehrer.Anteil != null) ? dtoKursLehrer.Anteil : 0;
		return kursLehrer;
	}

	@Override
	protected void mapAttribute(final DTOKursLehrer dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "idKurs" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.Kurs_ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.Kurs_ID));
			}
			case "idLehrer" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.Lehrer_ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.Lehrer_ID));
			}
			case "wochenstundenLehrer" -> mapWochenstundenLehrer(dto, name, value);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST,  "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void mapWochenstundenLehrer(final DTOKursLehrer dto, final String name, final Object value) throws ApiOperationException {
		final double wochenstunden = JSONMapper.convertToDouble(value, true, name);
		if (wochenstunden < 0)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine negative Anzahl an Wochenstunden ist nicht gestattet");

		dto.Anteil = wochenstunden;
	}

	@Override
	public List<DTOKursLehrer> getDatabaseDTOsByIds(final List<Long[]> ids) {
		final List<DTOKursLehrer> result = new ArrayList<>();
		for (final Long[] id : ids) {
			final DTOKursLehrer dto = this.conn.queryByKey(DTOKursLehrer.class, id[0], id[1]);
			if (dto != null)
				result.add(dto);
		}
		return result;
	}

	@Override
	public DTOKursLehrer getDatabaseDTOByID(final Long[] ids) {
		return this.conn.queryByKey(DTOKursLehrer.class, ids[0], ids[1]);
	}

	@Override
	protected long getLongId(final DTOKursLehrer dbDTO) {
		return dbDTO.Lehrer_ID;
	}
}
