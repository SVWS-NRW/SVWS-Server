package de.svws_nrw.data.schule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.WiedervorlageEintrag;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppe;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzergruppenMitglied;
import de.svws_nrw.db.dto.current.schild.erzieher.DTOSchuelerErzieherAdresse;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrer;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.schule.DTOWiedervorlage;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den
 * Core-DTO {@link WiedervorlageEintrag}.
 */
public final class DataWiedervorlage extends DataManagerRevised<Long, DTOWiedervorlage, WiedervorlageEintrag> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link WiedervorlageEintrag}.
	 *
	 * @param conn         die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataWiedervorlage(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bemerkung");
	}

	@Override
	protected void initDTO(final DTOWiedervorlage dto, final Long newId) throws ApiOperationException {
		dto.ID = newId;
		dto.Benutzer_ID = conn.getUser().getId();
		dto.tsAngelegt = JSONMapper.tsFormatter.format(ZonedDateTime.now(ZoneId.of("Europe/Berlin")));
	}

	@Override
	protected WiedervorlageEintrag map(final DTOWiedervorlage dto) throws ApiOperationException {
		final WiedervorlageEintrag daten = new WiedervorlageEintrag();
		daten.id = dto.ID;
		daten.idBenutzer = dto.Benutzer_ID;
		daten.idBenutzergruppe = dto.Benutzergruppe_ID;
		daten.typPerson = (dto.personTyp == null) ? null : dto.personTyp.id;
		daten.idPerson = switch (dto.personTyp) {
			case null -> null;
			case LEHRER -> dto.Lehrer_ID;
			case SCHUELER -> dto.Schueler_ID;
			case ERZIEHER -> dto.Erzieher_ID;
		};
		daten.bemerkung = dto.Bemerkung;
		daten.tsAngelegt = null;
		daten.tsWiedervorlage = null;
		daten.tsErledigt = null;
		daten.idBenutzerErledigt = null;
		daten.automatischErledigt = false;
		return daten;
	}

	private void mapPersonTypAndId(final DTOWiedervorlage dto, final Object valueTyp, final Object valueId) throws ApiOperationException {
		final Integer idTypPerson = JSONMapper.convertToIntegerInRange(valueTyp, true, 1, 4);
		dto.personTyp = (idTypPerson == null) ? null : PersonTyp.getByID(idTypPerson);
		final Long id = JSONMapper.convertToLong(valueId, true);
		switch (dto.personTyp) {
			case null -> {
				if (id != null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Ist der Typ der Person null, so darf auch keine ID angegeben werden.");
				dto.Lehrer_ID = null;
				dto.Schueler_ID = null;
				dto.Erzieher_ID = null;
			}
			case LEHRER -> {
				if (id == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Für den Person-Typ Lehrer wurde keine ID angegeben.");
				final DTOLehrer lehrer = conn.queryByKey(DTOLehrer.class, id);
				if (lehrer == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Für die ID %d konnte kein Lehrer gefunden werden.".formatted(id));
				dto.Lehrer_ID = lehrer.ID;
				dto.Schueler_ID = null;
				dto.Erzieher_ID = null;
			}
			case SCHUELER -> {
				if (id == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Für den Person-Typ Schueler wurde keine ID angegeben.");
				final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
				if (schueler == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Für die ID %d konnte kein Schüler gefunden werden.".formatted(id));
				dto.Lehrer_ID = null;
				dto.Schueler_ID = schueler.ID;
				dto.Erzieher_ID = null;
			}
			case ERZIEHER -> {
				if (id == null)
					throw new ApiOperationException(Status.BAD_REQUEST, "Für den Person-Typ Erzieher wurde keine ID angegeben.");
				final DTOSchuelerErzieherAdresse erzieher = conn.queryByKey(DTOSchuelerErzieherAdresse.class, id);
				if (erzieher == null)
					throw new ApiOperationException(Status.NOT_FOUND, "Für die ID %d konnte kein Erzieher gefunden werden.".formatted(id));
				dto.Lehrer_ID = null;
				dto.Schueler_ID = null;
				dto.Erzieher_ID = null;
			}
		}
	}

	@Override
	protected void mapAttribute(final DTOWiedervorlage dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		checkBenutzer(dto);
		switch (name) {
			case "id" -> {
				final Long patch_id = JSONMapper.convertToLong(value, true);
				if ((patch_id == null) || (patch_id.longValue() != dto.ID))
					throw new ApiOperationException(Status.BAD_REQUEST, "die ID im Patch muss mit der ID im DTO übereinstimmen.");
			}
			case "idBenutzergruppe" -> {
				final Long idBenutzergruppe = JSONMapper.convertToLong(value, true);
				if ((idBenutzergruppe != null) && conn.queryByKey(DTOBenutzergruppe.class, idBenutzergruppe) == null)
					throw new ApiOperationException(Status.CONFLICT, "Die Benutzergruppe mit der ID %d ist ungültig. ".formatted(idBenutzergruppe));
				dto.Benutzergruppe_ID = idBenutzergruppe;
			}
			case "typPerson" -> mapPersonTypAndId(dto, value, map.get("idPerson"));
			case "idPerson" -> mapPersonTypAndId(dto, map.get("typPerson"), value);
			case "bemerkung" -> dto.Bemerkung = JSONMapper.convertToString(value, false, false, null);
			case "tsWiedervorlage" -> {
				final LocalDateTime tmpTsWiedervorlage = JSONMapper.convertToLocalDateTime(value, true);
				dto.tsWiedervorlage = JSONMapper.tsFormatter.format(tmpTsWiedervorlage);
			}
			case "automatischErledigt" -> dto.AutomatischErledigt = JSONMapper.convertToBoolean(value, false);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes oder unerlaubtes Attribut.");
		}
	}


	@Override
	public List<WiedervorlageEintrag> getList() throws ApiOperationException {
		final long idBenutzer = conn.getUser().getId();
		final Set<Long> idsGruppen = conn.queryList(DTOBenutzergruppenMitglied.QUERY_BY_BENUTZER_ID, DTOBenutzergruppenMitglied.class, idBenutzer)
				.stream().map(g -> g.Gruppe_ID).collect(Collectors.toSet());
		final List<DTOWiedervorlage> dtos = new ArrayList<>();
		dtos.addAll(conn.queryList(DTOWiedervorlage.QUERY_BY_BENUTZER_ID, DTOWiedervorlage.class, idBenutzer));
		if (!idsGruppen.isEmpty())
			dtos.addAll(conn.queryList(DTOWiedervorlage.QUERY_LIST_BY_BENUTZERGRUPPE_ID, DTOWiedervorlage.class, idsGruppen));
		final List<WiedervorlageEintrag> result = new ArrayList<>();
		final Set<Long> ids = new HashSet<>();
		for (final DTOWiedervorlage dto : dtos) {
			if (!ids.contains(dto.ID)) {
				result.add(map(dto));
				ids.add(dto.ID);
			}
		}
		return result;
	}


	private void checkBenutzer(final DTOWiedervorlage dto) throws ApiOperationException {
		final long idBenutzer = conn.getUser().getId();
		if (dto.Benutzer_ID == idBenutzer)
			return;
		if (dto.Benutzergruppe_ID != null) {
			final Set<Long> idsGruppen = conn.queryList(DTOBenutzergruppenMitglied.QUERY_BY_BENUTZER_ID, DTOBenutzergruppenMitglied.class, idBenutzer)
					.stream().map(g -> g.Gruppe_ID).collect(Collectors.toSet());
			if (idsGruppen.contains(dto.Benutzergruppe_ID))
				return;
		}
		throw new ApiOperationException(Status.FORBIDDEN,
				"Der angemeldete Benutzer darf den Wiedervorlage-Eintrag nicht bearbeiten, da er ihm nicht gehört bzw. ihm nicht zugeordnet ist.");
	}


	private DTOWiedervorlage getDTO(final Long id) throws ApiOperationException {
		final DTOWiedervorlage dto = conn.queryByKey(DTOWiedervorlage.class, id);
		checkBenutzer(dto);
		return dto;
	}


	@Override
	public WiedervorlageEintrag getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Für den Person-Typ Erzieher wurde keine ID angegeben.");
		return map(getDTO(id));
	}


	@Override
	public void checkBeforeDeletion(final List<DTOWiedervorlage> dtos) throws ApiOperationException {
		for (final DTOWiedervorlage dto : dtos)
			checkBenutzer(dto);
	}


	/**
	 * Markiert den Wiedervorlage-Eintrag mit der angegeben ID als erledigt.
	 *
	 * @param id   die ID des Wiedervorlage-Eintrags
	 *
	 * @return die HTTP-Response
	 *
	 * @throws ApiOperationException im Fehlerfall
	 */
	public Response postErledigt(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID für den Wiedervorlage-Eintrag darf nicht null sein.");
		final DTOWiedervorlage dto = getDTO(id);
		dto.tsErledigt = JSONMapper.tsFormatter.format(ZonedDateTime.now(ZoneId.of("Europe/Berlin")));
		dto.Benutzer_ID_Erledigt = conn.getUser().getId();
		conn.transactionPersist(dto);
		conn.transactionFlush();
		return Response.status(Status.OK).type(MediaType.APPLICATION_JSON).entity(map(dto)).build();
	}

}
