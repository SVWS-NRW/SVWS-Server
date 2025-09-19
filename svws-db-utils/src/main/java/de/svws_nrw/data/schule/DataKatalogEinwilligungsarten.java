package de.svws_nrw.data.schule;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.svws_nrw.core.data.schule.Einwilligungsart;
import de.svws_nrw.core.types.schule.PersonTyp;
import de.svws_nrw.data.DataManagerRevised;
import de.svws_nrw.data.JSONMapper;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.katalog.DTOKatalogEinwilligungsart;
import de.svws_nrw.db.dto.current.schild.lehrer.DTOLehrerDatenschutz;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchuelerDatenschutz;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse erweitert den abstrakten {@link DataManagerRevised} für das Core-DTO {@link Einwilligungsart}.
 */
public final class DataKatalogEinwilligungsarten extends DataManagerRevised<Long, DTOKatalogEinwilligungsart, Einwilligungsart> {

	/**
	 * Erstellt einen neuen {@link DataManagerRevised} für das Core-DTO {@link Einwilligungsart}.
	 *
	 * @param conn die Datenbank-Verbindung für den Datenbankzugriff
	 */
	public DataKatalogEinwilligungsarten(final DBEntityManager conn) {
		super(conn);
		setAttributesRequiredOnCreation("bezeichnung", "personTyp");
		setAttributesNotPatchable("id");
	}

	@Override
	protected long getLongId(final DTOKatalogEinwilligungsart einwilligungsart) {
		return einwilligungsart.ID;
	}

	@Override
	public Einwilligungsart map(final DTOKatalogEinwilligungsart dto) {
		final Einwilligungsart daten = new Einwilligungsart();
		daten.id = dto.ID;
		daten.bezeichnung = (dto.Bezeichnung == null) ? "" : dto.Bezeichnung;
		daten.schluessel = (dto.Schluessel == null) ? "" : dto.Schluessel;
		daten.sortierung = dto.Sortierung;
		daten.beschreibung = (dto.Beschreibung == null) ? "" : dto.Beschreibung;
		daten.personTyp = (dto.personTyp == null) ? PersonTyp.SCHUELER.id : dto.personTyp.id;
		daten.anzahlEinwilligungen = 0;
		return daten;
	}

	/**
	 * Konvertiert ein DTOKatalogEinwilligungsart-Objekt in ein EinwilligungsartEintrag-Objekt und setzt die Anzahl der Einwilligungen.
	 *
	 * @param dtoKatalogEinwilligungsart Das DTOEinwilligungArt-Objekt, das konvertiert werden soll.
	 * @param anzahlEinwilligungen Die Anzahl der Einwilligungen, die gesetzt werden sollen.
	 *
	 * @return Ein EinwilligungskartEintrag-Objekt, das aus dem DTOEinwilligungArt-Objekt konvertiert und mit der Anzahl der Einwilligungen gesetzt wurde.
	 */
	public Einwilligungsart map(final DTOKatalogEinwilligungsart dtoKatalogEinwilligungsart, final int anzahlEinwilligungen) {
		final Einwilligungsart ea = map(dtoKatalogEinwilligungsart);
		ea.anzahlEinwilligungen = anzahlEinwilligungen;
		return ea;
	}

