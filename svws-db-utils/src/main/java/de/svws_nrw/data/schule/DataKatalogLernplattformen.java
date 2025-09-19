package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Lernplattform;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerLernplattform;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerLernplattform;
import de.svws_nrw.db.dto.current.svws.auth.DTOLernplattformen;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
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
		setAttributesNotPatchable("id");
	}

	@Override
	public Lernplattform map(final DTOLernplattformen dto) {
		final Lernplattform daten = new Lernplattform();
		daten.id = dto.ID;
		daten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		daten.benutzernameSuffixLehrer = (dto.BenutzernameSuffixLehrer == null) ? "" : dto.BenutzernameSuffixLehrer;
		daten.benutzernameSuffixErzieher = (dto.BenutzernameSuffixErzieher == null) ? "" : dto.BenutzernameSuffixErzieher;
		daten.benutzernameSuffixSchueler = (dto.BenutzernameSuffixSchueler == null) ? "" :  dto.BenutzernameSuffixSchueler;
		daten.konfiguration = (dto.Konfiguration == null) ? "" :  dto.Konfiguration;
		daten.anzahlEinwilligungen = 0;
		return daten;
	}

	@Override
	protected long getLongId(final DTOLernplattformen lernplattform) {
		return lernplattform.ID;
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
		final List<DTOLernplattformen> lernplattformen = this.conn.queryAll(DTOLernplattformen.class);
		final Map<Long, Long> schuelerCountsById =
				this.conn.queryList(DTOSchuelerLernplattform.QUERY_ALL.concat(" WHERE e.LernplattformID IS NOT NULL"), DTOSchuelerLernplattform.class).stream()
						.collect(Collectors.groupingBy(s -> s.LernplattformID, Collectors.counting()));
		final Map<Long, Long> lehrerCountsById =
				this.conn.queryList(DTOLehrerLernplattform.QUERY_ALL.concat(" WHERE e.LernplattformID IS NOT NULL"), DTOLehrerLernplattform.class).stream()
						.collect(Collectors.groupingBy(l -> l.LernplattformID, Collectors.counting()));
		return lernplattformen.stream()
				.map(lp -> {
					final int schuelerCount = schuelerCountsById.getOrDefault(lp.ID, 0L).intValue();
					final int lehrerCount = lehrerCountsById.getOrDefault(lp.ID, 0L).intValue();
					return map(lp, schuelerCount + lehrerCount);
				}).toList();
	}

	@Override
	public Lernplattform getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Lernplattform mit der ID null ist unzulässig.");

		final DTOLernplattformen lernplattform = conn.queryByKey(DTOLernplattformen.class, id);
		if (lernplattform == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Lernplattform mit der ID %d wurde nicht gefunden.".formatted(id));

		final int schuelerCount = this.conn.queryList(DTOSchuelerLernplattform.QUERY_BY_LERNPLATTFORMID, DTOSchuelerLernplattform.class, id).size();
		final int lehrerCount = this.conn.queryList(DTOLehrerLernplattform.QUERY_BY_LERNPLATTFORMID, DTOLehrerLernplattform.class, id).size();

		return map(lernplattform, schuelerCount + lehrerCount);
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
		persistLernplattformen("SELECT e.ID FROM DTOSchueler e", id -> new DTOSchuelerLernplattform(id, neueLernplattform.id, false, false, false, false));
		persistLernplattformen("SELECT e.ID FROM DTOLehrer e", id -> new DTOLehrerLernplattform(id, neueLernplattform.id, false, false, false, false));
		return neueLernplattform;
	}

	private <T> void persistLernplattformen(final String query, final Function<Long, T> dtoMapper) {
		final List<T> lernplattformen = conn.queryList(query, Long.class).stream().map(dtoMapper).toList();
		conn.transactionPersistAll(lernplattformen);
	}

	@Override
	protected void mapAttribute(final DTOLernplattformen dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> validateBezeichnung(dto, value, name);
			case "benutzernameSuffixLehrer" -> dto.BenutzernameSuffixLehrer =
					JSONMapper.convertToString(value, true, true, Schema.tab_Lernplattformen.col_BenutzernameSuffixLehrer.datenlaenge(), name);
			case "benutzernameSuffixErzieher" -> dto.BenutzernameSuffixErzieher =
					JSONMapper.convertToString(value, true, true, Schema.tab_Lernplattformen.col_BenutzernameSuffixErzieher.datenlaenge(), name);
			case "benutzernameSuffixSchueler" -> dto.BenutzernameSuffixSchueler =
					JSONMapper.convertToString(value, true, true, Schema.tab_Lernplattformen.col_BenutzernameSuffixSchueler.datenlaenge(), name);
			case "konfiguration" -> dto.Konfiguration =
					JSONMapper.convertToString(value, true, true, Schema.tab_Lernplattformen.col_Konfiguration.datenlaenge(), name);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));

		}
	}

	private void validateBezeichnung(final DTOLernplattformen dto, final Object value, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(
				value, false, false, Schema.tab_Lernplattformen.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return;

		final boolean bezeichnungAlreadyUsed = this.conn.queryAll(DTOLernplattformen.class).stream()
				.anyMatch(l -> (l.ID != dto.ID) && l.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		dto.Bezeichnung = bezeichnung;
	}

}
