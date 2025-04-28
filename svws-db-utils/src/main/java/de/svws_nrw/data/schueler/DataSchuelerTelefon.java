package de.svws_nrw.data.schueler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.svws_nrw.core.data.schueler.SchuelerTelefon;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerTelefon;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link SchuelerTelefon}.
 */
public final class DataSchuelerTelefon extends DataManagerRevised<Long, DTOSchuelerTelefon, SchuelerTelefon> {

	private final Long idSchueler;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerTelefon}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataSchuelerTelefon(final DBEntityManager conn) {
		this(conn, null);
	}

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link SchuelerTelefon}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 * @param idSchueler   die ID des Schülerdatensatzes auf dem die Datenbankoperationen ausgeführt werden
	 */
	public DataSchuelerTelefon(final DBEntityManager conn, final Long idSchueler) {
		super(conn);
		this.idSchueler = idSchueler;
		setAttributesNotPatchable("id", "idSchueler");
	}

	@Override
	public SchuelerTelefon map(final DTOSchuelerTelefon dtoSchuelerTelefon) {
		final SchuelerTelefon daten = new SchuelerTelefon();
		daten.id = dtoSchuelerTelefon.ID;
		daten.idSchueler = dtoSchuelerTelefon.Schueler_ID;
		daten.idTelefonArt = dtoSchuelerTelefon.TelefonArt_ID;
		daten.telefonnummer = dtoSchuelerTelefon.Telefonnummer;
		daten.bemerkung = dtoSchuelerTelefon.Bemerkung;
		daten.sortierung = dtoSchuelerTelefon.Sortierung;
		daten.istGesperrt = dtoSchuelerTelefon.Gesperrt;
		return daten;
	}

	@Override
	protected void initDTO(final DTOSchuelerTelefon dto, final Long idTelefonArt, final Map<String, Object> initAttributes) {
		dto.ID = idTelefonArt;
		dto.Schueler_ID = this.idSchueler;
		dto.Telefonnummer = "";
		dto.Bemerkung = "";
		dto.Sortierung = 32000;
		dto.Gesperrt = false;
	}

	@Override
	public List<SchuelerTelefon> getAll() throws ApiOperationException {
		final List<DTOSchuelerTelefon> schuelerTelefon = conn.queryAll(DTOSchuelerTelefon.class);
		return schuelerTelefon.stream().map(this::map).toList();
	}

	@Override
	public List<SchuelerTelefon> getList() throws ApiOperationException {
		final List<DTOSchuelerTelefon> telefone = conn.queryList(DTOSchuelerTelefon.QUERY_BY_SCHUELER_ID, DTOSchuelerTelefon.class, idSchueler);
		return telefone.stream().map(this::map).toList();
	}

	@Override
	public SchuelerTelefon getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Telefonart mit der ID null ist unzulässig.");
		final DTOSchuelerTelefon schuelerTelefon = conn.queryByKey(DTOSchuelerTelefon.class, id);
		if (schuelerTelefon == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Ein Schülertelefon mit der ID %d wurde nicht gefunden".formatted(id));
		return map(schuelerTelefon);
	}

	@Override
	protected void mapAttribute(final DTOSchuelerTelefon dto, final String name, final Object value,
			final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(id, dto.ID));
			}
			case "idSchueler" -> {
				final Long id = JSONMapper.convertToLong(value, false, "idSchueler");
				if (!Objects.equals(dto.Schueler_ID, id))
					throw new ApiOperationException(Response.Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(idSchueler, dto.Schueler_ID));
			}
			case "idTelefonArt" -> {
				final Long idTelefonArt = JSONMapper.convertToLong(value, false, "idTelefonArt");
				dto.TelefonArt_ID = idTelefonArt;
			}
			case "telefonnummer" -> dto.Telefonnummer = JSONMapper.convertToString(value, false, false,
					Schema.tab_SchuelerTelefone.col_Telefonnummer.datenlaenge(), "telefonnummer");
			case "bemerkung" -> dto.Bemerkung = JSONMapper.convertToString(value, true, true,
					Schema.tab_SchuelerTelefone.col_Bemerkung.datenlaenge(), "bemerkung");
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, true, "sortierung");
			case "istGesperrt" -> dto.Gesperrt = JSONMapper.convertToBoolean(value, true, "istGesperrt");
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST,  "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	/**
	 * Bestimmt die IDs der Schülertelefoneinträge, welche zu der übergebenen ID der Telefonarten gehören.
	 *
	 * @param id    die ID der Telefonart
	 * @return      die List von IDs der SchülertelefonEinträge, welche der entsprechenden Telefonart zugeordnet sind
	 */
	public List<Long> getIDsByTelefonArtId(final Long id) {
		return conn.queryList(DTOSchuelerTelefon.QUERY_BY_TELEFONART_ID, DTOSchuelerTelefon.class, id).stream().map(t -> t.Schueler_ID).toList();
	}
}
