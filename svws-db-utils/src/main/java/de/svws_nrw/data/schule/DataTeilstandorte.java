package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Teilstandort;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schule.DTOTeilstandorte;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Teilstandort}.
 */
public final class DataTeilstandorte extends DataManagerRevised<String, DTOTeilstandorte, Teilstandort> {

	private static final String ADRESS_MERKMAL = "adrMerkmal";

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Teilstandort}.
	 *
	 * @param conn	 die Datenbankverbindung
	 */
	public DataTeilstandorte(final DBEntityManager conn) {
		super(conn);
		setAttributesNotPatchable(ADRESS_MERKMAL, "referenziertInAnderenTabellen");
		setAttributesRequiredOnCreation(ADRESS_MERKMAL);
	}

	@Override
	protected void initDTO(final DTOTeilstandorte dto, final String newAdrMerkmal, final Map<String, Object> initAttributes) {
		dto.AdrMerkmal = newAdrMerkmal;
	}

	@Override
	public List<Teilstandort> getAll() {
		final List<DTOTeilstandorte> teilstandorte = this.conn.queryAll(DTOTeilstandorte.class);
		final Set<String> idsOfReferencedTeilstandorte = this.getIdsOfReferencedTeilstandorte(mapToIds(teilstandorte));

		return teilstandorte
				.stream()
				.map(this::map)
				.map(t -> setReferenceFlag(t, idsOfReferencedTeilstandorte))
				.sorted(Comparator.comparing(t -> t.adrMerkmal))
				.toList();
	}

	@Override
	public Teilstandort getById(final String adrMerkmal) {
		if ((adrMerkmal == null) || adrMerkmal.isBlank()) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Adressmerkmal darf nicht leer sein.");
		}

		final DTOTeilstandorte dto = conn.queryByKey(DTOTeilstandorte.class, adrMerkmal);
		if (dto == null) {
			throw new ApiOperationException(Status.NOT_FOUND, "Teilstandort mit Merkmal %s wurde nicht gefunden.".formatted(adrMerkmal));
		}

		return this.map(dto);
	}

	@Override
	protected Teilstandort map(final DTOTeilstandorte dto) {
		final Teilstandort teilstandort = new Teilstandort();
		teilstandort.adrMerkmal = dto.AdrMerkmal;
		teilstandort.plz = dto.PLZ;
		teilstandort.ort = dto.Ort;
		teilstandort.strassenname = dto.Strassenname;
		teilstandort.hausNr = dto.HausNr;
		teilstandort.hausNrZusatz = dto.HausNrZusatz;
		teilstandort.bemerkung = dto.Bemerkung;
		teilstandort.kuerzel = dto.Kuerzel;
		return teilstandort;
	}

	@Override
	protected void mapAttribute(final DTOTeilstandorte dto, final String name, final Object value, final Map<String, Object> attributes) {
		switch (name) {
			case ADRESS_MERKMAL -> updateAdrMerkmal(dto, name, value);
			case "plz" -> dto.PLZ = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Teilstandorte.col_PLZ.datenlaenge(), name);
			case "ort" -> dto.Ort = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Teilstandorte.col_Ort.datenlaenge(), name);
			case "strassenname" ->
				dto.Strassenname = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Teilstandorte.col_Strassenname.datenlaenge(), name);
			case "hausNr" -> dto.HausNr = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Teilstandorte.col_HausNr.datenlaenge(), name);
			case "hausNrZusatz" ->
				dto.HausNrZusatz = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Teilstandorte.col_HausNrZusatz.datenlaenge(), name);
			case "bemerkung" ->
				dto.Bemerkung = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Teilstandorte.col_Bemerkung.datenlaenge(), name);
			case "kuerzel" ->
				dto.Kuerzel = JSONMapper.convertToString(value, true, true, Schema.tab_EigeneSchule_Teilstandorte.col_Kuerzel.datenlaenge(), name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected String getID(final Map<String, Object> attributes) {
		return JSONMapper.convertToString(attributes.get(ADRESS_MERKMAL), false, false, Schema.tab_EigeneSchule_Teilstandorte.col_AdrMerkmal.datenlaenge(),
				ADRESS_MERKMAL);
	}

	@Override
	public Response deleteMultipleAsSimpleResponseList(final List<String> ids) {
		if (ids == null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Für das Löschen müssen IDs angegeben werden. Null ist nicht zulässig.");
		}

		final Set<String> referencedIds = getIdsOfReferencedTeilstandorte(new HashSet<>(ids));

		final List<SimpleOperationResponse> responses = ids.stream()
				.map(id -> deleteTeilstandort(id, referencedIds))
				.toList();

		return Response.ok().type(MediaType.APPLICATION_JSON).entity(responses).build();
	}

	private SimpleOperationResponse deleteTeilstandort(final String adrMerkmal, final Set<String> referencedIds) {
		final SimpleOperationResponse operationResponse = new SimpleOperationResponse();
		operationResponse.success = false;

		try {
			if (referencedIds.contains(adrMerkmal)) {
				operationResponse.log
						.add("Teilstandort mit Merkmal '%s' kann nicht gelöscht werden, da er in einer Klasse referenziert wird.".formatted(adrMerkmal));
				return operationResponse;
			}

			final DTOTeilstandorte dto = conn.queryByKey(DTOTeilstandorte.class, adrMerkmal);
			if (dto == null) {
				operationResponse.log.add("Teilstandort mit Merkmal '%s' nicht gefunden.".formatted(adrMerkmal));
				return operationResponse;
			}

			deleteDatabaseDTO(dto);
			operationResponse.success = true;

		} catch (final Exception e) {
			operationResponse.log.add("Fehler beim Löschen von '%s': %s".formatted(adrMerkmal, e.getMessage()));
		}

		return operationResponse;
	}

	private void updateAdrMerkmal(final DTOTeilstandorte dto, final String name, final Object value) {
		final String newAdrMerkmal = JSONMapper.convertToString(value, false, false, Schema.tab_EigeneSchule_Teilstandorte.col_AdrMerkmal.datenlaenge(), name);
		validateAdrMerkmal(newAdrMerkmal);
		dto.AdrMerkmal = newAdrMerkmal;
	}

	private void validateAdrMerkmal(final String adrMerkmal) {
		final DTOTeilstandorte existiert = conn.queryByKey(DTOTeilstandorte.class, adrMerkmal);
		if (existiert != null) {
			throw new ApiOperationException(Status.BAD_REQUEST, "Ein Teilstandort mit dem AdrMerkmal %s existiert bereits.".formatted(adrMerkmal));
		}
	}

	private Set<String> getIdsOfReferencedTeilstandorte(final Set<String> adrMerkmale) {
		if ((adrMerkmale == null) || adrMerkmale.isEmpty()) {
			return Collections.emptySet();
		}

		final String query = "SELECT DISTINCT k.AdrMerkmal FROM DTOKlassen k WHERE k.AdrMerkmal IN :adrMerkmale";
		final List<String> results = this.conn
				.query(query, String.class)
				.setParameter("adrMerkmale", adrMerkmale)
				.getResultList();
		return new HashSet<>(results);
	}

	private static Set<String> mapToIds(final List<DTOTeilstandorte> teilstandorte) {
		return teilstandorte
				.stream()
				.map(t -> t.AdrMerkmal)
				.collect(Collectors.toSet());
	}

	private static Teilstandort setReferenceFlag(final Teilstandort teilstandort, final Set<String> idsOfReferencedTeilstandorte) {
		teilstandort.referenziertInAnderenTabellen = idsOfReferencedTeilstandorte.contains(teilstandort.adrMerkmal);
		return teilstandort;
	}
}
