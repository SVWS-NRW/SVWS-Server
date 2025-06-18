package de.svws_nrw.data.schule;

import de.svws_nrw.core.data.schule.Merkmal;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOMerkmale;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Merkmal}*/
public final class DataMerkmale extends DataManagerRevised<Long, DTOMerkmale, Merkmal> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} mit der angegebenen Verbindung
	 *
	 * @param conn    die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataMerkmale(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("kuerzel");
	}

	@Override
	protected void initDTO(final DTOMerkmale dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOMerkmale dto) {
		return dto.ID;
	}

	@Override
	public Merkmal getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für das Merkmal darf nicht null sein.");

		final DTOMerkmale dto = conn.queryByKey(DTOMerkmale.class, id);
		if (dto == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Es wurde kein Merkmal mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<Merkmal> getAll() {
		final List<DTOMerkmale> merkmale = this.conn.queryAll(DTOMerkmale.class);
		return merkmale.stream().map(this::map).toList();
	}

	@Override
	protected Merkmal map(final DTOMerkmale dto) {
		final Merkmal merkmal = new Merkmal();
		merkmal.id = dto.ID;
		merkmal.istSchulmerkmal = Optional.ofNullable(dto.Schule).orElse(true);
		merkmal.istSchuelermerkmal = Optional.ofNullable(dto.Schueler).orElse(true);
		merkmal.kuerzel = dto.Kurztext;
		merkmal.bezeichnung = dto.Langtext;
		return merkmal;
	}

	@Override
	protected void mapAttribute(final DTOMerkmale dto, final String name, final Object value, final Map<String, Object> map)
		throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "istSchulmerkmal" -> dto.Schule = JSONMapper.convertToBoolean(value, true, "istSchulmerkmal");
			case "istSchuelermerkmal" -> dto.Schueler = JSONMapper.convertToBoolean(value, true, "istSchuelermerkmal");
			case "kuerzel" -> updateKuerzel(dto, value);
			case "bezeichnung" -> dto.Langtext = JSONMapper.convertToString(
					value, true, true, Schema.tab_EigeneSchule_Merkmale.col_Langtext.datenlaenge(), "bezeichnung");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateKuerzel(final DTOMerkmale dto, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(
				value, false, false, Schema.tab_EigeneSchule_Merkmale.col_Kurztext.datenlaenge(), "kuerzel");
		// Kürzel ist unverändert
		if ((dto.Kurztext != null) && !dto.Kurztext.isBlank() && dto.Kurztext.equals(kuerzel))
			return;

		// theoretischer Fall, der nicht eintreten sollte
		final List<DTOMerkmale> merkmale = conn.queryList(DTOMerkmale.QUERY_BY_KURZTEXT, DTOMerkmale.class, kuerzel);
		if (merkmale.size() > 1)
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Mehr als ein Merkmal mit dem gleichen Kürzel vorhanden");

		// kuerzel bereits vorhanden
		if (!merkmale.isEmpty()) {
			final DTOMerkmale dtoMerkmal = merkmale.getFirst();
			if ((dtoMerkmal != null) && (dtoMerkmal.ID != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s ist bereits vorhanden.".formatted(value));
		}

		// kuerzel wird gepatched
		dto.Kurztext = kuerzel;
	}
}
