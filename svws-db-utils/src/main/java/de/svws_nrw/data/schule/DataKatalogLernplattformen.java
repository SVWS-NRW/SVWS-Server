package de.svws_nrw.data.schule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.SimpleOperationResponse;
import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.core.exceptions.DeveloperNotificationException;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für den Core-DTO {@link Lernplattform}.
 */
public final class DataKatalogLernplattformen extends DataManagerRevised<Long, DTOLernplattformen, Lernplattform> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für den Core-DTO {@link Lernplattform}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogLernplattformen(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung");
	}

	@Override
	public Lernplattform map(final DTOLernplattformen dtoKatalogLernplattformen) {
		final Lernplattform daten = new Lernplattform();
		daten.id = dtoKatalogLernplattformen.ID;
		daten.bezeichnung = dtoKatalogLernplattformen.Bezeichnung;
		daten.benutzernameSuffixLehrer = (dtoKatalogLernplattformen.BenutzernameSuffixLehrer == null) ? "" : dtoKatalogLernplattformen.BenutzernameSuffixLehrer;
		daten.benutzernameSuffixErzieher = (dtoKatalogLernplattformen.BenutzernameSuffixErzieher == null) ? "" : dtoKatalogLernplattformen.BenutzernameSuffixErzieher;
		daten.benutzernameSuffixSchueler = (dtoKatalogLernplattformen.BenutzernameSuffixSchueler == null) ? "" :  dtoKatalogLernplattformen.BenutzernameSuffixSchueler;
		daten.konfiguration = (dtoKatalogLernplattformen.Konfiguration == null) ? "" :  dtoKatalogLernplattformen.Konfiguration;
		daten.anzahlEinwilligungen = 0;
		return daten;
	}

	/**
	 * Konvertiert ein DTOLernplattformen-Objekt in ein Lernplattform-Objekt und setzt die Anzahl der Einwilligungen.
	 *
	 * @param dtoLernplattformen Das DTOLernplattformen-Objekt, das konvertiert werden soll.
	 * @param anzahlEinwilligungen Die Anzahl der Einwilligungen, die gesetzt werden sollen.
	 *
	 * @return Ein Lernplattform-Objekt, das aus dem DTOLernplattformen-Objekt konvertiert und mit der Anzahl der Einwilligungen gesetzt wurde.
	 */
	public Lernplattform map(final DTOLernplattformen dtoLernplattformen, final int anzahlEinwilligungen) {
		final Lernplattform lp = map(dtoLernplattformen);
		lp.anzahlEinwilligungen = anzahlEinwilligungen;
		return lp;
	}

	@Override
	public List<Lernplattform> getAll() throws ApiOperationException {
		final List<DTOLernplattformen> mapLernplattform = conn.queryAll(DTOLernplattformen.class);
		final Map<Long, Long> mapSchuelerEinwilligungen = conn.queryList(DTOSchuelerLernplattform.QUERY_ALL.concat(" WHERE e.LernplattformID IS NOT NULL"),
				DTOSchuelerLernplattform.class).stream().collect(Collectors.groupingBy(s -> s.LernplattformID, Collectors.counting()));
		final Map<Long, Long> mapLehrerEinwilligungen = conn.queryList(DTOLehrerLernplattform.QUERY_ALL.concat(" WHERE e.LernplattformID IS NOT NULL"),
				DTOLehrerLernplattform.class).stream().collect(Collectors.groupingBy(l -> l.LernplattformID, Collectors.counting()));
		return mapLernplattform.stream()
				.map(lp -> {
					final int anzahlEinwilligungenSchueler;
					final int anzahlEinwilligungenLehrer;
					final int anzahlEinwilligungenGesamt;
					anzahlEinwilligungenSchueler = mapSchuelerEinwilligungen.computeIfAbsent(lp.ID, a -> 0L).intValue();
					anzahlEinwilligungenLehrer = mapLehrerEinwilligungen.computeIfAbsent(lp.ID, a -> 0L).intValue();
					anzahlEinwilligungenGesamt = anzahlEinwilligungenSchueler + anzahlEinwilligungenLehrer;
					return map(lp, anzahlEinwilligungenGesamt);
				}).toList();
	}

	@Override
	public Lernplattform getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Lernplattform mit der ID null ist unzulässig.");
		final DTOLernplattformen lernplattform = conn.queryByKey(DTOLernplattformen.class, id);
		if (lernplattform == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Lernplattform mit der ID %d wurde nicht gefunden.".formatted(id));
		return map(lernplattform);
	}

	@Override
	protected void initDTO(final DTOLernplattformen dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.BenutzernameSuffixLehrer = "";
		dto.BenutzernameSuffixErzieher = "";
		dto.BenutzernameSuffixSchueler = "";
		dto.Konfiguration = "";
	}

	@Override
	protected Lernplattform addBasic(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Lernplattform neueLernplattform = super.addBasic(newID, initAttributes);
		final List<DTOSchuelerLernplattform> schuelerLernplattformen = new ArrayList<>();
		final List<Long> schuelerIds = conn.queryList("SELECT e.ID FROM DTOSchueler e", Long.class);
		for (final Long schuelerId : schuelerIds) {
			final DTOSchuelerLernplattform dto = new DTOSchuelerLernplattform(schuelerId, neueLernplattform.id, false, false, false, false);
			schuelerLernplattformen.add(dto);
		}
		conn.transactionPersistAll(schuelerLernplattformen);
		final List<DTOLehrerLernplattform> lehrerLernplattformen = new ArrayList<>();
		final List<Long> lehrerIds = conn.queryList("SELECT e.ID FROM DTOLehrer e", Long.class);
		for (final Long lehrerId : lehrerIds) {
			final DTOLehrerLernplattform dto = new DTOLehrerLernplattform(lehrerId, neueLernplattform.id, false, false, false, false);
			lehrerLernplattformen.add(dto);
		}
		conn.transactionPersistAll(lehrerLernplattformen);
		return neueLernplattform;
	}

	private String validateBezeichnung(final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, 255, name);
		final List<DTOLernplattformen> bezeichnungenFiltered = conn.queryList(
				"SELECT e FROM DTOLernplattformen e WHERE e.Bezeichnung = ?1", DTOLernplattformen.class, bezeichnung);
		if (!bezeichnungenFiltered.isEmpty())
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Die Bezeichnung '%s' wird bereits für eine andere Lernplattform genutzt.".formatted(bezeichnung));
		return bezeichnung;
	}

	@Override
	protected void mapAttribute(final DTOLernplattformen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, "id");
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST, "IdPatch %d ist ungleich dtoId %d".formatted(id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = validateBezeichnung(value, name);
			case "benutzernameSuffixLehrer" -> dto.BenutzernameSuffixLehrer =
					JSONMapper.convertToString(value, true, true, 20, "benutzernameSuffixLehrer");
			case "benutzernameSuffixErzieher" -> dto.BenutzernameSuffixErzieher =
					JSONMapper.convertToString(value, true, true, 20, "benutzernameSuffixErzieher");
			case "benutzernameSuffixSchueler" -> dto.BenutzernameSuffixSchueler =
					JSONMapper.convertToString(value, true, true, 20, "benutzernameSuffixSchueler");
			case "konfiguration" -> dto.Konfiguration = JSONMapper.convertToString(value, true, true, 255, "konfiguration");
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten ein unbekanntes Attribut.");
		}
	}

	@Override
	public Response deleteMultipleAsResponse(final List<Long> ids) {
		// Bestimme die Datenbank-DTOs der Lernplattformen
		final List<DTOLernplattformen> lernplattformen = this.conn.queryByKeyList(DTOLernplattformen.class, ids).stream().toList();
		// Für jedes DTOLernplattformen-Objekt wird ein SimpleOperationResponse ohne Vorab-Prüfung erzeugt.
		final Map<Long, SimpleOperationResponse> mapResponse = lernplattformen.stream()
				.collect(Collectors.toMap(dto -> dto.ID, dto -> {
					final SimpleOperationResponse response = new SimpleOperationResponse();
					response.id = dto.ID;
					return response;
				}));
		// Lösche die Einwilligungsarten und gib den Erfolg in der Response zurück
		for (final DTOLernplattformen dto : lernplattformen) {
			final SimpleOperationResponse operationResponse = mapResponse.get(dto.ID);
			if (operationResponse == null)
				throw new DeveloperNotificationException("Das SimpleOperationResponse Objekt zu der ID %d existiert nicht.".formatted(dto.ID));
			if (operationResponse.log.isEmpty())
				operationResponse.success = this.conn.transactionRemove(dto);
		}
		return Response.ok().entity(mapResponse.values()).build();
	}

}