	@Override
	public List<Einwilligungsart> getAll() throws ApiOperationException {
		final Map<Long, Long> anzahlSchuelerByIdEinwilligungsart = conn
				.queryList(DTOSchuelerDatenschutz.QUERY_ALL.concat(" WHERE e.Datenschutz_ID IS NOT NULL"), DTOSchuelerDatenschutz.class).stream()
				.collect(Collectors.groupingBy(s -> s.Datenschutz_ID, Collectors.counting()));

		final Map<Long, Long> anzahlLehrerByIdEinwilligungsart = conn
				.queryList(DTOLehrerDatenschutz.QUERY_ALL.concat(" WHERE e.DatenschutzID IS NOT NULL"), DTOLehrerDatenschutz.class).stream()
				.collect(Collectors.groupingBy(l -> l.DatenschutzID, Collectors.counting()));

		return conn.queryAll(DTOKatalogEinwilligungsart.class).stream()
				.map(ea -> {
					final int anzahlEinwilligungen = switch (ea.personTyp) {
						case SCHUELER -> anzahlSchuelerByIdEinwilligungsart.getOrDefault(ea.ID, 0L).intValue();
						case LEHRER -> anzahlLehrerByIdEinwilligungsart.getOrDefault(ea.ID, 0L).intValue();
						default -> 0;
					};
					return map(ea, anzahlEinwilligungen);
				})
				.toList();
	}


