package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Abteilung;
import de.svws_nrw.core.data.schule.AbteilungKlassenzuordnung;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOAbteilungen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Abteilung}.
 */
public final class DataAbteilungen extends DataManagerRevised<Long, DTOAbteilungen, Abteilung> {

	private final Long idSchuljahresabschnitt;
	private final DataAbteilungenKlassenzuordnungen dataAbteilungenKlassenzuordnungen;

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Abteilung}.
	 *
	 * @param conn						die Datenbankverbindung
	 * @param idSchuljahresabschnitt	die ID des Schuljahresabschnittes, auf den sich die Anfragen beziehen
	 * @param data						DataAbteilungenKlassenzuordnungen
	 */
	public DataAbteilungen(final DBEntityManager conn, final Long idSchuljahresabschnitt, final DataAbteilungenKlassenzuordnungen data) {
		super(conn);
		this.idSchuljahresabschnitt = idSchuljahresabschnitt;
		this.dataAbteilungenKlassenzuordnungen = data;
		setAttributesNotPatchable("id");
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	protected void initDTO(final DTOAbteilungen dto, final Long newId, final Map<String, Object> initAttributes) {
		dto.Schuljahresabschnitts_ID = this.idSchuljahresabschnitt;
		dto.ID = newId;
	}

	@Override
	protected long getLongId(final DTOAbteilungen dto) {
		return dto.ID;
	}

	@Override
	public Abteilung getById(final Long id) throws ApiOperationException {
		if (id == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID der Abteilung darf nicht null sein.");
		}
		final DTOAbteilungen dto = conn.queryByKey(DTOAbteilungen.class, id);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Die Abteilung mit der ID %d wurde nicht gefunden.".formatted(id));
		}
		return this.map(dto);
	}

	@Override
	protected Abteilung map(final DTOAbteilungen dto) {
		final Abteilung abteilung = new Abteilung();
		abteilung.id = dto.ID;
		abteilung.bezeichnung = dto.Bezeichnung;
		abteilung.idAbteilungsleiter = dto.AbteilungsLeiter_ID;
		abteilung.raum = dto.Raum;
		abteilung.email = dto.Email;
		abteilung.durchwahl = dto.Durchwahl;
		abteilung.sortierung = Objects.requireNonNullElse(dto.Sortierung, 32000);
		abteilung.istSichtbar = Boolean.TRUE.equals(dto.Sichtbar);
		return abteilung;
	}

	@Override
	public List<Abteilung> getList() {
		if (this.idSchuljahresabschnitt == null) {
			return Collections.emptyList();
		}
		final Map<Long, List<AbteilungKlassenzuordnung>> zuordnungenByIdAbteilung = this.mapZuordnungenByIdAbteilung();
		return this.conn
				.queryList(DTOAbteilungen.QUERY_BY_SCHULJAHRESABSCHNITTS_ID, DTOAbteilungen.class, idSchuljahresabschnitt)
				.stream()
				.map(this::map)
				.map(a -> addKlassenzuordnungen(a, zuordnungenByIdAbteilung))
				.sorted(Comparator.comparing(a -> a.id))
				.toList();
	}

	@Override
	protected void mapAttribute(final DTOAbteilungen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "bezeichnung" -> updateBezeichnung(dto, name, value);
			case "idAbteilungsleiter" -> dto.AbteilungsLeiter_ID = JSONMapper.convertToLong(value, true, name);
			case "raum" -> dto.Raum =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Abteilungen.col_Raum.datenlaenge(), name);
			case "email" -> dto.Email =
					JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Abteilungen.col_Email.datenlaenge(), name);
			case "durchwahl" -> dto.Durchwahl =
					JSONMapper.convertToString(value, true, false, Schema.tab_EigeneSchule_Abteilungen.col_Durchwahl.datenlaenge(), name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private static void validateId(final DTOAbteilungen dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (id != dto.ID) {
			throw new ApiOperationException(
					Status.BAD_REQUEST, "Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
		}
	}

	private void updateBezeichnung(final DTOAbteilungen dto, final String name, final Object value) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Abteilungen.col_Bezeichnung.datenlaenge(), name);
		if (valueIsBlankOrHasNotChanged(dto.Bezeichnung, bezeichnung)) {
			return;
		}
		validateBezeichnung(dto.ID, bezeichnung);

		dto.Bezeichnung = bezeichnung;
	}

	private Map<Long, List<AbteilungKlassenzuordnung>> mapZuordnungenByIdAbteilung() {
		if (this.dataAbteilungenKlassenzuordnungen == null) {
			return new HashMap<>();
		}
		return this.dataAbteilungenKlassenzuordnungen
				.getAll()
				.stream()
				.collect(Collectors.groupingBy(a -> a.idAbteilung));
	}

	private Abteilung addKlassenzuordnungen(final Abteilung abteilung, final Map<Long, List<AbteilungKlassenzuordnung>> zuordnungenByIdAbteilung) {
		final List<AbteilungKlassenzuordnung> result = zuordnungenByIdAbteilung.get(abteilung.id);
		if (result != null) {
			abteilung.klassenzuordnungen.addAll(zuordnungenByIdAbteilung.get(abteilung.id));
		}
		return abteilung;
	}

	private void validateBezeichnung(final Long id, final String bezeichnung) throws ApiOperationException {
		final boolean isAlreadyUsed = this.conn
				.queryAll(DTOAbteilungen.class)
				.stream()
				.anyMatch(e -> (e.ID != id) && bezeichnung.equalsIgnoreCase(e.Bezeichnung));

		if (isAlreadyUsed) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));
		}
	}

	private static boolean valueIsBlankOrHasNotChanged(final String oldValue, final String newValue) {
		return Objects.equals(oldValue, newValue) || ((newValue != null) && newValue.isBlank());
	}

}
