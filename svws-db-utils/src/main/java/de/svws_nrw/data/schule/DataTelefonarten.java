package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Telefonart;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOTelefonArt;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang3.Strings;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Telefonart}.
 */
public final class DataTelefonarten extends DataManagerRevised<Long, DTOTelefonArt, Telefonart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Telefonart}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataTelefonarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
		setAttributesNotPatchable("id");
	}

	@Override
	protected void initDTO(final DTOTelefonArt dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
	}

	@Override
	protected long getLongId(final DTOTelefonArt dto) {
		return dto.ID;
	}

	@Override
	public List<Telefonart> getAll() {
		final List<DTOTelefonArt> telefonarten = conn.queryAll(DTOTelefonArt.class);
		final Set<Long> idsTelefonarten = this.mapToIds(telefonarten);
		final Set<Long> idsOfReferencedTelefonarten = this.getIdsOfReferencedTelefonarten(idsTelefonarten);

		return telefonarten
				.stream()
				.map(this::map)
				.map(telefonart -> setReferenceFlag(telefonart, idsOfReferencedTelefonarten))
				.sorted(Comparator.comparing(telefonart -> telefonart.id))
				.toList();
	}

	@Override
	public Telefonart getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Eine Anfrage zu einer Telefonart mit der ID null ist unzulässig.");
		}
		final DTOTelefonArt telefonart = conn.queryByKey(DTOTelefonArt.class, id);
		if (telefonart == null) {
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Die Telefonart mit der ID %d wurde nicht gefunden.".formatted(id));
		}
		return map(telefonart);
	}

	@Override
	public Telefonart map(final DTOTelefonArt dto) {
		final Telefonart daten = new Telefonart();
		daten.id = dto.ID;
		daten.bezeichnung = Objects.requireNonNullElse(dto.Bezeichnung, "");
		daten.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		daten.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return daten;
	}

	@Override
	protected void mapAttribute(final DTOTelefonArt dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOTelefonArt dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id)) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateBezeichnung(final DTOTelefonArt dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_TelefonArt.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}

		validateBezeichnung(dto.ID, bezeichnung);
		dto.Bezeichnung = bezeichnung;
	}

	private Set<Long> mapToIds(final List<DTOTelefonArt> telefonarten) {
		return telefonarten
				.stream()
				.map(telefonart -> telefonart.ID)
				.collect(Collectors.toSet());
	}

	private Set<Long> getIdsOfReferencedTelefonarten(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty()) {
			return Collections.emptySet();
		}

		final String query = "SELECT DISTINCT s.TelefonArt_ID FROM DTOSchuelerTelefon s WHERE s.TelefonArt_ID IN :ids";
		final List<Long> results = this.conn
				.query(query, Long.class)
				.setParameter("ids", ids)
				.getResultList();
		return new HashSet<>(results);
	}

	private Telefonart setReferenceFlag(final Telefonart telefonart, final Set<Long> idsOfReferencedTelefonarten) {
		telefonart.referenziertInAnderenTabellen = idsOfReferencedTelefonarten.contains(telefonart.id);
		return telefonart;
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean bezeichnungAlreadyUsed = this.conn
				.queryAll(DTOTelefonArt.class)
				.stream()
				.anyMatch(t -> (t.ID != id) && Strings.CI.equals(bezeichnung, t.Bezeichnung));

		if (bezeichnungAlreadyUsed) {
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}
}