	@Override
	public Einwilligungsart getById(final Long id) throws ApiOperationException {
		if (id == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Eine Anfrage zu einer Einwilligungsart mit der ID null ist unzulässig.");

		final DTOKatalogEinwilligungsart einwilligungsart = conn.queryByKey(DTOKatalogEinwilligungsart.class, id);
		if (einwilligungsart == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Die Einwilligungsart mit der ID %d wurde nicht gefunden.".formatted(id));

		int anzahlEinwilligungen = 0;
		if (einwilligungsart.personTyp == PersonTyp.SCHUELER)
			anzahlEinwilligungen = conn.queryList(DTOSchuelerDatenschutz.QUERY_BY_DATENSCHUTZ_ID.replace("SELECT e ", "SELECT COUNT(e) "),
					DTOSchuelerDatenschutz.class, einwilligungsart.ID).size();
		else if (einwilligungsart.personTyp == PersonTyp.LEHRER)
			anzahlEinwilligungen = conn.queryList(DTOLehrerDatenschutz.QUERY_BY_DATENSCHUTZID.replace("SELECT e ", "SELECT COUNT(e) "),
					DTOLehrerDatenschutz.class, einwilligungsart.ID).size();
		return map(einwilligungsart, anzahlEinwilligungen);
	}


	@Override
	protected void initDTO(final DTOKatalogEinwilligungsart dto, final Long id, final Map<String, Object> initAttributes) {
		dto.ID = id;
		dto.Bezeichnung = "";
		dto.Schluessel = "";
		dto.Sortierung = 32000;
		dto.Beschreibung = "";
		dto.personTyp = PersonTyp.SCHUELER;
	}


	@Override
	protected Einwilligungsart addBasic(final Long newID, final Map<String, Object> initAttributes) throws ApiOperationException {
		final Einwilligungsart ea = super.addBasic(newID, initAttributes);

		if (ea.personTyp == PersonTyp.LEHRER.id)
			persistEinwilligungen("SELECT e.ID FROM DTOLehrer e", id -> new DTOLehrerDatenschutz(id, ea.id, false, false));
		else if (ea.personTyp == PersonTyp.SCHUELER.id)
			persistEinwilligungen("SELECT e.ID FROM DTOSchueler e", id -> new DTOSchuelerDatenschutz(id, ea.id, false, false));
		else
			throw new ApiOperationException(Status.BAD_REQUEST, "Unbekannter Personentyp: %d".formatted(ea.personTyp));

		return ea;
	}

	private <T> void persistEinwilligungen(final String query, final Function<Long, T> dtoMapper) {
		final List<Long> ids = conn.queryList(query, Long.class);
		final List<T> einwilligungen = ids.stream().map(dtoMapper).toList();
		conn.transactionPersistAll(einwilligungen);
	}

	@Override
	protected void mapAttribute(final DTOKatalogEinwilligungsart dto, final String name, final Object value, final Map<String, Object> map)
			throws ApiOperationException {
		final PersonTyp personTyp = (map.containsKey("personTyp")) ? PersonTyp.getByID(JSONMapper.convertToInteger(map.get("personTyp"), true)) : dto.personTyp;
		switch (name) {
			case "id" -> {
				final Long id = JSONMapper.convertToLong(value, false, name);
				if (!Objects.equals(dto.ID, id))
					throw new ApiOperationException(Status.BAD_REQUEST,
							"Die ID %d des Patches ist null oder stimmt nicht mit der ID %d in der Datenbank überein.".formatted(id, dto.ID));
			}
			case "bezeichnung" -> dto.Bezeichnung = validateBezeichnung(dto, value, personTyp, name);
			case "istSichtbar" -> dto.Sichtbar = JSONMapper.convertToBoolean(value, false, name);
			case "schluessel" -> dto.Schluessel = validateSchluessel(dto, value, personTyp, name);
			case "sortierung" -> dto.Sortierung = JSONMapper.convertToInteger(value, false, name);
			case "beschreibung" -> dto.Beschreibung =
					JSONMapper.convertToString(value, true, true, Schema.tab_K_Datenschutz.col_Beschreibung.datenlaenge(), name);
			case "personTyp" -> validatePersonTyp(dto, name, value, map);
			default -> throw new ApiOperationException(Status.BAD_REQUEST, "Die Daten des Patches enthalten das unbekannte Attribut %s.".formatted(name));
		}
	}

	private String validateBezeichnung(final DTOKatalogEinwilligungsart dto, final Object value, final PersonTyp personTyp, final String name) throws ApiOperationException {
		final String bezeichnung = JSONMapper.convertToString(value, false, false, Schema.tab_K_Datenschutz.col_Bezeichnung.datenlaenge(), name);
		if (Objects.equals(dto.Bezeichnung, bezeichnung) || bezeichnung.isBlank())
			return dto.Bezeichnung;

		final boolean bezeichnungAlreadyUsed =  this.conn.queryAll(DTOKatalogEinwilligungsart.class).stream()
				.anyMatch(e -> (e.ID != dto.ID) && (e.personTyp == personTyp) && e.Bezeichnung.equalsIgnoreCase(bezeichnung));
		if (bezeichnungAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die Bezeichnung %s ist bereits vorhanden.".formatted(bezeichnung));

		return bezeichnung;
	}


	private String validateSchluessel(final DTOKatalogEinwilligungsart dto, final Object value, final PersonTyp personTyp, final String name) throws ApiOperationException {
		final String schluessel = JSONMapper.convertToString(value, true, true, Schema.tab_K_Datenschutz.col_Schluessel.datenlaenge(), name);
		if (Objects.equals(dto.Schluessel, schluessel))
			return dto.Schluessel;

		if (schluessel.isBlank())
			return schluessel;

		final boolean schluesselAlreadyUsed =  this.conn.queryAll(DTOKatalogEinwilligungsart.class).stream()
				.anyMatch(e -> (e.ID != dto.ID) && (e.personTyp == personTyp) && e.Schluessel.equalsIgnoreCase(schluessel));
		if (schluesselAlreadyUsed)
			throw new ApiOperationException(Status.BAD_REQUEST, "Der Schlüssel %s ist bereits vorhanden.".formatted(schluessel));

		return schluessel;
	}

	private void validatePersonTyp(final DTOKatalogEinwilligungsart dto, final String name, final Object value, final Map<String, Object> map) throws ApiOperationException {
		final int id = JSONMapper.convertToInteger(value, false, name);
		final PersonTyp personTyp = PersonTyp.getByID(id);
		if (personTyp == null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Die ID %d ist für den Personentyp ungültig.".formatted(id));

		validateBezeichnung(dto, map.containsKey("bezeichnung") ? map.get("bezeichnung") : dto.Bezeichnung, personTyp, name);
		validateSchluessel(dto, map.containsKey("schluessel") ? map.get("schluessel") : dto.Schluessel, personTyp, name);
		dto.personTyp = personTyp;
	}

}
