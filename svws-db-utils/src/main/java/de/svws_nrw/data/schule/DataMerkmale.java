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
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "istSchulmerkmal" -> dto.Schule = JSONMapper.convertToBoolean(value, true, name);
			case "istSchuelermerkmal" -> dto.Schueler = JSONMapper.convertToBoolean(value, true, name);
			case "kuerzel" -> updateKuerzel(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private void updateBezeichnung(final DTOMerkmale dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(
				value, false, false, Schema.tab_EigeneSchule_Merkmale.col_Langtext.datenlaenge(), name);
		if (Objects.equals(dto.Langtext, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOMerkmale.class).stream()
				.anyMatch(m -> (m.ID != dto.ID) && m.Langtext.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Langtext = bezeichnung;
	}

	private void updateKuerzel(final DTOMerkmale dto, final String name, final Object value) throws ApiOperationException {
		final String kuerzel = JSONMapper.convertToString(
				value, false, false, Schema.tab_EigeneSchule_Merkmale.col_Kurztext.datenlaenge(), name);
		if (Objects.equals(dto.Kurztext, kuerzel) || kuerzel.isBlank())
			return;

		final boolean kuerzelAlreadyUsed = this.conn.queryAll(DTOMerkmale.class).stream()
				.anyMatch(m -> (m.ID != dto.ID) && m.Kurztext.equalsIgnoreCase(kuerzel));
		if (kuerzelAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Kürzel %s ist bereits vorhanden.".formatted(kuerzel));

		dto.Kurztext = kuerzel;
	}
}
