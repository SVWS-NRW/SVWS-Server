package de.svws_nrw.data.schule;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.BetriebeAnsprechpartner;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetriebeAnsprechpartner;
import de.svws_nrw.db.dto.current.schild.katalog.DTOBetrieb;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;

import static de.svws_nrw.db.schema.Schema.tab_AllgAdrAnsprechpartner;

/** Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das CoreDTO {@link BetriebeAnsprechpartner} */
public final class DataBetriebeAnsprechpartner extends DataManagerRevised<Long, DTOBetriebeAnsprechpartner, BetriebeAnsprechpartner> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link BetriebeAnsprechpartner}.
	 *
	 * @param conn   die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataBetriebeAnsprechpartner(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("idBetrieb", "name");
		setAttributesNotPatchable("id");
	}

	@Override
	protected void initDTO(final DTOBetriebeAnsprechpartner dto, final Long newID, final Map<String, Object> initAttributes) {
		dto.ID = newID;
	}

	@Override
	protected long getLongId(final DTOBetriebeAnsprechpartner dto) {
		return dto.ID;
	}

	@Override
	public BetriebeAnsprechpartner getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die ID des Ansprechpartners darf nicht null sein.");

		final DTOBetriebeAnsprechpartner dto = this.conn.queryByKey(DTOBetriebeAnsprechpartner.class, id);
		if (dto == null)
			throw new ApiOperationException(Response.Status.NOT_FOUND, "Es wurde kein Ansprechpartner mit der ID %d gefunden.".formatted(id));

		return map(dto);
	}

	@Override
	public List<BetriebeAnsprechpartner> getAll() {
		final List<DTOBetriebeAnsprechpartner> ansprechpartner = this.conn.queryAll(DTOBetriebeAnsprechpartner.class);
		final Set<Long> idsOfReferencedAnsprechpartner = this.getIdsOfReferencedAnsprechpartner(mapToIds(ansprechpartner));

		return ansprechpartner
				.stream()
				.map(this::map)
				.map(b -> setReferenceFlag(b, idsOfReferencedAnsprechpartner))
				.sorted(Comparator.comparing(b -> b.id))
				.toList();
	}

	@Override
	protected BetriebeAnsprechpartner map(final DTOBetriebeAnsprechpartner dto) {
		final BetriebeAnsprechpartner ansprechpartner = new BetriebeAnsprechpartner();
		ansprechpartner.id = dto.ID;
		ansprechpartner.idBetrieb = dto.Adresse_ID;
		ansprechpartner.anrede = dto.Anrede;
		ansprechpartner.name = dto.Name;
		ansprechpartner.rufname = dto.Vorname;
		ansprechpartner.telefon = dto.Telefon;
		ansprechpartner.eMail = dto.Email;
		return ansprechpartner;
	}

	@Override
	protected void mapAttribute(final DTOBetriebeAnsprechpartner dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> validateId(dto, name, value);
			case "idBetrieb" -> updateIdBetrieb(dto, name, value);
			case "anrede" -> dto.Anrede = JSONMapper.convertToString(value, true, true, tab_AllgAdrAnsprechpartner.col_Anrede.datenlaenge(), name);
			case "name" -> dto.Name = JSONMapper.convertToString(value, true, true, tab_AllgAdrAnsprechpartner.col_Name.datenlaenge(), name);
			case "rufname" -> dto.Vorname = JSONMapper.convertToString(value, true, true, tab_AllgAdrAnsprechpartner.col_Vorname.datenlaenge(), name);
			case "telefon" -> dto.Telefon = JSONMapper.convertToString(value, true, true, tab_AllgAdrAnsprechpartner.col_Telefon.datenlaenge(), name);
			case "eMail" -> dto.Email = JSONMapper.convertToString(value, true, true, tab_AllgAdrAnsprechpartner.col_Email.datenlaenge(), name);
			default -> throw new ApiOperationException(Response.Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	@Override
	protected void checkBeforeDeletionWithSimpleOperationResponse(final List<DTOBetriebeAnsprechpartner> ansprechpartner,
			final Map<Long, SimpleOperationResponse> responses) {
		final Set<Long> idsOfReferencedAnsprechpartner = getIdsOfReferencedAnsprechpartner(mapToIds(ansprechpartner));
		ansprechpartner.stream()
				.filter(a -> idsOfReferencedAnsprechpartner.contains(a.ID))
				.forEach(a -> markResponseAsFailed(responses.get(a.ID), a.Name));
	}

	private void updateIdBetrieb(final DTOBetriebeAnsprechpartner dto, final String name, final Object value) throws ApiOperationException {
		final Long idBetrieb = JSONMapper.convertToLong(value, false, name);
		if (idBetrieb.equals(dto.Adresse_ID))
			return;

		final DTOBetrieb betrieb = this.conn.queryByKey(DTOBetrieb.class, idBetrieb);
		if (betrieb == null)
			throw new ApiOperationException(Response.Status.BAD_REQUEST, "Kein Betrieb zur ID %d gefunden.".formatted(idBetrieb));

		dto.Adresse_ID =  idBetrieb;
	}

	private static void validateId(final DTOBetriebeAnsprechpartner dto, final String name, final Object value) throws ApiOperationException {
		final Long id = JSONMapper.convertToLong(value, false, name);
		if (!Objects.equals(dto.ID, id))
			throw new ApiOperationException(Response.Status.BAD_REQUEST,
					"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
	}

	private static void markResponseAsFailed(final SimpleOperationResponse response, final String name) {
		response.success = false;
		response.log.add(
				("Der Ansprechpartner mit dem Name %s ist in der Datenbank referenziert und kann daher nicht gelöscht werden.").formatted(name));
	}

	private Set<Long> getIdsOfReferencedAnsprechpartner(final Set<Long> ids) {
		if ((ids == null) || ids.isEmpty())
			return Collections.emptySet();

		final String query = "SELECT DISTINCT a.Ansprechpartner_ID FROM DTOSchuelerAllgemeineAdresse a WHERE a.Ansprechpartner_ID IN :ids";
		final List<Long> results = this.conn.query(query, Long.class).setParameter("ids", ids).getResultList();
		return new HashSet<>(results);
	}

	private static Set<Long> mapToIds(final List<DTOBetriebeAnsprechpartner> ansprechpartner) {
		return ansprechpartner.stream()
				.map(f -> f.ID)
				.collect(Collectors.toSet());
	}

	private BetriebeAnsprechpartner setReferenceFlag(final BetriebeAnsprechpartner ansprechpartner, final Set<Long> idsOfReferencedAnsprechpartner) {
		ansprechpartner.referenziertInAnderenTabellen = idsOfReferencedAnsprechpartner.contains(ansprechpartner.id);
		return ansprechpartner;
	}

}
